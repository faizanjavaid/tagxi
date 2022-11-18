package com.tyt.driver.ui.feedback;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.tyt.driver.BR;
import com.tyt.driver.R;
import com.tyt.driver.databinding.FeedLayBinding;
import com.tyt.driver.retro.responsemodel.EndTripData;
import com.tyt.driver.retro.responsemodel.ProfileModel;
import com.tyt.driver.ui.base.BaseActivity;
import com.tyt.driver.ui.base.BaseFragment;
import com.tyt.driver.ui.bill.BillDialogFragment;
import com.tyt.driver.ui.homeScreen.HomeAct;
import com.tyt.driver.ui.homeScreen.manageCard.ManageCardAdapter;

import javax.inject.Inject;

public class FeedbackFrag extends BaseFragment<FeedLayBinding, FeedbackFragViewModel> implements FeedbackNavigator {

    public static final String TAG = "AddCardFrag";
    @Inject
    public FeedbackFragViewModel mViewModel;
    public FeedLayBinding mBinding;
    ManageCardAdapter historyAdapter;

    public static ProfileModel tripResponse;
    public static EndTripData endTripData;


    // TODO: Rename and change types and number of parameters
    public static FeedbackFrag newInstance(ProfileModel s) {
        tripResponse = s;
        return new FeedbackFrag();
    }


    public static FeedbackFrag newInstance(EndTripData s) {
        endTripData = s;
        return new FeedbackFrag();
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

        if (tripResponse != null) {
            openBillDialog(tripResponse);
            mViewModel.setValues(tripResponse, mBinding.driverProfImg);
        } else if (endTripData != null) {
            openBillDialog(endTripData);
            mViewModel.setValues(endTripData, mBinding.driverProfImg);
        }


//        ReqData data = CommonUtils.getSingleObject(tripResponse, ReqData.class);
//        Log.e("BillPrice---", "price---" + data.getRequestBill().getData().getBasePrice());
    }

    private void openBillDialog(ProfileModel tripResponse) {
        Log.d("xxxFeedBackFr", "openBillDialog: ProfileModel");
        BillDialogFragment.newInstance(tripResponse).show(this.getChildFragmentManager());
    }

    private void openBillDialog(EndTripData endTripData) {
        Log.d("xxxFeedBackFr", "openBillDialog: EndTripData");
        BillDialogFragment.newInstance(endTripData).show(this.getChildFragmentManager());
    }

    @Override
    public FeedbackFragViewModel getViewModel() {
        return mViewModel;
    }

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.feed_lay;
    }


    @Override
    public void openHomePage() {
        startActivity(new Intent(getActivity(), HomeAct.class));
    }

    @Override
    public BaseActivity getBaseAct() {
        return getBaseActivity();
    }

    @Override
    public void onResume() {
        super.onResume();
        ((HomeAct) getActivity()).ShowHideBar(false);
    }
}
