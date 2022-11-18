package com.tyt.driver.ui.bill;


import com.tyt.driver.ui.base.BaseActivity;
import com.tyt.driver.ui.base.BaseView;

/**
 * Created by root on 12/28/17.
 */

public interface BillDialogNavigator extends BaseView {
    public void dismissDialog();
    public BaseActivity getAttachedContext();
}
