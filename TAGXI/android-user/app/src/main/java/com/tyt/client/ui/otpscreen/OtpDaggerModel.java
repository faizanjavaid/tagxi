package com.tyt.client.ui.otpscreen;

import java.util.HashMap;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Mahi in 2021.
 */
@Module
public class OtpDaggerModel {

    @Provides
    @Named("HashMapData")
    static HashMap<String, String> provideData(OTPActivity otpActivity) {
        return otpActivity.Bindabledata;
    }

}
