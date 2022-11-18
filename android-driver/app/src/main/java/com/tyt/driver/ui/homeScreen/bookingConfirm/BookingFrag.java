package com.tyt.driver.ui.homeScreen.bookingConfirm;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.tyt.driver.BR;
import com.tyt.driver.R;
import com.tyt.driver.databinding.BookingFragLayBinding;
import com.tyt.driver.databinding.MakeTripLayBinding;
import com.tyt.driver.retro.responsemodel.EtaModel;
import com.tyt.driver.ui.base.BaseActivity;
import com.tyt.driver.ui.base.BaseFragment;
import com.tyt.driver.ui.homeScreen.HomeAct;
import com.tyt.driver.utilz.CommonUtils;
import com.tyt.driver.utilz.Constants;

import javax.inject.Inject;

public class BookingFrag extends BaseFragment<BookingFragLayBinding, BookingViewModel> implements BookingNavigator {

    public static final String TAG = "BookingFrag";
    @Inject
    public BookingViewModel mViewModel;
    public BookingFragLayBinding mBinding;

    private static EtaModel etaModel;
    private static String pickAddress, dropAddress;
    private static LatLng pLatLng, dLatLng;
    PayTypeSheet payTypeSheet;

    BottomSheetDialogFragment dialogFragment;


    // TODO: Rename and change types and number of parameters
    public static BookingFrag newInstance(EtaModel getEtaModel, String pAddress, String dAddress, LatLng PlatLng, LatLng Dlng) {
        etaModel = getEtaModel;
        pickAddress = pAddress;
        dropAddress = dAddress;
        pLatLng = PlatLng;
        dLatLng = Dlng;
        return new BookingFrag();
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


        mViewModel.etaDataModel = etaModel;

        mViewModel.pickupAddress.set(pickAddress);
        mViewModel.dropAddress.set(dropAddress);
        mViewModel.pickupLatLng.set(pLatLng);
        mViewModel.dropLatLng.set(dLatLng);
        mViewModel.distance.set("" + etaModel.getDistance());
        mViewModel.carModel.set(etaModel.getName());
        mViewModel.tax.set(etaModel.getCurrency() + CommonUtils.doubleDecimalFromat(etaModel.getTaxAmount()));
        mViewModel.price.set(etaModel.getCurrency() + CommonUtils.doubleDecimalFromat(etaModel.getTotal()));
        mViewModel.farePrice.set(etaModel.getCurrency() + CommonUtils.doubleDecimalFromat(etaModel.getRideFare()));

        if (etaModel.getPaymentType().contains("card")) {
            mViewModel.cardAvail.set(true);
        }

        if (etaModel.getPaymentType().contains("cash")) {
            mViewModel.cashAvail.set(true);
        }

        if (etaModel.getPaymentType().contains("wallet")) {
            mViewModel.walletAvail.set(true);
        }

    }


    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    /**
     * initial setup
     **/
    public void Setup() {
        getActivity().setTitle(getBaseAct().getTranslatedString(R.string.app_name));

    }

    @Override
    public BookingViewModel getViewModel() {
        return mViewModel;
    }

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.booking_frag_lay;
    }


    /**
     * returns reference of {@link BaseActivity}
     **/
    @Override
    public BaseActivity getBaseAct() {
        return ((BaseActivity) getActivity());
    }

    @Override
    public Context getAttachedcontext() {
        return getContext();
    }


    @Override
    public void onClickBack() {
        Fragment fragment = getActivity().getSupportFragmentManager().findFragmentByTag(BookingFrag.TAG);
        if (fragment != null)
            getActivity().getSupportFragmentManager().beginTransaction().remove(fragment).commit();
    }

    Dialog waitdialog;

    @Override
    public void openWaitingDialog() {
        waitdialog = new Dialog(getAttachedcontext());
        waitdialog.setContentView(R.layout.waiting_dialog);
        TextView cancelBooking = waitdialog.findViewById(R.id.cancel_booking);
        TextView txtTitle = waitdialog.findViewById(R.id.txt_waiting_title);
        TextView txtContent = waitdialog.findViewById(R.id.txt_waiting_content);

        txtTitle.setText(getBaseAct().getTranslatedString(R.string.txt_connecting_to_driver));
        txtContent.setText(getBaseAct().getTranslatedString(R.string.txt_your_req_progress));
        cancelBooking.setText(getBaseAct().getTranslatedString(R.string.txt_cancel_ride));

        if (waitdialog.getWindow() != null) {
            waitdialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        waitdialog.setCanceledOnTouchOutside(false);
        waitdialog.setCancelable(false);

        cancelBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                waitdialog.dismiss();
                startActivity(new Intent(getActivity(), HomeAct.class));
                if (getActivity().getSupportFragmentManager().findFragmentByTag(BookingFrag.TAG) != null)
                    getActivity().getSupportFragmentManager().beginTransaction().remove(new BookingFrag()).commit();
            }
        });

        waitdialog.show();
    }

    @Override
    public void onClickChoose() {
        payTypeSheet = new PayTypeSheet();
        Bundle bundle = new Bundle();
        bundle.putString("cash", "" + mViewModel.cashAvail.get());
        bundle.putString("card", "" + mViewModel.cardAvail.get());
        bundle.putString("wallet", "" + mViewModel.walletAvail.get());
        payTypeSheet.setArguments(bundle);
        payTypeSheet.setTargetFragment(this, Constants.BOTTOMSHEETCALLBACK);
        payTypeSheet.show(getBaseActivity().getSupportFragmentManager(), "BottomSheet Fragment");
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.BOTTOMSHEETCALLBACK) {
            switch (data.getStringExtra(Constants.EXTRA_Data)) {
                case "card":
                    mViewModel.payType.set("Card");
                    break;
                case "cash":
                    mViewModel.payType.set("Cash");
                    break;
                case "wallet":
                    mViewModel.payType.set("Wallet");
                    break;
                default:
                    break;
            }
        }
    }
}
