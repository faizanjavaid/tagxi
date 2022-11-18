package com.tyt.client.ui.country;

import com.google.gson.Gson;
import com.tyt.client.retro.GitHubService;
import com.tyt.client.ui.homeScreen.bill.BillDialogViewModel;
import com.tyt.client.ui.homeScreen.faq.FaqViewModel;
import com.tyt.client.ui.homeScreen.sos.SosFrag;
import com.tyt.client.ui.homeScreen.sos.SosViewModel;
import com.tyt.client.utilz.Constants;
import com.tyt.client.utilz.SharedPrefence;

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

    @Provides
    SosViewModel SosViewModel(@Named(Constants.ourApp) GitHubService gitHubService,
                                            SharedPrefence sharedPrefence, Gson gson) {
        return new SosViewModel(gitHubService, sharedPrefence, gson);
    }


    @Provides
    FaqViewModel FaqViewModel(@Named(Constants.ourApp) GitHubService gitHubService,
                              SharedPrefence sharedPrefence, Gson gson) {
        return new FaqViewModel(gitHubService, sharedPrefence, gson);
    }

}
