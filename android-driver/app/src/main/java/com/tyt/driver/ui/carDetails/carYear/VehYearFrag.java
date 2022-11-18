package com.tyt.driver.ui.carDetails.carYear;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.Fragment;

import com.tyt.driver.BR;
import com.tyt.driver.R;
import com.tyt.driver.databinding.VehNumLayBinding;
import com.tyt.driver.databinding.VehYearLayBinding;
import com.tyt.driver.ui.applyRefferal.ApplyRefferalAct;
import com.tyt.driver.ui.base.BaseActivity;
import com.tyt.driver.ui.base.BaseFragment;
import com.tyt.driver.ui.carDetails.cardetailsAct.CarDetailsAct;
import com.tyt.driver.ui.carDetails.carmodel.CarModelFrag;
import com.tyt.driver.ui.homeScreen.HomeAct;

import javax.inject.Inject;

public class VehYearFrag extends BaseFragment<VehYearLayBinding, VehYearViewModel> implements VehYearNavigator {

    public static final String TAG = "VehYearFrag";
    @Inject
    public VehYearViewModel viewModel;
    public VehYearLayBinding binding;

    public static Fragment newInstance() {
        return new VehYearFrag();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = getViewDataBinding();
        viewModel.setNavigator(this);
    }


    @Override
    public VehYearViewModel getViewModel() {
        return viewModel;
    }

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.veh_year_lay;
    }


    /**
     * returns reference of {@link BaseActivity}
     **/
    @Override
    public BaseActivity getBaseAct() {
        return ((BaseActivity) getActivity());
    }


    @Override
    public void onClickConfirm() {

    }

    @Override
    public void openHomeAct() {
        startActivity(new Intent(getActivity(), HomeAct.class));
    }

    @Override
    public void openRefferalAct() {
        startActivity(new Intent(getActivity(), ApplyRefferalAct.class));
    }

    @Override
    public void openProfile() {
//        if (getActivity() != null)
//            ((CarDetailsAct) getActivity()).finish();
        openHomeAct();
    }

    @Override
    public void onCLickBack() {
        if (getActivity().getSupportFragmentManager().findFragmentByTag(VehYearFrag.TAG) != null) {
            Fragment fragment = getActivity().getSupportFragmentManager().findFragmentByTag(VehYearFrag.TAG);
            getActivity().getSupportFragmentManager().beginTransaction().remove(fragment).commit();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


}
