package com.tyt.driver.ui.docsEdit;

import com.tyt.driver.ui.base.BaseActivity;
import com.tyt.driver.ui.base.BaseView;

/**
 * Created by root on 10/7/17.
 */

public interface DocsEditNavigator extends BaseView {

    BaseActivity getBaseAct();

    void onClickExpiry();

    void onClickImage();

    void closeAct();

    void onClickBack();
}
