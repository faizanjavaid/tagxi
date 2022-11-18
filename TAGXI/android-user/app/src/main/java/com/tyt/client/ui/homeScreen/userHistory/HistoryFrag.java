package com.tyt.client.ui.homeScreen.userHistory;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.tyt.client.BR;
import com.tyt.client.R;
import com.tyt.client.databinding.HistoryLayBinding;
import com.tyt.client.retro.responsemodel.HistoryCard;
import com.tyt.client.retro.responsemodel.HistoryModel;
import com.tyt.client.ui.base.BaseActivity;
import com.tyt.client.ui.base.BaseFragment;
import com.tyt.client.ui.homeScreen.HomeAct;
import com.tyt.client.ui.homeScreen.historyDetail.HistoryDetailFrag;
import com.tyt.client.ui.homeScreen.profile.ProfileFrag;

import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

/**
 * A Fragment to store all kinds of history details using recyclerview with data from HistoryAdapter.
* */

public class HistoryFrag extends BaseFragment<HistoryLayBinding, HistoryViewModel> implements HistoryNavigator {

    public static final String TAG = "HistoryFrag";
    @Inject
    public HistoryViewModel mViewModel;
    public HistoryLayBinding mBinding;
    HistoryAdapter historyAdapter;

    ShimmerFrameLayout shimmerFrameLayout;

    Integer page =1;
    Integer total_pages = 1;

    // TODO: Rename and change types and number of parameters
    public static HistoryFrag newInstance() {
        return new HistoryFrag();
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

        shimmerFrameLayout=view.findViewById(R.id.shim1);
        mViewModel.historyApi(page);

        mBinding.NestedSV.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {

            if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {

                int page_no = page+=1;
                mViewModel.page.set(page_no);

                if (page_no <= total_pages) {
                    mViewModel.historyApi(page_no);
                    //showMessage("Page : " + page_no);
                } else {
                    showMessage("That's All ...");
                }
            }

        });

        startshim();
    }


    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }


    @Override
    public HistoryViewModel getViewModel() {
        return mViewModel;
    }

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.history_lay;
    }

    /**
     * returns reference of {@link BaseActivity}
     **/
    @Override
    public BaseActivity getBaseAct() {
        return ((BaseActivity) getActivity());
    }

    @Override
    public void loadHistory(List<HistoryCard> historyModels) {
        if (historyModels.size() > 0) {
            stopshim();
            mViewModel.noItem.set(false);
            historyAdapter = new HistoryAdapter(getActivity(), historyModels, this);
            mBinding.historyRecycle.setLayoutManager(new LinearLayoutManager(getActivity()));
            mBinding.historyRecycle.setAdapter(historyAdapter);
        } else {
            mViewModel.noItem.set(true);
            stopshim();
        }
    }

    @Override
    public void onClickItem(String id) {
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .disallowAddToBackStack()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .add(R.id.fragment_frame, HistoryDetailFrag.newInstance(id), HistoryDetailFrag.TAG)
                .commit();
    }

    @Override
    public void openFromStart() {
        startActivity(new Intent(getActivity(), HomeAct.class));
    }

    @Override
    public void startshim() {
        shimmerFrameLayout.startShimmer();
        getViewDataBinding().historyRecycle.setVisibility(View.GONE);
        getViewModel().noItem.set(false);
    }

    @Override
    public void stopshim() {
        shimmerFrameLayout.stopShimmer();
        getViewDataBinding().historyRecycle.setVisibility(View.VISIBLE);
    }

    @Override
    public void setPageValue(Integer num, Integer tot) {
        page=num;
        total_pages=tot;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        shimmerFrameLayout.stopShimmer();
    }

    @Override
    public void onCLickBack() {
        Fragment fragment = getBaseAct().getSupportFragmentManager().findFragmentByTag(TAG);
        if (fragment != null)
            if (requireActivity().getSupportFragmentManager().findFragmentByTag(ProfileFrag.TAG) == null)
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .disallowAddToBackStack()
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .add(R.id.fragment_frame, ProfileFrag.newInstance(), ProfileFrag.TAG)
                        .commit();
    }
}
