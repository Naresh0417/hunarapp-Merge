package com.hamstechonline.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefsUtils {

    private SharedPreferences sharedPref;

    public SharedPrefsUtils(Context pContext, String pFileName) {
        if (sharedPref == null) {
            sharedPref = pContext.getSharedPreferences(pFileName,
                    Context.MODE_PRIVATE);
        }
    }

    public boolean getIsFromCoursePageBoolean(String pKey, boolean pDefault) {

        return sharedPref.getBoolean(pKey, pDefault);
    }
    public void setSharedPrefBoolean(String pKey, boolean pValue) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(pKey, pValue);
        editor.apply();
    }

    public String getDeviceId(String key, String value){

        return sharedPref.getString(key,value);
    }
    public void setDeviceId(String key, String value) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public String getGaidId(String key, String value){

        return sharedPref.getString(key,value);
    }
    public void setGaidId(String key, String value) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public void setUserProfilePic(String key, String value) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public String getUserProfilePic(String key, String value){

        return sharedPref.getString(key,value);
    }

}
