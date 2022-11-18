package com.tyt.client.ui.homeScreen.wallet;

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

import com.tyt.client.BR;
import com.tyt.client.R;
import com.tyt.client.databinding.WalletBinding;
import com.tyt.client.retro.responsemodel.WalletHistoryModel;
import com.tyt.client.ui.base.BaseActivity;
import com.tyt.client.ui.base.BaseFragment;
import com.tyt.client.ui.homeScreen.HomeAct;
import com.tyt.client.ui.homeScreen.addMoney.AddMoneyFrag;
import com.tyt.client.ui.homeScreen.manageCard.ManageCardFrag;
import com.tyt.client.ui.homeScreen.profile.ProfileFrag;
import com.tyt.client.utilz.Constants;

import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

public class WalletFrag extends BaseFragment<WalletBinding, WalletFragViewModel> implements WalletNavigator {

    public static final String TAG = "WalletFrag";
    @Inject
    public WalletFragViewModel mViewModel;
    public WalletBinding mBinding;


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
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .disallowAddToBackStack()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .add(R.id.fragment_frame, ManageCardFrag.newInstance(), ManageCardFrag.TAG)
                .commit();
    }

    @Override
    public void onClickAddMoney() {
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .disallowAddToBackStack()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .add(R.id.fragment_frame, AddMoneyFrag.newInstance(mViewModel.DefaultCardId.get(), mViewModel.walletCurrency.get(), mViewModel.WalletBalance.get()), AddMoneyFrag.TAG)
                .commit();
    }

    @Override
    public void loadWalletHistoryData(List<WalletHistoryModel.WalletHistoryData> walletHistoryData, String currencySymbol) {
        WalletAdapter walletAdapter = new WalletAdapter(getActivity(), walletHistoryData, currencySymbol);
        mBinding.recycle.setLayoutManager(new LinearLayoutManager(getActivity()));
        mBinding.recycle.setAdapter(walletAdapter);
    }

    @Override
    public void onCLickBack() {
        Intent intentBroadcasts = new Intent();
        intentBroadcasts.setAction(Constants.BroadcastsActions.BackPressedReceiever);
        LocalBroadcastManager.getInstance(requireActivity()).sendBroadcast(intentBroadcasts);
        Fragment fragment = requireActivity().getSupportFragmentManager().findFragmentByTag(TAG);
        if (fragment != null)
            if (requireActivity().getSupportFragmentManager().findFragmentByTag(ProfileFrag.TAG) == null)
                requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .disallowAddToBackStack()
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .add(R.id.fragment_frame, ProfileFrag.newInstance(), ProfileFrag.TAG)
                        .commit();
    }

    @Override
    public void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(requireActivity()).registerReceiver (WBackReceieved, new IntentFilter(Constants.BroadcastsActions.BackPressedReceiever));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(requireActivity()).unregisterReceiver(WBackReceieved);
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
        ((HomeAct) requireActivity()).ShowHideBar(false);
    }
}
