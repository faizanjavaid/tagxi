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

import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import androidx.fragment.app.Fragment;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.tyt.driver.R;
import com.tyt.driver.retro.responsemodel.Driver;
import com.tyt.driver.retro.responsemodel.Request;
import com.tyt.driver.retro.responsemodel.Type;
import com.tyt.driver.utilz.exception.CustomException;

import dagger.android.support.AndroidSupportInjection;

/**
 * Created by amitshekhar on 09/07/17.
 */

public abstract class BaseFragment<T extends ViewDataBinding, V> extends Fragment implements BaseView {


    private BaseActivity mActivity;
    private Context mcontext;
    private T mViewDataBinding;
    private V mViewModel;
    private View mRootView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
       /* StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectAll()
                .penaltyLog()
                .penaltyDialog()
                .build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll()
                .penaltyLog()
                .build());*/


        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        performDependencyInjection();
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mViewDataBinding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false);
        mRootView = mViewDataBinding.getRoot();
        return mRootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mViewModel = getViewModel();
        mViewDataBinding.setVariable(getBindingVariable(), mViewModel);
        mViewDataBinding.executePendingBindings();


    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mcontext = context;
        if (context instanceof BaseActivity) {
            BaseActivity activity = (BaseActivity) context;
            this.mActivity = activity;
            activity.onFragmentAttached();
        }
    }

    @Override
    public void onDetach() {
        mActivity = null;
        mcontext = null;
        super.onDetach();
    }


    /** gets the current context **/
    @Override
    public Context getContext() {
        return mcontext;
    }

    /** gets the BaseActivity **/
    public BaseActivity getBaseActivity() {
        return mActivity;
    }

    public T getViewDataBinding() {
        return mViewDataBinding;
    }


    @Override
    public void onDestroyView() {

        super.onDestroyView();
    }

    /** checks if internet is connected **/
    public boolean isNetworkConnected() {
        return mActivity != null && mActivity.isNetworkConnected();
    }

    /** hides soft keyboard **/
    public void hideKeyboard() {
        if (mActivity != null) {
            mActivity.hideKeyboard();

        }
    }


    public void openActivityOnTokenExpire() {
        if (mActivity != null) {
            //   mActivity.openActivityOnTokenExpire();
        }
    }

    /** shows toast msg **/
    public void showMessage(int resId) {
        Toast.makeText(mActivity.getApplicationContext(), mActivity.getTranslatedString(resId), Toast.LENGTH_SHORT).show();
    }

    /** shows toast msg **/
    public void showMessage(CustomException e) {
        if (mActivity != null)
            Toast.makeText(mActivity.getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
    }

    /** shows toast msg **/
    public void showMessage(String message) {
        if (mActivity != null) {
            Toast.makeText(mcontext, message, Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(getBaseActivity().getApplicationContext(),message,Toast.LENGTH_SHORT).show();
        }
    }

    public Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message message) {
            // This is where you do your work in the UI thread.
            // Your worker tells you in the message what to do.
            if(getActivity()!=null&&message!=null &&message.obj!=null)
                Toast.makeText(getActivity(), message.obj.toString(), Toast.LENGTH_SHORT).show();
        }
    };

    /** shows snackbar **/
    @Override
    public void showSnackBar(String message) {
        if (getViewModel() != null) {
            Snackbar snackbar = Snackbar.make((View) getViewModel(), message, Snackbar.LENGTH_LONG);
            snackbar.setActionTextColor(Color.RED);
            snackbar.show();
        }
    }

    /** shows snackbar **/
    @Override
    public void showSnackBar(@NonNull View view, String message) {
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
        snackbar.setActionTextColor(Color.RED);
        snackbar.show();
    }

    /** shows network connection error msg **/
    @Override
    public void showNetworkMessage() {
        Toast.makeText(mActivity, mActivity.getTranslatedString(R.string.txt_NoInternet), Toast.LENGTH_SHORT).show();
    }

    /** opens an activity to refresh company key **/
    @Override
    public void refreshCompanyKey() {
        if(mActivity!=null)
        mActivity.refreshCompanyKey();
    }

    private void performDependencyInjection() {
        AndroidSupportInjection.inject(this);
    }

    public interface Callback extends ChangeFrament {

        void onFragmentAttached();

        void onFragmentDetached(String tag);
    }

    public interface ChangeFrament {
        void NeedRideFragment(Type type);

        void NeedTripFragment(Request request, Driver driver);

        void NeedHomeFragment();

        void setResultToDropAddress(String Address);

        void NeedFeedbackFragment(Request request, boolean isCorporate);

        void changeTripnFeedback();
    }

    /**
     * Override for set view model
     *
     * @return view model instance
     */
    public abstract V getViewModel();

    /**
     * Override for set binding variable
     *
     * @return variable id
     */
    public abstract int getBindingVariable();

    /**
     * @return layout resource id
     */
    public abstract
    @LayoutRes
    int getLayoutId();


    //Check the google map is installed or not
    public boolean isGoogleMapsInstalled(Activity activity) {
        if (mActivity == null)
            mActivity = (BaseActivity) activity;
        try {
            ApplicationInfo info = mActivity.getPackageManager().getApplicationInfo("com.google.android.apps.maps", 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    public void replaceFragment(FragmentManager fragmentManager, Fragment fragment, String TAG){
        fragmentManager.beginTransaction()
                .disallowAddToBackStack()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .replace(R.id.fragment_frame, fragment, TAG)
                .commitAllowingStateLoss();
    }
    public void addFragment(FragmentManager fragmentManager,Fragment fragment, String TAG){
        fragmentManager.beginTransaction()
                .disallowAddToBackStack()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .add(R.id.fragment_frame, fragment, TAG)
                .commit();
    }

}
