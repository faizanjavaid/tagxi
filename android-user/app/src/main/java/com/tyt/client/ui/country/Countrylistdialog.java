package com.tyt.client.ui.country;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.tyt.client.R;
import com.tyt.client.databinding.CountrylistBinding;
import com.tyt.client.ui.base.BaseDialog;
import com.tyt.client.utilz.CommonUtils;
import com.tyt.client.utilz.Constants;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;


/**
 * A dialog which is used to show the list of countries with all details like countrycode.
 */

public class Countrylistdialog extends BaseDialog implements Countrylistnavigator {

    private static final String TAG = "Countrylistdialog";
    @Inject
    Countrylistdialogviewmodel countrylistdialogviewmodel;
    CountrylistBinding countrylistBinding;
    CountryListAdapter countryListAdapter;
    LinearLayoutManager linearLayoutManager;

    public static Countrylistdialog newInstance() {
        return new Countrylistdialog();
    }


    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        countrylistBinding = DataBindingUtil.inflate(
                inflater, R.layout.countrylist, container, false);
        View view = countrylistBinding.getRoot();
        AndroidSupportInjection.inject(this);
        countrylistdialogviewmodel.setNavigator(this);
        countrylistBinding.setViewModel(countrylistdialogviewmodel);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        countrylistdialogviewmodel.getCountryListApi();

        countrylistBinding.searchEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (countryListAdapter != null)
                    countryListAdapter.getFilter().filter(s.toString());
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("Resume Called--", "kjr");
    }

    public void show(FragmentManager fragmentManager) {
        super.show(fragmentManager, TAG);
    }

    @Override
    public void countryResponse(Object data) {
        String countryListModel = CommonUtils.arrayToString((ArrayList<Object>) data);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        countryListAdapter = new CountryListAdapter(getActivity(), countryListModel, this);
        countrylistBinding.recylerview.setLayoutManager(linearLayoutManager);
        countrylistBinding.recylerview.setAdapter(countryListAdapter);
    }

    @Override
    public void dismissDialg() {
        dismissDialog(TAG);
    }

    @Override
    public void clickedItem(String flag, String code, String name, String CountryId) {
        Intent intent = new Intent(Constants.INTENT_COUNTRY_CHOOSE);
        intent.putExtra(Constants.FLAG, flag);
        intent.putExtra(Constants.countryCode, code);
        LocalBroadcastManager.getInstance(requireActivity()).sendBroadcast(intent);
        dismiss();
    }
}
