package pl.cyrzan.e3future.activities;

import android.content.Context;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.zcw.togglebutton.ToggleButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.cyrzan.e3future.R;
import pl.cyrzan.e3future.models.MessageResponse;
import pl.cyrzan.e3future.models.ReadStateResponse;
import pl.cyrzan.e3future.rest.ApiService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    // Logcat tag
    private static final String TAG = "MainActivity";
    private boolean isFlashOn = true;
    private Context context;
    private PorterDuffColorFilter greyFilter;
    private int primaryColor;

    @BindView(R.id.logo)
    ImageView logo;

    @BindView(R.id.toggleBtn)
    ToggleButton toggleButton;

    @BindView(R.id.topLayout)
    RelativeLayout topLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = getApplicationContext();
        ButterKnife.bind(this);
        toggleButton.setEnabled(false);

        primaryColor = ContextCompat.getColor(context, R.color.colorPrimary);
        greyFilter = new PorterDuffColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);

        logo.setColorFilter(greyFilter);

        ApiService.getApiService().setOutput().enqueue(new Callback<MessageResponse>() {
            @Override
            public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {
                if (response.isSuccessful()) {
                    readState();
                }
            }

            @Override
            public void onFailure(Call<MessageResponse> call, Throwable t) {
                Log.i(TAG, t.getMessage());
            }
        });
    }

    public void initToggle(){
        toggleButton.setOnToggleChanged(new ToggleButton.OnToggleChanged() {
            @Override
            public void onToggle(boolean on) {
                Log.i(TAG, "toggle: "+on);
                if(on){
                    toggleButton.setEnabled(false);
                    setOutputOn();
                } else {
                    toggleButton.setEnabled(false);
                    setOutputOff();
                }
            }
        });
    }

    public void setOnLamp(){
        ApiService.getApiService().setOn().enqueue(new Callback<MessageResponse>() {
            @Override
            public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {
                if(response.isSuccessful()){
                    toggleButton.setEnabled(true);
                    initViewsIfOn();
                }
            }

            @Override
            public void onFailure(Call<MessageResponse> call, Throwable t) {

            }
        });
    }

    public void setOffLamp(){
        ApiService.getApiService().setOff().enqueue(new Callback<MessageResponse>() {
            @Override
            public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {
                if(response.isSuccessful()){
                    toggleButton.setEnabled(true);
                    initViewsIfOff();
                }
            }

            @Override
            public void onFailure(Call<MessageResponse> call, Throwable t) {

            }
        });
    }

    public void readState(){
        ApiService.getApiService().readState().enqueue(new Callback<ReadStateResponse>() {
            @Override
            public void onResponse(Call<ReadStateResponse> call, Response<ReadStateResponse> response) {
                if(response.isSuccessful()){
                    ReadStateResponse readStateResponse = response.body();

                    if(readStateResponse.getReturn_value()==1){
                        toggleButton.setToggleOn();
                        initViewsIfOn();
                    } else {
                        toggleButton.setToggleOff();
                        initViewsIfOff();
                    }

                    toggleButton.setEnabled(true);
                    initToggle();
                }
            }

            @Override
            public void onFailure(Call<ReadStateResponse> call, Throwable t) {

            }
        });
    }

    public void initViewsIfOff(){
        topLayout.setBackgroundColor(primaryColor);
        logo.setColorFilter(greyFilter);
    }

    public void initViewsIfOn(){
        int sdk = android.os.Build.VERSION.SDK_INT;
        if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            topLayout.setBackgroundDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.radial_background, null));
        } else {
            topLayout.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.radial_background, null));
        }
        logo.clearColorFilter();
    }

    public void setOutputOn(){
        ApiService.getApiService().setOutput().enqueue(new Callback<MessageResponse>() {
            @Override
            public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {
                if (response.isSuccessful()) {
                    setOnLamp();
                }
            }

            @Override
            public void onFailure(Call<MessageResponse> call, Throwable t) {
                Log.i(TAG, t.getMessage());
            }
        });
    }

    public void setOutputOff(){
        ApiService.getApiService().setOutput().enqueue(new Callback<MessageResponse>() {
            @Override
            public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {
                if (response.isSuccessful()) {
                    setOffLamp();
                }
            }

            @Override
            public void onFailure(Call<MessageResponse> call, Throwable t) {
                Log.i(TAG, t.getMessage());
            }
        });
    }

    /*@OnClick(R.id.btnSwitch)
    public void onSwitchClick(){
        if(isFlashOn){
            Log.i(TAG, "Wyłącz");
            //lamp.setColorFilter(ContextCompat.getColor(context, R.color.blackTransparent), PorterDuff.Mode.MULTIPLY);
            lamp.setColorFilter(Color.parseColor("#ffffff"));
            isFlashOn = false;
        } else {
            Log.i(TAG, "Włącz");
            lamp.setImageResource(R.drawable.btn_switch_on);
            isFlashOn = true;
        }
    }*/
}
