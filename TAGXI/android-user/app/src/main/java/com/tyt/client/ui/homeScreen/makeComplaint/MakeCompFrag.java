package com.tyt.client.ui.homeScreen.makeComplaint;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.core.content.ContextCompat;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.tyt.client.BR;
import com.tyt.client.R;
import com.tyt.client.app.MyApp;
import com.tyt.client.databinding.FragmentMakeCompBinding;
import com.tyt.client.retro.responsemodel.ComplaintTypesModel;
import com.tyt.client.ui.base.BaseFragment;
import com.tyt.client.ui.homeScreen.HomeAct;
import com.tyt.client.ui.homeScreen.profile.ProfileFrag;
import com.tyt.client.ui.homeScreen.userHistory.HistoryFrag;
import com.tyt.client.utilz.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

/**
 * Fragment which is used to make queries to admin from history page and profile page
* */

public class MakeCompFrag extends BaseFragment<FragmentMakeCompBinding, MakeCompViewModel> implements MakeCompNavigator, View.OnClickListener {

    public static final String TAG = "MakeCompFrag";
    private String Complaint_Type = "";
    private static String History_ID;
    private static String Page_Name;
    @Inject
    public MakeCompViewModel makeCompViewModel;
    public FragmentMakeCompBinding fragmentMakeCompBinding;

    List<ComplaintTypesModel> complaintsList;

    String Complaint_ID = "";
    String Complaint = "";

    ColorStateList colorDefaultStateList, colorSelectedText;

    public static MakeCompFrag newInstance(String history_ID, String page_name) {

        History_ID = history_ID;
        Page_Name = page_name;

        return new MakeCompFrag();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fragmentMakeCompBinding = getViewDataBinding();
        makeCompViewModel.setNavigator(this);

        complaintsList = new ArrayList<>();

        if (Page_Name.equalsIgnoreCase(Constants.history_comptype)) {
            Complaint_Type = "request";
        } else if (Page_Name.equalsIgnoreCase(Constants.profile_comptype)) {
            Complaint_Type = "general";
        }

        if (!Complaint_Type.equals(""))
            makeCompViewModel.getcomplaint_types(Complaint_Type);

        fragmentMakeCompBinding.btSubmitComplaint.setOnClickListener(this);
        fragmentMakeCompBinding.closeIcComp.setOnClickListener(this);
        colorSelectedText = ContextCompat.getColorStateList(MyApp.getmContext(), R.color.clr_FF0000);
        colorDefaultStateList = ContextCompat.getColorStateList(MyApp.getmContext(), R.color.blue);

        fragmentMakeCompBinding.complaintEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                fragmentMakeCompBinding.compCharCount.setText("Char Count : " + charSequence.length());

                if (charSequence.length() <= 9 || charSequence.length() >= 150) {
                    fragmentMakeCompBinding.compCharCount.setTextColor(colorSelectedText);
                } else {
                    fragmentMakeCompBinding.compCharCount.setTextColor(colorDefaultStateList);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    ArrayAdapter<ComplaintTypesModel> spin_adapter;

    private void setSpinner() {
        spin_adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, complaintsList);

        spin_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        fragmentMakeCompBinding.complaintsTypeSpinner.setAdapter(spin_adapter);

        fragmentMakeCompBinding.complaintsTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ComplaintTypesModel complaints = (ComplaintTypesModel) adapterView.getSelectedItem();
                displayComplaintData(complaints);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    @Override
    public MakeCompViewModel getViewModel() {
        return makeCompViewModel;
    }

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_make_comp;
    }

    @Override
    public void loadComplaintTypes(List<ComplaintTypesModel> complaintTypesModels) {
        complaintsList = complaintTypesModels;
        setSpinner();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.bt_submit_complaint) {
            SubmitButtionClicked();
        }

        if (view.getId() == R.id.close_ic_comp) {
            CloseIcClickedCall();
        }
    }

    public void CloseIcClickedCall() {

        fragmentMakeCompBinding.complaintsTypeSpinner.setSelection(0);
        fragmentMakeCompBinding.complaintEt.setText("");

        if (Page_Name.equalsIgnoreCase(Constants.history_comptype)) {

            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .disallowAddToBackStack()
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .add(R.id.fragment_frame, HistoryFrag.newInstance(), HistoryFrag.TAG)
                    .commit();
        } else {
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .disallowAddToBackStack()
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .add(R.id.fragment_frame, ProfileFrag.newInstance(), ProfileFrag.TAG)
                    .commit();
        }
    }

    @Override
    public void CloseIcClicked() {

    }


    private void SubmitButtionClicked() {

        Complaint = fragmentMakeCompBinding.complaintEt.getText().toString();

        if (Complaint.equalsIgnoreCase("") || Complaint.length() <= 9) {
            showMessage("Enter Your description in atleast 10 characters");
        } else {

            makeCompViewModel.show_status.set(true);
            makeCompViewModel.showprocessImg.set(true);

            HashMap<String, String> cmap = new HashMap<>();

            if (!Complaint_ID.equals("")) {
                cmap.put(Constants.NetworkParameters.complaint_id, Complaint_ID);
            }

            cmap.put(Constants.NetworkParameters.descrption, Complaint);

            if (Page_Name.equals("History") && !History_ID.equals("")) {
                cmap.put(Constants.NetworkParameters.request_id, History_ID);
            }

            getViewModel().submitComplaints(cmap);

        }

    }

    @Override
    public void showAlertMsg(String msg, String title) {
        AlertDialog msgAlert = new AlertDialog.Builder(getContext()).create();

        msgAlert.setTitle(title);
        msgAlert.setMessage(msg);
        msgAlert.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                (dialogInterface, i) -> {
                    msgAlert.dismiss();
                });
        msgAlert.show();

        removefrag3sec();
    }

    private void removefrag3sec() {
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            //code to remove fragment

            if (Page_Name.equalsIgnoreCase(Constants.history_comptype)) {
                requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .disallowAddToBackStack()
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .replace(R.id.fragment_frame, HistoryFrag.newInstance(), HistoryFrag.TAG)
                        .commit();

                fragmentMakeCompBinding.complaintsTypeSpinner.setSelection(0);
                fragmentMakeCompBinding.complaintEt.setText("");
            } else {
                requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .disallowAddToBackStack()
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .replace(R.id.fragment_frame, ProfileFrag.newInstance(), ProfileFrag.TAG)
                        .commit();
            }

        }, 1000);
    }

    private void displayComplaintData(ComplaintTypesModel complaints) {

        Complaint_ID = complaints.getId();

    }


    @Override
    public void clickBack() {
        Intent intentBroadcast = new Intent();
        intentBroadcast.setAction(Constants.BroadcastsActions.BackPressedReceiever);
        LocalBroadcastManager.getInstance(getBaseActivity()).sendBroadcast(intentBroadcast);

        boolean isNavigateHistoryFragment;

        isNavigateHistoryFragment = Page_Name.equalsIgnoreCase(Constants.history_comptype);

        FragmentManager fragmentManager = getBaseActivity().getSupportFragmentManager();
        if (isNavigateHistoryFragment) {
            Log.d("xxxMakeCompFrag", "clickBack:replaceFragment =WalletFrag");

            replaceFragment(fragmentManager, HistoryFrag.newInstance(), HistoryFrag.TAG);
            ((HomeAct) requireActivity()).ShowHideBar(true);
        } else {
            Log.d("xxxMakeCompFrag", "clickBack:addFragment =ManageCardFrag");
            replaceFragment(fragmentManager, ProfileFrag.newInstance(), ProfileFrag.TAG);
        }
    }
}