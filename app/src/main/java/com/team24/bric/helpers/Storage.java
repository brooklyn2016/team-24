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
    private static final String LOGGED_IN_KEY = "logged_in";

    /**
     * Check if user is currently logged in.
     * @return boolean representing whether or not user is currently logged in
     */
    public static boolean isLoggedIn() {
        return PREFS.getBoolean(LOGGED_IN_KEY, false);
    }

    /**
     * Logs user in/out
     * @param loggedIn boolean representing whether the user should be logged in (true) or out
     *                 (false)
     */
    public static void setLogin(boolean loggedIn) {
        EDITOR.putBoolean(LOGGED_IN_KEY, loggedIn);
        EDITOR.commit();
    }
}
