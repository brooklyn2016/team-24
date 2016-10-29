package com.team24_jpm.bric;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.team24_jpm.bric.helpers.Storage;

import org.json.JSONException;
import org.json.JSONObject;

public class SplashscreenActivity extends AppCompatActivity {

    //private TextView info;
    private LoginButton loginButton;
    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

        callbackManager = CallbackManager.Factory.create();
        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // Success, get info from GraphRequest
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(
                                    JSONObject object,
                                    GraphResponse response) {
                                if (response != null) {
                                    try {
                                        // Log user in
                                        String name = object.getString("name");
                                        Log.e("Splashscreen", name);
                                        String id = object.getString("id");
                                        Log.e("Splashscreen", id);
                                        Storage.logIn(name, id);

                                        // Go to main page
                                        Intent intent = new Intent(SplashscreenActivity.this,
                                                MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    } catch (JSONException e) {
                                        Log.e("Splashscreen", e.getMessage());
                                        e.printStackTrace();
                                    }
                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                //info.setText("Login attempt canceled.");
            }

            @Override
            public void onError(FacebookException e) {
                //info.setText("Login attempt failed.");
            }


        });

        // If not logged in, animate login button onto screen
        if (!Storage.isLoggedIn()) {
            ObjectAnimator fadeIn = ObjectAnimator.ofFloat(loginButton, View.ALPHA, .1f, 1f);
            fadeIn.setDuration(2000);
            fadeIn.start();
        } else {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
