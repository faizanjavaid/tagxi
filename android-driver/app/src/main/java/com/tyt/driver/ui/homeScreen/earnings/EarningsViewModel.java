package com.tyt.driver.ui.homeScreen.earnings;

import android.view.View;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;

import com.google.gson.Gson;
import com.tyt.driver.R;
import com.tyt.driver.retro.GitHubService;
import com.tyt.driver.retro.base.BaseNetwork;
import com.tyt.driver.retro.base.BaseResponse;
import com.tyt.driver.retro.responsemodel.EarningsReportModel;
import com.tyt.driver.retro.responsemodel.TodayEarningsModel;
import com.tyt.driver.retro.responsemodel.WeeklyModel;
import com.tyt.driver.utilz.CommonUtils;
import com.tyt.driver.utilz.SharedPrefence;
import com.tyt.driver.utilz.exception.CustomException;

import java.util.HashMap;

/**
 * Created by root on 11/13/17.
 */

public class EarningsViewModel extends BaseNetwork<BaseResponse, EarningsNavigator> {
    private static final String TAG = "EarningsViewModel";

    public ObservableBoolean onTodayClick = new ObservableBoolean(true);
    public ObservableBoolean onWeekClick = new ObservableBoolean(false);
    public ObservableBoolean onReportClick = new ObservableBoolean(false);

    public ObservableBoolean fromDateClick = new ObservableBoolean(false);
    public ObservableBoolean toDateClick = new ObservableBoolean(false);

    public ObservableBoolean GetPreviousClick = new ObservableBoolean(false);
    public ObservableBoolean GetNextClick = new ObservableBoolean(false);
    public ObservableBoolean earningsReportGenrated = new ObservableBoolean(false);

    public ObservableField<String> totalTrips = new ObservableField<>();
    public ObservableField<String> cashTrips = new ObservableField<>();
    public ObservableField<String> tripKms = new ObservableField<>();
    public ObservableField<String> walletPayment = new ObservableField<>();
    public ObservableField<String> walletCount = new ObservableField<>();
    public ObservableField<String> cashCount = new ObservableField<>();
    public ObservableField<String> totalAmount = new ObservableField<>();
    public ObservableField<String> CurrentDay = new ObservableField<>();
    public ObservableField<String> currency = new ObservableField<>();
    public ObservableField<String> currentWeekNumber = new ObservableField<>();
    public ObservableField<String> onlineHrs = new ObservableField<>("");

    public ObservableField<String> fromDateValue = new ObservableField<>("");
    public ObservableField<String> toDateValue = new ObservableField<>("");

    public EarningsViewModel(GitHubService gitHubService, SharedPrefence sharedPrefence, Gson gson) {
        super(gitHubService, sharedPrefence, gson);
    }


    /**
     * called when API call is successful
     **/
    @Override
    public void onSuccessfulApi(long taskId, BaseResponse response) {
        setIsLoading(false);
        if (response.message.equalsIgnoreCase("todays_earnings")) {
            TodayEarningsModel todayEarningsModel = CommonUtils.getSingleObject(new Gson().toJson(response.data), TodayEarningsModel.class);
            if (todayEarningsModel!=null) {
                currency.set(todayEarningsModel.getCurrencySymbol());
                onlineHrs.set(todayEarningsModel.getTotalHoursWorked());
                CurrentDay.set(todayEarningsModel.getCurrentDate());
                cashCount.set("" + todayEarningsModel.getTotalCashTripCount());
                walletCount.set("" + todayEarningsModel.getTotalWalletTripCount());
                tripKms.set("" + CommonUtils.doubleDecimalFromat(todayEarningsModel.getTotalTripKms()));
                totalTrips.set("" + todayEarningsModel.getTotalTripsCount());
                cashTrips.set(currency.get() + CommonUtils.doubleDecimalFromat(todayEarningsModel.getTotalCashTripAmount()));
                totalAmount.set(currency.get() + " " + CommonUtils.doubleDecimalFromat(todayEarningsModel.getTotalEarnings()));
                walletPayment.set(currency.get() + " " + CommonUtils.doubleDecimalFromat(todayEarningsModel.getTotalWalletTripAmount()));
            }
        } else if (response.message.equalsIgnoreCase("earnings_report")) {
            EarningsReportModel earningsReportModel = CommonUtils.getSingleObject(new Gson().toJson(response.data), EarningsReportModel.class);

            if (earningsReportModel != null) {
                fromDateValue.set("");
                toDateValue.set("");
                earningsReportGenrated.set(true);

                onlineHrs.set(earningsReportModel.getTotalHoursWorked());
                currency.set(earningsReportModel.getCurrencySymbol());
                CurrentDay.set(earningsReportModel.getFromDate() + " - " + earningsReportModel.getToDate());
                cashCount.set("" + earningsReportModel.getTotalCashTripCount());
                walletCount.set("" + earningsReportModel.getTotalWalletTripCount());
                tripKms.set("" + CommonUtils.doubleDecimalFromat(earningsReportModel.getTotalTripKms()));
                totalTrips.set("" + earningsReportModel.getTotalTripsCount());
                cashTrips.set(currency.get() + CommonUtils.doubleDecimalFromat(earningsReportModel.getTotalCashTripAmount()));
                totalAmount.set(currency.get() + " " + CommonUtils.doubleDecimalFromat(earningsReportModel.getTotalEarnings()));
                walletPayment.set(currency.get() + " " + CommonUtils.doubleDecimalFromat(earningsReportModel.getTotalWalletTripAmount()));
            } else {
                earningsReportGenrated.set(false);
            }

        } else {
            WeeklyModel weeklyModel = CommonUtils.getSingleObject(new Gson().toJson(response.data), WeeklyModel.class);
           if (weeklyModel != null) {
               currency.set(weeklyModel.getCurrencySymbol());
               currentWeekNumber.set("" + weeklyModel.getCurrentWeekNumber());
               GetPreviousClick.set(weeklyModel.getDisablePreviousWeek());
               GetNextClick.set(weeklyModel.getDisableNextWeek());
               onlineHrs.set(weeklyModel.getTotalHoursWorked());
               CurrentDay.set(weeklyModel.getStartOfWeek() + " - " + weeklyModel.getEndOfWeek());
               cashCount.set("" + weeklyModel.getTotalCashTripCount());
               walletCount.set("" + weeklyModel.getTotalWalletTripCount());
               tripKms.set("" + CommonUtils.doubleDecimalFromat(weeklyModel.getTotalTripKms()));
               totalTrips.set("" + weeklyModel.getTotalTripsCount());
               cashTrips.set(currency.get() + CommonUtils.doubleDecimalFromat(weeklyModel.getTotalCashTripAmount()));
               totalAmount.set(currency.get() + " " + CommonUtils.doubleDecimalFromat(weeklyModel.getTotalEarnings()));
               walletPayment.set(currency.get() + " " + CommonUtils.doubleDecimalFromat(weeklyModel.getTotalWalletTripAmount()));
           }

            if (response.message.equalsIgnoreCase("weekly_earnings")) {
                getmNavigator().loadChartData(weeklyModel);
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
        return null;
    }


    public void getEarningsApi() {
        if (getmNavigator().isNetworkConnected()) {

        } else getmNavigator().showNetworkMessage();
    }

    public void onCLickBack(View v) {
        getmNavigator().onClickBack();
    }

    public void onCLickToday(View v) {
        onTodayClick.set(true);
        onWeekClick.set(false);
        onReportClick.set(false);
        earningsReportGenrated.set(false);
        todayEarningsApi();
    }

    public void onClickReport(View v) {
        earningsReportGenrated.set(false);
        onTodayClick.set(false);
        onWeekClick.set(false);
        onReportClick.set(true);
    }

    public void onCLickWeek(View v) {
        earningsReportGenrated.set(false);
        onTodayClick.set(false);
        onWeekClick.set(true);
        onReportClick.set(false);
        weekEarningsApi();
    }


    public void onClickSearch(View v) {
        if (getmNavigator().isNetworkConnected()) {
            if (!fromDateValue.get().isEmpty()) {
                if (!toDateValue.get().isEmpty()) {
                    setIsLoading(true);
                    EarningsReport(fromDateValue.get(), toDateValue.get());
                } else getmNavigator().showMessage(getmNavigator().getBaseAct().getTranslatedString(R.string.txt_pls_choose_to_date));
            } else getmNavigator().showMessage(getmNavigator().getBaseAct().getTranslatedString(R.string.txt_pls_choose_from_date));

        } else getmNavigator().showNetworkMessage();
    }

    public void todayEarningsApi() {
        setIsLoading(true);
        if (getmNavigator().isNetworkConnected()) {
            getTodayEarningsApi();
        } else getmNavigator().showNetworkMessage();
    }

    public void weekEarningsApi() {
        setIsLoading(true);
        if (getmNavigator().isNetworkConnected()) {
            getWeekEarningsApi();
        } else getmNavigator().showNetworkMessage();
    }

    public void onClickNext(View v) {
        setIsLoading(true);
        if (!GetNextClick.get()) {
            if (getmNavigator().isNetworkConnected()) {
                getWeekEarningsWithNumber(currentWeekNumber.get());
            } else getmNavigator().showNetworkMessage();
        }
    }

    public void onClickPrevious(View v) {
        setIsLoading(true);
        if (!GetPreviousClick.get()) {
            if (getmNavigator().isNetworkConnected()) {
                getWeekEarningsWithNumber(currentWeekNumber.get());
            } else getmNavigator().showNetworkMessage();
        }
    }

    public void onClickStartDate(View v) {
        getmNavigator().onClickStartDate();
    }

    public void onClickToDate(View v) {
        getmNavigator().onClickToDate();
    }
}

