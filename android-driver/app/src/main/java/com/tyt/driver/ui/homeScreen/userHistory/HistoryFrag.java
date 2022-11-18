package com.tyt.driver.ui.homeScreen.userHistory;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.tyt.driver.BR;
import com.tyt.driver.R;
import com.tyt.driver.databinding.HistoryLayBinding;
import com.tyt.driver.databinding.ProfileFragLayBinding;
import com.tyt.driver.retro.responsemodel.HistoryCard;
import com.tyt.driver.retro.responsemodel.HistoryModel;
import com.tyt.driver.ui.base.BaseActivity;
import com.tyt.driver.ui.base.BaseFragment;
import com.tyt.driver.ui.historyDetail.HistoryDetailFrag;
import com.tyt.driver.ui.homeScreen.HomeAct;

import java.util.List;

import javax.inject.Inject;

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
                //int page =  mViewModel.page.get();
                //int limit = mViewModel.limit.get();

                int page_no = page+=1;
                mViewModel.page.set(page_no);

                if (page_no <= total_pages) {
                    mViewModel.historyApi(page_no);
                    showMessage("Page : " + page_no);
                } else {
                    showMessage("That's All ...");
                }

              /*  if (page >= limit) {
                    showMessage("That's All Data");
                }*/

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
            Glide.with(getContext()).asGif().load(R.drawable.history_gif).into(mBinding.historyGifDrawable);
            stopshim();
        }

    }

    @Override
    public void setPageValue(Integer num, Integer tot) {
        page=num;
        total_pages=tot;
    }

    @Override
    public void onClickItem(String id) {
        getActivity().getSupportFragmentManager()
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
    public void onResume() {
        super.onResume();

        ((HomeAct) getActivity()).ShowOnloneOffline(false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        shimmerFrameLayout.stopShimmer();
    }
}
