package com.team24_jpm.bric;

import android.app.Application;

import com.facebook.FacebookSdk;

/**
 * Created by Robbie on 10/29/2016.
 */

public class BricApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FacebookSdk.sdkInitialize(getApplicationContext());
    }

}
