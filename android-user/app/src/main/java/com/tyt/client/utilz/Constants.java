package com.tyt.client.utilz;

/**
 * Created by Mahi in 2021.
 */

/**
 * This Constants class contains the unique int codes to find specific one
 * while requesting permissions and Some static String Checks
 */

public final class Constants {

    public static String history_comptype = "History";
    public static String profile_comptype = "Profile";

    public static final long CAMERA_IMAGE_MAX_DESIRED_SIZE_IN_BYTES = 2524970;
    public static final double CAMERA_IMAGE_MAX_SIZE_AFTER_COMPRESSSION_IN_BYTES = 1893729.0;

    public static final int REQUEST_LOCATION_DIALOG = 199;

    public static final int REQUEST_SELECT_CONTACT = 10454;

    public static final int UPDATE_REQ_CODE = 5874;

    public static final int RESOLVE_HINT = 4302;

    public static final int CANCELTRIPCALLBACK = 23134;
    public static final int BOTTOMSHEETCALLBACK = 110011;
    public static boolean ACTIVITY_OPENEND_ALRDY = false;

    public static String[] Array_permissions = new String[]{
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            /*android.Manifest.permission.ACCESS_BACKGROUND_LOCATION*/
    };

    public static String[] storagePermission = new String[]{
            android.Manifest.permission.CAMERA,
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE};

    public static final int REQUEST_PERMISSION = 9000;
    public final static int PLAY_SERVICES_REQUEST = 1000;
    public static final int REQUEST_CODE_AUTOCOMPLETE = 700;
    public static final int REQUEST_CODE_ENABLING_GOOGLE_LOCATION = 400;
    public static final int REQUEST_CHANGE_ADDRESS = 900;
    public final static int PROMOSETRESULT = 8100;
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
    public static String keyValidated = "keyValidated";
    public static String country = "intentCountry";
    public static String phone = "intentPhone";

    /**
     * It contains the Keys used by Local broadcast receiever.
     */
    public interface BroadcastsActions {
        String B_REQUEST = "B_REQUEST";
        String LATER_NO_DRIVER = "LATER_NO_DRIVER";
        String RemoveWaitingDialog = "RemoveWaitingDialog";
        String CANCEL_RECEIVER = "CANCEL_RECEIVER";
        String BackPressedReceiever = "BackPressedReceiever";
    }

    /**
     * It contains all Error code from the API responses
     */
    public interface ErrorCode {
        Integer EMPTY_FAV_LIST = 721;
        int COMPANY_KEY_DATE_EXPIRED = 1101;
        int COMPANY_KEY_NOT_ACTIVE = 1102;
        int COMPANY_KEY_NOT_VALID = 1105;
        int COMPANY_CREDENTIALS_NOT_VALID = 1100;
    }

    /**
     * It contains all API URLs for all Network Events
     */
    public interface URL {
        String web_enquiry_url = "https://tagyourtaxi.com/taxi-booking-software-free-trial";

        String privacy_policy = "https://tagyourtaxi.com/privacy-policy";
        String Translations = "api/v1/translation/get";
        String BaseURL = "https://admin.tagyourtaxi.com/";
        String SOCKET_URL = "http://3.90.25.20:3001/user";
        String GooglBaseURL = "https://maps.googleapis.com/";
        String GETCOUNTRYLIST = "api/v1/countries";
        String SENDOTP = "api/v1/user/register/send-otp";
        String USERREGISTER = "api/v1/user/register";
        String COUNTRY_CODE_URL = "http://ip-api.com/";
        String LOGIN = "api/v1/user/login";
        String ReqETA = "api/v1/request/eta";
        String GetCarTypes = "api/v1/types/{lat}/{lng}";
        String SingleHistory = "api/v1/request/history/{id}";
        String smsSendApi = "api/v1/request/send";
        String smsHistoryApi = "api/v1/request/chat-history/{id}";

        String validateOtp = "api/v1/user/register/validate-otp";

        String UpdateProfile = "api/v1/user/profile";
        String GetUserProfile = "api/v1/user";
        String UpdatePassword = "api/v1/user/password";
        String CreateRequest = "api/v1/request/create";
        String userHistory = "api/v1/request/history";
        String list_cards = "api/v1/payment/card/list";
        String CLIENT_TOKEN = "api/v1/payment/client/token";
        String ADDCARD = "api/v1/payment/card/add";
        String LOGOUTURL = "api/v1/logout";
        String REQUESTCANCEL = "api/v1/request/cancel";
        String DELETECARD = "api/v1/payment/card/delete/{card}";
        String USERCANCEL = "api/v1/request/cancel";
        String CANCELREASON = "api/v1/common/cancallation/reasons";
        String RATING = "api/v1/request/rating";
        String MakeCardDefault = "api/v1/payment/card/make/default";
        String Refferal = "api/v1/update/user/referral";
        String GetRefferal = "api/v1/get/referral";
        String AddMoney = "api/v1/payment/wallet/add/money";
        String WalletHistory = "api/v1/payment/wallet/history";
        String CompanyKey = "api/v1/validate-company-key";

        String SOSDelete = "api/v1/common/sos/delete/{id}";
        String SOSAdd = "api/v1/common/sos/store";
        String ValidateMobile = "api/v1/user/validate-mobile";
        String PromoCodesApi = "api/v1/request/promocode-list";
        String Validate_mobile = "api/v1/docs/validate-mobile-for-user";
        String SOS = "api/v1/common/sos/list/{lat}/{lng}";
        String FAQ = "api/v1/common/faq/list/{lat}/{lng}";
        String Validate_mobile_login = "api/v1/user/validate-mobile-for-login";

        String addfavloc = "api/v1/user/add-favourite-location";
        String contact_us = "api/v1/common/demo-request";
        String listfav = "api/v1/user/list-favourite-location";
        String delete_fav = "api/v1/user/delete-favourite-location/{favourite_location}";
        String getrentalpacks = "api/v1/request/list-packages";

        String Complaint_Types = "api/v1/common/complaint-titles";
        String Submit_Complaint = "api/v1/common/make-complaint";

    }

    /**
     * It contains all API paramater names to put it in a HashMap
     */
    public interface NetworkParameters {

        String upcominghistory = "is_later";
        String completedhistory = "is_completed";
        String cancelledhistory = "is_cancelled";

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
        String client_id = "client_id";
        String client_token = "client_token";
        String country = "country";
        String mobile = "mobile";
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
        String Address_Name = "address_name";
        String OldPwd = "old_password";

        String UserRole = "user_role";
        String LastNumber = "last_number";
        String PayNonce = "payment_nonce";
        String Rating = "rating";
        String Comment = "comment";
        String isLater = "is_later";
        String tripStartTime = "trip_start_time";
        String cancel_other_reason = "custom_reason";
        String RefferalCode = "refferal_code";
        String cardId = "card_id";
        String Amount = "amount";
        String companyKey = "company_key";
        String Number = "number";
        String Promocode = "promo_code";

        String Myself_Contact_No = "Contact_No_Other";
        String Myself = "Myself";

        String Rent_pack_ID = "rental_pack_id";

        String complaint_id = "complaint_title_id";
        String descrption = "description";
    }

    /**
     * It contains the events used to listen and send Messages in InAppChat Feature.
     */
    public interface MqttEvents {
        String TripStatus = "trip_status";
        String received_chat_status = "new_message";
    }
}
