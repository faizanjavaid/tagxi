package com.tyt.client.ui.homeScreen.addMoney;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.braintreepayments.api.dropin.DropInRequest;
import com.tyt.client.BR;
import com.tyt.client.R;
import com.tyt.client.databinding.AddCardLayBinding;
import com.tyt.client.databinding.AddMoneyBinding;
import com.tyt.client.ui.base.BaseActivity;
import com.tyt.client.ui.base.BaseFragment;
import com.tyt.client.ui.homeScreen.HomeAct;
import com.tyt.client.utilz.Constants;

import javax.inject.Inject;

import static com.google.zxing.integration.android.IntentIntegrator.REQUEST_CODE;

import java.util.Objects;

/**
 * Fragment which is used to add the Money into an user's wallet using the given card by the user.
 *
* */


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
    public void onClickBack() {
        Fragment fragment = requireActivity().getSupportFragmentManager().findFragmentByTag(TAG);
        if (fragment != null)
            requireActivity().getSupportFragmentManager().beginTransaction().remove(fragment).commit();
    }

    @Override
    public void onResume() {
        super.onResume();
        ((HomeAct) requireActivity()).ShowHideBar(false);
    }
}
