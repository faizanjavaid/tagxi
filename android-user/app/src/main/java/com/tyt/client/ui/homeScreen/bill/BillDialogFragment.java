package com.tyt.client.ui.homeScreen.bill;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.tyt.client.R;
import com.tyt.client.databinding.BillDialogBinding;
import com.tyt.client.retro.base.BaseResponse;
import com.tyt.client.retro.responsemodel.ProfileModel;
import com.tyt.client.ui.base.BaseActivity;
import com.tyt.client.ui.base.BaseDialog;

import java.util.Objects;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;

/**
 * Fragment which is used to show the bill after successful completion of the trip.
 */

public class BillDialogFragment extends BaseDialog implements BillDialogNavigator {
    private static final String TAG = "BillDialogFragment";

    LinearLayoutManager mLayoutManager;
    @Inject
    BillDialogViewModel billDialogViewModel;
    BillDialogBinding binding;
    public static ProfileModel profileModel;
    public static BaseResponse response;

    public BillDialogFragment() {

    }


    // TODO: Rename and change types and number of parameters
    public static BillDialogFragment newInstance(ProfileModel tripResponse) {
        BillDialogFragment fragment = new BillDialogFragment();
        profileModel = tripResponse;
        return fragment;
    }

    public static BillDialogFragment newInstance(BaseResponse baseResponse) {
        BillDialogFragment fragment = new BillDialogFragment();
        response = baseResponse;
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
        Objects.requireNonNull(getDialog()).getWindow().setLayout(width, height);
        AndroidSupportInjection.inject(this);

        billDialogViewModel.setNavigator(this);
        binding.setViewModel(billDialogViewModel);

        if (profileModel != null) {
            billDialogViewModel.setValues(profileModel, binding.userImg);
        } else if (response != null) {
            billDialogViewModel.setValues(response, binding.userImg);
        }

        return view;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.setViewModel(billDialogViewModel);
        if (Objects.requireNonNull(getDialog()).getWindow() != null) {
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
