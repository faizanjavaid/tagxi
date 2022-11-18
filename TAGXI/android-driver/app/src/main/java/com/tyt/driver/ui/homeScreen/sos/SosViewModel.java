package com.tyt.driver.ui.homeScreen.sos;

import android.text.Editable;
import android.view.View;

import androidx.databinding.ObservableField;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.tyt.driver.R;
import com.tyt.driver.retro.GitHubService;
import com.tyt.driver.retro.base.BaseNetwork;
import com.tyt.driver.retro.base.BaseResponse;
import com.tyt.driver.retro.responsemodel.SOSModel;
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
        if (response.message.equalsIgnoreCase("sos_created")) {
            getmNavigator().showMessage(getmNavigator().getAttachedContext().getTranslatedString(R.string.txt_num_added));
            sosApi();
        } else if (response.message.equalsIgnoreCase("sos_deleted")) {
            getmNavigator().showMessage(getmNavigator().getAttachedContext().getTranslatedString(R.string.txt_num_deleted));
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
            if (name.get().isEmpty()) {
                getmNavigator().showMessage(getmNavigator().getAttachedContext().getTranslatedString(R.string.txt_pls_enter_name));
                return;
            } else if (number.get().isEmpty()) {
                getmNavigator().showMessage(getmNavigator().getAttachedContext().getTranslatedString(R.string.txt_pls_enter_num));
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
