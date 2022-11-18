package com.tyt.client.ui.homeScreen.bill;

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
import com.tyt.client.R;
import com.tyt.client.app.MyApp;
import com.tyt.client.retro.GitHubService;
import com.tyt.client.retro.base.BaseNetwork;
import com.tyt.client.retro.base.BaseResponse;
import com.tyt.client.retro.responsemodel.ProfileModel;
import com.tyt.client.utilz.CommonUtils;
import com.tyt.client.utilz.SharedPrefence;
import com.tyt.client.utilz.exception.CustomException;

import java.util.HashMap;

/**
 * Created by root on 12/28/17.
 */

public class BillDialogViewModel extends BaseNetwork<BaseResponse, BillDialogNavigator> {

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
    public ObservableField<Float> rating = new ObservableField<>();
    public ObservableField<String> distance = new ObservableField<>();
    public ObservableField<String> taxAmount = new ObservableField<>();
    public ObservableField<String> totalAmount = new ObservableField<>();

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

    public void setValues(ProfileModel profileModel, ImageView userImg) {

        Log.d("xxxTAG", "setValues: " + profileModel.toString());
        String currency = profileModel.getOnTripRequest().getData().getRequestBill().getData().getRequestedCurrencyCode();

        payopt.set(profileModel.getOnTripRequest().getData().getPaymentOpt());

        if ("1".equals(payopt.get())) {
            pay_type = "Cash";
        } else if ("2".equals(payopt.get())) {
            pay_type = "Wallet";
        } else if ("0".equals(payopt.get())) {
            pay_type = "Card";
        }

        btripdate.set((String) profileModel.getOnTripRequest().getData().getTripStartTime());
        btripid.set(profileModel.getOnTripRequest().getData().getRequestNumber());
        bdriverating.set((float) profileModel.getOnTripRequest().getData().getDriverRated());
        bdetail_car_type.set(profileModel.getOnTripRequest().getData().getVehicleTypeName());
        bdetail_car_desc.set(profileModel.getOnTripRequest().getData().getCar_make_name() + "-" + profileModel.getOnTripRequest().getData().getCar_model_name());
        btotal_distance.set(profileModel.getOnTripRequest().getData().getTotalDistance() + " km");
        btotal_duration.set(minitueToHr(profileModel.getOnTripRequest().getData().getTotalTime()));
        bstarttime.set(profileModel.getOnTripRequest().getData().getTripStartTime().substring(9));
        bendtime.set(profileModel.getOnTripRequest().getData().getCompletedAt().substring(9));
        bcar_img.set(profileModel.getOnTripRequest().getData().getVehicleTypeImage());

        pickupAddress.set(profileModel.getOnTripRequest().getData().getPickAddress());
        dropAddress.set(profileModel.getOnTripRequest().getData().getDropAddress());

        driverName.set(profileModel.getOnTripRequest().getData().getDriverDetail().getData().getName());
        driverImage.set(profileModel.getOnTripRequest().getData().getDriverDetail().getData().getProfilePicture());
        Log.d("xxxBillTAG", "setValues: " + String.valueOf(profileModel.getRating()));
        rating.set(profileModel.getRating());
        Glide.with(MyApp.getmContext()).load(driverName.get()).
                apply(RequestOptions.circleCropTransform().error(R.drawable.profile_place_holder).
                        placeholder(R.drawable.profile_place_holder)).into(userImg);

        airport_fee.set(currency + " " + CommonUtils.doubleDecimalFromat(profileModel.getOnTripRequest().getData().getRequestBill().getData().getAirport_fee()));
        show_airport_fee.set(profileModel.getOnTripRequest().getData().getRequestBill().getData().getAirport_fee() > 0);
        basePrice.set(currency + " " + CommonUtils.doubleDecimalFromat(profileModel.getOnTripRequest().getData().getRequestBill().getData().getBasePrice()));
        totalAmount.set(currency + " " + CommonUtils.doubleDecimalFromat(profileModel.getOnTripRequest().getData().getRequestBill().getData().getTotalAmount()));
        taxAmount.set(currency + " " + CommonUtils.doubleDecimalFromat(profileModel.getOnTripRequest().getData().getRequestBill().getData().getServiceTax()));

        distPrice.set(currency + " " + CommonUtils.doubleDecimalFromat(profileModel.getOnTripRequest().getData().getRequestBill().getData().getDistancePrice()));
        timePrice.set(currency + " " + CommonUtils.doubleDecimalFromat(profileModel.getOnTripRequest().getData().getRequestBill().getData().getTimePrice()));
        cancelFee.set(currency + " " + CommonUtils.doubleDecimalFromat(profileModel.getOnTripRequest().getData().getRequestBill().getData().getCancellationFee()));
        driverCommision.set(currency + " " + CommonUtils.doubleDecimalFromat(profileModel.getOnTripRequest().getData().getRequestBill().getData().getDriverCommision()));
        adminCommision.set(currency + " " + CommonUtils.doubleDecimalFromat(profileModel.getOnTripRequest().getData().getRequestBill().getData().getAdminCommision()));
    }

    private String minitueToHr(int mins) {
        try {
            if (mins > 59) {
                return mins + " m";
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

    public void setValues(BaseResponse profileModel, ImageView userImg) {

        payopt.set(profileModel.result.getData().getPaymentOpt());

        if ("1".equals(payopt.get())) {
            pay_type = "Cash";
        } else if ("2".equals(payopt.get())) {
            pay_type = "Wallet";
        } else if ("0".equals(payopt.get())) {
            pay_type = "Card";
        }

        btripdate.set((String) profileModel.result.getData().getTripStartTime());
        btripid.set(profileModel.result.getData().getRequestNumber());
        //temp eror
        bdriverating.set(profileModel.result.getData().getDriverRated().floatValue());
        bdetail_car_type.set(profileModel.result.getData().getVehicleTypeName());
        bdetail_car_desc.set(profileModel.result.getData().getCar_make_name() + "-" + profileModel.result.getData().getCar_model_name());
        btotal_distance.set(String.valueOf(profileModel.result.getData().getTotalDistance()));
        btotal_duration.set(profileModel.result.getData().getTotalTime().toString());

        btotal_distance.set(profileModel.result.getData().getTotalDistance() + " km");
        btotal_duration.set(minitueToHr(profileModel.result.getData().getTotalTime().intValue()));

        bstarttime.set(profileModel.result.getData().getTripStartTime().toString().substring(9));
        bendtime.set(profileModel.result.getData().getCompletedAt().toString().substring(9));
        bcar_img.set(profileModel.result.getData().getVehicleTypeImage());

        pickupAddress.set(profileModel.result.getData().getPickAddress());
        dropAddress.set(profileModel.result.getData().getDropAddress());

        driverName.set(profileModel.result.getData().getUserDetail().getData().getName());
        driverImage.set(profileModel.result.getData().getUserDetail().getData().getProfilePicture());

        Glide.with(MyApp.getmContext()).load(driverName.get()).
                apply(RequestOptions.circleCropTransform().error(R.drawable.profile_place_holder).
                        placeholder(R.drawable.profile_place_holder)).into(userImg);

        String currency = profileModel.result.getData().getRequestBill().getData().getRequestedCurrencyCode();

        airport_fee.set(currency + " " + CommonUtils.doubleDecimalFromat(profileModel.result.getData().getRequestBill().getData().getAirport_fee()));
        show_airport_fee.set(profileModel.result.getData().getRequestBill().getData().getAirport_fee() > 0);
        basePrice.set(currency + " " + CommonUtils.doubleDecimalFromat(profileModel.result.getData().getRequestBill().getData().getBasePrice()));
        totalAmount.set(currency + " " + CommonUtils.doubleDecimalFromat(profileModel.result.getData().getRequestBill().getData().getTotalAmount()));
        taxAmount.set(currency + " " + CommonUtils.doubleDecimalFromat(profileModel.result.getData().getRequestBill().getData().getServiceTax()));

        distPrice.set(currency + " " + CommonUtils.doubleDecimalFromat(profileModel.result.getData().getRequestBill().getData().getDistancePrice()));
        timePrice.set(currency + " " + CommonUtils.doubleDecimalFromat(profileModel.result.getData().getRequestBill().getData().getTimePrice()));
        cancelFee.set(currency + " " + CommonUtils.doubleDecimalFromat(profileModel.result.getData().getRequestBill().getData().getCancellationFee()));
        driverCommision.set(currency + " " + CommonUtils.doubleDecimalFromat(profileModel.result.getData().getRequestBill().getData().getDriverCommision()));
        adminCommision.set(currency + " " + CommonUtils.doubleDecimalFromat(profileModel.result.getData().getRequestBill().getData().getAdminCommision()));
    }

    public void onClickConfirm(View v) {
        getmNavigator().dismissDialog();
    }
}
