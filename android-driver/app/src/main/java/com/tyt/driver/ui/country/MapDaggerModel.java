package com.tyt.driver.ui.country;

import com.google.gson.Gson;
import com.tyt.driver.retro.GitHubService;
import com.tyt.driver.ui.bill.BillDialogViewModel;
import com.tyt.driver.utilz.Constants;
import com.tyt.driver.utilz.SharedPrefence;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@Module
public class MapDaggerModel {

    @Provides
    Countrylistdialogviewmodel provideCountrylistdialogviewmodel(@Named(Constants.ourApp) GitHubService gitHubService,
                                                                 SharedPrefence sharedPrefence, Gson gson) {
        return new Countrylistdialogviewmodel(gitHubService, sharedPrefence, gson);
    }

    @Provides
    BillDialogViewModel provideBillDialogViewModel(@Named(Constants.ourApp) GitHubService gitHubService,
                                                   SharedPrefence sharedPrefence, Gson gson) {
        return new BillDialogViewModel(gitHubService, sharedPrefence, gson);
    }

}
