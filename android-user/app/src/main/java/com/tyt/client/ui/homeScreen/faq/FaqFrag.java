package com.tyt.client.ui.homeScreen.faq;

import android.os.Bundle;
import android.view.View;

import androidx.databinding.library.baseAdapters.BR;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.tyt.client.R;
import com.tyt.client.databinding.FaqBinding;
import com.tyt.client.retro.responsemodel.Faqmodel;
import com.tyt.client.retro.responsemodel.FavouriteLocations;
import com.tyt.client.ui.base.BaseActivity;
import com.tyt.client.ui.base.BaseFragment;
import com.tyt.client.ui.homeScreen.FavouitesLocAdapter;
import com.tyt.client.ui.homeScreen.HomeAct;
import com.tyt.client.ui.homeScreen.mapFrag.MapFragNavigator;
import com.tyt.client.ui.homeScreen.profile.ProfileFrag;
import com.tyt.client.ui.homeScreen.profile.profilefavadapter;

import java.util.List;

import javax.inject.Inject;

/**
 * Fragment which is used to show the FAQ questions with answers.
 *
* */

public class FaqFrag extends BaseFragment<FaqBinding, FaqViewModel> implements FaqNavigator {

    public static final String TAG = "FaqFrag";
    public static String p_type = "";
    @Inject
    public FaqViewModel mViewModel;
    FaqBinding mBinding;

    public static Fragment newInstance(String type) {
        p_type=type;
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

        if (p_type.equalsIgnoreCase("FAV")) {
            mViewModel.is_Fav.set(true);
            mViewModel.PageTitle.set("Favourites");
            mViewModel.getFavApi();
        }else {
            mViewModel.nofav.set(false);
            mViewModel.is_Fav.set(false);
            mViewModel.PageTitle.set("FAQ");
            mViewModel.faqApi();
        }


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
    public void loadFavourites(List<FavouriteLocations.FavLocData> favouriteLocations) {
        if (favouriteLocations.size()>0){
            getViewModel().nofav.set(false);
            profilefavadapter favouitesLocAdapter = new profilefavadapter(getActivity(),favouriteLocations,this);
            mBinding.favRecycle.setLayoutManager(new LinearLayoutManager(getActivity()));
            mBinding.favRecycle.setAdapter(favouitesLocAdapter);
        }else {
            getViewModel().nofav.set(true);
        }
    }

    @Override
    public void onClickBAck() {
        Fragment fragment = getActivity().getSupportFragmentManager().findFragmentByTag(FaqFrag.TAG);
        if (fragment != null)
            if (getActivity().getSupportFragmentManager().findFragmentByTag(ProfileFrag.TAG) == null)
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .disallowAddToBackStack()
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .replace(R.id.fragment_frame, ProfileFrag.newInstance(), ProfileFrag.TAG)
                        .commit();
    }

    @Override
    public BaseActivity getAttachedContext() {
        return (HomeAct)getActivity();
    }

    @Override
    public void deleteitem(String id) {
        mViewModel.deleteFavApi(id);
    }
}
