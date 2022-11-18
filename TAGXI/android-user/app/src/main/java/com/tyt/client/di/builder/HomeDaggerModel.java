package com.tyt.client.di.builder;

import com.tyt.client.ui.country.Countrylistdialog;
import com.tyt.client.ui.country.MapDaggerModel;
import com.tyt.client.ui.homeScreen.addCard.AddCardFrag;
import com.tyt.client.ui.homeScreen.addMoney.AddMoneyFrag;
import com.tyt.client.ui.homeScreen.faq.FaqFrag;
import com.tyt.client.ui.homeScreen.feedback.FeedbackFrag;
import com.tyt.client.ui.homeScreen.getRefferal.GetRefferalFrag;
import com.tyt.client.ui.homeScreen.historyDetail.HistoryDetailFrag;
import com.tyt.client.ui.homeScreen.inAppChat.InAppChatFragment;
import com.tyt.client.ui.homeScreen.makeComplaint.MakeCompFrag;
import com.tyt.client.ui.homeScreen.makeTrip.MakeTripFrag;
import com.tyt.client.ui.homeScreen.manageCard.ManageCardFrag;
import com.tyt.client.ui.homeScreen.mapFrag.MapFrag;
import com.tyt.client.ui.homeScreen.profile.profileEdit.ProfileEditFrag;
import com.tyt.client.ui.homeScreen.profile.ProfileFrag;
import com.tyt.client.ui.homeScreen.sos.SosFrag;
import com.tyt.client.ui.homeScreen.tripscreen.TripFragment;
import com.tyt.client.ui.homeScreen.userHistory.HistoryFrag;
import com.tyt.client.ui.homeScreen.wallet.WalletFrag;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Created by Mahi in 2021.
 *
 * Here Binding all the Fragments inlcuded in the TYT User
 *
 */
@Module
public abstract class HomeDaggerModel {

    @ContributesAndroidInjector(modules = HomeViewModelDagger.class)
    abstract MapFrag provideHomeFragmentProviderFactory();

    @ContributesAndroidInjector(modules = HomeViewModelDagger.class)
    abstract TripFragment provideTripFragmentProviderFactory();

    @ContributesAndroidInjector(modules = HomeViewModelDagger.class)
    abstract InAppChatFragment provideInAppChatFragmentProviderFactory();

    @ContributesAndroidInjector(modules = HomeViewModelDagger.class)
    abstract FeedbackFrag provideFeedbackFragProviderFactory();

    @ContributesAndroidInjector(modules = HomeViewModelDagger.class)
    abstract MakeTripFrag provideMakeTripFragProviderFactory();

    @ContributesAndroidInjector(modules = HomeViewModelDagger.class)
    abstract ProfileFrag provideProfileFragProviderFactory();

    @ContributesAndroidInjector(modules = HomeViewModelDagger.class)
    abstract WalletFrag provideWalletFragProviderFactory();

    @ContributesAndroidInjector(modules = HomeViewModelDagger.class)
    abstract AddMoneyFrag provideAddMoneyFragProviderFactory();

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

    @ContributesAndroidInjector(modules = HomeViewModelDagger.class)
    abstract GetRefferalFrag provideGetRefferalFrag();

    @ContributesAndroidInjector(modules = MapDaggerModel.class)
    abstract Countrylistdialog provideSheduleCancelDialogFrag();

    @ContributesAndroidInjector(modules = MapDaggerModel.class)
    abstract SosFrag provideSosFrag();

    @ContributesAndroidInjector(modules = MapDaggerModel.class)
    abstract FaqFrag provideFaqFrag();

    @ContributesAndroidInjector(modules = HomeViewModelDagger.class)
    abstract MakeCompFrag provideMakeCompFrag();

}
