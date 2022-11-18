/*
 *  Copyright (C) 2017 MINDORKS NEXTGEN PRIVATE LIMITED
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      https://mindorks.com/license/apache-v2
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License
 */

package com.tyt.client.di.builder;

import com.tyt.client.ui.applyRefferal.ApplyRefferalAct;
import com.tyt.client.ui.country.MapDaggerModel;
import com.tyt.client.ui.country.MapFragmentProvider;
import com.tyt.client.ui.homeScreen.HomeAct;
import com.tyt.client.ui.homeScreen.bill.BillDialogFragment;
import com.tyt.client.ui.homeScreen.placeapiscreen.PlaceApiAct;
import com.tyt.client.ui.homeScreen.placeapiscreen.PlaceApiDaggerModel;
import com.tyt.client.ui.keyValidation.KeyValidationAct;
import com.tyt.client.ui.login.LoginAct;
import com.tyt.client.ui.loginOrSign.LoginOrSignAct;
import com.tyt.client.ui.otpscreen.OTPActivity;
import com.tyt.client.ui.otpscreen.OtpDaggerModel;
import com.tyt.client.ui.signup.SignupActivity;
import com.tyt.client.ui.splash.SplashScreen;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Here bind the subcomponent of All Activities included except Enable Location Activity Screen
 **/

@Module
public abstract class ActivityBuilder {

    @ContributesAndroidInjector(modules = MapFragmentProvider.class)
    abstract SignupActivity bindSignupActivity();

    @ContributesAndroidInjector()
    abstract SplashScreen bindSplashScreen();

    @ContributesAndroidInjector(modules = OtpDaggerModel.class)
    abstract OTPActivity bindOTPActivity();

    @ContributesAndroidInjector(modules = HomeDaggerModel.class)
    abstract HomeAct bindDrawerActActivity();

    @ContributesAndroidInjector(modules = MapFragmentProvider.class)
    abstract LoginAct bindLoginAct();

    @ContributesAndroidInjector(modules = MapFragmentProvider.class)
    abstract KeyValidationAct bindKeyValidationAct();

    @ContributesAndroidInjector()
    abstract LoginOrSignAct bindLoginOrSignAct();

    @ContributesAndroidInjector(modules = PlaceApiDaggerModel.class)
    abstract PlaceApiAct bindPlaceApiActActivity();

    @ContributesAndroidInjector(modules = MapDaggerModel.class)
    abstract BillDialogFragment provideBillDialogFragment();

    @ContributesAndroidInjector()
    abstract ApplyRefferalAct bindApplyRefferalAct();

}
