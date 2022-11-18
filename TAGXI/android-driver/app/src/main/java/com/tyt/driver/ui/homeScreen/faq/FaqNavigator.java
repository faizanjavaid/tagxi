package com.tyt.driver.ui.homeScreen.faq;

import com.tyt.driver.retro.responsemodel.Faqmodel;
import com.tyt.driver.ui.base.BaseActivity;
import com.tyt.driver.ui.base.BaseView;

import java.util.List;

/**
 * Created by root on 11/13/17.
 */

public interface FaqNavigator extends BaseView {
    boolean isNetworkConnected();

    void loadFaq(List<Faqmodel> faqmodels);

    void onClickBAck();

    public BaseActivity getAttachedContext();
}
