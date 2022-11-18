package com.tyt.driver.ui.homeScreen.addCard;

import android.os.Bundle;
import android.view.View;

import com.braintreepayments.api.dropin.DropInRequest;
import com.tyt.driver.BR;
import com.tyt.driver.R;
import com.tyt.driver.databinding.AddCardLayBinding;
import com.tyt.driver.databinding.ManageCardsBinding;
import com.tyt.driver.ui.base.BaseActivity;
import com.tyt.driver.ui.base.BaseFragment;
import com.tyt.driver.ui.homeScreen.HomeAct;
import com.tyt.driver.ui.homeScreen.manageCard.ManageCardAdapter;

import javax.inject.Inject;

import static com.google.zxing.integration.android.IntentIntegrator.REQUEST_CODE;

public class AddCardFrag extends BaseFragment<AddCardLayBinding, AddCardViewModel> implements AddCardNavigator {

    public static final String TAG = "AddCardFrag";
    @Inject
    public AddCardViewModel mViewModel;
    public AddCardLayBinding mBinding;
    ManageCardAdapter historyAdapter;


    // TODO: Rename and change types and number of parameters
    public static AddCardFrag newInstance() {
        return new AddCardFrag();
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
    public AddCardViewModel getViewModel() {
        return mViewModel;
    }

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.add_card_lay;
    }

    /**
     * returns reference of {@link BaseActivity}
     **/
    @Override
    public BaseActivity getBaseAct() {
        return ((BaseActivity) getActivity());
    }

    @Override
    public void intiatePayment(String clientToken) {
        DropInRequest dropInRequest = new DropInRequest()
                .clientToken(clientToken);
        dropInRequest.disablePayPal();
        startActivityForResult(dropInRequest.getIntent(getActivity()), REQUEST_CODE);
    }



    @Override
    public void onResume() {
        super.onResume();
        ((HomeAct) getActivity()).ShowHideBar(false);
    }
}
