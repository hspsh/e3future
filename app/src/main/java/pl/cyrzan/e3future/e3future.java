package pl.cyrzan.e3future;

import android.app.Application;
import android.content.Context;

import pl.cyrzan.e3future.rest.ApiService;

/**
 * Created by Patryk on 11.10.2016.
 */

public class e3future extends Application {

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        ApiService.init(this.getApplicationContext());


    }

    public static Context getAppContext() {
        return e3future.mContext;
    }
}
