package com.tyt.driver.ui.homeScreen.bookingConfirm;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.gson.Gson;
import com.tyt.driver.R;
import com.tyt.driver.app.MyApp;
import com.tyt.driver.retro.responsemodel.TranslationModel;
import com.tyt.driver.utilz.CommonUtils;
import com.tyt.driver.utilz.Constants;
import com.tyt.driver.utilz.SharedPrefence;

import java.util.ArrayList;


public class PayTypeSheet extends BottomSheetDialogFragment {

    ArrayList<String> mParam1;
    LinearLayout PB_Cashlayout, PB_Cardlayout, PB_walletlayout;

    String cashAvail, cardAvail, walletAvail;
    SharedPrefence sharedPrefence;
    TranslationModel translationModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getStringArrayList(Constants.EXTRA_Data);
            cashAvail = getArguments().getString("cash");
            cardAvail = getArguments().getString("card");
            walletAvail = getArguments().getString("wallet");
        }

        sharedPrefence = new SharedPrefence(PreferenceManager.getDefaultSharedPreferences(MyApp.getmContext()), PreferenceManager.getDefaultSharedPreferences(MyApp.getmContext()).edit());

        if (!CommonUtils.IsEmpty(sharedPrefence.Getvalue(SharedPrefence.LANGUANGE))) {
            translationModel = new Gson().fromJson(sharedPrefence.Getvalue(sharedPrefence.Getvalue(SharedPrefence.LANGUANGE)), TranslationModel.class);
        }
    }

    /**
     * Set layout to {@link BottomSheetDialogFragment}
     **/
    @Override
    public void setupDialog(final Dialog dialog, int style) {
        super.setupDialog(dialog, style);

        View contentView = View.inflate(getContext(), R.layout.bottom_sheet, null);
        IntializeView(contentView);

        dialog.setContentView(contentView);
    }

    /**
     * Define & set visibility to widgets from xml layout
     **/
    private void IntializeView(View contentView) {
        TextView txt_pay_title = contentView.findViewById(R.id.txt_pay_title);
        TextView txt_pay_cash = contentView.findViewById(R.id.txt_pay_cash);
        TextView txt_pay_card = contentView.findViewById(R.id.txt_pay_card);
        TextView txt_pay_wallet = contentView.findViewById(R.id.txt_pay_wallet);

        txt_pay_title.setText(translationModel.txt_pay_title);
        txt_pay_cash.setText(translationModel.txt_cash);
        txt_pay_card.setText(translationModel.txt_card);
        txt_pay_wallet.setText(translationModel.txt_wallet);

        PB_Cashlayout = contentView.findViewById(R.id.PB_Cashlayout);
        PB_Cardlayout = contentView.findViewById(R.id.PB_Cardlayout);
        PB_walletlayout = contentView.findViewById(R.id.PB_walletlayout);

        Log.e("lknjfklrjfkl", "efefk" + cashAvail + " " + cardAvail + " " + walletAvail);

        if (cashAvail.equalsIgnoreCase("false"))
            PB_Cashlayout.setVisibility(View.GONE);
        if (cardAvail.equalsIgnoreCase("false"))
            PB_Cardlayout.setVisibility(View.GONE);
        if (walletAvail.equalsIgnoreCase("false"))
            PB_walletlayout.setVisibility(View.GONE);

        PB_walletlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, getActivity().getIntent().putExtra(Constants.EXTRA_Data, "wallet"));
                dismiss();
            }
        });

        PB_Cashlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, getActivity().getIntent().putExtra(Constants.EXTRA_Data, "cash"));
                dismiss();
            }
        });

        PB_Cardlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, getActivity().getIntent().putExtra(Constants.EXTRA_Data, "card"));
                dismiss();
            }
        });


    }

    public interface PaymentOnclick {

    }

}