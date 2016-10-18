package pl.cyrzan.e3future.rest;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import pl.cyrzan.e3future.R;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Patryk on 11.10.2016.
 */

public class ApiService {

    /**
     * TAG
     */
    private static final String TAG = "ApiService";

    /**
     * IApiService
     */
    private static IApiService apiService = null;

    /**
     * Retrofit
     */
    protected static Retrofit retrofit;

    /**
     * Inicjalizacja api service
     *
     * @param context
     */
    public static void init(final Context context) {

        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        Interceptor interceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                Response response = null;

                try {
                    request = chain.request()
                            .newBuilder()
                            .build();
                    response = chain.proceed(request);
                } catch (SocketTimeoutException exception) {
                    Log.i(TAG, "Timeout");
                    exception.printStackTrace();
                    return chain.proceed(chain.request());
                } catch (UnknownHostException e) {
                    Log.i(TAG, "Connection error");
                    Toast.makeText(context, R.string.error_connection, Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                    return chain.proceed(chain.request());
                }

                switch (response.code()) {
                    case 401:
                        Log.i("APIService", "401: unauthorized");
                        break;

                    case 404:
                        Log.i("APIService", "404: not found");
                        break;

                    case 500:
                        Log.i("APIService", "500: Internal server error");
                        break;

                    default:
                        Log.i("APIService", "Response code error: " + response.code());
                        break;

                }
                return response;
            }
        };

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(httpLoggingInterceptor)
                .addInterceptor(interceptor)
                .connectTimeout(5, TimeUnit.SECONDS)
                .readTimeout(5, TimeUnit.SECONDS)
                .writeTimeout(5, TimeUnit.SECONDS)
                .build();


        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        retrofit = new Retrofit.Builder()
                .baseUrl(context.getString(R.string.API_url))
                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                .client(client)
                .build();


        //Api service
        apiService = retrofit.create(IApiService.class);

    }

    /**
     * Get api service
     *
     * @return
     */
    public static IApiService getApiService() {
        if (apiService == null) {
            throw new IllegalArgumentException("API Service must be initalize");
        }
        return apiService;
    }
}
