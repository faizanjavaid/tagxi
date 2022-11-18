package com.tyt.driver.ui.homeScreen.vehicleInfo;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.databinding.library.baseAdapters.BR;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.tyt.driver.R;
import com.tyt.driver.databinding.GetRefferalLayBinding;
import com.tyt.driver.databinding.VehInfoLayBinding;
import com.tyt.driver.ui.base.BaseActivity;
import com.tyt.driver.ui.base.BaseFragment;
import com.tyt.driver.ui.carDetails.cardetailsAct.CarDetailsAct;
import com.tyt.driver.ui.homeScreen.HomeAct;
import com.tyt.driver.ui.homeScreen.profile.ProfileFrag;

import javax.inject.Inject;

import static android.content.Context.CLIPBOARD_SERVICE;

public class VehicleInfoFrag extends BaseFragment<VehInfoLayBinding, VehicleInfoViewModel> implements VehicleInfoNavigator {

    public static final String TAG = "VehicleInfoFrag";
    @Inject
    public VehicleInfoViewModel mViewModel;
    public VehInfoLayBinding mBinding;


    // TODO: Rename and change types and number of parameters
    public static VehicleInfoFrag newInstance() {
        return new VehicleInfoFrag();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBinding = getViewDataBinding();
        mViewModel.setNavigator(this);

        mViewModel.getProfileApi();
    }


    @Override
    public VehicleInfoViewModel getViewModel() {
        return mViewModel;
    }

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.veh_info_lay;
    }

    /**
     * returns reference of {@link BaseActivity}
     **/


    @Override
    public void onClickBack() {
        if (getActivity().getSupportFragmentManager().findFragmentByTag(VehicleInfoFrag.TAG) != null) {
            ((HomeAct) getActivity()).ShowHideBar(true);
            Fragment frag = getActivity().getSupportFragmentManager().findFragmentByTag(VehicleInfoFrag.TAG);
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_frame, ProfileFrag.newInstance(), ProfileFrag.TAG)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .commit();
        } else {
            Log.e("elkfjefjke", "lejfioe");
        }
    }

    @Override
    public void onClickEdit() {
        startActivity(new Intent(getActivity(), CarDetailsAct.class).putExtra("from", "vehicle"));
    }


    @Override
    public void onResume() {
        super.onResume();
        ((HomeAct) getActivity()).ShowHideBar(false);
    }
}
