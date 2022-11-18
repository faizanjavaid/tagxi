package com.nplus.countrylist;

/**
 * Created by root on 3/6/18.
 */

public interface CountryCodeChangeListener {
    /**
     * call back method for returning the country code, country Names on change of Country while
     * Registration, and Company Key requesting Screen
     * @param countryCode is the number +91
     * @param countryShort is the short country name IN
     * @param countryLong is the long country name India
     * * */
    void onCountryCodeChanged(String countryCode, String countryShort, String countryLong);
}
