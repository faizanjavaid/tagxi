package com.tyt.client.ui.homeScreen.userHistory;

import android.util.Log;
import android.view.View;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;

import com.google.gson.Gson;
import com.tyt.client.retro.GitHubService;
import com.tyt.client.retro.base.BaseNetwork;
import com.tyt.client.retro.base.BaseResponse;
import com.tyt.client.retro.responsemodel.HistoryCard;
import com.tyt.client.retro.responsemodel.HistoryModel;
import com.tyt.client.utilz.CommonUtils;
import com.tyt.client.utilz.SharedPrefence;
import com.tyt.client.utilz.exception.CustomException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Mahi in 2021.
 */

public class HistoryViewModel extends BaseNetwork<BaseResponse, HistoryNavigator> {
    private static final String TAG = "AddCardViewModel";
    HashMap<String, String> map = new HashMap<>();

    List<HistoryModel> historyModels = new ArrayList<>();

    List<HistoryCard> HistoryCardList = new ArrayList<>();

    public ObservableBoolean noItem = new ObservableBoolean(false);

    public ObservableBoolean upcomingClicked = new ObservableBoolean(false);
    public ObservableBoolean completedClicked = new ObservableBoolean(true);
    public ObservableBoolean cancelledClicked = new ObservableBoolean(false);

    public ObservableInt page = new ObservableInt(1);
    public ObservableInt limit = new ObservableInt(0);

    public ObservableField<String> page_link = new ObservableField<>("");

    public HistoryViewModel(GitHubService gitHubService, SharedPrefence sharedPrefence, Gson gson) {
        super(gitHubService, sharedPrefence, gson);
    }

    public void onClickUpcoming(View v){
        upcomingClicked.set(true);
        completedClicked.set(false);
        cancelledClicked.set(false);
        getmNavigator().startshim();
        HistoryCardList.clear();
        historyApi(1);
    }

    public void onClickCompleted(View v) {
        completedClicked.set(true);
        upcomingClicked.set(false);
        cancelledClicked.set(false);
        getmNavigator().startshim();
        HistoryCardList.clear();
        historyApi(1);
    }

    public void onClickCancelled(View v) {
        upcomingClicked.set(false);
        completedClicked.set(false);
        cancelledClicked.set(true);
        getmNavigator().startshim();
        HistoryCardList.clear();
        historyApi(1);
    }

    /**
     * called when API call is successful
     **/
    @Override
    public void onSuccessfulApi(long taskId, BaseResponse response) {
        setIsLoading(false);
        historyModels.clear();
        if (response.success && response.data!=null ) {

            GetPaginationData(response);

            Log.d("xxxHistoryModel", "onSuccessfulApi() : Res==>"+gson.toJson(response.data));
            String historyData = CommonUtils.arrayToString((ArrayList<Object>) response.data);
            historyModels.addAll(CommonUtils.stringToArray(historyData, HistoryModel[].class));
            for (int i = 0; i < historyModels.size(); i++) {
                //get single item and add it to custom history list

                if (!HistoryCardList.contains(historyModels.get(i).getId())) {
                    HistoryCard historyCard=new HistoryCard(historyModels.get(i).getId(),
                            historyModels.get(i).getRequestNumber(),
                            historyModels.get(i).getPickAddress(),
                            historyModels.get(i).getDropAddress(),
                            historyModels.get(i).getIsLater(),
                            historyModels.get(i).getTripStartTime(),
                            historyModels.get(i).getIsCancelled(),
                            historyModels.get(i).getUpdated_at(),
                            historyModels.get(i).getIsCompleted(),
                            historyModels.get(i).getRequestBill(),
                            historyModels.get(i).getDriverDetail(),
                            historyModels.get(i).getArrivedAt(),
                            historyModels.get(i).getCompletedAt());

                    HistoryCardList.add(historyCard);
                }

                getmNavigator().loadHistory(HistoryCardList);

            }

            if (HistoryCardList.size() <= 0) {
                noItem.set(true);
            }
        }
    }

    private void GetPaginationData(BaseResponse response) {
        int cur_page = response.getHistoryMeta().getPagination().getCurrent_page();
        int tot_page = response.getHistoryMeta().getPagination().getTotal_pages();
        page.set(cur_page);
        limit.set(tot_page);
        getmNavigator().setPageValue(cur_page,tot_page);

        if (limit.get() > 1) {
            page_link.set(response.getHistoryMeta().getPagination().getLinks().getNextpagelink());
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


    public void historyApi(int page) {
        if (getmNavigator().isNetworkConnected()) {
            setIsLoading(true);
            getMap();
            if (upcomingClicked.get())
                GetUpcomingHistory("1",page);

            if (completedClicked.get())
                GetCompletedHistory("1",page);

            if (cancelledClicked.get())
                GetCancelledHistory("1",page);

        } else getmNavigator().showNetworkMessage();

    }

    public void onCLickBack(View v) {
        getmNavigator().onCLickBack();
    }
}
