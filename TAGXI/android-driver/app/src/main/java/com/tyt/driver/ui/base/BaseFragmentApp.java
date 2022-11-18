package com.tyt.driver.ui.base;

import android.app.Fragment;
import android.content.Context;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.snackbar.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.tyt.driver.R;
import com.tyt.driver.retro.responsemodel.Type;
import com.tyt.driver.utilz.exception.CustomException;

import dagger.android.AndroidInjection;


/**
 * Created by root on 12/19/17.
 */

public abstract class BaseFragmentApp <T extends ViewDataBinding, V> extends Fragment implements BaseView {

    private BaseActivity mActivity;
    private Context mcontext;
    private T mViewDataBinding;
    private V mViewModel;
    private View mRootView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
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
    public void onAttach(Context context) {
        super.onAttach(context);
        mcontext=context;
        if (context instanceof BaseActivity) {
            BaseActivity activity = (BaseActivity) context;
            this.mActivity = activity;
            activity.onFragmentAttached();
        }
    }

    @Override
    public void onDetach() {
        mActivity = null;
        mcontext=null;
        super.onDetach();
    }

    /** gets the current context **/
    @Override
    public Context getContext() {
        return mcontext;
    }

    /** gets reference of BaseActivity **/
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

    /** checks if internet connection is available or not **/
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
        Toast.makeText(mActivity, mActivity.getTranslatedString(resId), Toast.LENGTH_SHORT).show();
    }

    /** shows toast msg **/
    public void showMessage(CustomException e) {
        Toast.makeText(mActivity, e.getMessage(), Toast.LENGTH_SHORT).show();
    }

    /** shows toast msg **/
    public void showMessage(String message) {
        Toast.makeText(mActivity, message, Toast.LENGTH_SHORT).show();
    }

    /** shows snackbar msg **/
    @Override
    public void showSnackBar(String message) {
        if (getViewModel() != null) {
            Snackbar snackbar = Snackbar.make((View)getViewModel(), message, Snackbar.LENGTH_LONG);
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
        Toast.makeText(mActivity, mActivity.getTranslatedString(R.string.txt_NoInternet), Toast.LENGTH_SHORT).show();
    }

    private void performDependencyInjection() {
        AndroidInjection.inject(this);
    }


    public interface Callback extends BaseFragment.ChangeFrament {

        void onFragmentAttached();

        void onFragmentDetached(String tag);
    }

    public interface ChangeFrament{
        void NeedRideFragment(Type type);
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

}
