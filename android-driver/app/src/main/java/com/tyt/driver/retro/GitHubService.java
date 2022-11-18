package com.tyt.driver.retro;

import com.tyt.driver.retro.base.BaseResponse;
import com.tyt.driver.retro.responsemodel.User;
import com.tyt.driver.utilz.Constants;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.HeaderMap;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by root on 9/25/17.
 */

public interface GitHubService {

    @FormUrlEncoded
    @POST(Constants.URL.Submit_Complaint)
    Call<BaseResponse> submitcomplaints(@Header("Authorization") String bearer, @FieldMap Map<String, String> options, @HeaderMap Map<String, String> headerMap);

    @GET(Constants.URL.Complaint_Types)
    Call<BaseResponse> complainttypes(@Header("Authorization") String bearer, @Query("complaint_type") String comptype, @HeaderMap Map<String, String> headerMap);

    @FormUrlEncoded
    @POST(Constants.URL.instant_ride)
    Call<BaseResponse> makeinsride(@Header("Authorization") String bearer,@FieldMap Map<String,Object> options,@HeaderMap Map<String,String> headerMap);

    @FormUrlEncoded
    @POST(Constants.URL.contact_us)
    Call<BaseResponse> contactus(@Header("Authorization") String bearer, @FieldMap Map<String,String> options,@HeaderMap Map<String,String> headerMap);

    @FormUrlEncoded
    @POST(Constants.URL.smsSendApi)
    Call<BaseResponse> smsSend(@Header("Authorization") String bearer, @FieldMap Map<String, String> options, @HeaderMap
            Map<String, String> headerMap);

    @GET(Constants.URL.smsHistoryApi)
    Call<BaseResponse> smsHistory(@Header("Authorization") String bearer, @Path("id") String id, @HeaderMap
            Map<String, String> headerMap);

    @FormUrlEncoded
    @POST(Constants.URL.Validate_mobile_login)
    Call<BaseResponse> otplogin(@FieldMap Map<String, String> options, @HeaderMap Map<String, String> headerMap);

    @FormUrlEncoded
    @POST(Constants.URL.SENDOTP)
    Call<BaseResponse> sendOTP(@FieldMap Map<String, String> options, @HeaderMap Map<String, String> headerMap);

    @FormUrlEncoded
    @POST(Constants.URL.LOGIN)
    Call<BaseResponse> loginApi(@FieldMap Map<String, String> options, @HeaderMap Map<String, String> headerMap);

    @FormUrlEncoded
    @POST(Constants.URL.USERREGISTER)
    Call<BaseResponse> userRegApi(@FieldMap Map<String, String> options, @HeaderMap Map<String, String> headerMap);

    @GET(Constants.URL.GETCOUNTRYLIST)
    Call<BaseResponse> getCountryList(@HeaderMap Map<String, String> headerMap);

    @GET(Constants.URL.GetCarTypes)
    Call<BaseResponse> getTypesApi(@Path("lat") String Lat, @Path("lng") String Lng, @HeaderMap Map<String, String> headerMap);


    @FormUrlEncoded
    @POST(Constants.URL.validateOtp)
    Call<BaseResponse> RegisterValidateOtp(@FieldMap Map<String, String> options, @HeaderMap Map<String, String> headerMap);


    @FormUrlEncoded
    @POST(Constants.URL.ReqETA)
    Call<BaseResponse> getEtaApi(@Header("Authorization") String bearer, @FieldMap Map<String, String> options, @HeaderMap Map<String, String> headerMap);

    @FormUrlEncoded
    @POST(Constants.URL.CreateRequest)
    Call<BaseResponse> CreateReqApi(@Header("Authorization") String bearer, @FieldMap Map<String, String> options, @HeaderMap Map<String, String> headerMap);

    @Multipart
    @POST(Constants.URL.UpdateProfile)
    Call<BaseResponse> UpdateUserProfile(
            @PartMap() Map<String, RequestBody> partMap,
            @Part MultipartBody.Part file, @Header("Authorization") String Token, @HeaderMap Map<String, String> headerMap);


    @GET(Constants.URL.GetUserProfile)
    Call<BaseResponse> GetUserProfile(@Header("Authorization") String bearer, @HeaderMap Map<String, String> headerMap);

    @POST(Constants.URL.ONLINOFFLINE)
    Call<BaseResponse> OnlineOfflineStatus(@Header("Authorization") String bearer, @HeaderMap Map<String, String> headerMap);


    @GET(Constants.URL.userHistory)
    Call<BaseResponse> GetUserHistory(@Header("Authorization") String bearer, @Query("is_completed") String done, @Query("is_cancelled") String cancel, @HeaderMap Map<String, String> headerMap);

    @GET(Constants.URL.userHistory)
    Call<BaseResponse> getcompletedhistory(@Header("Authorization") String bearer, @Query("is_completed") String complete, @Query("page") Integer page);

    @GET(Constants.URL.userHistory)
    Call<BaseResponse> getcancelledhistory(@Header("Authorization") String bearer, @Query("is_cancelled") String cancel, @Query("page") Integer page);

    @GET(Constants.URL.list_cards)
    Call<BaseResponse> ListCards(@Header("Authorization") String bearer, @HeaderMap Map<String, String> headerMap);

    @FormUrlEncoded
    @POST(Constants.URL.UpdatePassword)
    Call<BaseResponse> UpdatePwd(@Header("Authorization") String bearer, @FieldMap Map<String, String> options, @HeaderMap Map<String, String> headerMap);

    @GET(Constants.URL.Translations)
    Call<BaseResponse> getTranslationApi(@HeaderMap Map<String, String> headerMap);

    @GET(Constants.URL.CLIENT_TOKEN)
    Call<BaseResponse> paymentClientToken(@Header("Authorization") String bearer, @HeaderMap Map<String, String> headerMap);

    @FormUrlEncoded
    @POST(Constants.URL.ADDCARD)
    Call<BaseResponse> AddCardDetails(@Header("Authorization") String bearer, @FieldMap Map<String, String> options, @HeaderMap
            Map<String, String> headerMap);

    @DELETE(Constants.URL.DELETECARD)
    Call<BaseResponse> DeletCard(@Header("Authorization") String bearer, @Path("card") String card_ID, @HeaderMap Map<String, String> headerMap);

    @POST(Constants.URL.LOGOUTURL)
    Call<BaseResponse> LogoutApi(@Header("Authorization") String bearer, @HeaderMap Map<String, String> headerMap);

    @GET(Constants.URL.CARMAKE)
    Call<BaseResponse> CARMAKE(@HeaderMap Map<String, String> headerMap);

    @GET(Constants.URL.SERVICE_LOC)
    Call<BaseResponse> SERVICELOC(@Query("company_key") String key, @HeaderMap Map<String, String> headerMap);


    @GET(Constants.URL.CARMODEL)
    Call<BaseResponse> CARMODEL(@Path("make_id") String makeID, @HeaderMap Map<String, String> headerMap);

    @GET(Constants.URL.SERVICETYPES)
    Call<BaseResponse> ServiceTypes(@Path("id") String makeID, @HeaderMap Map<String, String> headerMap);

    @FormUrlEncoded
    @POST(Constants.URL.REQUESTRESPOND)
    Call<BaseResponse> RequestRespond(@Header("Authorization") String bearer, @FieldMap Map<String, String> options, @HeaderMap
            Map<String, String> headerMap);

    @FormUrlEncoded
    @POST(Constants.URL.DriverArrived)
    Call<BaseResponse> DriverArrived(@Header("Authorization") String bearer, @FieldMap Map<String, String> options, @HeaderMap
            Map<String, String> headerMap);

    @FormUrlEncoded
    @POST(Constants.URL.DriverStarted)
    Call<BaseResponse> DriverStarted(@Header("Authorization") String bearer, @FieldMap Map<String, String> options, @HeaderMap
            Map<String, String> headerMap);

    @FormUrlEncoded
    @POST(Constants.URL.DriverEnd)
    Call<BaseResponse> DriverEnd(@Header("Authorization") String bearer, @FieldMap Map<String, String> options, @HeaderMap
            Map<String, String> headerMap);

    @FormUrlEncoded
    @POST(Constants.URL.DriverCancel)
    Call<BaseResponse> DriverCancel(@Header("Authorization") String bearer, @FieldMap Map<String, String> options, @HeaderMap
            Map<String, String> headerMap);

    @FormUrlEncoded
    @POST(Constants.URL.RATING)
    Call<BaseResponse> RateDriver(@Header("Authorization") String bearer, @FieldMap Map<String, String> options, @HeaderMap
            Map<String, String> headerMap);

    @GET(Constants.URL.CANCELREASON)
    Call<BaseResponse> driverCancelReasonApi(@Header("Authorization") String bearer, @Query("arrived") String arrivedStatus, @HeaderMap Map<String, String> headerMap);

    @GET(Constants.URL.SingleHistory)
    Call<BaseResponse> SingleHistory(@Header("Authorization") String bearer, @Path("id") String id, @HeaderMap Map<String, String> headerMap);

    @GET(Constants.URL.WalletHistory)
    Call<BaseResponse> WalletHIstory(@Header("Authorization") String bearer, @HeaderMap Map<String, String> headerMap);

    @FormUrlEncoded
    @POST(Constants.URL.MakeCardDefault)
    Call<BaseResponse> MakeCardDefault(@Header("Authorization") String bearer, @FieldMap Map<String, String> options, @HeaderMap
            Map<String, String> headerMap);


    @FormUrlEncoded
    @POST(Constants.URL.Refferal)
    Call<BaseResponse> RefferalCode(@Header("Authorization") String bearer, @FieldMap Map<String, String> options, @HeaderMap
            Map<String, String> headerMap);

    @GET(Constants.URL.GetRefferal)
    Call<BaseResponse> getRefferal(@Header("Authorization") String bearer, @HeaderMap Map<String, String> headerMap);

    @GET(Constants.URL.DocumentsNeed)
    Call<BaseResponse> getDocuments(@Header("Authorization") String bearer, @HeaderMap Map<String, String> headerMap);

    @GET(Constants.URL.TodayEarnings)
    Call<BaseResponse> getTodaEarnigs(@Header("Authorization") String bearer, @HeaderMap Map<String, String> headerMap);

    @GET(Constants.URL.WeekEarnings)
    Call<BaseResponse> getWeekEarnings(@Header("Authorization") String bearer, @HeaderMap Map<String, String> headerMap);

    @GET(Constants.URL.WeekEarnings)
    Call<BaseResponse> getWeekEarningsWithNumber(@Header("Authorization") String bearer, @Query("week_number") String number, @HeaderMap Map<String, String> headerMap);


    @Multipart
    @POST(Constants.URL.DocumentsUpload)
    Call<BaseResponse> DocumentUpload(@PartMap() Map<String, RequestBody> partMap,
                                      @Part MultipartBody.Part profile_picture, @Header("Authorization") String bearer, @HeaderMap Map<String, String> headerMap);

    @FormUrlEncoded
    @POST(Constants.URL.AddMoney)
    Call<BaseResponse> addMoney(@Header("Authorization") String bearer, @FieldMap Map<String, String> options, @HeaderMap
            Map<String, String> headerMap);

    @FormUrlEncoded
    @POST(Constants.URL.CompanyKey)
    Call<BaseResponse> CompanyKey(@FieldMap Map<String, String> options, @HeaderMap
            Map<String, String> headerMap);

    @GET(Constants.URL.SOS)
    Call<BaseResponse> SOS(@Header("Authorization") String bearer, @Path("lat") String Lat, @Path("lng") String Lng, @HeaderMap Map<String, String> headerMap);

    @POST(Constants.URL.SOSDelete)
    Call<BaseResponse> SOSDelete(@Header("Authorization") String bearer, @Path("id") String id, @HeaderMap Map<String, String> headerMap);

    @FormUrlEncoded
    @POST(Constants.URL.SOSAdd)
    Call<BaseResponse> SOSADD(@Header("Authorization") String bearer, @FieldMap Map<String, String> options, @HeaderMap
            Map<String, String> headerMap);


    @GET(Constants.URL.EarningsReport)
    Call<BaseResponse> EarningsReport(@Header("Authorization") String bearer, @Path("from_date") String fromDate, @Path("to_date") String toDate, @HeaderMap Map<String, String> headerMap);


    @GET(Constants.URL.FAQ)
    Call<BaseResponse> getFAQ(@Header("Authorization") String bearer, @Path("lat") String Lat, @Path("lng") String Lng, @HeaderMap Map<String, String> headerMap);

    @FormUrlEncoded
    @POST(Constants.URL.ValidateMobile)
    Call<User> ValidateMobile(@Header("Authorization") String bearer, @FieldMap Map<String, String> options, @HeaderMap
            Map<String, String> headerMap);
}

