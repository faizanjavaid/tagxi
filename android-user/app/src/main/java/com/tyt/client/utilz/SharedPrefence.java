package com.tyt.client.utilz;

import android.content.SharedPreferences;

import com.google.android.gms.maps.model.LatLng;

import java.util.Set;

import javax.inject.Inject;

/**
 * Its a local storage class to store some sensitive information.
 */
public class SharedPrefence {
    public static final String Comp_Page_Type = "comp_type_page";
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
    public static final String USER_ID = "user_id";
    public static final String REQUEST_ID = "req_id";
    public static final String CURRLAT = "CURRLAT";
    public static final String CURRLNG = "CURRLNG";
    public static String keyValue = "keyValue";
    public static String showFBOTP = "showFBOTP";
    public static String wallet_back = "WalletBack";
    public static String dont_refresh = "onGoogleSearch";

    public static String KEY_VERIFICATION_ID = "key_verification_id";
    public static String DriverLong = "driverLong";
    public static String DriverLat = "driverLat";

    public static String In_Home = "InHome";
    public static String In_PickDrop = "InPickDrop";
    public static String In_Book = "InBook";

    public static String Myself_Contact_no = "MyselfCotactNo";
    public static String Myself_Contact_name = "MyselfCotactName";

    public static String Appversion = "appversion";

    public static String Book_Pick_Address = "book_pick_address";
    public static String Book_Drop_Address = "book_drop_address";

    public static String Book_Drop_Lat = "book_drop_lat";
    public static String Book_Drop_lng = "book_drop_lng";

    public static String Book_Pick_Lat = "book_pick_lat";
    public static String Book_Pick_lng = "book_pick_lng";

    public static String Request_in_Progress = "request_in_progress";

    public static String Show_Rental = "show_rental";
    public static String Rental_Request_ID = "rental_request_id";

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

    public void savelatlng(String key, double value) {
        editor.putString(key, String.valueOf(value));
        editor.apply();
    }

    public void removeValue(String key) {
        editor.remove(key);
        editor.apply();
    }

    public String GetLatlng(String key) {
        return prefs.getString(key, "");
    }

    public void saveInt(String key, int value) {
        editor.putInt(key, value);
        editor.apply();
    }

    public void saveLong(String key, long value) {
        editor.putLong(key, value);
        editor.apply();
    }

    public long getLong(String key) {
        return prefs.getLong(key, -1);
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