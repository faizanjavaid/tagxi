package com.tyt.driver.ui.otpscreen;

import java.util.HashMap;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

/**
 * Created by root on 10/9/17.
 */
@Module
public class OtpDaggerModel {

    @Provides
    @Named("HashMapData")
    static HashMap<String, String> provideData(OTPActivity otpActivity) {
        return otpActivity.Bindabledata;
    }
}
