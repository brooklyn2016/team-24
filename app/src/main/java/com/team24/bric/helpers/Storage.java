package com.team24_jpm.bric.helpers;

import android.content.Context;
import android.content.SharedPreferences;

import com.team24_jpm.bric.BricApplication;

/**
 * Created by harrisonmelton on 10/29/16.
 * Helper file for SharedPreferences
 */
public class Storage {

    private static final SharedPreferences PREFS =
            BricApplication.getInstance().getSharedPreferences("bric_shared_prefs",
                    Context.MODE_PRIVATE);
    private static final SharedPreferences.Editor EDITOR = PREFS.edit();

    /*
    KEYS
     */
    private static final String USER_NAME_KEY = "user_name";
    private static final String USER_EMAIL_KEY = "user_email";

    /**
     * Check if user is currently logged in.
     * @return boolean representing whether or not user is currently logged in
     */
    public static boolean isLoggedIn() {
        return PREFS.getString(USER_NAME_KEY, null) != null;
    }

    /**
     * Logs user in with user name and email provided
     * @param name user's name
     * @param email  user's email
     */
    public static void logIn(String name, String email) {
        EDITOR.putString(USER_NAME_KEY, name);
        EDITOR.putString(USER_EMAIL_KEY, email);
        EDITOR.commit();
    }

    public static void logOut() {
        EDITOR.putString(USER_NAME_KEY, null);
        EDITOR.putString(USER_EMAIL_KEY, null);

    }
}
