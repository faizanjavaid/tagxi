package com.tyt.driver.di.builder;


import com.tyt.driver.ui.addMoney.AddMoneyFrag;
import com.tyt.driver.ui.carDetails.carColor.CarColorFrag;
import com.tyt.driver.ui.carDetails.carNumber.VehNumberFrag;
import com.tyt.driver.ui.carDetails.carTypes.CarTypeFrag;
import com.tyt.driver.ui.carDetails.carYear.VehYearFrag;
import com.tyt.driver.ui.carDetails.carmake.CarMakeFrag;
import com.tyt.driver.ui.carDetails.carmodel.CarModelFrag;
import com.tyt.driver.ui.carDetails.servicelocation.ServiceLocationFrag;
import com.tyt.driver.ui.country.Countrylistdialog;
import com.tyt.driver.ui.country.MapDaggerModel;
import com.tyt.driver.ui.feedback.FeedbackFrag;
import com.tyt.driver.ui.historyDetail.HistoryDetailFrag;
import com.tyt.driver.ui.homeScreen.instantRide.InstantRideFrag;
import com.tyt.driver.ui.homeScreen.addCard.AddCardFrag;
import com.tyt.driver.ui.homeScreen.bookingConfirm.BookingFrag;
import com.tyt.driver.ui.homeScreen.earnings.EarningsFrag;
import com.tyt.driver.ui.homeScreen.faq.FaqFrag;
import com.tyt.driver.ui.homeScreen.getRefferal.GetRefferalFrag;

import com.tyt.driver.ui.homeScreen.inAppChat.InAppChatFragment;
import com.tyt.driver.ui.homeScreen.makeComplaint.MakeCompFrag;
import com.tyt.driver.ui.homeScreen.makeTrip.MakeTripFrag;
import com.tyt.driver.ui.homeScreen.manageCard.ManageCardFrag;
import com.tyt.driver.ui.homeScreen.mapscrn.MapScrn;
import com.tyt.driver.ui.homeScreen.profile.profileEdit.ProfileEditFrag;
import com.tyt.driver.ui.homeScreen.profile.ProfileFrag;
import com.tyt.driver.ui.homeScreen.sos.SosFrag;
import com.tyt.driver.ui.homeScreen.tripscreen.TripFragment;
import com.tyt.driver.ui.homeScreen.userHistory.HistoryFrag;
import com.tyt.driver.ui.homeScreen.vehicleInfo.VehicleInfoFrag;
import com.tyt.driver.ui.wallet.WalletFrag;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Created by root on 10/9/17.
 */
@Module
public abstract class HomeDaggerModel {

    @ContributesAndroidInjector(modules = HomeViewModelDagger.class)
    abstract MapScrn provideHomeFragmentProviderFactory();

    @ContributesAndroidInjector(modules = HomeViewModelDagger.class)
    abstract VehicleInfoFrag provideVehicleInfoFragProviderFactory();

    @ContributesAndroidInjector(modules = HomeViewModelDagger.class)
    abstract TripFragment provideTripFragmentProviderFactory();

    @ContributesAndroidInjector(modules = HomeViewModelDagger.class)
    abstract InAppChatFragment provideInAppChatFragmentProviderFactory();

    @ContributesAndroidInjector(modules = HomeViewModelDagger.class)
    abstract MakeTripFrag provideMakeTripFragProviderFactory();

    @ContributesAndroidInjector(modules = HomeViewModelDagger.class)
    abstract BookingFrag provideBookingFragProviderFactory();

    @ContributesAndroidInjector(modules = HomeViewModelDagger.class)
    abstract ProfileFrag provideProfileFragProviderFactory();

    @ContributesAndroidInjector(modules = HomeViewModelDagger.class)
    abstract ProfileEditFrag provideProfileEditFrag();

    @ContributesAndroidInjector(modules = HomeViewModelDagger.class)
    abstract HistoryFrag provideHistoryFrag();

    @ContributesAndroidInjector(modules = HomeViewModelDagger.class)
    abstract HistoryDetailFrag provideHistoryDetailFrag();


    @ContributesAndroidInjector(modules = HomeViewModelDagger.class)
    abstract ManageCardFrag provideManageCardFrag();

    @ContributesAndroidInjector(modules = HomeViewModelDagger.class)
    abstract AddCardFrag provideAddCardFrag();

    @ContributesAndroidInjector(modules = MapDaggerModel.class)
    abstract Countrylistdialog provideSheduleCancelDialogFrag();


    @ContributesAndroidInjector(modules = HomeViewModelDagger.class)
    abstract CarMakeFrag provideCarMakeFrag();

    @ContributesAndroidInjector(modules = HomeViewModelDagger.class)
    abstract CarModelFrag provideCarModelFrag();

    @ContributesAndroidInjector(modules = HomeViewModelDagger.class)
    abstract ServiceLocationFrag provideServiceLocationFrag();

    @ContributesAndroidInjector(modules = HomeViewModelDagger.class)
    abstract CarTypeFrag provideCarTypeFrag();

    @ContributesAndroidInjector(modules = HomeViewModelDagger.class)
    abstract CarColorFrag provideCarColorFrag();


    @ContributesAndroidInjector(modules = HomeViewModelDagger.class)
    abstract VehNumberFrag provideVehNumberFrag();

    @ContributesAndroidInjector(modules = HomeViewModelDagger.class)
    abstract VehYearFrag provideVehYearFrag();

    @ContributesAndroidInjector(modules = HomeViewModelDagger.class)
    abstract FeedbackFrag provideFeedbackFragProviderFactory();

    @ContributesAndroidInjector(modules = HomeViewModelDagger.class)
    abstract WalletFrag provideWalletFragProviderFactory();

    @ContributesAndroidInjector(modules = HomeViewModelDagger.class)
    abstract AddMoneyFrag provideAddMoneyFragProviderFactory();

    @ContributesAndroidInjector(modules = HomeViewModelDagger.class)
    abstract GetRefferalFrag provideGetRefferalFrag();

    @ContributesAndroidInjector(modules = HomeViewModelDagger.class)
    abstract EarningsFrag provideEarningsFrag();


    @ContributesAndroidInjector(modules = HomeViewModelDagger.class)
    abstract SosFrag provideSosFrag();

    @ContributesAndroidInjector(modules = HomeViewModelDagger.class)
    abstract FaqFrag provideFaqFrag();

    @ContributesAndroidInjector(modules = HomeViewModelDagger.class)
    abstract InstantRideFrag provideInstantRideFrag();

    @ContributesAndroidInjector(modules = HomeViewModelDagger.class)
    abstract MakeCompFrag provideMakeCompFrag();

}
