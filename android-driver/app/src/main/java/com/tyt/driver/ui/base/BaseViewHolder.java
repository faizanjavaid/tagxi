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

import android.graphics.Color;
import androidx.annotation.NonNull;
import com.google.android.material.snackbar.Snackbar;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.tyt.driver.R;
import com.tyt.driver.utilz.exception.CustomException;
import com.tyt.driver.utilz.NetworkUtils;

/**
 * Created by amitshekhar on 11/07/17.
 */

public abstract class BaseViewHolder extends RecyclerView.ViewHolder implements BaseView {

    public BaseViewHolder(View itemView) {
        super(itemView);
    }

    public abstract void onBind(int position);

    /** shows toast msg **/
    public void showMessage(int resId) {
        Toast.makeText(itemView.getContext(), itemView.getContext().getString(resId), Toast.LENGTH_SHORT).show();
    }

    /** shows toast msg **/
    public void showMessage(CustomException e) {
        Toast.makeText(itemView.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
    }

    /** checks if internet is connected or not **/
    public boolean isNetworkConnected() {
        return NetworkUtils.isNetworkConnected(itemView.getContext().getApplicationContext());
    }

    /** shows toast msg **/
    public void showMessage(String message) {
        Toast.makeText(itemView.getContext(), message, Toast.LENGTH_SHORT).show();
    }

    /** shows snackbar msg **/
    @Override
    public void showSnackBar(String message) {
        if (itemView != null) {
            Snackbar snackbar = Snackbar.make(itemView, message, Snackbar.LENGTH_LONG);
            snackbar.setActionTextColor(Color.RED);
            snackbar.show();
        }
    }

    /** shows snackbar msg **/
    @Override
    public void showSnackBar(@NonNull View view, String message) {
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
        snackbar.setActionTextColor(Color.RED);
        snackbar.show();
    }

    /** shows network connection error msg **/
    @Override
    public void showNetworkMessage() {
        Toast.makeText(itemView.getContext(), itemView.getContext().getString(R.string.txt_NoInternet), Toast.LENGTH_SHORT).show();
    }

    /** opens an activity to refresh company key **/
    @Override
    public void refreshCompanyKey() {

        if (itemView.getContext() != null && itemView.getContext() instanceof BaseActivity) {
            ((BaseActivity) itemView.getContext()).finish();
        }
    }
}
