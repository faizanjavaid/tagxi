package com.tyt.driver.ui.carDetails.carNumber;

import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.Fragment;

import com.tyt.driver.BR;
import com.tyt.driver.R;
import com.tyt.driver.databinding.CarTypeLayBinding;
import com.tyt.driver.databinding.VehNumLayBinding;
import com.tyt.driver.ui.base.BaseActivity;
import com.tyt.driver.ui.base.BaseFragment;
import com.tyt.driver.ui.carDetails.carYear.VehYearFrag;

import javax.inject.Inject;

public class VehNumberFrag extends BaseFragment<VehNumLayBinding, VehNumberViewModel> implements VehNumNavigator {

    public static final String TAG = "VehNumberFrag";
    @Inject
    public VehNumberViewModel viewModel;
    public VehNumLayBinding binding;

    public static Fragment newInstance() {
        return new VehNumberFrag();
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
    public VehNumberViewModel getViewModel() {
        return viewModel;
    }

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.veh_num_lay;
    }


    /**
     * returns reference of {@link BaseActivity}
     **/
    @Override
    public BaseActivity getBaseAct() {
        return ((BaseActivity) getActivity());
    }


    @Override
    public void onClickContiue() {
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .disallowAddToBackStack()
                .setCustomAnimations(R.anim.slide_left, R.anim.slide_right)
                .add(R.id.car_details_frame, VehYearFrag.newInstance(), VehYearFrag.TAG)
                .commitAllowingStateLoss();
    }

    @Override
    public void onClickBack() {
        if (getActivity().getSupportFragmentManager().findFragmentByTag(VehNumberFrag.TAG) != null) {
            Fragment fragment = getActivity().getSupportFragmentManager().findFragmentByTag(VehNumberFrag.TAG);
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
