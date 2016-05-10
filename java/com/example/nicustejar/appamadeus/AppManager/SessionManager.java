package com.example.nicustejar.appamadeus.AppManager;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

/**
 * Class for managing user sessions.
 * Used by {@link com.example.nicustejar.appamadeus.BackEndLogic.LogInManager} to login.
 * */
public class SessionManager {
    // LogCat tag
    private static String TAG = SessionManager.class.getSimpleName();

    // Shared Preferences
    SharedPreferences pref;
    Editor editor;
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "AndroidHiveLogin";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";

    /**
     * Creates a new SessionManager
     * @param context current context in which the app runs
     * */
    public SessionManager(Context context) {
        this._context = context;
        this.pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        this.editor = pref.edit();
    }

    /**
     * When the user logs in, it changes the state of the current Session
     * @param isLoggedIn sets the current state
     * */
    public void setLogin(boolean isLoggedIn) {
        this.editor.putBoolean(KEY_IS_LOGGED_IN, isLoggedIn);

        // Commit changes
        this.editor.commit();

        // Log them
        Log.d(TAG, "User login session modified!");
    }

    /**
     * Checks if the user is currently logged in
     * */
    public boolean isLoggedIn(){
        return pref.getBoolean(KEY_IS_LOGGED_IN, false);
    }
}
