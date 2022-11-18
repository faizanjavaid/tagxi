/*
 *  Copyright (C) 2017 MINDORKS NEXTGEN PRIVATE LIMITED
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      https://mindorks.com/license/apache-v2
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License
 */

package com.tyt.driver.ui.base;






import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.snackbar.Snackbar;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.tyt.driver.R;
import com.tyt.driver.utilz.exception.CustomException;

/**
 * Created by amitshekhar on 10/07/17.
 */

public abstract class BaseDialog extends DialogFragment {

    private BaseActivity mActivity;
    Dialog dialog, mDialog;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof BaseActivity) {
            BaseActivity mActivity = (BaseActivity) context;
            this.mActivity = mActivity;
            mActivity.onFragmentAttached();
        }
    }

    @Override
    public void onDetach() {
        mActivity = null;
        super.onDetach();
    }

    public BaseActivity getBaseActivity() {
        return mActivity;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // the content
        final RelativeLayout root = new RelativeLayout(getActivity());
        root.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));

        // creating the fullscreen dialog

        dialog = new Dialog(getContext());
        /*if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            dialog = new Dialog(getContext());
        }else {
            dialog = new Dialog(getActivity());
        }*/
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(root);
        if (dialog.getWindow() != null) {
            /*dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));*/
            dialog.getWindow().setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        dialog.setCanceledOnTouchOutside(false);

        return dialog;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void showLoading() {
        if (mActivity != null) {
            mActivity.showLoading();
        }
    }

    public void hideLoading() {
        if (mActivity != null) {
            mActivity.hideLoading();
        }
    }



    public void hideKeyboard() {
        if (mActivity != null) {
            mActivity.hideKeyboard();
        }
    }

    /** shows dialog fragment **/
    public void show(FragmentManager fragmentManager, String tag) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        Fragment prevFragment = fragmentManager.findFragmentByTag(tag);
        if (prevFragment != null) {
            Log.d("xxxBaseDialog", "show: prevFragment ==>"+prevFragment);
            transaction.remove(prevFragment);
        }
        transaction.addToBackStack(null);
        try {
            show(transaction, tag);
        }catch (IllegalStateException e){
            e.printStackTrace();
            Log.e("xxxBaseDialog", "show: Error==>"+e.getLocalizedMessage() );
        }
    }

    /** dismisses dialog fragment **/
    public void dismissDialog(String tag) {
        super.dismissAllowingStateLoss();
        /*dismiss();*/
        if(getBaseActivity()!=null)
            getBaseActivity().onFragmentDetached(tag);
    }

    /** checks and returns true if internet connected,false if not connected **/
    public boolean isNetworkConnected() {
        return mActivity != null && mActivity.isNetworkConnected();
    }

    /** shows toast message **/
    public void showMessage(int resId) {
        if(mActivity!=null)
        Toast.makeText(mActivity, mActivity.getTranslatedString(resId), Toast.LENGTH_SHORT).show();
    }

    /** shows toast message **/
    public void showMessage(CustomException e) {
        if(mActivity!=null)
        Toast.makeText(mActivity, e.getMessage(), Toast.LENGTH_SHORT).show();
    }

    /** shows toast message **/
    public void showMessage(String message) {
        Toast.makeText(mActivity, message, Toast.LENGTH_SHORT).show();
    }

    /** shows snackbar **/
    public void showSnackBar(@NonNull View view, String message) {
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
        snackbar.setActionTextColor(Color.RED);
        snackbar.show();
    }

    /** shows error message dialog **/
    public void showAlert(String errorMessage) {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
            mDialog = null;
        }
        mDialog = new Dialog(mActivity);
        mDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mDialog.findViewById(R.id.okButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.cancel();
            }
        });
        mDialog.show();
    }

    /** shows network connection error **/
    public void showNetworkMessage() {
        Toast.makeText(mActivity, mActivity.getTranslatedString(R.string.txt_NoInternet), Toast.LENGTH_SHORT).show();
    }

    public void showSnackBar(String message) {
       /* if (getViewModel() != null) {
            Snackbar snackbar = Snackbar.make((View)getViewModel(), message, Snackbar.LENGTH_LONG);
            snackbar.setActionTextColor(Color.RED);
            snackbar.show();
        }*/
    }

    /** set the dialog fragment to fullscreen **/
    public void setDialogFullSCreen() {
        if (getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            getDialog().getWindow().setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
        }
        getDialog().setCanceledOnTouchOutside(false);
    }

    /** opens an activity to refresh company key **/
    public void refreshCompanyKey() {
        if (getActivity() != null) {
            getActivity().finish();
        }
    }

}
