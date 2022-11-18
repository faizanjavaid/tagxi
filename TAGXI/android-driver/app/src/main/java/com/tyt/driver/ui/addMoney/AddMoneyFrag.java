package com.tyt.driver.ui.addMoney;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.databinding.library.baseAdapters.BR;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.braintreepayments.api.dropin.DropInRequest;
import com.tyt.driver.R;
import com.tyt.driver.databinding.AddMoneyBinding;
import com.tyt.driver.ui.base.BaseActivity;
import com.tyt.driver.ui.base.BaseFragment;
import com.tyt.driver.ui.homeScreen.HomeAct;
import com.tyt.driver.utilz.Constants;


import javax.inject.Inject;

import static com.google.zxing.integration.android.IntentIntegrator.REQUEST_CODE;

public class AddMoneyFrag extends BaseFragment<AddMoneyBinding, AddMoneyViewModel> implements AddMoneyNavigator {

    public static final String TAG = "AddMoneyFrag";
    @Inject
    public AddMoneyViewModel mViewModel;
    public AddMoneyBinding mBinding;
    public static String CARDID, CURRENCY, WALLETBALANCE;

    // TODO: Rename and change types and number of parameters
    public static AddMoneyFrag newInstance(String cardId, String currency, String walletBal) {
        CARDID = cardId;
        CURRENCY = currency;
        WALLETBALANCE = walletBal;
        return new AddMoneyFrag();
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


        Log.e("currency---", "currncy--" + CURRENCY);
        mViewModel.cardIDD.set(CARDID);
        mViewModel.currency.set(CURRENCY);
        mViewModel.walletBal.set(WALLETBALANCE);

        mViewModel.amnt_10.set(CURRENCY + "10");
        mViewModel.amnt_20.set(CURRENCY + "20");
        mViewModel.amnt_30.set(CURRENCY + "30");
        mViewModel.amnt_40.set(CURRENCY + "40");
        mViewModel.amnt_50.set(CURRENCY + "50");
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
    public AddMoneyViewModel getViewModel() {
        return mViewModel;
    }

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.add_money;
    }

    /**
     * returns reference of {@link BaseActivity}
     **/

    @Override
    public void intiatePayment(String clientToken) {
        DropInRequest dropInRequest = new DropInRequest()
                .clientToken(clientToken);
        dropInRequest.disablePayPal();
        startActivityForResult(dropInRequest.getIntent(getActivity()), REQUEST_CODE);
    }

    @Override
    public void onClickBack() {
        Intent intentBroadcasts = new Intent();
        intentBroadcasts.setAction(Constants.BroadcastsActions.BackPressedReceiever);
        LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intentBroadcasts);

        Fragment fragment = getActivity().getSupportFragmentManager().findFragmentByTag(TAG);
        if (fragment != null)
            getActivity().getSupportFragmentManager().beginTransaction().remove(fragment).commit();
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
