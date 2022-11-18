package com.tyt.driver.utilz;

import android.content.SharedPreferences;

import java.util.Set;

import javax.inject.Inject;

/**
 * Its a local storage class to store some sensitive information.
 */
public class SharedPrefence {

    public static final String FCMTOKEN = "FCMTOKEN";
    public static final String FName = "fname";
    public static final String LName = "LName";
    public static final String Name = "name";
    public static final String Password = "Password";
    public static final String ConfirmPassword = "ConfPassword";
    public static final String Email = "Email";
    public static final String PhoneNumber = "PhoneNumber";
    public static final String LANGUANGE = "";
    public static final String CountryCode = "CountryCode";
    public static final String AccessToken = "accessToken";
    public static final String LANGUANGES = "LANGUANGES";
    public static final String Profile = "Profile";
    public static final String ID = "id";
    public static final String BEARING = "bearing";
    public static final String USER_ID = "user_id";
    public static final String TRIP_START = "trip_start";
    public static final String TRIP_ARRIVED = "trip_arrived";
    public static final String REQUEST_ID = "req_id";
    public static final String SHOW_EARNINGS = "show_earnings";
    public static final String SHOW_INSTANTRIDE = "show_instant_ride";
    public static String isOnline = "isOnline";
    public static String lat = "lat";
    public static String lng = "lng";
    public static String VehTypeID = "VehTypeID";
    public static String keyValue = "keyValue";
    public static final String CURRLAT = "CURRLAT";
    public static final String CURRLNG = "CURRLNG";
    public static String carFrom = "carFrom";
    public static String showFBOTP = "showFBOTP";
    public static String AcceptRejProc = "AcceptRejProcess";

    public static String CurrDate = "current_date";
    public static String CurrenSymbol = "current_symbol";
    public static String TotalEarn = "total_earnings";

    public static String Appversion = "appversion";
    public static String onGoogleSearch = "onGoogleSearch";

    SharedPreferences.Editor editor;
    SharedPreferences prefs;

    @Inject
    public SharedPrefence(SharedPreferences sharedPreferences, SharedPreferences.Editor editor) {
        prefs = sharedPreferences;
        this.editor = editor;
    }

    public void savevalue(String key, String value) {
        editor.putString(key, value);
        editor.apply();
    }

    public void saveInt(String key, int value) {
        editor.putInt(key, value);
        editor.apply();
    }

    public int getInt(String key) {
        return prefs.getInt(key, -1);
    }
    public int getIntSelect(String key) {
        return prefs.getInt(key, 1);
    }
    public void saveBoolean(String key, boolean value) {
        editor.putBoolean(key, value);
        editor.apply();
    }

    public Boolean GetBoolean(String key) {
        return prefs.getBoolean(key, false);
    }

    public String Getvalue(String key) {
        return prefs.getString(key, "");
    }

    public Set<String> getSet(String key) {
        return prefs.getStringSet(key, null);
    }

    public boolean containsKey(String key) {
        return prefs.contains(key);
    }

    public void deleteKey(String key) {
        editor.remove(key);
        editor.apply();
    }

    public void clearAll() {
        editor.clear().commit();
    }

    public class Maptype {
    }


    public String getCompanyToken() {
        return prefs.getString("CompanyKey", "");
    }

    public String getCompanyID() {
        return prefs.getString("CompanyId", "");
    }

}