package com.tyt.client.ui.homeScreen.faq;

import android.view.View;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.tyt.client.retro.GitHubService;
import com.tyt.client.retro.base.BaseNetwork;
import com.tyt.client.retro.base.BaseResponse;
import com.tyt.client.retro.responsemodel.Faqmodel;
import com.tyt.client.retro.responsemodel.FavouriteLocations;
import com.tyt.client.utilz.CommonUtils;
import com.tyt.client.utilz.SharedPrefence;
import com.tyt.client.utilz.exception.CustomException;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Mahi in 2021.
 */

public class FaqViewModel extends BaseNetwork<BaseResponse, FaqNavigator> {
    private static final String TAG = "FaqViewModel";
    List<Faqmodel> faqmodels = new ArrayList<>();
    ArrayList<FavouriteLocations.FavLocData> FAVData = new ArrayList<>();
    List<FavouriteLocations.FavLocData> FAVDataList = new ArrayList<>();


    public ObservableField<String> PageTitle = new ObservableField<>("");
    public ObservableBoolean is_Fav = new ObservableBoolean(false);
    public ObservableBoolean nofav = new ObservableBoolean(false);

    public FaqViewModel(GitHubService gitHubService, SharedPrefence sharedPrefence, Gson gson) {
        super(gitHubService, sharedPrefence, gson);
    }


    /**
     * called when API call is successful
     **/
    @Override
    public void onSuccessfulApi(long taskId, BaseResponse response) {
        setIsLoading(false);
        if (response.success) {
            if (response.message.equalsIgnoreCase("address listed successfully")) {
                FAVData.clear();
                FAVDataList.clear();
                String favdata = new Gson().toJson(response.data);
                JsonElement json = JsonParser.parseString(favdata);
                if (json.isJsonArray()) {
                    JsonArray jsonArray = json.getAsJsonArray();
                    for (int i = 0; i < jsonArray.getAsJsonArray().size(); i++) {
                        JsonElement data = jsonArray.getAsJsonArray().get(i);
                        if (data.isJsonObject()) {
                            FavouriteLocations.FavLocData favouriteLocations = new Gson().fromJson(data, FavouriteLocations.FavLocData.class);
                            FAVData.add(favouriteLocations);
                        } else if (data.isJsonArray()) {

                        }
                    }
                } else if (json.isJsonObject()) {
                    FavouriteLocations.FavLocData favouriteLocations = new Gson().fromJson(json, FavouriteLocations.FavLocData.class);
                    FAVData.add(favouriteLocations);
                }

                String favstring = CommonUtils.arrayToString(FAVData);

                FAVDataList.addAll(CommonUtils.stringToArray(favstring, FavouriteLocations.FavLocData[].class));

                getmNavigator().loadFavourites(FAVDataList);
            } else if (response.message.equalsIgnoreCase("favorite location deleted successfully")) {
                getmNavigator().showMessage("Deleted...");
                getFavApi();
            } else {
                String faqdata = CommonUtils.arrayToString((ArrayList<Object>) response.data);
                faqmodels.addAll(CommonUtils.stringToArray(faqdata, Faqmodel[].class));
                getmNavigator().loadFaq(faqmodels);
            }
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
        return new HashMap<>();
    }


    public void faqApi() {
        if (getmNavigator().isNetworkConnected()) {
            setIsLoading(true);
            FAQLIST(sharedPrefence.Getvalue(SharedPrefence.CURRLAT), sharedPrefence.Getvalue(SharedPrefence.CURRLNG));
        } else getmNavigator().showNetworkMessage();
    }


    public void getFavApi() {
        if (getmNavigator().isNetworkConnected()) {
            GetFavList();
        } else getmNavigator().showNetworkMessage();
    }

    public void deleteFavApi(String fid) {
        if (getmNavigator().isNetworkConnected()) {
            DeleteFav(fid);
        } else getmNavigator().showNetworkMessage();
    }

    public void onClickBack(View v) {
        getmNavigator().onClickBAck();
    }
}
