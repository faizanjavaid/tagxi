package com.tyt.client.ui.homeScreen.historyDetail;

import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableFloat;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.tyt.client.R;
import com.tyt.client.retro.GitHubService;
import com.tyt.client.retro.base.BaseNetwork;
import com.tyt.client.retro.base.BaseResponse;
import com.tyt.client.retro.responsemodel.CancelReasonModel;
import com.tyt.client.retro.responsemodel.HistoryDetailMOdel;
import com.tyt.client.retro.responsemodel.HistoryModel;
import com.tyt.client.retro.responsemodel.tripRequest.ReqBillData;
import com.tyt.client.retro.responsemodel.tripRequest.UserDetailData;
import com.tyt.client.ui.homeScreen.makeComplaint.MakeCompFrag;
import com.tyt.client.utilz.CommonUtils;
import com.tyt.client.utilz.Constants;
import com.tyt.client.utilz.SharedPrefence;
import com.tyt.client.utilz.exception.CustomException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * Created by Mahi in 2021.
 */

public class HistoryDetailViewModel extends BaseNetwork<BaseResponse, HistoryDetailNavigator> {
    private static final String TAG = "AddCardViewModel";

    public static final String completed = "Completed";
    public static final String cancelled = "Cancelled";
    public static final String upcoming = "Upcoming";

    public ObservableField<String> his_pay_type = new ObservableField<>();

    public ObservableField<String> payopt = new ObservableField<>();

    public ObservableFloat driverating = new ObservableFloat(0);

    public ObservableField<String> tripid = new ObservableField<>();

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
    public ObservableField<String> car_img = new ObservableField<>();

    public ObservableField<String> detail_car_type = new ObservableField<>();
    public ObservableField<String> detail_car_desc = new ObservableField<>();

    public ObservableField<String> car_number = new ObservableField<>();
    public ObservableField<String> reasonText = new ObservableField<>();
    public ObservableField<String> reqId = new ObservableField<>();

    public ObservableField<String> tripdate = new ObservableField<>();
    public ObservableField<String> starttime = new ObservableField<>();
    public ObservableField<String> endtime = new ObservableField<>();

    public ObservableBoolean isCompleted = new ObservableBoolean(false);
    public ObservableBoolean isCancelled = new ObservableBoolean(false);
    public ObservableBoolean showCancelButton = new ObservableBoolean(false);

    public ObservableBoolean show_airport_fee = new ObservableBoolean(false);
    public ObservableField<String> airport_fee = new ObservableField<>();

    HashMap<String, String> hashMap = new HashMap<>();

    public ObservableField<String> IDD = new ObservableField<>();

    List<HistoryModel> historyModels = new ArrayList<>();

    public ObservableBoolean noItem = new ObservableBoolean(false);

    public HistoryDetailViewModel(GitHubService gitHubService, SharedPrefence sharedPrefence, Gson gson) {
        super(gitHubService, sharedPrefence, gson);
    }


    @BindingAdapter("app:driverImg")
    public static void loadDriverImg(ImageView drvimageView, String driverimgurl) {
        if (driverimgurl != null) {
            Glide.with(drvimageView.getContext()).load(Uri.parse(driverimgurl)).into(drvimageView);
        }
    }

    @BindingAdapter("app:carImg")
    public static void loadCarImg(ImageView carimageView, String carimgurl) {
        if (carimgurl != null) {
            Glide.with(carimageView.getContext()).load(Uri.parse(carimgurl)).into(carimageView);
        }
        Log.e("Car Img", "loadCarImg: " + carimgurl);
    }

    /**
     * called when API call is successful
     **/
    UserDetailData driverDetail;
    public List<CancelReasonModel> reasonModels = new ArrayList<>();


    @Override
    public void onSuccessfulApi(long taskId, BaseResponse response) {
        setIsLoading(false);
        if (response.success) {
            if (response.message.equalsIgnoreCase("cancellation_reasons_listed")) {
                reasonModels.clear();
                String reasonResp = CommonUtils.arrayToString((ArrayList<Object>) response.data);
                reasonModels.addAll(CommonUtils.stringToArray(reasonResp, CancelReasonModel[].class));
                getmNavigator().loadCancelAlert(reasonModels);
            } else {
                String uuid = CommonUtils.ObjectToString(response.data);

                HistoryDetailMOdel uuidInstance = (HistoryDetailMOdel) CommonUtils.StringToObject(uuid, HistoryDetailMOdel.class);
                if (uuidInstance.getDriverDetail() != null && uuidInstance.getDriverDetail().getData() != null) {
                    driverDetail = uuidInstance.getDriverDetail().getData();
                }

                if (uuidInstance.getRequestBill() != null) {
                    ReqBillData requestBill = uuidInstance.getRequestBill().getData();
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
                reqId.set(uuidInstance.getId());

                payopt.set(uuidInstance.getPaymentOpt());

                if ("1".equals(payopt.get())) {
                    his_pay_type.set("Cash");
                } else if ("2".equals(payopt.get())) {
                    his_pay_type.set("Wallet");
                } else if ("0".equals(payopt.get())) {
                    his_pay_type.set("Card");
                }

                tripid.set(uuidInstance.getRequestNumber());

                driverName.set(driverDetail.getName());
                driverImage.set(driverDetail.getProfilePicture());

                car_img.set(uuidInstance.getVehicleTypeimage());

                total_distance.set(uuidInstance.getTotalDistance() + " km".toString());
                total_duration.set(minitueToHr(uuidInstance.getTotalTime()));
                Log.d("xxxHistoryDetaModel", "onSuccessfulApi: time =" + minitueToHr(uuidInstance.getTotalTime()) + "|| distance = " + uuidInstance.getTotalDistance() + " km");
                driverating.set((float) uuidInstance.getDriverRated());

                tripdate.set(uuidInstance.getTripStartTime());

                starttime.set(uuidInstance.getTripStartTime().substring(9));
                endtime.set(uuidInstance.getCompletedAt().substring(9));

                car_make.set(driverDetail.getCarMakeName() + "(" + driverDetail.getVehicleTypeName() + ")");

                car_number.set(driverDetail.getCarNumber());

                detail_car_type.set(driverDetail.getVehicleTypeName());
                detail_car_desc.set(driverDetail.getCarMakeName() + "-" + driverDetail.getCarModelName());

                isCompleted.set(uuidInstance.getIsCompleted() == 1);
                isCancelled.set(uuidInstance.getIsCancelled() == 1);
                showCancelButton.set(uuidInstance.getIsLater() == 1);

            }
        }
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

    /**
     * called when API call fails
     **/
    @Override
    public void onFailureApi(long taskId, CustomException e) {
        setIsLoading(false);
        getmNavigator().showMessage(e.getMessage());
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

    public void onClickCancel(View v) {
        if (getmNavigator().isNetworkConnected()) {
            userCancelReasonApi("before");
        } else getmNavigator().showNetworkMessage();
    }

    public void onCLickBack(View v) {
        getmNavigator().onClickBack();
    }

    public void cancelApi() {
        if (getmNavigator().isNetworkConnected()) {
            hashMap.clear();
            hashMap.put(Constants.NetworkParameters.request_id, reqId.get());

            if (getmNavigator().getItemPosition().isEmpty() || getmNavigator().getItemPosition() == null) {
                getmNavigator().showMessage("Choose Reasons");
                return;
            }

            if (getmNavigator().getItemPosition().equalsIgnoreCase("0")) {
                if (!CommonUtils.IsEmpty(reasonText.get())) {
                    hashMap.put(Constants.NetworkParameters.cancel_other_reason, reasonText.get());
                    setIsLoading(true);
                    userCancelApi(hashMap);
                } else
                    getmNavigator().showMessage(getmNavigator().getBaseAct().getTranslatedString(R.string.txt_pls_enter_reason));
            } else {
                hashMap.put(Constants.NetworkParameters.reason, getmNavigator().getItemPosition());
                setIsLoading(true);
                userCancelApi(hashMap);
            }
        } else getmNavigator().showNetworkMessage();

    }

    public void makeComplaint(View v) {
        getmNavigator().getBaseAct().getSupportFragmentManager()
                .beginTransaction()
                .disallowAddToBackStack()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .replace(R.id.fragment_frame, MakeCompFrag.newInstance(IDD.get(), Constants.history_comptype), HistoryDetailFrag.TAG)
                .commit();
    }

}
