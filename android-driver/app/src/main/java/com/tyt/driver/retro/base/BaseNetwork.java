package com.tyt.driver.retro.base;

import android.content.Context;

import androidx.databinding.BindingAdapter;
import androidx.databinding.ObservableBoolean;

import android.graphics.Typeface;

import androidx.appcompat.app.AppCompatDelegate;

import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tyt.driver.app.MyApp;
import com.tyt.driver.retro.GitHubService;
import com.tyt.driver.retro.responsemodel.TranslationModel;
import com.tyt.driver.retro.responsemodel.User;
import com.tyt.driver.utilz.CommonUtils;
import com.tyt.driver.utilz.Constants;
import com.tyt.driver.utilz.exception.CustomException;
import com.tyt.driver.utilz.SharedPrefence;

import java.util.HashMap;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by root on 9/27/17.
 */

/**
 * Here is where all API calls connected with view and githubService
 **/
public abstract class BaseNetwork<T extends BaseResponse, N> implements Basecallback<T> {

    public GitHubService gitHubService;
    private N mNavigator;
    public HashMap<String, RequestBody> requestbody = new HashMap<>();
    public MultipartBody.Part body = null;
    /*public  ObservableInt mCurrentTaskId = new ObservableInt(-1);*/
    public final Integer mCurrentTaskId = -1;
    public ObservableBoolean mIsLoading = new ObservableBoolean(false);
    public TranslationModel translationModel;
    public SharedPrefence sharedPrefence;
    public Gson gson;
    public MultipartBody.Part body_profile_pic = null;
    public HashMap<String, String> headerMap = new HashMap<>();


    /**
     * @param gitHubService is intiating the api parameter.
     */
    public BaseNetwork(GitHubService gitHubService) {
        this.gitHubService = gitHubService;

        if (MyApp.getmContext() != null) {
            sharedPrefence = new SharedPrefence(PreferenceManager.getDefaultSharedPreferences(MyApp.getmContext()), PreferenceManager.getDefaultSharedPreferences(MyApp.getmContext()).edit());
            headerMap.put("Content-Language", sharedPrefence.Getvalue(SharedPrefence.LANGUANGE));
        }
        headerMap.put("Accept", "application/json");
    }

    /**
     * @param gitHubService  object of GithubService class for api.
     * @param sharedPrefence object of SharedPreference and to used Every Viewmodel class.
     * @param gson           object of Gson and this is for getting the Translation Model.
     */
    public BaseNetwork(GitHubService gitHubService, SharedPrefence sharedPrefence, Gson gson) {
        this.gitHubService = gitHubService;
        this.sharedPrefence = sharedPrefence;
        this.gson = gson;

        if (!CommonUtils.IsEmpty(sharedPrefence.Getvalue(SharedPrefence.LANGUANGE))) {
            translationModel = gson.fromJson(sharedPrefence.Getvalue(sharedPrefence.Getvalue(SharedPrefence.LANGUANGE)), TranslationModel.class);
        }

        if (MyApp.getmContext() != null) {
            sharedPrefence = new SharedPrefence(PreferenceManager.getDefaultSharedPreferences(MyApp.getmContext()), PreferenceManager.getDefaultSharedPreferences(MyApp.getmContext()).edit());
            headerMap.put("Content-Language", sharedPrefence.Getvalue(SharedPrefence.LANGUANGE));
        }
        headerMap.put("Accept", "application/json");
    }

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    public void MakeComplaint(HashMap<String,String> map) {
        setIsLoading(true);
        gitHubService.submitcomplaints("Bearer "+sharedPrefence.Getvalue(SharedPrefence.AccessToken),map,headerMap).enqueue((Callback<BaseResponse>) baseModelCallBackListener);
    }


    public void  GetComplaintTypes(String comptype,HashMap<String,String> map) {
        setIsLoading(true);
        gitHubService.complainttypes("Bearer "+ sharedPrefence.Getvalue(SharedPrefence.AccessToken),comptype,headerMap).enqueue((Callback<BaseResponse>) baseModelCallBackListener);
    }

    public void MakeInstantRide(HashMap<String,Object> map) {
        setIsLoading(true);
        gitHubService.makeinsride("Bearer " + sharedPrefence.Getvalue(SharedPrefence.AccessToken),map,headerMap).enqueue((Callback<BaseResponse>) baseModelCallBackListener);
    }

    public void ContactUS(HashMap<String,String> map) {
        gitHubService.contactus("Bearer " + sharedPrefence.Getvalue(SharedPrefence.AccessToken),map,headerMap).enqueue((Callback<BaseResponse>) baseModelCallBackListener);
    }

    public void smsSendApi(HashMap<String, String> map) {
        setIsLoading(true);
        Log.d("--BaseNetTAG", "smsSendApi() called with: map = [" + map.get(Constants.NetworkParameters.request_id) + "]"+"["+map.get(Constants.NetworkParameters.message)+"]"+" token ="+sharedPrefence.Getvalue(SharedPrefence.AccessToken));
        gitHubService.smsSend("Bearer " + sharedPrefence.Getvalue(SharedPrefence.AccessToken), map, headerMap).enqueue((Callback<BaseResponse>) baseModelCallBackListener);
    }

    public void smsHistoryApi(String id) {
        setIsLoading(true);
        Log.d("--BaseNetTAG", "smsSendApi() called with: token ="+sharedPrefence.Getvalue(SharedPrefence.AccessToken)+" request-id "+id);
        gitHubService.smsHistory("Bearer " + sharedPrefence.Getvalue(SharedPrefence.AccessToken), id, headerMap).enqueue((Callback<BaseResponse>) baseModelCallBackListener);
    }


    public void SendRegisterOtp(HashMap<String, String> map) {
        setIsLoading(true);
        gitHubService.sendOTP(map, headerMap).enqueue((Callback<BaseResponse>) baseModelCallBackListener);
    }

    public void SendLoginApi(HashMap<String, String> map) {
        setIsLoading(true);
        for (Map.Entry<String,String> j:
            map.entrySet() ) {
            Log.d("--BaseNetTAG", "SendLoginApi: "+j.getKey()+" : "+j.getValue());
        }

        gitHubService.loginApi(map, headerMap).enqueue((Callback<BaseResponse>) baseModelCallBackListener);
    }

    public void MobileLogin(HashMap<String,String> map){
        setIsLoading(true);
        for (Map.Entry<String,String> j:
                map.entrySet() ) {
            Log.d("--BaseNetTAG", "MobileLogin: "+j.getKey()+" : "+j.getValue());
        }
        gitHubService.otplogin(map,headerMap).enqueue((Callback<BaseResponse>) baseModelCallBackListener);
    }

    public void UserRegisterApi(HashMap<String, String> map) {
        setIsLoading(true);
        for (Map.Entry<String,String> j:
                map.entrySet() ) {
            Log.d("--BaseNetTAG", "UserRegisterApi: "+j.getKey()+" : "+j.getValue());
        }
        gitHubService.userRegApi(map, headerMap).enqueue((Callback<BaseResponse>) baseModelCallBackListener);
    }

    public void ProfileUpdate(HashMap<String, String> map) {
        setIsLoading(true);
        gitHubService.userRegApi(map, headerMap).enqueue((Callback<BaseResponse>) baseModelCallBackListener);
    }


    public void getCountryList() {
        setIsLoading(true);
        gitHubService.getCountryList(headerMap).enqueue((Callback<BaseResponse>) baseModelCallBackListener);
    }

    public void ValidateOtp(HashMap<String, String> map) {
        setIsLoading(true);
        gitHubService.RegisterValidateOtp(map, headerMap).enqueue((Callback<BaseResponse>) baseModelCallBackListener);
    }


    public void getTypesApiii(String lat, String lng) {

        gitHubService.getTypesApi(lat, lng, headerMap).enqueue((Callback<BaseResponse>) baseModelCallBackListener);
    }

    public void GetETAApi(HashMap<String, String> map) {
        for (Map.Entry<String,String> j:
                map.entrySet() ) {
            Log.d("--BaseNetTAG", "UserRegisterApi: "+j.getKey()+" : "+j.getValue());
        }
        setIsLoading(true);
        gitHubService.getEtaApi("Bearer " + sharedPrefence.Getvalue(SharedPrefence.AccessToken), map, headerMap).enqueue((Callback<BaseResponse>) baseModelCallBackListener);
    }

    public void CreateReqApicall(HashMap<String, String> map) {
        for (Map.Entry<String,String> j:
                map.entrySet() ) {
            Log.d("--BaseNetTAG", "UserRegisterApi: "+j.getKey()+" : "+j.getValue());
        }
        setIsLoading(true);
        gitHubService.CreateReqApi("Bearer " + sharedPrefence.Getvalue(SharedPrefence.AccessToken), map, headerMap).enqueue((Callback<BaseResponse>) baseModelCallBackListener);
    }


    public void UpdateUserProfile() {
        setIsLoading(true);
        gitHubService.UpdateUserProfile(requestbody, body, "Bearer " + sharedPrefence.Getvalue(SharedPrefence.AccessToken), headerMap).enqueue((Callback<BaseResponse>) baseModelCallBackListener);
    }

    public void GetUserProfile() {
        Log.e("xxxHomeAct","Profile API CAlled");
        setIsLoading(true);
        gitHubService.GetUserProfile("Bearer " + sharedPrefence.Getvalue(SharedPrefence.AccessToken), headerMap).enqueue((Callback<BaseResponse>) baseModelCallBackListener);
    }

    public void GetOnlineOffline() {
        setIsLoading(true);
        gitHubService.OnlineOfflineStatus("Bearer " + sharedPrefence.Getvalue(SharedPrefence.AccessToken), headerMap).enqueue((Callback<BaseResponse>) baseModelCallBackListener);
    }


    public void GetUserHistory(String complete,String Cancel) {
        setIsLoading(true);
        gitHubService.GetUserHistory("Bearer " + sharedPrefence.Getvalue(SharedPrefence.AccessToken),complete,Cancel, headerMap).enqueue((Callback<BaseResponse>) baseModelCallBackListener);
    }

    public void GetCompletedHistory(String complete,Integer page) {
        setIsLoading(true);
        gitHubService.getcompletedhistory("Bearer " + sharedPrefence.Getvalue(SharedPrefence.AccessToken),complete,page).enqueue((Callback<BaseResponse>) baseModelCallBackListener);
    }

    public void GetCancelledHistory(String cancel,Integer page) {
        setIsLoading(true);
        gitHubService.getcancelledhistory("Bearer " + sharedPrefence.Getvalue(SharedPrefence.AccessToken),cancel,page).enqueue((Callback<BaseResponse>) baseModelCallBackListener);
    }

    public void ListCards() {
        setIsLoading(true);
        gitHubService.ListCards("Bearer " + sharedPrefence.Getvalue(SharedPrefence.AccessToken), headerMap).enqueue((Callback<BaseResponse>) baseModelCallBackListener);
    }

    public void UpdatePwd(HashMap<String, String> map) {
        setIsLoading(true);
        gitHubService.UpdatePwd("Bearer " + sharedPrefence.Getvalue(SharedPrefence.AccessToken), map, headerMap).enqueue((Callback<BaseResponse>) baseModelCallBackListener);
    }


    public void getTranslations() {
        setIsLoading(true);
        gitHubService.getTranslationApi(headerMap).enqueue((Callback<BaseResponse>) baseModelCallBackListener);
    }

    public void paymentClientToken() {
        setIsLoading(true);
        gitHubService.paymentClientToken("Bearer " + sharedPrefence.Getvalue(SharedPrefence.AccessToken), headerMap).enqueue((Callback<BaseResponse>) baseModelCallBackListener);
    }

    public void AddCardDetails(HashMap<String, String> map) {
        for (Map.Entry<String,String> j:
                map.entrySet() ) {
            Log.d("--BaseNetTAG", "AddCardDetails: "+j.getKey()+" : "+j.getValue());
        }
        setIsLoading(true);
        gitHubService.AddCardDetails("Bearer " + sharedPrefence.Getvalue(SharedPrefence.AccessToken), map, headerMap).enqueue((Callback<BaseResponse>) baseModelCallBackListener);
    }


    public void DeletCardApi(String id) {
        setIsLoading(true);
        gitHubService.DeletCard("Bearer " + sharedPrefence.Getvalue(SharedPrefence.AccessToken), id, headerMap).enqueue((Callback<BaseResponse>) baseModelCallBackListener);
    }


    public void DefaultCard(HashMap<String, String> map) {
        setIsLoading(true);
        for (Map.Entry<String,String> j:
                map.entrySet() ) {
            Log.d("--BaseNetTAG", "DefaultCard: "+j.getKey()+" : "+j.getValue());
        }
        gitHubService.MakeCardDefault("Bearer " + sharedPrefence.Getvalue(SharedPrefence.AccessToken), map, headerMap).enqueue((Callback<BaseResponse>) baseModelCallBackListener);
    }

    public void LogoutApi() {
        setIsLoading(true);

        gitHubService.LogoutApi("Bearer " + sharedPrefence.Getvalue(SharedPrefence.AccessToken), headerMap).enqueue((Callback<BaseResponse>) baseModelCallBackListener);
    }

    public void getCarMake() {
        setIsLoading(true);
        gitHubService.CARMAKE(headerMap).enqueue((Callback<BaseResponse>) baseModelCallBackListener);
    }

    public void getCarModel(String carMakeId) {
        setIsLoading(true);
        gitHubService.CARMODEL(carMakeId, headerMap).enqueue((Callback<BaseResponse>) baseModelCallBackListener);
    }

    public void serviceLocApiCall(String getvalue) {
        setIsLoading(true);
        gitHubService.SERVICELOC(getvalue, headerMap).enqueue((Callback<BaseResponse>) baseModelCallBackListener);
    }

    public void getServiceType(String ID) {
        setIsLoading(true);
        gitHubService.ServiceTypes(ID, headerMap).enqueue((Callback<BaseResponse>) baseModelCallBackListener);
    }


    public void RequestRepondApi(HashMap<String, String> map) {
        setIsLoading(true);
        for (Map.Entry<String,String> j:
                map.entrySet() ) {
            Log.d("--BaseNetTAG", "RequestRepondApi: "+j.getKey()+" : "+j.getValue());
        }
        gitHubService.RequestRespond("Bearer " + sharedPrefence.Getvalue(SharedPrefence.AccessToken), map, headerMap).enqueue((Callback<BaseResponse>) baseModelCallBackListener);
    }


    public void DriverStartedApi(HashMap<String, String> map) {
        setIsLoading(true);
        for (Map.Entry<String,String> j:
                map.entrySet() ) {
            Log.d("--BaseNetTAG", "DriverStartedApi: "+j.getKey()+" : "+j.getValue());
        }
        gitHubService.DriverStarted("Bearer " + sharedPrefence.Getvalue(SharedPrefence.AccessToken), map, headerMap).enqueue((Callback<BaseResponse>) baseModelCallBackListener);
    }

    public void DriverArrivedApi(HashMap<String, String> map) {
        setIsLoading(true);
        for (Map.Entry<String,String> j:
                map.entrySet() ) {
            Log.d("--BaseNetTAG", "DriverArrivedApi: "+j.getKey()+" : "+j.getValue());
        }
        gitHubService.DriverArrived("Bearer " + sharedPrefence.Getvalue(SharedPrefence.AccessToken), map, headerMap).enqueue((Callback<BaseResponse>) baseModelCallBackListener);
    }

    public void DriverEndApi(HashMap<String, String> map) {
        setIsLoading(true);
        for (Map.Entry<String,String> j:
                map.entrySet() ) {
            Log.d("--BaseNetTAG", "DriverEndApi: "+j.getKey()+" : "+j.getValue());
        }
        gitHubService.DriverEnd("Bearer " + sharedPrefence.Getvalue(SharedPrefence.AccessToken), map, headerMap).enqueue((Callback<BaseResponse>) baseModelCallBackListener);
    }

    public void DriverCancelApi(HashMap<String, String> map) {
        setIsLoading(true);
        for (Map.Entry<String,String> j:
                map.entrySet() ) {
            Log.d("--BaseNetTAG", "DriverCancelApi: "+j.getKey()+" : "+j.getValue());
        }
        gitHubService.DriverCancel("Bearer " + sharedPrefence.Getvalue(SharedPrefence.AccessToken), map, headerMap).enqueue((Callback<BaseResponse>) baseModelCallBackListener);
    }

    public void RateDriver(HashMap<String, String> map) {
        setIsLoading(true);
        for (Map.Entry<String,String> j:
                map.entrySet() ) {
            Log.d("--BaseNetTAG", "RateDriver: "+j.getKey()+" : "+j.getValue());
        }
        gitHubService.RateDriver("Bearer " + sharedPrefence.Getvalue(SharedPrefence.AccessToken), map, headerMap).enqueue((Callback<BaseResponse>) baseModelCallBackListener);
    }

    public void DriverCancelReasonApi(String arrivedStatus) {
        setIsLoading(true);

        gitHubService.driverCancelReasonApi("Bearer " + sharedPrefence.Getvalue(SharedPrefence.AccessToken), arrivedStatus, headerMap).enqueue((Callback<BaseResponse>) baseModelCallBackListener);
    }


    public void getSingleHistory(String id) {
        gitHubService.SingleHistory("Bearer " + sharedPrefence.Getvalue(SharedPrefence.AccessToken), id, headerMap).enqueue((Callback<BaseResponse>) baseModelCallBackListener);
    }

    public void WalletHIstory() {
        setIsLoading(true);
        gitHubService.WalletHIstory("Bearer " + sharedPrefence.Getvalue(SharedPrefence.AccessToken), headerMap).enqueue((Callback<BaseResponse>) baseModelCallBackListener);
    }

    public void RefferalCode(HashMap<String, String> map) {
        setIsLoading(true);
        gitHubService.RefferalCode("Bearer " + sharedPrefence.Getvalue(SharedPrefence.AccessToken), map, headerMap).enqueue((Callback<BaseResponse>) baseModelCallBackListener);
    }

    public void getRefferal() {
        setIsLoading(true);
        gitHubService.getRefferal("Bearer " + sharedPrefence.Getvalue(SharedPrefence.AccessToken), headerMap).enqueue((Callback<BaseResponse>) baseModelCallBackListener);
    }

    public void getDocuments() {
        setIsLoading(true);
        gitHubService.getDocuments("Bearer " + sharedPrefence.Getvalue(SharedPrefence.AccessToken), headerMap).enqueue((Callback<BaseResponse>) baseModelCallBackListener);
    }

    public void getTodayEarningsApi() {
        setIsLoading(true);
        gitHubService.getTodaEarnigs("Bearer " + sharedPrefence.Getvalue(SharedPrefence.AccessToken), headerMap).enqueue((Callback<BaseResponse>) baseModelCallBackListener);
    }

    public void getWeekEarningsApi() {
        setIsLoading(true);
        gitHubService.getWeekEarnings("Bearer " + sharedPrefence.Getvalue(SharedPrefence.AccessToken), headerMap).enqueue((Callback<BaseResponse>) baseModelCallBackListener);
    }

    public void getWeekEarningsWithNumber(String number) {
        setIsLoading(true);
        gitHubService.getWeekEarningsWithNumber("Bearer " + sharedPrefence.Getvalue(SharedPrefence.AccessToken), number, headerMap).enqueue((Callback<BaseResponse>) baseModelCallBackListener);
    }

    public void AddMoneyApi(HashMap<String, String> map) {
        setIsLoading(true);
        gitHubService.addMoney("Bearer " + sharedPrefence.Getvalue(SharedPrefence.AccessToken), map, headerMap).enqueue((Callback<BaseResponse>) baseModelCallBackListener);
    }

    public void CompanyKey(HashMap<String, String> map) {
        setIsLoading(true);
        gitHubService.CompanyKey(map, headerMap).enqueue((Callback<BaseResponse>) baseModelCallBackListener);
    }


    public void DocumentUpload() {
        setIsLoading(true);
        gitHubService.DocumentUpload(requestbody, body_profile_pic, "Bearer " + sharedPrefence.Getvalue(SharedPrefence.AccessToken), headerMap).enqueue((Callback<BaseResponse>) baseModelCallBackListener);
    }

    public void SOSDelete(String id) {
        gitHubService.SOSDelete("Bearer " + sharedPrefence.Getvalue(SharedPrefence.AccessToken), id, headerMap).enqueue((Callback<BaseResponse>) baseModelCallBackListener);
    }

    public void SOS(String latitude, String longitude) {
        Log.e("AcessToken----", "token--" + sharedPrefence.Getvalue(SharedPrefence.AccessToken));
        gitHubService.SOS("Bearer " + sharedPrefence.Getvalue(SharedPrefence.AccessToken), latitude, longitude, headerMap).enqueue((Callback<BaseResponse>) baseModelCallBackListener);
    }

    public void SOSAdd(HashMap<String, String> map) {
        gitHubService.SOSADD("Bearer " + sharedPrefence.Getvalue(SharedPrefence.AccessToken), map, headerMap).enqueue((Callback<BaseResponse>) baseModelCallBackListener);
    }

    public void FAQLIST(String latitude, String longitude) {
        Log.e("AcessToken----", "token--" + sharedPrefence.Getvalue(SharedPrefence.AccessToken));
        gitHubService.getFAQ("Bearer " + sharedPrefence.Getvalue(SharedPrefence.AccessToken), latitude, longitude, headerMap).enqueue((Callback<BaseResponse>) baseModelCallBackListener);
    }

    public void EarningsReport(String fromDate, String toDate) {
        Log.e("AcessToken----", "token--" + sharedPrefence.Getvalue(SharedPrefence.AccessToken));
        gitHubService.EarningsReport("Bearer " + sharedPrefence.Getvalue(SharedPrefence.AccessToken), fromDate, toDate, headerMap).enqueue((Callback<BaseResponse>) baseModelCallBackListener);
    }


    public void validateMobile(HashMap<String, String> map) {
        gitHubService.ValidateMobile("Bearer " + sharedPrefence.Getvalue(SharedPrefence.AccessToken), map, headerMap).enqueue((Callback<User>) baseModelCallBackListener);
    }


    /**
     * Api callback to detect the Api response whether success or failure.
     */
    private Callback<T> baseModelCallBackListener = new Callback<T>() {
        @Override
        public void onResponse(Call<T> call, Response<T> response) {
            if (response.isSuccessful() && response.body() != null) {
                if (response.body().success || response.body().tokenType != null) {
                    onSuccessfulApi(mCurrentTaskId, response.body());
                } else {
                    if (response.message() != null) {
                        onFailureApi(mCurrentTaskId, new CustomException(response.code(), response.message()));
                    } else {
                        String errorMsg = CommonUtils.converErrors(response.errorBody());

                        Log.e("Response==", "respp===" + response.code());
                        if (TextUtils.isEmpty(errorMsg))
                            errorMsg = response.message();

                        onFailureApi(mCurrentTaskId, new CustomException(response.code(), errorMsg));
                    }

                }
            } else {
                String errorMsg = CommonUtils.converErrors(response.errorBody());
                Log.e("Response==", "respp111===" + response.code());
                if (TextUtils.isEmpty(errorMsg))
                    errorMsg = response.message();
                onFailureApi(mCurrentTaskId, new CustomException(response.code(), errorMsg));
            }

        }

        @Override
        public void onFailure(Call<T> call, Throwable t) {
            onFailureApi(mCurrentTaskId, new CustomException(t.getMessage()));
        }
    };

    public abstract HashMap<String, String> getMap();

    public ObservableBoolean getIsLoading() {
        return mIsLoading;
    }

    /**
     * @param isLoading contains whether loader is need ot not.
     */
    public void setIsLoading(boolean isLoading) {
        mIsLoading.set(isLoading);
    }

    /**
     * @param navigator is the inter
     */
    public void setNavigator(N navigator) {
        this.mNavigator = navigator;
    }

    public N getmNavigator() {
        return mNavigator;
    }


    @BindingAdapter({"bind:textfont"})
    public static void settextFont(TextView textView, String fontName) {
        textView.setTypeface(Typeface.createFromAsset(textView.getContext().getAssets(), "fonts/" + fontName));
    }

    @BindingAdapter({"bind:Editfont"})
    public static void setEditFont(EditText textView, String fontName) {
        textView.setTypeface(Typeface.createFromAsset(textView.getContext().getAssets(), "fonts/" + fontName));
    }


    @BindingAdapter({"bind:Buttonfont"})
    public static void setButtonFont(Button textView, String fontName) {
        textView.setTypeface(Typeface.createFromAsset(textView.getContext().getAssets(), "fonts/" + fontName));
    }

    /**
     * @param v hide the opened keyboard.
     */
    public void hideKeyboard(View v) {
        InputMethodManager imm = (InputMethodManager) v.getContext().getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

}
