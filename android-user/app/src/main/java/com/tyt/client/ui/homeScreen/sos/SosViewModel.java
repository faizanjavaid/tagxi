package com.tyt.client.ui.homeScreen.sos;

import android.text.Editable;
import android.util.Log;
import android.view.View;

import androidx.databinding.ObservableField;

import com.google.android.gms.maps.model.LatLng;
import com.tyt.client.retro.GitHubService;
import com.tyt.client.retro.base.BaseNetwork;
import com.tyt.client.retro.base.BaseResponse;
import com.tyt.client.retro.responsemodel.SOSModel;
import com.tyt.client.utilz.CommonUtils;
import com.tyt.client.utilz.Constants;
import com.tyt.client.utilz.SharedPrefence;
import com.tyt.client.utilz.exception.CustomException;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * Created by Mahi in 2021.
 */

public class SosViewModel extends BaseNetwork<BaseResponse, SosNavigator> {

    List<SOSModel> sosModels = new ArrayList<>();
    public ObservableField<String> name = new ObservableField<>("");
    public ObservableField<String> number = new ObservableField<>("");

    public ObservableField<LatLng> curLatLng = new ObservableField<>();
    HashMap<String, String> map = new HashMap<>();

    public SosViewModel(GitHubService gitHubService, SharedPrefence sharedPrefence, Gson gson) {
        super(gitHubService, sharedPrefence, gson);
    }


    /**
     * called when API call is successful
     **/
    @Override
    public void onSuccessfulApi(long taskId, BaseResponse response) {
        setIsLoading(false);
        Log.i("xxxTAG", "onSuccessfulApi: "+response.message);
        Log.i("xxxTAG", "onSuccessfulApi: "+response.data);
        if (response.message.equalsIgnoreCase("sos_created")) {
            sosApi();
        } else if (response.message.equalsIgnoreCase("sos_deleted")) {
            sosApi();
        } else if (response.success) {
            name.set("");
            number.set("");
            sosModels.clear();
            String faqdata = CommonUtils.arrayToString((ArrayList<Object>) response.data);
            sosModels.addAll(CommonUtils.stringToArray(faqdata, SOSModel[].class));
            getmNavigator().loadSOS(sosModels);

        }
    }

    /**
     * called when API call fails
     **/
    @Override
    public void onFailureApi(long taskId, CustomException e) {
        setIsLoading(false);
    }

    /**
     * adds client_id, client_token to {@link HashMap} used for API calls
     **/
    @Override
    public HashMap<String, String> getMap() {
        return null;
    }


    public void sosApi() {
        if (getmNavigator().isNetworkConnected()) {
            setIsLoading(true);
            SOS(sharedPrefence.Getvalue(SharedPrefence.CURRLAT), sharedPrefence.Getvalue(SharedPrefence.CURRLNG));
        } else
            getmNavigator().showNetworkMessage();
    }

    public void onClickBack(View v) {
        getmNavigator().onClickBAck();
    }

    public void onClickAdd(View v) {
        if (getmNavigator().isNetworkConnected()) {
            if (Objects.requireNonNull(name.get()).isEmpty()) {
                getmNavigator().showMessage("Please enter the name");
                return;
            } else if (Objects.requireNonNull(number.get()).isEmpty()) {
                getmNavigator().showMessage("Please enter the number");
                return;
            }
            setIsLoading(true);
            map.clear();
            map.put(Constants.NetworkParameters.name, name.get());
            map.put(Constants.NetworkParameters.Number, number.get());

            SOSAdd(map);
        } else
            getmNavigator().showNetworkMessage();
    }

    public void onNumberChanged(Editable e) {
        number.set(e.toString());
    }

    public void onNameChanged(Editable e) {
        name.set(e.toString());
    }

    public void deleteItem(String id) {
        if (getmNavigator().isNetworkConnected()) {
            setIsLoading(true);
            SOSDelete(id);
        } else
            getmNavigator().showNetworkMessage();
    }
}
