package com.tyt.driver.ui.approval;

import com.tyt.driver.ui.base.BaseActivity;
import com.tyt.driver.ui.base.BaseView;

/**
 * Created by root on 10/7/17.
 */

public interface ApprovalNavigator extends BaseView {

    BaseActivity getBaseAct();

    void openHomeAct();

    void openDocumentUpload();
}
