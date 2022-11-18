package com.tyt.driver.ui.bill;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.tyt.driver.R;
import com.tyt.driver.databinding.BillDialogBinding;
import com.tyt.driver.retro.responsemodel.EndTripData;
import com.tyt.driver.retro.responsemodel.ProfileModel;
import com.tyt.driver.ui.base.BaseActivity;
import com.tyt.driver.ui.base.BaseDialog;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;

public class BillDialogFragment extends BaseDialog implements BillDialogNavigator {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "BillDialogFragment";

    LinearLayoutManager mLayoutManager;
    @Inject
    BillDialogViewModel billDialogViewModel;
    BillDialogBinding binding;

    public static ProfileModel profileModel;
    public static EndTripData endTripData;

    public BillDialogFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static BillDialogFragment newInstance(ProfileModel tripResponse) {
        Log.d("xxxBillDialogFra", "newInstance: ProfileModel ==>");
        BillDialogFragment fragment = new BillDialogFragment();
        profileModel = tripResponse;
       // endTripData=null;
        return fragment;
    }

    // TODO: Rename and change types and number of parameters
    public static BillDialogFragment newInstance(EndTripData endTripResp) {
        Log.d("xxxBillDialogFra", "newInstance: EndTripData==>>");
        BillDialogFragment fragment = new BillDialogFragment();
        endTripData = endTripResp;
       // profileModel=null;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
                inflater, R.layout.bill_dialog, container, false);
        View view = binding.getRoot();
        int width = ViewGroup.LayoutParams.MATCH_PARENT;
        int height = ViewGroup.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setLayout(width, height);
        AndroidSupportInjection.inject(this);

        billDialogViewModel.setNavigator(this);
        binding.setViewModel(billDialogViewModel);

        if (profileModel != null) {
            billDialogViewModel.setValues(profileModel, binding.userImg);
        }
        if (endTripData != null) {
            billDialogViewModel.setValues(endTripData, binding.userImg);
        }
        return view;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.setViewModel(billDialogViewModel);
        if (getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            getDialog().getWindow().setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
        }
        mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
    }

    public void show(FragmentManager fragmentManager) {

        super.show(fragmentManager, TAG);
    }


    @Override
    public void dismissDialog() {
        dismissDialog(TAG);
    }

    @Override
    public BaseActivity getAttachedContext() {
        return getBaseActivity();
    }


}
