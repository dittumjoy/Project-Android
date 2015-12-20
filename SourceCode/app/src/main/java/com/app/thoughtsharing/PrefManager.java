package com.app.thoughtsharing;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

/**
 * Created by dewneot-pc on 8/5/2015.
 */
public class PrefManager {
    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    SharedPreferences.Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    public static final String PREF_NAME = "MyPrefs";

    // All Shared Preferences Keys
    private static final String KEY_CITY = "city";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_NAME = "name";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_MOBILE = "mobile";
    private static final String KEY_LOCATION = "location";
    private static final String KEY_IS_FIRSTTIME = "first";
    private static final String KEY_IS_REGISTER = "registeredvalue";
    private static final String KEY_ID = "id";

    public PrefManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, context.MODE_PRIVATE);
        editor = pref.edit();
    }

    public void setUserDetails(String name,String email,String phone,String location) {
        editor.putString(KEY_NAME, name);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_MOBILE, phone);
        editor.putString(KEY_LOCATION, location);
        editor.commit();
    }
    public void setCurrentCity(String city,String phone,String id) {
        editor.putString(KEY_CITY, city);
        editor.putString(KEY_PHONE, phone);
        editor.putString(KEY_ID, id);
        editor.commit();
    }
    public HashMap<String, String> getCityDetails() {
        HashMap<String, String> profile = new HashMap<>();
        profile.put("city", pref.getString(KEY_CITY, null));
        profile.put("phone", pref.getString(KEY_PHONE, null));
        profile.put("id", pref.getString(KEY_ID, null));
        return profile;
    }
    public String getPhoneNumber() {
        return pref.getString(KEY_PHONE, null);
    }
    public String getCityID() {
        return pref.getString(KEY_ID, null);
    }



    public boolean isRegistered() {
        return pref.getBoolean(KEY_IS_REGISTER, false);
    }

    public boolean isFirstTime() {
        return pref.getBoolean(KEY_IS_FIRSTTIME, true);
    }

    public void setFirstTime() {
        editor.putBoolean(KEY_IS_FIRSTTIME, false);
        editor.commit();
    }
    public void setRegistered(){
        System.out.println("registering");
        //Log.d("hai","Registering");
        editor.putBoolean(KEY_IS_REGISTER, true);
        editor.commit();
    }
    public void clearSession() {
        editor.clear();
        editor.commit();
    }

    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> profile = new HashMap<>();
        profile.put("name", pref.getString(KEY_NAME, null));
        profile.put("email", pref.getString(KEY_EMAIL, null));
        profile.put("mobile", pref.getString(KEY_MOBILE, null));
        profile.put("location", pref.getString(KEY_LOCATION, null));
        return profile;
    }
}