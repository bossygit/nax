package com.nasande.nasande;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefManager {

    public static final String SP_HUBBING_APP = "spNasandeApp";

    public static final String SP_NAME = "spName";
    public static final String SP_EMAIL = "spEmail";
    public static final String SP_CSRF_TOKEN = "spCsrfToken";
    public static final String SP_USER_ID = "spUserId";
    public static final String SP_BASIC_AUTH = "spBasicAuth";



    public static final String SP_LOGOUT_TOKEN = "spLogoutToken";

    public static final String SP_IS_LOGGED_IN = "spIsLoggedLogin";

    SharedPreferences sp;
    SharedPreferences.Editor spEditor;

    public SharedPrefManager(Context context) {
        sp = context.getSharedPreferences(SP_HUBBING_APP, Context.MODE_PRIVATE);
        spEditor = sp.edit();
    }

    public void saveSPString(String keySP, String value) {
        spEditor.putString(keySP, value);
        spEditor.commit();
    }

    public void saveSPInt(String keySP, int value) {
        spEditor.putInt(keySP, value);
        spEditor.commit();
    }

    public void saveSPBoolean(String keySP, boolean value) {
        spEditor.putBoolean(keySP, value);
        spEditor.commit();
    }

    public String getSPName() {
        return sp.getString(SP_NAME, "");
    }

    public String getSPEmail() {
        return sp.getString(SP_EMAIL, "");
    }

    public Boolean getSPIsLoggedIn() {
        return sp.getBoolean(SP_IS_LOGGED_IN, false);
    }


    public String getSPCsrfToken() {
        return sp.getString(SP_CSRF_TOKEN, "");
    }

    public String getSpLogoutToken() {
        return sp.getString(SP_LOGOUT_TOKEN , "");
    }

    public String getSPUserId() {
        return sp.getString(SP_USER_ID, "");
    }

    public String getSPBasicAuth() {
        return sp.getString(SP_BASIC_AUTH, "");
    }

    public void efface(){
       spEditor.clear();
       spEditor.commit();
    }
}
