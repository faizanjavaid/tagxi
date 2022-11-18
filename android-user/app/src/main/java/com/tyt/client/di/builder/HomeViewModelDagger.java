package com.tyt.client.di.builder;

import com.google.gson.Gson;
import com.tyt.client.retro.GitHubService;
import com.tyt.client.ui.homeScreen.addCard.AddCardViewModel;
import com.tyt.client.ui.homeScreen.addMoney.AddMoneyViewModel;
import com.tyt.client.ui.homeScreen.feedback.FeedbackFragViewModel;
import com.tyt.client.ui.homeScreen.getRefferal.GetRefferalViewModel;
import com.tyt.client.ui.homeScreen.historyDetail.HistoryDetailViewModel;
import com.tyt.client.ui.homeScreen.inAppChat.AppChatViewModel;
import com.tyt.client.ui.homeScreen.makeComplaint.MakeCompViewModel;
import com.tyt.client.ui.homeScreen.makeTrip.MakeTripViewModel;
import com.tyt.client.ui.homeScreen.manageCard.ManageCardViewModel;
import com.tyt.client.ui.homeScreen.mapFrag.MapFragViewModel;
import com.tyt.client.ui.homeScreen.profile.profileEdit.ProfileEditViewModel;
import com.tyt.client.ui.homeScreen.profile.ProfileViewModel;
import com.tyt.client.ui.homeScreen.tripscreen.TripFragViewModel;
import com.tyt.client.ui.homeScreen.userHistory.HistoryViewModel;
import com.tyt.client.ui.homeScreen.wallet.WalletFragViewModel;
import com.tyt.client.utilz.Constants;
import com.tyt.client.utilz.SharedPrefence;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Mahi in 2021.
 *
 * Here Binding all the Fragment's and Activity's View Model Classes
 *
 */

@Module
public class HomeViewModelDagger {

    @Provides
    MapFragViewModel provideHomeFragmentViewModel(@Named(Constants.ourApp) GitHubService gitHubService,
                                                  SharedPrefence sharedPrefence,
                                                  Gson gson) {
        return new MapFragViewModel(gitHubService, sharedPrefence, gson);
    }

    @Provides
    TripFragViewModel TripFragViewModel(@Named(Constants.ourApp) GitHubService gitHubService,
                                        SharedPrefence sharedPrefence,
                                        Gson gson) {
        return new TripFragViewModel(gitHubService, sharedPrefence, gson);
    }
    @Provides
    AppChatViewModel AppChatViewModel(@Named(Constants.ourApp) GitHubService gitHubService,
                                      SharedPrefence sharedPrefence,
                                      Gson gson) {
        return new AppChatViewModel(gitHubService, sharedPrefence, gson);
    }
    @Provides
    MakeTripViewModel provideMakeTripViewModel(@Named(Constants.ourApp) GitHubService gitHubService,
                                               SharedPrefence sharedPrefence,
                                               Gson gson) {
        return new MakeTripViewModel(gitHubService, sharedPrefence, gson);
    }

    @Provides
    ProfileViewModel ProfileViewModel(@Named(Constants.ourApp) GitHubService gitHubService,
                                      SharedPrefence sharedPrefence,
                                      Gson gson) {
        return new ProfileViewModel(gitHubService, sharedPrefence, gson);
    }

    @Provides
    HistoryViewModel HistoryViewModel(@Named(Constants.ourApp) GitHubService gitHubService,
                                      SharedPrefence sharedPrefence,
                                      Gson gson) {
        return new HistoryViewModel(gitHubService, sharedPrefence, gson);
    }

    @Provides
    HistoryDetailViewModel HistoryDetailViewModel(@Named(Constants.ourApp) GitHubService gitHubService,
                                                  SharedPrefence sharedPrefence,
                                                  Gson gson) {
        return new HistoryDetailViewModel(gitHubService, sharedPrefence, gson);
    }


    @Provides
    ProfileEditViewModel ProfileEditViewModel(@Named(Constants.ourApp) GitHubService gitHubService,
                                              SharedPrefence sharedPrefence,
                                              Gson gson) {
        return new ProfileEditViewModel(gitHubService, sharedPrefence, gson);
    }

    @Provides
    ManageCardViewModel ManageCardViewModel(@Named(Constants.ourApp) GitHubService gitHubService,
                                            SharedPrefence sharedPrefence,
                                            Gson gson) {
        return new ManageCardViewModel(gitHubService, sharedPrefence, gson);
    }

    @Provides
    AddCardViewModel AddCardViewModel(@Named(Constants.ourApp) GitHubService gitHubService,
                                      SharedPrefence sharedPrefence,
                                      Gson gson) {
        return new AddCardViewModel(gitHubService, sharedPrefence, gson);
    }

    @Provides
    FeedbackFragViewModel FeedbackFragViewModel(@Named(Constants.ourApp) GitHubService gitHubService,
                                                SharedPrefence sharedPrefence,
                                                Gson gson) {
        return new FeedbackFragViewModel(gitHubService, sharedPrefence, gson);
    }

    @Provides
    WalletFragViewModel WalletFragViewModel(@Named(Constants.ourApp) GitHubService gitHubService,
                                            SharedPrefence sharedPrefence,
                                            Gson gson) {
        return new WalletFragViewModel(gitHubService, sharedPrefence, gson);
    }

    @Provides
    AddMoneyViewModel AddMoneyViewModel(@Named(Constants.ourApp) GitHubService gitHubService,
                                        SharedPrefence sharedPrefence,
                                        Gson gson) {
        return new AddMoneyViewModel(gitHubService, sharedPrefence, gson);
    }

    @Provides
    GetRefferalViewModel GetRefferalViewModel(@Named(Constants.ourApp) GitHubService gitHubService,
                                              SharedPrefence sharedPrefence,
                                              Gson gson) {
        return new GetRefferalViewModel(gitHubService, sharedPrefence, gson);
    }

    @Provides
    MakeCompViewModel makeCompViewModel(@Named(Constants.ourApp) GitHubService gitHubService,
                                        SharedPrefence sharedPrefence,
                                        Gson gson) {
        return new MakeCompViewModel(gitHubService,sharedPrefence,gson);
    }
}
