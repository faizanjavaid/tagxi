package com.tyt.driver.ui.historyDetail;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.Fragment;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.tyt.driver.BR;
import com.tyt.driver.R;
import com.tyt.driver.databinding.HistoryDetailsBinding;
import com.tyt.driver.ui.base.BaseActivity;
import com.tyt.driver.ui.base.BaseFragment;
import com.tyt.driver.ui.homeScreen.HomeAct;
import com.tyt.driver.ui.loginOrSign.LoginOrSignAct;

import javax.inject.Inject;

public class HistoryDetailFrag extends BaseFragment<HistoryDetailsBinding, HistoryDetailViewModel> implements HistoryDetailNavigator {

    public static final String TAG = "HistoryDetailFrag";
    @Inject
    public HistoryDetailViewModel mViewModel;
    public HistoryDetailsBinding mBinding;
    public static String IDD = "";
    ShimmerFrameLayout shimmerFrameLayout;


    // TODO: Rename and change types and number of parameters
    public static HistoryDetailFrag newInstance(String id) {
        IDD = id;
        return new HistoryDetailFrag();
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

        shimmerFrameLayout=view.findViewById(R.id.detail_shim);
        mViewModel.getSingleHistoryApi(IDD);
        mViewModel.IDD.set(IDD);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }


    @Override
    public HistoryDetailViewModel getViewModel() {
        return mViewModel;
    }

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.history_details;
    }


    @Override
    public BaseActivity getBaseAct() {
        return ((BaseActivity) getActivity());
    }

    @Override
    public void onClickBack() {
        if (getActivity().getSupportFragmentManager().findFragmentByTag(TAG) != null) {
            ((HomeAct) getActivity()).ShowHideBar(true);
            Fragment fragment = getActivity().getSupportFragmentManager().findFragmentByTag(TAG);
            getActivity().getSupportFragmentManager().beginTransaction().remove(fragment).commit();
        }
    }

    @Override
    public void openFromStart() {
        startActivity(new Intent(getActivity(), LoginOrSignAct.class));
    }

    @Override
    public void startshim() {
        shimmerFrameLayout.startShimmer();
        getViewDataBinding().detailShim.setVisibility(View.VISIBLE);
        getViewDataBinding().hisDetailMainLay.setVisibility(View.GONE);
    }

    @Override
    public void stopshim() {
        shimmerFrameLayout.stopShimmer();
        getViewDataBinding().detailShim.setVisibility(View.GONE);
        getViewDataBinding().hisDetailMainLay.setVisibility(View.VISIBLE);
    }


    @Override
    public void onResume() {
        super.onResume();
        ((HomeAct) getActivity()).ShowHideBar(false);
    }
}
