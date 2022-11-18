package com.tyt.driver.ui.wallet;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.tyt.driver.BR;
import com.tyt.driver.R;
import com.tyt.driver.databinding.WalletBinding;
import com.tyt.driver.retro.responsemodel.WalletHistoryModel;
import com.tyt.driver.ui.addMoney.AddMoneyFrag;
import com.tyt.driver.ui.base.BaseActivity;
import com.tyt.driver.ui.base.BaseFragment;
import com.tyt.driver.ui.homeScreen.HomeAct;
import com.tyt.driver.ui.homeScreen.manageCard.ManageCardFrag;
import com.tyt.driver.ui.homeScreen.profile.ProfileFrag;
import com.tyt.driver.utilz.Constants;

import java.util.List;

import javax.inject.Inject;

public class WalletFrag extends BaseFragment<WalletBinding, WalletFragViewModel> implements WalletNavigator {

    public static final String TAG = "WalletFrag";
    @Inject
    public WalletFragViewModel mViewModel;
    public WalletBinding mBinding;
    WalletAdapter walletAdapter;


    public static WalletFrag newInstance() {
        return new WalletFrag();
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

        mViewModel.walletHistoryApi();
    }

    @Override
    public WalletFragViewModel getViewModel() {
        return mViewModel;
    }

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.wallet;
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
    public void onCLickAddCard() {
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .disallowAddToBackStack()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .add(R.id.fragment_frame, ManageCardFrag.newInstance(), ManageCardFrag.TAG)
                .commit();
    }

    @Override
    public void onClickAddMoney() {
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .disallowAddToBackStack()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .add(R.id.fragment_frame, AddMoneyFrag.newInstance(mViewModel.DefaultCardId.get(), mViewModel.walletCurrency.get(), mViewModel.WalletBalance.get()), AddMoneyFrag.TAG)
                .commit();
    }

    @Override
    public void loadWalletHistoryData(List<WalletHistoryModel.WalletHistoryData> walletHistoryData, String currencySymbol) {
        walletAdapter = new WalletAdapter(getActivity(), walletHistoryData, currencySymbol);
        mBinding.walletRecycle.setLayoutManager(new LinearLayoutManager(getActivity()));
        mBinding.walletRecycle.setAdapter(walletAdapter);
    }

    @Override
    public void onCLickBack() {
        Intent intentBroadcasts = new Intent();
        intentBroadcasts.setAction(Constants.BroadcastsActions.BackPressedReceiever);
        LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intentBroadcasts);
        Fragment fragment = getActivity().getSupportFragmentManager().findFragmentByTag(TAG);
        if (fragment != null)
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_frame, ProfileFrag.newInstance(), ProfileFrag.TAG)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .commit();
    }

    @Override
    public void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(WBackReceieved, new IntentFilter(Constants.BroadcastsActions.BackPressedReceiever));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(WBackReceieved);
    }

    BroadcastReceiver WBackReceieved = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            mViewModel.walletHistoryApi();
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        ((HomeAct) getActivity()).ShowHideBar(false);
    }
}
