package com.tyt.driver.ui.homeScreen.manageCard;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.braintreepayments.api.dropin.DropInActivity;
import com.braintreepayments.api.dropin.DropInRequest;
import com.braintreepayments.api.dropin.DropInResult;
import com.braintreepayments.api.models.PaymentMethodNonce;
import com.tyt.driver.BR;
import com.tyt.driver.R;
import com.tyt.driver.databinding.HistoryLayBinding;
import com.tyt.driver.databinding.ManageCardsBinding;
import com.tyt.driver.retro.responsemodel.CardListModel;
import com.tyt.driver.ui.base.BaseActivity;
import com.tyt.driver.ui.base.BaseFragment;
import com.tyt.driver.ui.homeScreen.HomeAct;
import com.tyt.driver.ui.homeScreen.addCard.AddCardFrag;
import com.tyt.driver.ui.homeScreen.bookingConfirm.BookingFrag;
import com.tyt.driver.ui.homeScreen.profile.ProfileFrag;
import com.tyt.driver.utilz.Constants;

import java.util.List;

import javax.inject.Inject;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static com.google.zxing.integration.android.IntentIntegrator.REQUEST_CODE;

public class ManageCardFrag extends BaseFragment<ManageCardsBinding, ManageCardViewModel> implements ManageCardNavigator {

    public static final String TAG = "ManageCardFrag";
    @Inject
    public ManageCardViewModel mViewModel;
    public ManageCardsBinding mBinding;
    ManageCardAdapter historyAdapter;


    // TODO: Rename and change types and number of parameters
    public static ManageCardFrag newInstance() {
        return new ManageCardFrag();
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

        mViewModel.manageCardApi();
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
    public ManageCardViewModel getViewModel() {
        return mViewModel;
    }

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.manage_cards;
    }

    /**
     * returns reference of {@link BaseActivity}
     **/
    @Override
    public BaseActivity getBaseAct() {
        return ((BaseActivity) getActivity());
    }

    @Override
    public void loadManageCardList(List<CardListModel> cardListModels) {
        if (cardListModels.size() > 0) {
            mViewModel.noItemFound.set(false);
            historyAdapter = new ManageCardAdapter(getActivity(), cardListModels, this);
            mBinding.manageCardRecycle.setLayoutManager(new LinearLayoutManager(getActivity()));
            mBinding.manageCardRecycle.setAdapter(historyAdapter);
        } else mViewModel.noItemFound.set(true);

    }

    @Override
    public void openAddcardFrag() {
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .disallowAddToBackStack()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .add(R.id.fragment_frame, AddCardFrag.newInstance(), AddCardFrag.TAG)
                .commit();
    }


    @Override
    public void onResume() {
        super.onResume();
        ((HomeAct) getActivity()).ShowHideBar(false);
    }

    @Override
    public void intiatePayment(String clientToken) {
        DropInRequest dropInRequest = new DropInRequest()
                .clientToken(clientToken);
        dropInRequest.disablePayPal();
        startActivityForResult(dropInRequest.getIntent(getActivity()), REQUEST_CODE);
    }

    @Override
    public void deleteClick(String id) {
        mViewModel.DeletCard(id);
    }

    @Override
    public void clickBack() {
        Intent intentBroadcasts = new Intent();
        intentBroadcasts.setAction(Constants.BroadcastsActions.BackPressedReceiever);
        LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intentBroadcasts);
        if (getActivity().getSupportFragmentManager().findFragmentByTag(BookingFrag.TAG) != null) {
            intentBroadcasts.setAction(Constants.BroadcastsActions.BackPressedReceiever);
            LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intentBroadcasts);
        }
        Fragment fragment = getActivity().getSupportFragmentManager().findFragmentByTag(TAG);
        if (fragment != null)
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_frame, ProfileFrag.newInstance(), ProfileFrag.TAG)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .commit();
    }

    @Override
    public void clickCardItem(String id) {
        mViewModel.DefaultCardAPi(id);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            Log.e("Resultt===", "Enteringggg===");
            if (resultCode == RESULT_OK) {
                DropInResult result = data.getParcelableExtra(DropInResult.EXTRA_DROP_IN_RESULT);
                PaymentMethodNonce paymentMethodNonce = result.getPaymentMethodNonce();
                mViewModel.AddCardApi(paymentMethodNonce);
                Log.e("Resultt===", "result===" + result);
            } else if (resultCode == RESULT_CANCELED) {
                Log.e("Resultt===", "Cancelleled===");
            } else {
                Exception error = (Exception) data.getSerializableExtra(DropInActivity.EXTRA_ERROR);
                Log.e("Resultt===", "Error===");
            }
        }
    }

}
