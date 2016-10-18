package pl.cyrzan.e3future.rest;

import okhttp3.ResponseBody;
import pl.cyrzan.e3future.models.MessageResponse;
import pl.cyrzan.e3future.models.ReadStateResponse;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by Patryk on 11.10.2016.
 */

public interface IApiService {

    @GET("/3G8KU4/mode/5/o")
    Call<MessageResponse> setOutput();

    @GET("/3G8KU4/digital/5/1")
    Call<MessageResponse> setOn();

    @GET("/3G8KU4/digital/5/0")
    Call<MessageResponse> setOff();

    @GET("/3G8KU4/digital/5")
    Call<ReadStateResponse> readState();
}
