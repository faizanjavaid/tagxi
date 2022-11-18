package com.tyt.client.ui.homeScreen.feedback;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.tyt.client.BR;
import com.tyt.client.R;
import com.tyt.client.databinding.FeedLayBinding;
import com.tyt.client.retro.base.BaseResponse;
import com.tyt.client.retro.responsemodel.ProfileModel;
import com.tyt.client.ui.base.BaseActivity;
import com.tyt.client.ui.base.BaseFragment;
import com.tyt.client.ui.homeScreen.HomeAct;
import com.tyt.client.ui.homeScreen.bill.BillDialogFragment;
import com.tyt.client.ui.homeScreen.manageCard.ManageCardAdapter;

import javax.inject.Inject;

/**
 * Fragment which is used to gave a feedback to driver at the completion of every trip.
 *
* */

public class FeedbackFrag extends BaseFragment<FeedLayBinding, FeedbackFragViewModel> implements FeedbackNavigator {

    public static final String TAG = "AddCardFrag";
    @Inject
    public FeedbackFragViewModel mViewModel;
    public FeedLayBinding mBinding;
    ManageCardAdapter historyAdapter;

    public static ProfileModel tripResponse;
    public static BaseResponse baseResponse;


    // TODO: Rename and change types and number of parameters
    public static FeedbackFrag newInstance(ProfileModel s) {
        tripResponse = s;
        return new FeedbackFrag();
    }

    public static FeedbackFrag newInstance(BaseResponse s) {
        baseResponse = s;
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
            baseResponse=null;
            Log.d(TAG, "onViewCreated: tripResponse ="+tripResponse.getOnTripRequest().getData().getId());
            openBillDialog(tripResponse);
            mViewModel.setValues(tripResponse, mBinding.driverProfImg);
        } else if (baseResponse != null) {
            Log.d(TAG, "onViewCreated: baseResponse ="+baseResponse.getRequestId());
            openBillDialog(baseResponse);
            mViewModel.setValues(baseResponse, mBinding.driverProfImg);
        }

    }

    private void openBillDialog(ProfileModel tripResponse) {
        BillDialogFragment.newInstance(tripResponse).show(this.getChildFragmentManager());
    }

    private void openBillDialog(BaseResponse baseResponse) {
        BillDialogFragment.newInstance(baseResponse).show(this.getChildFragmentManager());
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

    /**
     * returns reference of {@link BaseActivity}
     **/
    @Override
    public BaseActivity getBaseAct() {
        return ((BaseActivity) getActivity());
    }

    @Override
    public void openHomePage() {
        startActivity(new Intent(getActivity(), HomeAct.class));
    }


    @Override
    public void onResume() {
        super.onResume();
        ((HomeAct) getActivity()).ShowHideBar(false);
    }
}
