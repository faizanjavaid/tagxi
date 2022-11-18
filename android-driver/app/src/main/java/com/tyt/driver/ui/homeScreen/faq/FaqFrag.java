package com.tyt.driver.ui.homeScreen.faq;

import android.os.Bundle;
import android.view.View;

import androidx.databinding.library.baseAdapters.BR;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.tyt.driver.R;
import com.tyt.driver.databinding.FaqBinding;
import com.tyt.driver.retro.responsemodel.Faqmodel;
import com.tyt.driver.ui.base.BaseActivity;
import com.tyt.driver.ui.base.BaseFragment;
import com.tyt.driver.ui.homeScreen.HomeAct;
import com.tyt.driver.ui.homeScreen.profile.ProfileFrag;

import java.util.List;

import javax.inject.Inject;

public class FaqFrag extends BaseFragment<FaqBinding, FaqViewModel> implements FaqNavigator {

    public static final String TAG = "FaqFrag";
    @Inject
    public FaqViewModel mViewModel;
    FaqBinding mBinding;

    public static Fragment newInstance() {
        return new FaqFrag();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBinding = getViewDataBinding();
        mViewModel.setNavigator(this);

        mViewModel.faqApi();
    }

    @Override
    public FaqViewModel getViewModel() {
        return mViewModel;
    }

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.faq;
    }


    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void loadFaq(List<Faqmodel> faqmodels) {
        FaqAdapter faq = new FaqAdapter(getActivity(), faqmodels, this);
        mBinding.recycle.setLayoutManager(new LinearLayoutManager(getActivity()));
        mBinding.recycle.setAdapter(faq);
    }

    @Override
    public void onClickBAck() {
        Fragment fragment = getActivity().getSupportFragmentManager().findFragmentByTag(FaqFrag.TAG);
        if (fragment != null)
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_frame, ProfileFrag.newInstance(), ProfileFrag.TAG)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .commit();
    }

    @Override
    public BaseActivity getAttachedContext() {
        return (HomeAct)getActivity();
    }
}
