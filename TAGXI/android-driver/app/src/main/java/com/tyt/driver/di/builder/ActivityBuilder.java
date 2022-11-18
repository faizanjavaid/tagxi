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

package com.tyt.driver.di.builder;

import com.tyt.driver.ui.acceptReject.AccepRejectAct;
import com.tyt.driver.ui.applyRefferal.ApplyRefferalAct;
import com.tyt.driver.ui.approval.ApprovalAct;
import com.tyt.driver.ui.bill.BillDialogFragment;
import com.tyt.driver.ui.carDetails.cardetailsAct.CarDetailsAct;
import com.tyt.driver.ui.country.MapDaggerModel;
import com.tyt.driver.ui.country.MapFragmentProvider;
import com.tyt.driver.ui.docsEdit.DocsEditAct;
import com.tyt.driver.ui.documentUpload.DocumentUploadAct;
import com.tyt.driver.ui.homeScreen.HomeAct;
import com.tyt.driver.ui.homeScreen.placeapiscreen.PlaceApiAct;
import com.tyt.driver.ui.homeScreen.placeapiscreen.PlaceApiDaggerModel;
import com.tyt.driver.ui.forgot.ForgetPwdActivity;
import com.tyt.driver.ui.keyValidation.KeyValidationAct;
import com.tyt.driver.ui.login.LoginAct;
import com.tyt.driver.ui.loginOrSign.LoginOrSignAct;
import com.tyt.driver.ui.otpscreen.OTPActivity;
import com.tyt.driver.ui.otpscreen.OtpDaggerModel;
import com.tyt.driver.ui.signup.SignupActivity;
import com.tyt.driver.ui.splash.SplashScreen;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Here bind the subcomponent of Activitie's and
 **/

@Module
public abstract class ActivityBuilder {

    @ContributesAndroidInjector(modules = MapFragmentProvider.class)
    abstract SignupActivity bindSignupActivity();

    @ContributesAndroidInjector()
    abstract AccepRejectAct bindAccepRejectAct();

    @ContributesAndroidInjector()
    abstract SplashScreen bindSplashScreen();

    @ContributesAndroidInjector(modules = OtpDaggerModel.class)
    abstract OTPActivity bindOTPActivity();

    @ContributesAndroidInjector()
    abstract ForgetPwdActivity bindForgetPwdActivity();

    @ContributesAndroidInjector()
    abstract DocumentUploadAct bindDocumentUploadAct();

    @ContributesAndroidInjector()
    abstract DocsEditAct bindDocsEditAct();

    @ContributesAndroidInjector(modules = HomeDaggerModel.class)
    abstract HomeAct bindDrawerActActivity();

    @ContributesAndroidInjector(modules = HomeDaggerModel.class)
    abstract CarDetailsAct bindCarDetailsAct();

    @ContributesAndroidInjector(modules = MapFragmentProvider.class)
    abstract LoginAct bindLoginAct();

    @ContributesAndroidInjector()
    abstract ApprovalAct bindApprovalAct();

    @ContributesAndroidInjector()
    abstract LoginOrSignAct bindLoginOrSignAct();

    @ContributesAndroidInjector(modules = PlaceApiDaggerModel.class)
    abstract PlaceApiAct bindPlaceApiActActivity();

    @ContributesAndroidInjector()
    abstract ApplyRefferalAct bindApplyRefferalAct();

    @ContributesAndroidInjector(modules = MapFragmentProvider.class)
    abstract KeyValidationAct bindKeyValidationAct();

    @ContributesAndroidInjector(modules = MapDaggerModel.class)
    abstract BillDialogFragment provideBillDialogFragment();
}
