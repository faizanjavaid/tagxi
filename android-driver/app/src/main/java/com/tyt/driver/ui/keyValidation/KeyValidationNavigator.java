package com.tyt.driver.ui.keyValidation;


import com.tyt.driver.ui.base.BaseActivity;
import com.tyt.driver.ui.base.BaseView;

/**
 * Created by root on 10/7/17.
 */

public interface KeyValidationNavigator extends BaseView {

    void openPages();

    BaseActivity getBaseAct();

    void opencontactdialog();

    void onClickCountryChoose();

    String getCountryCode();

    String getCountryShortName();

    void setCountryCode(String flat);

    void closecontactpage();

    void openwebsite();

}
