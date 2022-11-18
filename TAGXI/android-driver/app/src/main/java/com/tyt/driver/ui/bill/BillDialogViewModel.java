package com.tyt.driver.ui.bill;

import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableFloat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.tyt.driver.R;
import com.tyt.driver.app.MyApp;
import com.tyt.driver.retro.GitHubService;
import com.tyt.driver.retro.base.BaseNetwork;
import com.tyt.driver.retro.base.BaseResponse;
import com.tyt.driver.retro.responsemodel.EndTripData;
import com.tyt.driver.retro.responsemodel.ProfileModel;
import com.tyt.driver.retro.responsemodel.RequestBill;
import com.tyt.driver.retro.responsemodel.tripRequest.OnTripRequest;
import com.tyt.driver.retro.responsemodel.tripRequest.TripRequestData;
import com.tyt.driver.utilz.CommonUtils;
import com.tyt.driver.utilz.SharedPrefence;
import com.tyt.driver.utilz.exception.CustomException;

import java.util.HashMap;

/**
 * Created by root on 12/28/17.
 */

public class BillDialogViewModel extends BaseNetwork<BaseResponse, BillDialogNavigator> {

    /*public static final String card="Card";
    public static final String cash="Cash";
    public static final String wallet="Wallet";

    public static final String zero="0";
    public static final String one="1";*/

    public static String pay_type = "";

    public ObservableField<String> payopt = new ObservableField<>();

    public ObservableField<String> btripid = new ObservableField<>();
    public ObservableField<String> btripdate = new ObservableField<>();

    public ObservableFloat bdriverating = new ObservableFloat(0);
    public ObservableField<String> bcar_img = new ObservableField<>();
    public ObservableField<String> bdetail_car_type = new ObservableField<>();
    public ObservableField<String> bdetail_car_desc = new ObservableField<>();

    public ObservableField<String> btotal_distance = new ObservableField<>();
    public ObservableField<String> btotal_duration = new ObservableField<>();

    public ObservableField<String> bstarttime = new ObservableField<>();
    public ObservableField<String> bendtime = new ObservableField<>();


    public ObservableField<String> pickupAddress = new ObservableField<>();
    public ObservableField<String> dropAddress = new ObservableField<>();
    public ObservableField<String> driverName = new ObservableField<>();
    public ObservableField<String> driverImage = new ObservableField<>();

    public ObservableField<String> basePrice = new ObservableField<>();
    public ObservableField<String> distance = new ObservableField<>();
    public ObservableField<String> taxAmount = new ObservableField<>();
    public ObservableField<String> totalAmount = new ObservableField<>();
    public ObservableField<Float> rating = new ObservableField<>();
    public ObservableField<String> distPrice = new ObservableField<>();
    public ObservableField<String> timePrice = new ObservableField<>();
    public ObservableField<String> driverCommision = new ObservableField<>();
    public ObservableField<String> adminCommision = new ObservableField<>();
    public ObservableField<String> cancelFee = new ObservableField<>();

    public ObservableBoolean show_airport_fee = new ObservableBoolean(false);
    public ObservableField<String> airport_fee = new ObservableField<>();


    public BillDialogViewModel(GitHubService gitHubService, SharedPrefence sharedPrefence, Gson gson) {
        super(gitHubService, sharedPrefence, gson);
    }

    @BindingAdapter("app:billdriverImg")
    public static void loadDriverImg(ImageView billimageView, String bdriverimgurl) {
        if (bdriverimgurl != null) {
            Glide.with(billimageView.getContext()).load(Uri.parse(bdriverimgurl)).into(billimageView);
        }
    }

    @BindingAdapter("app:billcarImg")
    public static void loadCarImg(ImageView bimageView, String billcarimgurl) {
        if (billcarimgurl != null) {
            Glide.with(bimageView.getContext()).load(Uri.parse(billcarimgurl)).into(bimageView);
        }
        Log.d("Car Img", "loadCarImg: " + billcarimgurl);
    }


    @Override
    public void onSuccessfulApi(long taskId, BaseResponse response) {

    }

    @Override
    public void onFailureApi(long taskId, CustomException e) {

    }

    @Override
    public HashMap<String, String> getMap() {
        return null;
    }

    //Response from requetINPRogrss
    public void setValues(ProfileModel profileModel, ImageView userImg) {

        OnTripRequest onTripRequest = profileModel.getOnTripRequest();

        if (onTripRequest != null) {
            TripRequestData tripRequestData = onTripRequest.getData();
            if (tripRequestData != null) {

                btripdate.set((String) tripRequestData.getTripStartTime());
                btripid.set(tripRequestData.getRequestNumber());
                bdriverating.set((float) tripRequestData.getDriverRated());
                bdetail_car_type.set(tripRequestData.getVehicleTypeName());
                bdetail_car_desc.set(tripRequestData.getCar_make_name() + "-" + tripRequestData.getCar_model_name());
                btotal_distance.set(String.valueOf(tripRequestData.getTotalDistance() + " km"));
                btotal_duration.set(minitueToHr(tripRequestData.getTotalTime()));
                bstarttime.set(tripRequestData.getTripStartTime().toString().substring(9));
                bendtime.set(tripRequestData.getCompletedAt().toString().substring(9));
                bcar_img.set(tripRequestData.getVehicleTypeImage());

                pickupAddress.set(tripRequestData.getPickAddress());
                dropAddress.set(tripRequestData.getDropAddress());
                rating.set(profileModel.getRating());
                driverName.set(tripRequestData.getUserDetail().getUserDetailData().getName());
                driverImage.set(tripRequestData.getUserDetail().getUserDetailData().getProfilePicture());
                payopt.set(tripRequestData.getPaymentOpt());

                RequestBill requestBill = tripRequestData.getRequestBill();
                if (requestBill != null) {
                    RequestBill.RequestBillData requestBillData = requestBill.getData();
                    String currency = requestBillData.getRequestedCurrencyCode();
                    airport_fee.set(currency + " " + CommonUtils.doubleDecimalFromat(profileModel.getOnTripRequest().getData().getRequestBill().getData().getAirport_fee()));
                    show_airport_fee.set(profileModel.getOnTripRequest().getData().getRequestBill().getData().getAirport_fee() > 0);
                    basePrice.set(currency + " " + CommonUtils.doubleDecimalFromat(requestBillData.getBasePrice()));
                    totalAmount.set(currency + " " + CommonUtils.doubleDecimalFromat(requestBillData.getTotalAmount()));
                    taxAmount.set(currency + " " + CommonUtils.doubleDecimalFromat(requestBillData.getServiceTax()));

                    distPrice.set(currency + " " + CommonUtils.doubleDecimalFromat(requestBillData.getDistancePrice()));
                    timePrice.set(currency + " " + CommonUtils.doubleDecimalFromat(requestBillData.getTimePrice()));
                    cancelFee.set(currency + " " + CommonUtils.doubleDecimalFromat(requestBillData.getCancellationFee()));
                    driverCommision.set(currency + " " + CommonUtils.doubleDecimalFromat(requestBillData.getDriverCommision()));
                    adminCommision.set(currency + " " + CommonUtils.doubleDecimalFromat(requestBillData.getAdminCommision()));
                }


            }


            if ("1".equals(payopt.get())) {
                pay_type = "Cash";
            } else if ("2".equals(payopt.get())) {
                pay_type = "Wallet";
            } else if ("0".equals(payopt.get())) {
                pay_type = "Card";
            }


            Glide.with(MyApp.getmContext()).load(driverName.get()).
                    apply(RequestOptions.circleCropTransform().error(R.drawable.profile_place_holder).
                            placeholder(R.drawable.profile_place_holder)).into(userImg);


        }


    }

    private String minitueToHr(int mins) {
        try {
            if (mins > 59) {
                return String.valueOf(mins + " m");
            } else {
                int hr = mins / 60;
                int min = mins % 60;
                return hr + " hr:" + min + " m";
            }
        } catch (Exception e) {
            Log.e("xxxBillDialoModel", "minitueToHr: ");
            return "--";
        }
    }

    //Response from socket
    public void setValues(EndTripData profileModel, ImageView userImg) {

        String currency = profileModel.getRequestBill().getData().getRequestedCurrencyCode();

        payopt.set(profileModel.getPaymentOpt());


        if ("1".equals(payopt.get())) {
            pay_type = "Cash";
        } else if ("2".equals(payopt.get())) {
            pay_type = "Wallet";
        } else if ("0".equals(payopt.get())) {
            pay_type = "Card";
        }

        btripdate.set((String) profileModel.getTripStartTime());
        btripid.set(profileModel.getRequestNumber());
        bdriverating.set((float) profileModel.getDriverRated());
        bdetail_car_type.set(profileModel.getVehicleTypeName());
        bdetail_car_desc.set(profileModel.getCar_make_name() + "-" + profileModel.getCar_model_name());
        btotal_distance.set(String.valueOf(profileModel.getTotalDistance() + " km"));
        btotal_duration.set(minitueToHr(profileModel.getTotalTime()));
        bstarttime.set(profileModel.getTripStartTime().substring(9));
        bendtime.set(profileModel.getCompletedAt().substring(9));
        bcar_img.set(profileModel.getVehicleTypeImage());

        //new
        /*btripdate.set(profileModel.getTripStartTime());
        btripid.set(profileModel.getRequestNumber());
        bdriverating.set((float) profileModel.getDriverRated());
        bdetail_car_type.set(profileModel.getVehicleTypeName());
        bdetail_car_desc.set(profileModel.getCar_make_name() + "-" + profileModel.getCar_model_name());
        btotal_distance.set(String.valueOf(profileModel.getTotalDistance()));
        btotal_duration.set(String.valueOf(profileModel.getTotalTime().toString()));
        bstarttime.set(profileModel.getTripStartTime().substring(9));
        bendtime.set(profileModel.getCompletedAt().substring(9));
        bcar_img.set(profileModel.getVehicleTypeImage());*/
        //new

        pickupAddress.set(profileModel.getPickAddress());
        dropAddress.set(profileModel.getDropAddress());

        driverName.set(profileModel.getUserDetail().getUserDetailData().getName());
        driverImage.set(profileModel.getUserDetail().getUserDetailData().getProfilePicture());

        Glide.with(MyApp.getmContext()).load(driverName.get()).
                apply(RequestOptions.circleCropTransform().error(R.drawable.profile_place_holder).
                        placeholder(R.drawable.profile_place_holder)).into(userImg);

        airport_fee.set(currency + " " + CommonUtils.doubleDecimalFromat(profileModel.getRequestBill().getData().getAirport_fee()));
        show_airport_fee.set(profileModel.getRequestBill().getData().getAirport_fee() > 0);
        basePrice.set(currency + " " + CommonUtils.doubleDecimalFromat(profileModel.getRequestBill().getData().getBasePrice()));
        totalAmount.set(currency + " " + CommonUtils.doubleDecimalFromat(profileModel.getRequestBill().getData().getTotalAmount()));
        taxAmount.set(currency + " " + CommonUtils.doubleDecimalFromat(profileModel.getRequestBill().getData().getServiceTax()));

        distPrice.set(currency + " " + CommonUtils.doubleDecimalFromat(profileModel.getRequestBill().getData().getDistancePrice()));
        timePrice.set(currency + " " + CommonUtils.doubleDecimalFromat(profileModel.getRequestBill().getData().getTimePrice()));
        cancelFee.set(currency + " " + CommonUtils.doubleDecimalFromat(profileModel.getRequestBill().getData().getCancellationFee()));
        driverCommision.set(currency + " " + CommonUtils.doubleDecimalFromat(profileModel.getRequestBill().getData().getDriverCommision()));
        adminCommision.set(currency + " " + CommonUtils.doubleDecimalFromat(profileModel.getRequestBill().getData().getAdminCommision()));
    }

    public void onClickConfirm(View v) {
        getmNavigator().dismissDialog();
    }
}
