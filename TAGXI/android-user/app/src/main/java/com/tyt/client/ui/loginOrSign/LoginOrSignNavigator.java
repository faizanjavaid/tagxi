package com.tyt.client.ui.loginOrSign;

import com.tyt.client.ui.base.BaseActivity;
import com.tyt.client.ui.base.BaseView;

import java.util.List;

/**
 * Created by Mahi in 2021.
 */

public interface LoginOrSignNavigator extends BaseView {

    BaseActivity getBaseAct();

    void onClickLogin();

    void  languageslist(List<String> languages);

}
