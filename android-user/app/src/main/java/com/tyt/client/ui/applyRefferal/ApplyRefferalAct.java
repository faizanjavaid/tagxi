package com.tyt.client.ui.applyRefferal;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tyt.client.BR;
import com.tyt.client.R;
import com.tyt.client.databinding.ApplyRefferralBinding;
import com.tyt.client.ui.base.BaseActivity;
import com.tyt.client.ui.homeScreen.HomeAct;
import com.tyt.client.utilz.SharedPrefence;

import javax.inject.Inject;

/**
 * Created by Mahi in 2021.
 */

public class ApplyRefferalAct extends BaseActivity<ApplyRefferralBinding, ApplyRefferalViewModel> implements ApplyRefferalNavigator {
    @Inject
    SharedPrefence sharedPrefence;

    @Inject
    ApplyRefferalViewModel applyRefferalViewModel;
    ApplyRefferralBinding applyRefferralBinding;
    EditText edt_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        applyRefferralBinding = getViewDataBinding();
        applyRefferalViewModel.setNavigator(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public BaseActivity getBaseAct() {
        return this;
    }

    //Make Intent to the HomeActivity After Applying the Referal Code
    @Override
    public void openHomeAct() {
        startActivity(new Intent(this, HomeAct.class));
        finish();
    }

    @Override
    public ApplyRefferalViewModel getViewModel() {
        return applyRefferalViewModel;
    }

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.apply_refferral;
    }

    @Override
    public SharedPrefence getSharedPrefence() {
        return sharedPrefence;
    }

}