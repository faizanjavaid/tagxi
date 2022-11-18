package com.tyt.client.ui.keyValidation;

import com.tyt.client.ui.base.BaseActivity;
import com.tyt.client.ui.base.BaseView;

/**
 * Created by Mahi in 2021.
 */

public interface KeyValidationNavigator extends BaseView {

    BaseActivity getBaseAct();

    void openPages();

    void opencontactdialog();

    void openwebsite();

    void onClickCountryChoose();

    String getCountryCode();

    String getCountryShortName();

    void setCountryCode(String flat);

    void closecontactpage();
}
