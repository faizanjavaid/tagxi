package com.tyt.driver.ui.carDetails.carYear;

import android.os.Build;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import androidx.annotation.RequiresApi;
import androidx.databinding.ObservableField;

import com.google.gson.Gson;
import com.tyt.driver.R;
import com.tyt.driver.retro.GitHubService;
import com.tyt.driver.retro.base.BaseNetwork;
import com.tyt.driver.retro.base.BaseResponse;
import com.tyt.driver.utilz.Constants;
import com.tyt.driver.utilz.SharedPrefence;
import com.tyt.driver.utilz.exception.CustomException;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Created by root on 11/13/17.
 */

public class VehYearViewModel extends BaseNetwork<BaseResponse, VehYearNavigator> {
    private static final String TAG = "MakeTripViewModel";

    SharedPrefence sharedPrefence;
    public ObservableField<String> carYear = new ObservableField<>();

    HashMap<String, String> Map = new HashMap<>();

    public VehYearViewModel(GitHubService gitHubService, SharedPrefence sharedPrefence, Gson gson) {
        super(gitHubService, sharedPrefence, gson);
        this.sharedPrefence = sharedPrefence;
    }

    @Override
    public HashMap<String, String> getMap() {
        return null;
    }


    /**
     * called when API call is successful
     **/
    @Override
    public void onSuccessfulApi(long taskId, BaseResponse response) {
        setIsLoading(false);
        if (response.accessToken != null) {
            if (!TextUtils.isEmpty(response.accessToken)) {
                Log.e("AccessToken==", "token");
                sharedPrefence.savevalue(SharedPrefence.AccessToken, response.accessToken);
                getmNavigator().openRefferalAct();
            }
        }
        else {
            getmNavigator().showMessage(getmNavigator().getBaseAct().getTranslatedString(R.string.txt_profile_updated));
            getmNavigator().openProfile();
        }
    }

    /**
     * called when API call fails
     **/
    @Override
    public void onFailureApi(long taskId, CustomException e) {
        setIsLoading(false);
        getmNavigator().showMessage(e);
    }

    public boolean yearvalidation(String year){
        Integer a = 1980;
        Integer b = Calendar.getInstance().get(Calendar.YEAR);
        Integer x= Integer.valueOf(year);

        return Math.abs(x-a)+Math.abs(b-x)== Math.abs(b-a);
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    public void onClickContinue(View v) {
        if (carYear.get() != null) {
            if (!Objects.requireNonNull(carYear.get()).isEmpty() && yearvalidation(carYear.get())) {
                sharedPrefence.savevalue(Constants.selectedCarYear, carYear.get());

                if (sharedPrefence.Getvalue(SharedPrefence.carFrom).equalsIgnoreCase("vehicle")) {
                    requestbody.put(Constants.NetworkParameters.CarMake, RequestBody.create(MediaType.parse("text/plain"), sharedPrefence.Getvalue(Constants.selectedMakeID)));
                    requestbody.put(Constants.NetworkParameters.CarModel, RequestBody.create(MediaType.parse("text/plain"), sharedPrefence.Getvalue(Constants.selectedModelID)));
                    requestbody.put(Constants.NetworkParameters.SerLocId, RequestBody.create(MediaType.parse("text/plain"), sharedPrefence.Getvalue(Constants.AreaID)));
                    requestbody.put(Constants.NetworkParameters.VehType, RequestBody.create(MediaType.parse("text/plain"), sharedPrefence.Getvalue(Constants.SelectedTypeID)));
                    requestbody.put(Constants.NetworkParameters.CarColor, RequestBody.create(MediaType.parse("text/plain"), sharedPrefence.Getvalue(Constants.selectedColor)));
                    requestbody.put(Constants.NetworkParameters.CarNumber, RequestBody.create(MediaType.parse("text/plain"), sharedPrefence.Getvalue(Constants.selectedCarNumber)));
                    requestbody.put(Constants.NetworkParameters.CarYear, RequestBody.create(MediaType.parse("text/plain"), sharedPrefence.Getvalue(Constants.selectedCarYear)));
                    UpdateUserProfile();
                } else {
                    Map.clear();
                    Map.put(Constants.NetworkParameters.country, sharedPrefence.Getvalue(SharedPrefence.CountryCode));
                    Map.put(Constants.NetworkParameters.UUId, sharedPrefence.Getvalue(Constants.UUID));
                    Map.put(Constants.NetworkParameters.device_token, sharedPrefence.Getvalue(SharedPrefence.FCMTOKEN));
                    Map.put(Constants.NetworkParameters.email, sharedPrefence.Getvalue(SharedPrefence.Email));
                    Map.put(Constants.NetworkParameters.name, sharedPrefence.Getvalue(SharedPrefence.FName));
                /*Map.put(Constants.NetworkParameters.password, sharedPrefence.Getvalue(SharedPrefence.Password));
                Map.put(Constants.NetworkParameters.confpwd, sharedPrefence.Getvalue(SharedPrefence.ConfirmPassword));*/
                    Map.put(Constants.NetworkParameters.phoneNumber, sharedPrefence.Getvalue(SharedPrefence.PhoneNumber));
                    Map.put(Constants.NetworkParameters.login_by, "android");
                    Map.put(Constants.NetworkParameters.TermsCond, "1");
                    Map.put(Constants.NetworkParameters.CarMake, sharedPrefence.Getvalue(Constants.selectedMakeID));
                    Map.put(Constants.NetworkParameters.CarModel, sharedPrefence.Getvalue(Constants.selectedModelID));
                    Map.put(Constants.NetworkParameters.SerLocId, sharedPrefence.Getvalue(Constants.AreaID));
                    Map.put(Constants.NetworkParameters.VehType, sharedPrefence.Getvalue(Constants.SelectedTypeID));
                    Map.put(Constants.NetworkParameters.CarYear, sharedPrefence.Getvalue(Constants.selectedCarYear));
                    Map.put(Constants.NetworkParameters.CarColor, sharedPrefence.Getvalue(Constants.selectedColor));
                    Map.put(Constants.NetworkParameters.CarNumber, sharedPrefence.Getvalue(Constants.selectedCarNumber));
                    Map.put(Constants.NetworkParameters.companyKey, sharedPrefence.Getvalue(SharedPrefence.keyValue));
                    UserRegisterApi(Map);
                }
                //  getmNavigator().onClickConfirm();

            } else
                getmNavigator().showMessage(getmNavigator().getBaseAct().getTranslatedString(R.string.txt_enter_car_number));
        }
    }

    public void onclickBack(View v) {
        getmNavigator().onCLickBack();
    }

    public void onYearChanged(Editable e) {
        carYear.set(e.toString());
    }

}
