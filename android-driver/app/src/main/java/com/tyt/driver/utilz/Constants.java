package com.tyt.driver.utilz;

/**
 * Created by root on 9/22/17.
 */

import android.os.Build;

import androidx.annotation.RequiresApi;

/**
 * Common used files
 */
@RequiresApi(api = Build.VERSION_CODES.Q)
public final class Constants {

    public static String history_comptype = "History";
    public static String profile_comptype = "Profile";

    public static final int REQUEST_LOCATION_DIALOG = 199;

    public static final int UPDATE_REQ_CODE=5874;

    public static final int RESOLVE_HINT=4302;
    public static final int Android11_CAM=4458;

    public static final int CANCELTRIPCALLBACK = 23134;
    public static final int BOTTOMSHEETCALLBACK = 110011;
    public static final String UUID = "uuid";
    public static final int NO_ID = -1;
    public static final int NO_REQUEST = -11;
    public static boolean ACTIVITY_OPENEND_ALRDY = false;
    public static String[] Array_permissions = new String[]{
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.ACCESS_FINE_LOCATION};

   /* public static String[] Array_permissions_10 = new String[]{
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.ACCESS_FINE_LOCATION,
           };*/

    public static String[] backgroundlocpermission = new String[]{
            android.Manifest.permission.ACCESS_BACKGROUND_LOCATION
    };

    public static String[]all10permission = new String[]{
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_BACKGROUND_LOCATION
    };

    public static String[] storagePermission = new String[]{
            android.Manifest.permission.CAMERA,
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE};

    public static final int REQUEST_PERMISSION = 9000;
    public static final int REQUEST_BGLOC_PERMISSION = 8000;
    public static final int REQUEST_ALL10_PERMISSION = 5000;
    public final static int PLAY_SERVICES_REQUEST = 1000;
    public static final int REQUEST_CODE_AUTOCOMPLETE = 700;
    public static final int REQUEST_CODE_ENABLING_GOOGLE_LOCATION = 400;
    public static final int REQUEST_CHANGE_ADDRESS = 900;
    public final static int PROMOSETRESULT = 8100;
    public static final String Pick_Marker = "pick_marker";
    public static final String Drop_Marker = "drop_marker";
    public static final String Map_Marker = "map_marker";
    public static final String EXTRA_Data = "Data";
    public static final String ourApp = "ourApp";
    public static final String googleMap = "googleMap";
    public static final String countryMap = "countryMap";
    public static String Extra_identity = "identity";
    public static String driverChanged = "driverChanged";
    // public static String PlaceApi_key = "AIzaSyB_oJC2B0Gpqq2obBLA-oUXw9HRBp7nDmQ";
    public static String PlaceApi_key = "AIzaSyBeVRs1icwooRpk7ErjCEQCwu0OQowVt9I";
    //public static String PlaceApi_key = "AIzaSyAaSUzrUYszpve7u-dI1w_LN4DOw8Vy3PA";
    public static String PushTripCancelled = "PushTripCancelled";
    public static String PushTripCompleted = "PushTripCompleted";
    public static String COUNTRY_CODE = "IN";
    public static String isLogin = "isLogin";
    public static String WAKE_LOCK_TAG = "myapp:LIMO_WAKE_LOCK_TAG";
    public static String WAKE_LOCK_TAG2 = "myapp:LIMO_WAKE_LOCK_TAG2";
    public static String PhonewithCountry = "phoneWithCountry";

    public static String defaultCountryCode = "+91";
    public static final String INTENT_COUNTRY_CHOOSE = "CountryChoose";
    public static final String FLAG = "flag";
    public static final String countryCode = "countryCode";
    public static final int REQUEST_CAMERA = 200;
    public static final int SELECT_FILE = 100;
    public static String selectedMakeName = "selectedMakeName";
    public static String selectedMakeID = "selectedMakeID";
    public static String selectedModelName = "selectedModelName";
    public static String selectedModelID = "selectedModelID";
    public static String AreaID = "AreaName";
    public static String AreaName = "AreaName";
    public static String SelectedTypeName = "SelectedTypeName";
    public static String SelectedTypeID = "SelectedTypeID";
    public static String selectedColor = "selectedColor";
    public static String selectedCarNumber = "selectedCarNumber";
    public static String selectedCarYear = "selectedCarYear";

    public static String country= "intentCountry";
    public static String phone= "intentPhone";
    static final String CHANNEL_NAME = "Location Background Service";
    static final String CHANNEL_ID = "my_service";

    /**
     * Local broadcast receiever.
     */
    public interface BroadcastsActions {
        String B_REQUEST = "B_REQUEST";
        String LATER_NO_DRIVER = "LATER_NO_DRIVER";
        String RemoveWaitingDialog = "RemoveWaitingDialog";
        String LOCATION_UPDATING_SERVICE = "LOCATION_UPDATING_SERVICE";
        String CANCEL_RECEIVER = "CANCEL_RECEIVER";

        String BackPressedReceiever = "BackPressedReceiever";
    }

    /**
     * Error code from Api
     */
    public interface ErrorCode {
        Integer EMPTY_FAV_LIST = 721;
        int COMPANY_KEY_DATE_EXPIRED = 1101;
        int COMPANY_KEY_NOT_ACTIVE = 1102;
        int COMPANY_KEY_NOT_VALID = 1105;
        int COMPANY_CREDENTIALS_NOT_VALID = 1100;
    }

    /**
     * Api URL
     */
    public interface URL {

        String web_enquiry_url = "https://tagyourtaxi.com/taxi-booking-software-free-trial";

        String smsSendApi = "api/v1/request/send";
        String smsHistoryApi = "api/v1/request/chat-history/{id}";

        String privacy_policy="https://tagyourtaxi.com/privacy-policy";

        String Translations = "api/v1/translation/get";
        String BaseURL = "https://admin.tagyourtaxi.com/";
        // String BaseURL = "http://54.237.246.73/taxi/public/";
        //  String SOCKET_URL = "http://3.90.25.20:3001/driver";
        String GooglBaseURL = "https://maps.googleapis.com/";
        String GETCOUNTRYLIST = "api/v1/countries";
        String SENDOTP = "api/v1/user/register/send-otp";
        String USERREGISTER = "api/v1/driver/register";
        String COUNTRY_CODE_URL = "http://ip-api.com/";
        String LOGIN = "api/v1/driver/login";
        String ReqETA = "api/v1/request/eta";
        String GetCarTypes = "api/v1/types/{lat}/{lng}";
        String validateOtp = "api/v1/user/register/validate-otp";
        String UpdateProfile = "api/v1/user/driver-profile";
        String GetUserProfile = "api/v1/user";
        String UpdatePassword = "api/v1/user/password";
        String CreateRequest = "api/v1/request/create";
        String userHistory = "api/v1/request/history";
        String list_cards = "api/v1/payment/card/list";
        String CLIENT_TOKEN = "api/v1/payment/client/token";
        String ADDCARD = "api/v1/payment/card/add";
        String LOGOUTURL = "api/v1/logout";
        String CARMAKE = "api/v1/common/car/makes";
        String SERVICE_LOC = "api/v1/servicelocation";
        String REQUESTRESPOND = "api/v1/request/respond";
        String ONLINOFFLINE = "api/v1/driver/online-offline";
        String CARMODEL = "api/v1/common/car/models/{make_id}";
        String SERVICETYPES = "api/v1/types/{id}";
        String DELETECARD = "api/v1/payment/card/delete/{card}";

        String DriverArrived = "api/v1/request/arrived";
        String DriverStarted = "api/v1/request/started";
        String DriverEnd = "api/v1/request/end";
        String DriverCancel = "api/v1/request/cancel/by-driver";

        String RATING = "api/v1/request/rating";
        String CANCELREASON = "api/v1/common/cancallation/reasons";
        String SingleHistory = "api/v1/request/history/{id}";

        String WalletHistory = "api/v1/payment/wallet/history";
        String MakeCardDefault = "api/v1/payment/card/make/default";
        String Refferal = "api/v1/update/driver/referral";
        String GetRefferal = "api/v1/get/referral";
        String AddMoney = "api/v1/payment/wallet/add/money";
        String DocumentsNeed = "api/v1/driver/documents/needed";
        String DocumentsUpload = "api/v1/driver/upload/documents";
        String CompanyKey = "api/v1/validate-company-key";
        String TodayEarnings = "api/v1/driver/today-earnings";
        String WeekEarnings = "api/v1/driver/weekly-earnings";

        String SOSDelete = "api/v1/common/sos/delete/{id}";
        String EarningsReport = "api/v1/driver/earnings-report/{from_date}/{to_date}";
        String SOSAdd = "api/v1/common/sos/store";
        String ValidateMobile = "api/v1/driver/validate-mobile";
        String SOS = "api/v1/common/sos/list/{lat}/{lng}";
        String FAQ = "api/v1/common/faq/list/{lat}/{lng}";

        String Validate_mobile_login="api/v1/driver/validate-mobile-for-login";
        String contact_us = "api/v1/common/demo-request";
        String instant_ride = "api/v1/request/create-instant-ride";

        String Complaint_Types="api/v1/common/complaint-titles";
        String Submit_Complaint="api/v1/common/make-complaint";
    }

    /**
     * Api paramater names
     */
    public interface NetworkParameters {
        String email = "email";
        String type = "type";
        String platitude = "platitude";
        String start_connect = "start_connect";
        String plongitude = "plongitude";
        String dlongitude = "dlongitude";
        String dlatitude = "dlatitude";
        String dlocation = "dlocation";
        String plocation = "plocation";
        String id = "id";
        String lat = "lat";
        String lng = "lng";
        String message = "message";
        String request_id = "request_id";
        String reason = "reason";
        String cancel_other_reason = "custom_reason";

        String latitude = "latitude";
        String longitude = "longitude";
        String phonenumber = "phone_number";
        String android = "android";
        String google = "google";
        String password = "password";
        String confpwd = "password_confirmation";
        String device_token = "device_token";
        String login_by = "login_by";
        String TIME_TAKES = "time_takes";
        String name = "name";
        String Number = "number";
        String client_id = "client_id";
        String client_token = "client_token";
        String country = "country";
        String phoneNumber = "mobile";
        String OTP = "otp";
        String UUId = "uuid";
        String TermsCond = "terms_condition";

        String pLat = "pick_lat";
        String pLng = "pick_lng";
        String dLat = "drop_lat";
        String dLng = "drop_lng";
        String vType = "vehicle_type";
        String rType = "ride_type";
        String PayOpt = "payment_opt";
        String pAddress = "pick_address";
        String dAddress = "drop_address";
        String OldPwd = "old_password";

        String UserRole = "user_role";
        String LastNumber = "last_number";
        String PayNonce = "payment_nonce";

        String LAT = "lat";
        String LNG = "lng";

        String SerLocId = "service_location_id";
        String VehType = "vehicle_type";
        String CarMake = "car_make";
        String CarModel = "car_model";
        String CarNumber = "car_number";
        String CarColor = "car_color";
        String CarYear = "car_year";
        String BEARING = "bearing";

        String RANDOM = "RANDOM";

        String LAT_LNG_ARRAY = "lat_lng_array";
        String isActive = "ActiveStatus";
        String VehId = "VehId";
        String is_accept = "is_accept";
        String Pick_lat = "pick_lat";
        String Pick_lng = "pick_lng";
        String Pick_Address = "pick_address";

        String Drop_lat = "drop_lat";
        String Drop_lng = "drop_lng";
        String Drop_Address = "drop_address";

        String TRIP_START = "trip_start";
        String WAITING_TIME = "waiting_time";
        String USER_ID = "user_id";
        String Distance = "distance";
        String BeforeArrival = "before_arrival_waiting_time";
        String AfterArrival = "after_arrival_waiting_time";

        String Rating = "rating";
        String Comment = "comment";
        String SET_LOCATION = "driver_location";
        String RefferalCode = "refferal_code";

        String cardId = "card_id";
        String Amount = "amount";
        String TRIP_ARRIVED = "trip_arrived";
        String DocumentId = "document_id";
        String IdentifyNumber = "identify_number";
        String ExpiryNumber = "expiry_date";
        String DocsIMage = "document";
        String companyKey = "company_key";
        String RideOTP = "ride_otp";

        String pick_IRCname ="pickup_poc_name";
        String pick_IRCmobile = "pickup_poc_mobile";

//        String CarNumber = "car_number";
//        String CarNumber = "car_number";

        String complaint_id="complaint_title_id";
        String descrption="description";

    }


    public interface IntentExtras {
        String LOCATION_ID = "LOCATION_ID";
        String LOCATION_LAT = "LAT";
        String LOCATION_LNG = "LNG";
        String LOCATION_BEARING = "BEARING";
        String LOCATION_DATA = "LOCATION_DATA";
        String ACCEPT_REJECT_DATA = "Accep_Rej_data";
        String TRIP_LAT = "trip_lat";
        String TRIP_LNG = "trip_lng";
        String TRIP_BEARING = "trip_bearing";
        String MetAcceptRejData = "MetAcceptRejData";
    }

    public interface MqttEvents {
        String create_request = "create_request";
        String request_handler = "request_handler";
        String approval_status = "approval_status";
        String received_chat_status = "new_message";
    }

}
