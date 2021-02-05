package com.m90.zero.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import static android.accounts.AccountManager.KEY_USERDATA;

/**
 * Created by Ganesh on 2/28/2017.
 */

public class SessionHelper {
    // LogCat tag
    private static String TAG = SessionHelper.class.getSimpleName();

    // Shared Preferences
    static SharedPreferences mInstance;

    static SessionHelper mSessionHelper;

    Editor editor;
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    SharedPreferences pref;
    // Shared preferences file name
    private static final String PREF_NAME = "Eureka";

    private static final String KEY_IS_LOGGEDIN = "isLoggedIn";
    private static final String IS_LOGIN = "IsLoggedIn";
    private static final String TAG_TOKEN = "Eureka";


    public SessionHelper(Context context) {
        this._context = context;
        mInstance = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = mInstance.edit();

        if(!mInstance.contains(KEY_IS_LOGGEDIN)) {
            editor.putBoolean(KEY_IS_LOGGEDIN, false);
            editor.putString(TAG_TOKEN, "No Token");
            editor.commit();
        }
    }

    public void setLogin(boolean isLoggedIn) {
        editor.putBoolean(KEY_IS_LOGGEDIN, isLoggedIn);
        // commit changes
        editor.commit();
        Log.d(TAG, "User login session modified!");
    }

    public void setLogout() {
        editor.clear();
        editor.commit();
        Log.d(TAG, "User logout from session!");
    }

    public boolean isLoggedIn(){
        return mInstance.getBoolean(KEY_IS_LOGGEDIN, false);
    }


}
