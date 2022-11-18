package com.tyt.driver.ui.historyDetail;

import android.app.FragmentTransaction;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableFloat;


import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.tyt.driver.R;
import com.tyt.driver.retro.GitHubService;
import com.tyt.driver.retro.base.BaseNetwork;
import com.tyt.driver.retro.base.BaseResponse;
import com.tyt.driver.retro.responsemodel.HistoryDetailMOdel;
import com.tyt.driver.retro.responsemodel.HistoryModel;
import com.tyt.driver.retro.responsemodel.RequestBill;
import com.tyt.driver.retro.responsemodel.tripRequest.UserDetailData;
import com.tyt.driver.ui.homeScreen.makeComplaint.MakeCompFrag;
import com.tyt.driver.utilz.CommonUtils;
import com.tyt.driver.utilz.Constants;
import com.tyt.driver.utilz.SharedPrefence;
import com.tyt.driver.utilz.exception.CustomException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by root on 11/13/17.
 */

public class HistoryDetailViewModel extends BaseNetwork<BaseResponse, HistoryDetailNavigator> {
    private static final String TAG = "AddCardViewModel";

    public static final String completed = "Completed";
    public static final String cancelled = "Cancelled";
    public static final String upcoming = "Upcoming";

    public ObservableField<String> pay_type = new ObservableField<>();

    public ObservableField<String> payopt = new ObservableField<>();

    public ObservableField<String> tripid = new ObservableField<>();
    public ObservableField<String> tripdate = new ObservableField<>();
    public ObservableFloat driverating = new ObservableFloat(0);
    public ObservableField<String> car_img = new ObservableField<>();
    public ObservableField<String> detail_car_type = new ObservableField<>();
    public ObservableField<String> detail_car_desc = new ObservableField<>();

    public ObservableField<String> starttime = new ObservableField<>();
    public ObservableField<String> endtime = new ObservableField<>();

    public ObservableField<String> pickupAddress = new ObservableField<>();
    public ObservableField<String> dropAddress = new ObservableField<>();
    public ObservableField<String> driverName = new ObservableField<>();
    public ObservableField<String> driverImage = new ObservableField<>();

    public ObservableField<String> basePrice = new ObservableField<>();
    public ObservableField<String> distance = new ObservableField<>();
    public ObservableField<String> taxAmount = new ObservableField<>();
    public ObservableField<String> totalAmount = new ObservableField<>();

    public ObservableField<String> total_distance = new ObservableField<>();
    public ObservableField<String> total_duration = new ObservableField<>();

    public ObservableField<String> distPrice = new ObservableField<>();
    public ObservableField<String> timePrice = new ObservableField<>();
    public ObservableField<String> driverCommision = new ObservableField<>();
    public ObservableField<String> adminCommision = new ObservableField<>();
    public ObservableField<String> cancelFee = new ObservableField<>();
    public ObservableField<String> car_make = new ObservableField<>();
    public ObservableField<String> car_number = new ObservableField<>();

    public float rating;

    HashMap<String, String> map = new HashMap<>();

    List<HistoryModel> historyModels = new ArrayList<>();

    public ObservableBoolean isCompleted = new ObservableBoolean(false);
    public ObservableBoolean isCancelled = new ObservableBoolean(false);

    public ObservableField<String> IDD = new ObservableField<>();

    public ObservableBoolean show_airport_fee = new ObservableBoolean(false);
    public ObservableField<String> airport_fee = new ObservableField<>();

    public HistoryDetailViewModel(GitHubService gitHubService, SharedPrefence sharedPrefence, Gson gson) {
        super(gitHubService, sharedPrefence, gson);
    }

    @BindingAdapter("app:driverImg")
    public static void loadDriverImg(ImageView imageView, String driverimgurl){
        if (driverimgurl != null) {
            Glide.with(imageView.getContext()).load(Uri.parse(driverimgurl)).into(imageView);
        }
    }

    @BindingAdapter("app:carImg")
    public static void loadCarImg(ImageView imageView,String carimgurl){
        if (carimgurl != null) {
            Glide.with(imageView.getContext()).load(Uri.parse(carimgurl)).into(imageView);
        }
        Log.d("Car Img", "loadCarImg: "+carimgurl);
    }

    /**
     * called when API call is successful
     **/
    @Override
    public void onSuccessfulApi(long taskId, BaseResponse response) {
        setIsLoading(false);
        if (response.success) {
            String uuid = CommonUtils.ObjectToString(response.data);
            HistoryDetailMOdel uuidInstance = (HistoryDetailMOdel) CommonUtils.StringToObject(uuid, HistoryDetailMOdel.class);

            isCompleted.set(uuidInstance.getIsCompleted() == 1);
            isCancelled.set(uuidInstance.getIsCancelled() == 1);
            UserDetailData driverDetail = uuidInstance.getUserDetail().getUserDetailData();

            if (uuidInstance.getRequestBill() != null) {
                RequestBill.RequestBillData requestBill = uuidInstance.getRequestBill().getData();
                String currency = requestBill.getRequestedCurrencyCode();
                airport_fee.set(currency + " " + CommonUtils.doubleDecimalFromat(requestBill.getAirport_fee()));
                basePrice.set(currency + " " + CommonUtils.doubleDecimalFromat(requestBill.getBasePrice()));
                totalAmount.set(currency + " " + CommonUtils.doubleDecimalFromat(requestBill.getTotalAmount()));
                taxAmount.set(currency + " " + CommonUtils.doubleDecimalFromat(requestBill.getServiceTax()));
                distPrice.set(currency + " " + CommonUtils.doubleDecimalFromat(requestBill.getDistancePrice()));
                timePrice.set(currency + " " + CommonUtils.doubleDecimalFromat(requestBill.getTimePrice()));
                cancelFee.set(currency + " " + CommonUtils.doubleDecimalFromat(requestBill.getCancellationFee()));
                driverCommision.set(currency + " " + CommonUtils.doubleDecimalFromat(requestBill.getDriverCommision()));
                adminCommision.set(currency + " " + CommonUtils.doubleDecimalFromat(requestBill.getAdminCommision()));
            }


            if (uuidInstance.getRequestBill() != null) {
                show_airport_fee.set(uuidInstance.getRequestBill().getData().getAirport_fee()>0);
            }

            pickupAddress.set(uuidInstance.getPickAddress());
            dropAddress.set(uuidInstance.getDropAddress());

            payopt.set(uuidInstance.getPaymentOpt());

            if ("1".equals(payopt.get())) {
                pay_type.set("Cash");
            } else if ("2".equals(payopt.get())) {
                pay_type.set("Wallet");
            } else if ("0".equals(payopt.get())) {
                pay_type.set("Card");
            }

            driverName.set(driverDetail.getName());
            driverImage.set(driverDetail.getProfilePicture());
            car_img.set(uuidInstance.getVehicleTypeImage());
            car_make.set(uuidInstance.getCar_make_name());

            tripid.set(uuidInstance.getRequestNumber());
            tripdate.set(uuidInstance.getTripStartTime());
            driverating.set((float) uuidInstance.getDriverRated());

            detail_car_type.set(uuidInstance.getVehicleTypeName());
            detail_car_desc.set(uuidInstance.getCar_make_name() + "-" + uuidInstance.getCar_model_name());

            starttime.set(uuidInstance.getTripStartTime().substring(9));
            endtime.set(uuidInstance.getCompletedAt().substring(9));

            total_distance.set(String.valueOf(uuidInstance.getTotalDistance()+" km"));
            total_duration.set(minitueToHr(uuidInstance.getTotalTime()));
            Log.d("xxxHistoryDetaModel", "onSuccessfulApi: time ="+minitueToHr(uuidInstance.getTotalTime())+"|| distance = "+uuidInstance.getTotalDistance()+" km");

            //rating = Float.valueOf(driverDetail.getRating());

            //car_make.set(driverDetail.() + "(" + driverDetail.getVehicleTypeName() + ")");
           //car_number.set(driverDetail.getCarNumber());

//            Glide.with(MyApp.getmContext()).load(driverName.get()).
//                    apply(RequestOptions.circleCropTransform().error(R.drawable.profile_place_holder).
//                            placeholder(R.drawable.profile_place_holder)).into(userImg);
        }
    }

    private String minitueToHr(int mins) {
        try{
            if (mins>59){
                return String.valueOf(mins +" m");
            }else{
                int hr= mins/60;
                int min=mins%60;
                return hr+" hr:"+ min+" m";
            }
        }catch (Exception e){
            Log.e("xxxBillDialoModel", "minitueToHr: " );
            return "--";
        }
    }

    /**
     * called when API call fails
     **/
    @Override
    public void onFailureApi(long taskId, CustomException e) {
        setIsLoading(false);
        getmNavigator().showMessage(e.getMessage());
        if (e.getCode() == 401) {
            sharedPrefence.savevalue(SharedPrefence.AccessToken, "");
            sharedPrefence.savevalue(SharedPrefence.Name, "");
            sharedPrefence.savevalue(SharedPrefence.Email, "");
            sharedPrefence.savevalue(SharedPrefence.Password, "");
            sharedPrefence.savevalue(SharedPrefence.CountryCode, "");
            sharedPrefence.savevalue(SharedPrefence.ConfirmPassword, "");
            sharedPrefence.savevalue(SharedPrefence.PhoneNumber, "");
            sharedPrefence.savevalue(SharedPrefence.Profile, "");
            sharedPrefence.savevalue(SharedPrefence.ID, "");
            sharedPrefence.savevalue(Constants.selectedMakeID, "");
            sharedPrefence.savevalue(Constants.selectedModelID, "");
            sharedPrefence.savevalue(Constants.selectedModelName, "");
            sharedPrefence.savevalue(Constants.selectedMakeName, "");
            sharedPrefence.savevalue(Constants.selectedCarNumber, "");
            sharedPrefence.savevalue(Constants.selectedCarYear, "");
            sharedPrefence.savevalue(Constants.selectedColor, "");
            getmNavigator().openFromStart();
        }
    }

    /**
     * adds client_id, client_token to {@link HashMap} used for API calls
     **/
    @Override
    public HashMap<String, String> getMap() {
        return null;
    }


    public void getSingleHistoryApi(String idd) {
        if (getmNavigator().isNetworkConnected()) {
            setIsLoading(true);
            getSingleHistory(idd);
        } else getmNavigator().showNetworkMessage();
    }

    public void onCLickBack(View v) {
        getmNavigator().onClickBack();
    }

    public void makeComplaint(View v){
        getmNavigator().getBaseAct().getSupportFragmentManager()
                .beginTransaction()
                .disallowAddToBackStack()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .replace(R.id.fragment_frame, MakeCompFrag.newInstance(IDD.get(),Constants.history_comptype), HistoryDetailFrag.TAG)
                .commit();
    }
}
