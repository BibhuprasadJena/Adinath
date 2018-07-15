package com.release.eztoll.Util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by MUVI on 01-Feb-18.
 */

public class PrefferenceManager {

    private static SharedPreferences sharedPreferences;
    private static PrefferenceManager prefferenceManager;
    private SharedPreferences.Editor editor;
    Context context;

    public static final String PrefName = "APP_INFO";
    public static final String REMEMBER_ME = "remember_me";
    public static final String DEFAULT_REMEMBER_ME = "0";

    public static final String USER_ID = "user_id";
    public static final String DEFAULT_USER_ID = "";

    public static final String GOOGLE_FCM_TOKEN = "token";
    public static final String DEFAULT_GOOGLE_FCM_TOKEN = "";

    public static final String LOGIN_TOKEN = "login_token";
    public static final String DEFAULT_LOGIN_TOKEN = "";

    public static final String WALLET_AMOUNT = "wallet_amount";
    public static final String DEFAULT_WALLET_AMOUNT = "0";


    public PrefferenceManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PrefName,Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public static PrefferenceManager getFeaturePreference(Context mContext) {
        if (prefferenceManager == null) {
            return new PrefferenceManager(mContext);
        }
        return prefferenceManager;
    }

    public void setDataInPref(String key , String value){
        editor.putString(key,value);
        editor.commit();
    }

    public String getDataFromPref(String key, String defaultValue) {
        return sharedPreferences.getString(key, defaultValue);
    }
}
