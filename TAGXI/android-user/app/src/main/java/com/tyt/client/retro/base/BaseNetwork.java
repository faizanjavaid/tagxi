package com.tyt.client.retro.base;

import android.content.Context;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.databinding.BindingAdapter;
import androidx.databinding.ObservableBoolean;

import com.google.gson.Gson;
import com.tyt.client.app.MyApp;
import com.tyt.client.retro.GitHubService;
import com.tyt.client.retro.responsemodel.TranslationModel;
import com.tyt.client.retro.responsemodel.User;
import com.tyt.client.utilz.CommonUtils;
import com.tyt.client.utilz.SharedPrefence;
import com.tyt.client.utilz.exception.CustomException;

import java.util.HashMap;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Mahi in 2021.
 */

/**
 * Here is where all API calls connected with view and githubService
 **/
public abstract class BaseNetwork<T extends BaseResponse, N> implements Basecallback<T> {
    private static final String TAG = "BaseNetwork";
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
            Log.d("xxBaseNetTAG", "BaseNetwork: sharedPrefence ="+sharedPrefence.Getvalue(sharedPrefence.Getvalue(SharedPrefence.LANGUANGE)));
            translationModel = gson.fromJson(sharedPrefence.Getvalue(sharedPrefence.Getvalue(SharedPrefence.LANGUANGE)), TranslationModel.class);
        }

        if (MyApp.getmContext() != null) {
            sharedPrefence = new SharedPrefence(PreferenceManager.getDefaultSharedPreferences(MyApp.getmContext()), PreferenceManager.getDefaultSharedPreferences(MyApp.getmContext()).edit());
            headerMap.put("Content-Language", sharedPrefence.Getvalue(SharedPrefence.LANGUANGE));
        }
        headerMap.put("Accept", "application/json");
    }


    public void MakeComplaint(HashMap<String,String> map) {
        setIsLoading(true);
        gitHubService.submitcomplaints("Bearer "+sharedPrefence.Getvalue(SharedPrefence.AccessToken),map,headerMap).enqueue((Callback<BaseResponse>) baseModelCallBackListener);
    }


    public void  GetComplaintTypes(String comptype,HashMap<String,String> map) {
        setIsLoading(true);
        gitHubService.complainttypes("Bearer "+ sharedPrefence.Getvalue(SharedPrefence.AccessToken),comptype,headerMap).enqueue((Callback<BaseResponse>) baseModelCallBackListener);
    }

    public void GetRentalPacks(HashMap<String,String> map) {
        gitHubService.getrentalpacklist("Bearer " + sharedPrefence.Getvalue(SharedPrefence.AccessToken),map,headerMap).enqueue((Callback<BaseResponse>) baseModelCallBackListener);
    }

    public void ContactUS(HashMap<String,String> map) {
        gitHubService.contactus("Bearer " + sharedPrefence.Getvalue(SharedPrefence.AccessToken),map,headerMap).enqueue((Callback<BaseResponse>) baseModelCallBackListener);
    }

    public void AddFavLocation(HashMap<String,Object> map) {
        setIsLoading(true);
        gitHubService.addfavloc("Bearer " + sharedPrefence.Getvalue(SharedPrefence.AccessToken), map, headerMap).enqueue((Callback<BaseResponse>) baseModelCallBackListener);
    }

    public void DeleteFav(String FavID) {
        setIsLoading(true);
        gitHubService.deletefavitem("Bearer " + sharedPrefence.Getvalue(SharedPrefence.AccessToken), FavID, headerMap).enqueue((Callback<BaseResponse>) baseModelCallBackListener);
    }

    public void GetFavList() {
        setIsLoading(true);
        gitHubService.getFavList("Bearer " + sharedPrefence.Getvalue(SharedPrefence.AccessToken),headerMap).enqueue((Callback<BaseResponse>) baseModelCallBackListener);
    }


    public void smsSendApi(HashMap<String, String> map) {
        setIsLoading(true);
        for (Map.Entry<String,String> j:
                map.entrySet() ) {
            Log.d("xxBaseNetTAG", "smsSendApi: "+j.getKey()+" : "+j.getValue());
        }
        gitHubService.smsSend("Bearer " + sharedPrefence.Getvalue(SharedPrefence.AccessToken), map, headerMap).enqueue((Callback<BaseResponse>) baseModelCallBackListener);
    }

    public void smsHistoryApi(String id) {
        setIsLoading(true);

        Log.d("xxBaseNetTAG", "smsSendApi() called with: token ="+sharedPrefence.Getvalue(SharedPrefence.AccessToken)+" request-id "+id);
        gitHubService.smsHistory("Bearer " + sharedPrefence.Getvalue(SharedPrefence.AccessToken), id, headerMap).enqueue((Callback<BaseResponse>) baseModelCallBackListener);
    }

    public void getMapPolyline(String id) {
        setIsLoading(true);
        Log.d("xxBaseNetTAG", "smsSendApi() called with: token ="+sharedPrefence.Getvalue(SharedPrefence.AccessToken)+" request-id "+id);
        gitHubService.smsHistory("Bearer " + sharedPrefence.Getvalue(SharedPrefence.AccessToken), id, headerMap).enqueue((Callback<BaseResponse>) baseModelCallBackListener);
    }
    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    public void MobileLogin(HashMap<String,String> map){
        setIsLoading(true);
        for (Map.Entry<String,String> j:
                map.entrySet() ) {
            Log.d("xxBaseNetTAG", "MobileLogin: "+j.getKey()+" : "+j.getValue());
        }
        gitHubService.otplogin(map,headerMap).enqueue((Callback<BaseResponse>) baseModelCallBackListener);
    }

    public void CallLoginApi(HashMap<String,String> map) {
        setIsLoading(true);
        for (Map.Entry<String,String> j:
                map.entrySet() ) {
            Log.d("xxBaseNetTAG", "CallLoginApi: "+j.getKey()+" : "+j.getValue());
        }
        gitHubService.loginApi(map,headerMap).enqueue((Callback<BaseResponse>) baseModelCallBackListener);
    }

    public void SendRegisterOtp(HashMap<String, String> map) {
        setIsLoading(true);
        Log.d("xxBaseNetTAG", "SendRegisterOtp: ");
        gitHubService.sendOTP(map, headerMap).enqueue((Callback<BaseResponse>) baseModelCallBackListener);
    }

    public void SendLoginApi(HashMap<String, String> map) {
        setIsLoading(true);
        for (Map.Entry<String,String> j:
                map.entrySet() ) {
            Log.d("xxBaseNetTAG", "smsSendApi: "+j.getKey()+" : "+j.getValue());
        }
        gitHubService.loginApi(map, headerMap).enqueue((Callback<BaseResponse>) baseModelCallBackListener);
    }

    public void UserRegisterApi(HashMap<String, String> map) {
        setIsLoading(true);
        for (Map.Entry<String,String> j:
                map.entrySet() ) {
            Log.d("xxBaseNetTAG", "UserRegisterApi: "+j.getKey()+" : "+j.getValue());
        };
        gitHubService.userRegApi(map, headerMap).enqueue((Callback<BaseResponse>) baseModelCallBackListener);
    }

    public void getCountryList() {
        setIsLoading(true);
        Log.d("xxBaseNetTAG", "getCountryList: ");
        gitHubService.getCountryList(headerMap).enqueue((Callback<BaseResponse>) baseModelCallBackListener);
    }

    public void ValidateOtp(HashMap<String, String> map) {
        setIsLoading(true);
        Log.d("xxBaseNetTAG", "ValidateOtp: ");
        gitHubService.RegisterValidateOtp(map, headerMap).enqueue((Callback<BaseResponse>) baseModelCallBackListener);
    }


    public void getTypesApiii(String lat, String lng) {
        Log.d("xxBaseNetTAG", "getTypesApiii: ");
        gitHubService.getTypesApi("Bearer " + sharedPrefence.Getvalue(SharedPrefence.AccessToken), lat, lng, headerMap).enqueue((Callback<BaseResponse>) baseModelCallBackListener);
    }

    public void getSingleHistory(String id) {
        Log.d("xxBaseNetTAG", "getSingleHistory: ");
        gitHubService.SingleHistory("Bearer " + sharedPrefence.Getvalue(SharedPrefence.AccessToken), id, headerMap).enqueue((Callback<BaseResponse>) baseModelCallBackListener);
    }


    public void GetETAApi(HashMap<String, String> map) {
        setIsLoading(true);
        for (Map.Entry<String,String> j:
                map.entrySet() ) {
            Log.d("xxBaseNetTAG", "GetETAApi: "+j.getKey()+" : "+j.getValue());
        }
        gitHubService.getEtaApi("Bearer " + sharedPrefence.Getvalue(SharedPrefence.AccessToken), map, headerMap).enqueue((Callback<BaseResponse>) baseModelCallBackListener);
    }

    public void CreateReqApicall(HashMap<String, String> map) {
        setIsLoading(true);
        for (Map.Entry<String,String> j:
                map.entrySet() ) {
            Log.d("xxBaseNetTAG", "CreateReqApicall: "+j.getKey()+" : "+j.getValue());
        }
        gitHubService.CreateReqApi("Bearer " + sharedPrefence.Getvalue(SharedPrefence.AccessToken), map, headerMap).enqueue((Callback<BaseResponse>) baseModelCallBackListener);
    }


    public void UpdateUserProfile() {
        setIsLoading(true);
        Log.d("xxBaseNetTAG", "UpdateUserProfile: ");
        gitHubService.UpdateUserProfile(requestbody, body, "Bearer " + sharedPrefence.Getvalue(SharedPrefence.AccessToken), headerMap).enqueue((Callback<BaseResponse>) baseModelCallBackListener);
    }

    public void GetUserProfile() {
        setIsLoading(true);
        Log.d("xxBaseNetTAG", "GetUserProfile: ");
        gitHubService.GetUserProfile("Bearer " + sharedPrefence.Getvalue(SharedPrefence.AccessToken), headerMap).enqueue((Callback<BaseResponse>) baseModelCallBackListener);
    }

    public void GetUserHistory(String upcome,String complete,String Cancel) {
        setIsLoading(true);
        Log.d("xxBaseNetTAG", "GetUserHistory: ");
        gitHubService.GetUserHistory("Bearer " + sharedPrefence.Getvalue(SharedPrefence.AccessToken),upcome,complete,Cancel, headerMap).enqueue((Callback<BaseResponse>) baseModelCallBackListener);
    }


    public void GetCompletedHistory(String complete,Integer page) {
        setIsLoading(true);
        gitHubService.getcompletedhistory("Bearer " + sharedPrefence.Getvalue(SharedPrefence.AccessToken),complete,page).enqueue((Callback<BaseResponse>) baseModelCallBackListener);
    }

    public void GetCancelledHistory(String cancel,Integer page) {
        setIsLoading(true);
        gitHubService.getcancelledhistory("Bearer " + sharedPrefence.Getvalue(SharedPrefence.AccessToken),cancel,page).enqueue((Callback<BaseResponse>) baseModelCallBackListener);
    }

    public void GetUpcomingHistory(String upcome,Integer page) {
        setIsLoading(true);
        gitHubService.getupcominghistory("Bearer " + sharedPrefence.Getvalue(SharedPrefence.AccessToken),upcome,page).enqueue((Callback<BaseResponse>) baseModelCallBackListener);
    }

    public void ListCards() {
        setIsLoading(true);
        Log.d("xxBaseNetTAG", "ListCards: ");
        gitHubService.ListCards("Bearer " + sharedPrefence.Getvalue(SharedPrefence.AccessToken), headerMap).enqueue((Callback<BaseResponse>) baseModelCallBackListener);
    }

    public void UpdatePwd(HashMap<String, String> map) {
        setIsLoading(true);
        for (Map.Entry<String,String> j:
                map.entrySet() ) {
            Log.d("xxBaseNetTAG", "UpdatePwd: "+j.getKey()+" : "+j.getValue());
        }
        gitHubService.UpdatePwd("Bearer " + sharedPrefence.Getvalue(SharedPrefence.AccessToken), map, headerMap).enqueue((Callback<BaseResponse>) baseModelCallBackListener);
    }


    public void getTranslations() {
        setIsLoading(true);
        Log.d("xxBaseNetTAG", "getTranslations: ");
        gitHubService.getTranslationApi(headerMap).enqueue((Callback<BaseResponse>) baseModelCallBackListener);
    }

    public void paymentClientToken() {
        setIsLoading(true);
        Log.d("xxBaseNetTAG", "paymentClientToken: ");
        gitHubService.paymentClientToken("Bearer " + sharedPrefence.Getvalue(SharedPrefence.AccessToken), headerMap).enqueue((Callback<BaseResponse>) baseModelCallBackListener);
    }

    public void AddCardDetails(HashMap<String, String> map) {
        setIsLoading(true);
        for (Map.Entry<String,String> j:
                map.entrySet() ) {
            Log.d("xxBaseNetTAG", "AddCardDetails: "+j.getKey()+" : "+j.getValue());
        }
        gitHubService.AddCardDetails("Bearer " + sharedPrefence.Getvalue(SharedPrefence.AccessToken), map, headerMap).enqueue((Callback<BaseResponse>) baseModelCallBackListener);
    }


    public void DeletCardApi(String id) {
        setIsLoading(true);
        Log.d("xxBaseNetTAG", "DeletCardApi: ");
        gitHubService.DeletCard("Bearer " + sharedPrefence.Getvalue(SharedPrefence.AccessToken), id, headerMap).enqueue((Callback<BaseResponse>) baseModelCallBackListener);
    }

    public void LogoutApi() {
        setIsLoading(true);
        Log.d("xxBaseNetTAG", "LogoutApi: ");
        gitHubService.LogoutApi("Bearer " + sharedPrefence.Getvalue(SharedPrefence.AccessToken), headerMap).enqueue((Callback<BaseResponse>) baseModelCallBackListener);
    }

    public void RequestCancelApi(HashMap<String, String> map) {
        setIsLoading(true);
        for (Map.Entry<String,String> j:
                map.entrySet() ) {
            Log.d("xxxBaseNetTAG", "RequestCancelApi: "+j.getKey()+" : "+j.getValue());
        }
        gitHubService.RequestCancelApi("Bearer " + sharedPrefence.Getvalue(SharedPrefence.AccessToken), map, headerMap).enqueue((Callback<BaseResponse>) baseModelCallBackListener);
    }


    public void userCancelApi(HashMap<String, String> map) {
        setIsLoading(true);
        for (Map.Entry<String,String> j:
                map.entrySet() ) {
            Log.d("xxBaseNetTAG", "userCancelApi: "+j.getKey()+" : "+j.getValue());
        }
        gitHubService.UserCancel("Bearer " + sharedPrefence.Getvalue(SharedPrefence.AccessToken), map, headerMap).enqueue((Callback<BaseResponse>) baseModelCallBackListener);
    }

    public void userCancelReasonApi(String arrivedStatus) {
        setIsLoading(true);
        Log.d("xxBaseNetTAG", "userCancelReasonApi: ");
        gitHubService.userCancelReasonApi("Bearer " + sharedPrefence.Getvalue(SharedPrefence.AccessToken), arrivedStatus, headerMap).enqueue((Callback<BaseResponse>) baseModelCallBackListener);
    }

    public void RateDriver(HashMap<String, String> map) {
        setIsLoading(true);
        for (Map.Entry<String,String> j:
                map.entrySet() ) {
            Log.d("xxBaseNetTAG", "RateDriver: "+j.getKey()+" : "+j.getValue());
        }
        gitHubService.RateDriver("Bearer " + sharedPrefence.Getvalue(SharedPrefence.AccessToken), map, headerMap).enqueue((Callback<BaseResponse>) baseModelCallBackListener);
    }

    public void WalletHIstory() {
        setIsLoading(true);
        Log.d("xxBaseNetTAG", "WalletHIstory: ");
        gitHubService.WalletHIstory("Bearer " + sharedPrefence.Getvalue(SharedPrefence.AccessToken), headerMap).enqueue((Callback<BaseResponse>) baseModelCallBackListener);
    }

    public void DefaultCard(HashMap<String, String> map) {
        setIsLoading(true);
        for (Map.Entry<String,String> j:
                map.entrySet() ) {
            Log.d("xxBaseNetTAG", "DefaultCard: "+j.getKey()+" : "+j.getValue());
        }
        gitHubService.MakeCardDefault("Bearer " + sharedPrefence.Getvalue(SharedPrefence.AccessToken), map, headerMap).enqueue((Callback<BaseResponse>) baseModelCallBackListener);
    }

    public void RefferalCode(HashMap<String, String> map) {
        setIsLoading(true);
        for (Map.Entry<String,String> j:
                map.entrySet() ) {
            Log.d("xxBaseNetTAG", "RefferalCode: "+j.getKey()+" : "+j.getValue());
        }
        gitHubService.RefferalCode("Bearer " + sharedPrefence.Getvalue(SharedPrefence.AccessToken), map, headerMap).enqueue((Callback<BaseResponse>) baseModelCallBackListener);
    }

    public void getRefferal() {
        setIsLoading(true);
        Log.d("xxBaseNetTAG", "getRefferal: ");
        gitHubService.getRefferal("Bearer " + sharedPrefence.Getvalue(SharedPrefence.AccessToken), headerMap).enqueue((Callback<BaseResponse>) baseModelCallBackListener);
    }

    public void AddMoneyApi(HashMap<String, String> map) {
        setIsLoading(true);
        for (Map.Entry<String,String> j:
                map.entrySet() ) {
            Log.d("xxBaseNetTAG", "AddMoneyApi: "+j.getKey()+" : "+j.getValue());
        }
        gitHubService.addMoney("Bearer " + sharedPrefence.Getvalue(SharedPrefence.AccessToken), map, headerMap).enqueue((Callback<BaseResponse>) baseModelCallBackListener);
    }

    public void CompanyKey(HashMap<String, String> map) {
        setIsLoading(true);
        for (Map.Entry<String,String> j:
                map.entrySet() ) {
            Log.d("xxBaseNetTAG", "CompanyKey: "+j.getKey()+" : "+j.getValue());
        }
        gitHubService.CompanyKey(map, headerMap).enqueue((Callback<BaseResponse>) baseModelCallBackListener);
    }

    public void SOSDelete(String id) {
        Log.d("xxBaseNetTAG", "SOSDelete() called with: id = [" + id + "]"+" headerMap ="+headerMap);
        gitHubService.SOSDelete("Bearer " + sharedPrefence.Getvalue(SharedPrefence.AccessToken), id, headerMap).enqueue((Callback<BaseResponse>) baseModelCallBackListener);
    }

    public void SOS(String latitude, String longitude) {
        Log.e("xxBaseNetTAG", "token--" + sharedPrefence.Getvalue(SharedPrefence.AccessToken)+" headerMap ="+headerMap +" latitude ="+latitude+" longitude ="+longitude);

        gitHubService.SOS("Bearer " + sharedPrefence.Getvalue(SharedPrefence.AccessToken), latitude, longitude, headerMap).enqueue((Callback<BaseResponse>) baseModelCallBackListener);
    }

    public void SOSAdd(HashMap<String, String> map) {
        for (Map.Entry<String,String> j:
                map.entrySet() ) {
            Log.d("xxBaseNetTAG", "SOSAdd: "+j.getKey()+" : "+j.getValue());
        }
        gitHubService.SOSADD("Bearer " + sharedPrefence.Getvalue(SharedPrefence.AccessToken), map, headerMap).enqueue((Callback<BaseResponse>) baseModelCallBackListener);
    }

    public void validateMobile(HashMap<String, String> map) {
        for (Map.Entry<String,String> j:
                map.entrySet() ) {
            Log.d("xxBaseNetTAG", "validateMobile: "+j.getKey()+" : "+j.getValue());
        }
        gitHubService.ValidateMobile("Bearer " + sharedPrefence.Getvalue(SharedPrefence.AccessToken), map, headerMap).enqueue((Callback<User>) baseModelCallBackListener);
    }

    public void GetPromoCodes() {
        Log.d("xxBaseNetTAG", "GetPromoCodes: ");
        gitHubService.PromoCOdes("Bearer " + sharedPrefence.Getvalue(SharedPrefence.AccessToken), headerMap).enqueue((Callback<BaseResponse>) baseModelCallBackListener);
    }

    public void GetPromoCodesQuery(String code) {
        Log.d("xxBaseNetTAG", "GetPromoCodesQuery: ");
        gitHubService.PromoCOdesQuery("Bearer " + sharedPrefence.Getvalue(SharedPrefence.AccessToken), code, headerMap).enqueue((Callback<BaseResponse>) baseModelCallBackListener);
    }


    public void FAQLIST(String latitude, String longitude) {
        Log.e("xxBaseNetTAG", "token--" + sharedPrefence.Getvalue(SharedPrefence.AccessToken));
        gitHubService.getFAQ("Bearer " + sharedPrefence.Getvalue(SharedPrefence.AccessToken), latitude, longitude, headerMap).enqueue((Callback<BaseResponse>) baseModelCallBackListener);
    }


    /**
     * Api callback to detect the Api response whether success or failure.
     */
    private final Callback<T> baseModelCallBackListener = new Callback<T>() {
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
                        Log.e("Response==", "respp===" + errorMsg);
                        if (TextUtils.isEmpty(errorMsg))
                            errorMsg = response.message();
                        onFailureApi(mCurrentTaskId, new CustomException(response.code(), errorMsg));
                    }
                }
            } else {
                String errorMsg = CommonUtils.converErrors(response.errorBody());
                Log.e("Response==", "respp111===" + errorMsg);
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
