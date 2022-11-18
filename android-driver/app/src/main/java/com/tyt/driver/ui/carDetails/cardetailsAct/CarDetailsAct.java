package com.tyt.driver.ui.carDetails.cardetailsAct;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.tyt.driver.BR;
import com.tyt.driver.R;
import com.tyt.driver.databinding.ActLoginBinding;
import com.tyt.driver.databinding.CarDetailsBinding;
import com.tyt.driver.ui.base.BaseActivity;
import com.tyt.driver.ui.carDetails.carColor.CarColorFrag;
import com.tyt.driver.ui.carDetails.carNumber.VehNumberFrag;
import com.tyt.driver.ui.carDetails.carTypes.CarTypeFrag;
import com.tyt.driver.ui.carDetails.carYear.VehYearFrag;
import com.tyt.driver.ui.carDetails.carmake.CarMakeFrag;
import com.tyt.driver.ui.carDetails.carmodel.CarModelFrag;
import com.tyt.driver.ui.carDetails.servicelocation.ServiceLocationFrag;
import com.tyt.driver.ui.loginOrSign.LoginOrSignAct;
import com.tyt.driver.utilz.SharedPrefence;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasAndroidInjector;

/**
 * Created by naveen on 8/24/17.
 */

public class CarDetailsAct extends BaseActivity<CarDetailsBinding, CarDetailsViewModel> implements CarDetailsNavigator, HasAndroidInjector {
    @Inject
    SharedPrefence sharedPrefence;
    @Inject
    CarDetailsViewModel loginViewModel;
    CarDetailsBinding actLoginBinding;
    EditText edt_text;
    boolean doubleBackToExitPressedOnce = false;

    String from = "";

    @Inject
    DispatchingAndroidInjector<Object> fragmentDispatchingAndroidInjector;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        actLoginBinding = getViewDataBinding();
        loginViewModel.setNavigator(this);

        if (getIntent() != null) {
            from = getIntent().getStringExtra("from");
            sharedPrefence.savevalue(SharedPrefence.carFrom, from);
        }

        openCarMakeFrag();
    }

    private void openCarMakeFrag() {

        getSupportFragmentManager()
                .beginTransaction()
                .disallowAddToBackStack()
                .setCustomAnimations(R.anim.slide_left, R.anim.slide_right)
                .add(R.id.car_details_frame, CarMakeFrag.newInstance(), CarMakeFrag.TAG)
                .commitAllowingStateLoss();

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
    public CarDetailsViewModel getViewModel() {
        return loginViewModel;
    }

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.car_details;
    }

    @Override
    public SharedPrefence getSharedPrefence() {
        return sharedPrefence;
    }


    @Override
    public void onBackPressed() {
        boolean isChanged = false;
        Fragment f = getSupportFragmentManager().findFragmentById(R.id.car_details_frame);
        switch (f.getTag()) {
            case CarModelFrag.TAG:
            case ServiceLocationFrag.TAG:
            case VehNumberFrag.TAG:
            case CarColorFrag.TAG:
            case VehYearFrag.TAG:
            case CarTypeFrag.TAG:
                getSupportFragmentManager().beginTransaction().remove(f).commit();
                isChanged = true;
                break;
            case CarMakeFrag.TAG:
                if (from.equalsIgnoreCase("otp")) {
                    isChanged = true;
                    startActivity(new Intent(this, LoginOrSignAct.class));
                    finish();
                } else {
                    isChanged = true;
                    finish();
                }
                break;
        }

        if (!isChanged) {
            if (doubleBackToExitPressedOnce) {
                Intent a = new Intent(Intent.ACTION_MAIN);
                a.addCategory(Intent.CATEGORY_HOME);
                a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(a);
                return;
            }
            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, getTranslatedString(R.string.text_double_tap_exit), Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        }
    }


    @Override
    public AndroidInjector<Object> androidInjector() {
        return fragmentDispatchingAndroidInjector;
    }
}