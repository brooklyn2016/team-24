package com.team24_jpm.bric;

import android.app.Application;

import com.facebook.FacebookSdk;

/**
 * Created by Robbie on 10/29/2016.
 */

public class BricApplication extends Application {

    private static BricApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        FacebookSdk.sdkInitialize(getApplicationContext());

        instance = this;
    }

    /**
     * This method is used to allow helper files access to Context.
     * @return
     */
    public static BricApplication getInstance() {
        return instance;
    }

}
