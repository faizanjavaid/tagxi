package com.tyt.driver.ui.loginOrSign;

import com.tyt.driver.ui.base.BaseActivity;
import com.tyt.driver.ui.base.BaseView;

import java.util.List;

/**
 * Created by root on 10/7/17.
 */

public interface LoginOrSignNavigator extends BaseView {

    BaseActivity getBaseAct();

    void onClickLogin();

    void onClickSignup();

    void  languageslist(List<String> languages);

}
