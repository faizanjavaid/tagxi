package com.tyt.driver.ui.homeScreen.makeComplaint;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.core.content.ContextCompat;
import androidx.databinding.library.baseAdapters.BR;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.tyt.driver.R;
import com.tyt.driver.app.MyApp;
import com.tyt.driver.databinding.FragmentMakeCompBinding;
import com.tyt.driver.retro.responsemodel.ComplaintTypesModel;
import com.tyt.driver.ui.base.BaseFragment;
import com.tyt.driver.ui.homeScreen.HomeAct;
import com.tyt.driver.ui.homeScreen.profile.ProfileFrag;
import com.tyt.driver.ui.homeScreen.userHistory.HistoryFrag;
import com.tyt.driver.utilz.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

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

//    public static void setPage_Name(String page_Name) {
//        Page_Name = page_Name;
//    }
//
//    public static void setHistory_ID(String history_ID) {
//        History_ID = history_ID;
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
     /*   OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                clickBack();
            }
        };

        requireActivity().getOnBackPressedDispatcher().addCallback(callback);*/
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
        //colorStateList = ContextCompat.getColorStateList(MyApp.getmContext(), R.color.colorPrimaryDark);
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

        //setViewVisibilities();

    }

    ArrayAdapter<ComplaintTypesModel> spin_adapter;

    private void setSpinner() {
        spin_adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, complaintsList);

        spin_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        fragmentMakeCompBinding.complaintsTypeSpinner.setAdapter(spin_adapter);
        //spin_adapter.notifyDataSetChanged();


        fragmentMakeCompBinding.complaintsTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //showMessage("item selected");
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


    /*private void setViewVisibilities() {
        if (makeCompViewModel.show_status.get()) {
            fragmentMakeCompBinding.compStatusImg.setVisibility(View.VISIBLE);

            fragmentMakeCompBinding.complaintEt.setVisibility(View.GONE);
            fragmentMakeCompBinding.complaintsTypeSpinner.setVisibility(View.GONE);
            fragmentMakeCompBinding.btSubmitComplaint.setVisibility(View.GONE);

            fragmentMakeCompBinding.tvHintcomp.setVisibility(View.GONE);
            fragmentMakeCompBinding.tvHintspin.setVisibility(View.GONE);

            if (makeCompViewModel.showdonesImg.get()) {
                //set done image
                fragmentMakeCompBinding.compStatusImg.setImageResource(R.drawable.done_comp);
            }
            if (makeCompViewModel.showprocessImg.get()) {
                //set processing image
                fragmentMakeCompBinding.compStatusImg.setImageResource(R.drawable.done_comp);
            }

        } else {
            fragmentMakeCompBinding.compStatusImg.setVisibility(View.GONE);

            fragmentMakeCompBinding.complaintEt.setVisibility(View.VISIBLE);
            fragmentMakeCompBinding.complaintsTypeSpinner.setVisibility(View.VISIBLE);
            fragmentMakeCompBinding.btSubmitComplaint.setVisibility(View.VISIBLE);

            fragmentMakeCompBinding.tvHintcomp.setVisibility(View.VISIBLE);
            fragmentMakeCompBinding.tvHintspin.setVisibility(View.VISIBLE);
        }
    }*/


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

        //showMessage("Cl Mthd Cld");

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
        //remove or close the fragment

       /* if (Page_Name.equalsIgnoreCase(Constants.history_comptype)) {
            if (requireActivity().getSupportFragmentManager().findFragmentByTag(TAG) != null) {
                ((HomeAct) requireActivity()).ShowHideBar(true);
                Fragment fragment = requireActivity().getSupportFragmentManager().findFragmentByTag(TAG);
                if (fragment != null)
                    requireActivity().getSupportFragmentManager().beginTransaction().remove(fragment).commit();
            }
        } else {
            if (requireActivity().getSupportFragmentManager().findFragmentByTag(TAG) != null) {
                ((HomeAct) requireActivity()).ShowHideBar(true);
                Fragment fragment = requireActivity().getSupportFragmentManager().findFragmentByTag(TAG);
                if (fragment != null)
                    requireActivity().getSupportFragmentManager().beginTransaction().remove(fragment).commit();
            }
        }*/
       /* fragmentMakeCompBinding.complaintsTypeSpinner.setSelection(0);
        fragmentMakeCompBinding.complaintEt.setText("");*/

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
            //showMessage("Processing Your Submission");
        }

        //call the make complaints API

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
        String titles = complaints.getTitle();

        Complaint_ID = complaints.getId();

        //showMessage("title : "+titles+"\n"+"ID :"+Complaint_ID);
    }

    /*@Override
    public void onDestroyView() {
        super.onDestroyView();
        //clickBack(Page_Name.equalsIgnoreCase(Constants.history_comptype));
    }*/


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


    public void goToHistory(){
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .disallowAddToBackStack()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .replace(R.id.fragment_frame, HistoryFrag.newInstance(), HistoryFrag.TAG)
                .commit();
    }

    public void goToProfile(){
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .disallowAddToBackStack()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .replace(R.id.fragment_frame, ProfileFrag.newInstance(), ProfileFrag.TAG)
                .commit();
    }
}