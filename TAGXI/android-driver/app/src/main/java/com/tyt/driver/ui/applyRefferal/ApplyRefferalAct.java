package com.tyt.driver.ui.applyRefferal;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import com.tyt.driver.BR;
import com.tyt.driver.R;
import com.tyt.driver.databinding.ApplyRefferralBinding;
import com.tyt.driver.ui.base.BaseActivity;
import com.tyt.driver.ui.homeScreen.HomeAct;
import com.tyt.driver.utilz.SharedPrefence;

import javax.inject.Inject;

/**
 * Created by naveen on 8/24/17.
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