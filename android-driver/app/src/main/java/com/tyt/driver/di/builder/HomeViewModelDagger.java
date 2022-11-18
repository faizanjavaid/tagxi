package com.tyt.driver.di.builder;

import com.google.gson.Gson;
import com.tyt.driver.retro.GitHubService;
import com.tyt.driver.ui.addMoney.AddMoneyViewModel;
import com.tyt.driver.ui.carDetails.carColor.CarColorViewModel;
import com.tyt.driver.ui.carDetails.carNumber.VehNumberViewModel;
import com.tyt.driver.ui.carDetails.carTypes.CarTypeViewModel;
import com.tyt.driver.ui.carDetails.carYear.VehYearViewModel;
import com.tyt.driver.ui.carDetails.carmake.CarMakeViewModel;
import com.tyt.driver.ui.carDetails.carmodel.CarModelViewModel;
import com.tyt.driver.ui.carDetails.servicelocation.SeviceLocationVieModel;
import com.tyt.driver.ui.feedback.FeedbackFragViewModel;
import com.tyt.driver.ui.historyDetail.HistoryDetailViewModel;
import com.tyt.driver.ui.homeScreen.instantRide.InstantRideViewModel;
import com.tyt.driver.ui.homeScreen.addCard.AddCardViewModel;
import com.tyt.driver.ui.homeScreen.bookingConfirm.BookingViewModel;
import com.tyt.driver.ui.homeScreen.earnings.EarningsViewModel;
import com.tyt.driver.ui.homeScreen.faq.FaqViewModel;
import com.tyt.driver.ui.homeScreen.getRefferal.GetRefferalViewModel;
import com.tyt.driver.ui.homeScreen.inAppChat.AppChatViewModel;

import com.tyt.driver.ui.homeScreen.makeComplaint.MakeCompViewModel;
import com.tyt.driver.ui.homeScreen.makeTrip.MakeTripViewModel;
import com.tyt.driver.ui.homeScreen.manageCard.ManageCardViewModel;
import com.tyt.driver.ui.homeScreen.mapscrn.MapScrnViewModel;
import com.tyt.driver.ui.homeScreen.profile.profileEdit.ProfileEditViewModel;
import com.tyt.driver.ui.homeScreen.profile.ProfileViewModel;
import com.tyt.driver.ui.homeScreen.sos.SosViewModel;
import com.tyt.driver.ui.homeScreen.tripscreen.TripFragViewModel;
import com.tyt.driver.ui.homeScreen.userHistory.HistoryViewModel;
import com.tyt.driver.ui.homeScreen.vehicleInfo.VehicleInfoViewModel;
import com.tyt.driver.ui.wallet.WalletFragViewModel;
import com.tyt.driver.utilz.Constants;
import com.tyt.driver.utilz.SharedPrefence;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@Module
public class HomeViewModelDagger {

    @Provides
    MapScrnViewModel provideHomeFragmentViewModel(@Named(Constants.ourApp) GitHubService gitHubService,
                                                  SharedPrefence sharedPrefence,
                                                  Gson gson) {
        return new MapScrnViewModel(gitHubService, sharedPrefence, gson);
    }

    @Provides
    MakeTripViewModel provideMakeTripViewModel(@Named(Constants.ourApp) GitHubService gitHubService,
                                               SharedPrefence sharedPrefence,
                                               Gson gson) {
        return new MakeTripViewModel(gitHubService, sharedPrefence, gson);
    }

    @Provides
    BookingViewModel BookingViewModel(@Named(Constants.ourApp) GitHubService gitHubService,
                                      SharedPrefence sharedPrefence,
                                      Gson gson) {
        return new BookingViewModel(gitHubService, sharedPrefence, gson);
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
    CarMakeViewModel CarMakeViewModel(@Named(Constants.ourApp) GitHubService gitHubService,
                                      SharedPrefence sharedPrefence,
                                      Gson gson) {
        return new CarMakeViewModel(gitHubService, sharedPrefence, gson);
    }

    @Provides
    CarModelViewModel CarModelViewModel(@Named(Constants.ourApp) GitHubService gitHubService,
                                        SharedPrefence sharedPrefence,
                                        Gson gson) {
        return new CarModelViewModel(gitHubService, sharedPrefence, gson);
    }

    @Provides
    SeviceLocationVieModel SeviceLocationVieModel(@Named(Constants.ourApp) GitHubService gitHubService,
                                                  SharedPrefence sharedPrefence,
                                                  Gson gson) {
        return new SeviceLocationVieModel(gitHubService, sharedPrefence, gson);
    }

    @Provides
    CarTypeViewModel CarTypeViewModel(@Named(Constants.ourApp) GitHubService gitHubService,
                                      SharedPrefence sharedPrefence,
                                      Gson gson) {
        return new CarTypeViewModel(gitHubService, sharedPrefence, gson);
    }

    @Provides
    CarColorViewModel CarColorViewModel(@Named(Constants.ourApp) GitHubService gitHubService,
                                        SharedPrefence sharedPrefence,
                                        Gson gson) {
        return new CarColorViewModel(gitHubService, sharedPrefence, gson);
    }

    @Provides
    VehNumberViewModel VehNumberViewModel(@Named(Constants.ourApp) GitHubService gitHubService,
                                          SharedPrefence sharedPrefence,
                                          Gson gson) {
        return new VehNumberViewModel(gitHubService, sharedPrefence, gson);
    }

    @Provides
    VehYearViewModel VehYearViewModel(@Named(Constants.ourApp) GitHubService gitHubService,
                                      SharedPrefence sharedPrefence,
                                      Gson gson) {
        return new VehYearViewModel(gitHubService, sharedPrefence, gson);
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
    FeedbackFragViewModel FeedbackFragViewModel(@Named(Constants.ourApp) GitHubService gitHubService,
                                                SharedPrefence sharedPrefence,
                                                Gson gson) {
        return new FeedbackFragViewModel(gitHubService, sharedPrefence, gson);
    }

    @Provides
    HistoryDetailViewModel HistoryDetailViewModel(@Named(Constants.ourApp) GitHubService gitHubService,
                                                  SharedPrefence sharedPrefence,
                                                  Gson gson) {
        return new HistoryDetailViewModel(gitHubService, sharedPrefence, gson);
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
    EarningsViewModel EarningsViewModel(@Named(Constants.ourApp) GitHubService gitHubService,
                                        SharedPrefence sharedPrefence,
                                        Gson gson) {
        return new EarningsViewModel(gitHubService, sharedPrefence, gson);
    }

    @Provides
    SosViewModel SosViewModel(@Named(Constants.ourApp) GitHubService gitHubService,
                              SharedPrefence sharedPrefence,
                              Gson gson) {
        return new SosViewModel(gitHubService, sharedPrefence, gson);
    }
    @Provides
    FaqViewModel FaqViewModel(@Named(Constants.ourApp) GitHubService gitHubService,
                              SharedPrefence sharedPrefence,
                              Gson gson) {
        return new FaqViewModel(gitHubService, sharedPrefence, gson);
    }

    @Provides
    VehicleInfoViewModel VehicleInfoViewModel(@Named(Constants.ourApp) GitHubService gitHubService,
                                      SharedPrefence sharedPrefence,
                                      Gson gson) {
        return new VehicleInfoViewModel(gitHubService, sharedPrefence, gson);
    }


    @Provides
    InstantRideViewModel InstantRideViewModel(@Named(Constants.ourApp) GitHubService gitHubService,
                                              SharedPrefence sharedPrefence,
                                              Gson gson){
        return new InstantRideViewModel(gitHubService,sharedPrefence,gson);
    }

    @Provides
    MakeCompViewModel makeCompViewModel(@Named(Constants.ourApp) GitHubService gitHubService,
                                        SharedPrefence sharedPrefence,
                                        Gson gson) {
        return new MakeCompViewModel(gitHubService,sharedPrefence,gson);
    }

}
