package com.tyt.client.ui.homeScreen.bill;


import com.tyt.client.ui.base.BaseActivity;
import com.tyt.client.ui.base.BaseView;

/**
 * Created by Mahi in 2021.
 */

public interface BillDialogNavigator extends BaseView {
    public void dismissDialog();

    public BaseActivity getAttachedContext();
}
