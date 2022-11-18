package com.tyt.client.ui.homeScreen.sos;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.databinding.library.baseAdapters.BR;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.tyt.client.R;
import com.tyt.client.databinding.SosLayBinding;
import com.tyt.client.retro.responsemodel.SOSModel;
import com.tyt.client.ui.base.BaseActivity;
import com.tyt.client.ui.base.BaseFragment;
import com.tyt.client.ui.homeScreen.HomeAct;
import com.tyt.client.ui.homeScreen.profile.ProfileFrag;

import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

/**
 * Fragment to show stored emergency contacts details.
 */

public class SosFrag extends BaseFragment<SosLayBinding, SosViewModel> implements SosNavigator {

    public static final String TAG = "SosFrag";
    @Inject
    public SosViewModel mViewModel;
    SosLayBinding mBinding;

    FusedLocationProviderClient fusedLocationProviderClient;

    public static Fragment newInstance() {
        return new SosFrag();
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
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
        mViewModel.sosApi();
    }

    @Override
    public SosViewModel getViewModel() {
        return mViewModel;
    }

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.sos_lay;
    }


    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void loadSOS(List<SOSModel> sosModels) {
        SosAdapter sos = new SosAdapter(getActivity(), sosModels, this);
        mBinding.recycle.setLayoutManager(new LinearLayoutManager(getActivity()));
        mBinding.recycle.setAdapter(sos);
    }

    @Override
    public void onClickDelete(SOSModel sosModel) {
        mViewModel.deleteItem(sosModel.getId());
    }

    @Override
    public void onClickCall(SOSModel sosModel) {
        Intent callIntent = new Intent(Intent.ACTION_DIAL);
        callIntent.setData(Uri.parse("tel:" + sosModel.getNumber()));
        startActivity(callIntent);
    }

    @Override
    public BaseActivity getAttachedContext() {
        return (HomeAct) getActivity();
    }


    @Override
    public void onClickBAck() {
        Fragment fragment = requireActivity().getSupportFragmentManager().findFragmentByTag(SosFrag.TAG);
        if (fragment != null)
            if (requireActivity().getSupportFragmentManager().findFragmentByTag(ProfileFrag.TAG) == null)
                requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .disallowAddToBackStack()
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .replace(R.id.fragment_frame, ProfileFrag.newInstance(), ProfileFrag.TAG)
                        .commit();
    }
}
