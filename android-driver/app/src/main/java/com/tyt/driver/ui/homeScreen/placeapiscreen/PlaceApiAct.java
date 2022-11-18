package com.tyt.driver.ui.homeScreen.placeapiscreen;

import android.os.Bundle;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.View;

import androidx.databinding.library.baseAdapters.BR;
import com.tyt.driver.R;
import com.tyt.driver.retro.responsemodel.Favplace;
import com.tyt.driver.databinding.ActivityPlaceApiBinding;
import com.tyt.driver.ui.base.BaseActivity;
import com.tyt.driver.ui.homeScreen.placeapiscreen.adapter.PlaceandFavAdapter;
import com.tyt.driver.utilz.SharedPrefence;

import java.util.List;

import javax.inject.Inject;

public class PlaceApiAct extends BaseActivity<ActivityPlaceApiBinding, PlaceApiViewModel> implements PlaceApiNavigator {

    @Inject
    PlaceApiViewModel placeApiViewModel;

    @Inject
    SharedPrefence sharedPrefence;

    @Inject
    PlaceandFavAdapter adapter;

    ActivityPlaceApiBinding activityPlaceApiBinding;


    @Inject
    LinearLayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityPlaceApiBinding = getViewDataBinding();
        placeApiViewModel.setNavigator(this);
        Setup();

    }

    /**
     * Set adapter to auto-complete search result adapter
     * Call API to get saved favourite places from server
     * **/
    private void Setup() {
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        activityPlaceApiBinding.ACPlaceRecyclerView.setLayoutManager(mLayoutManager);
        activityPlaceApiBinding.ACPlaceRecyclerView.setItemAnimator(new DefaultItemAnimator());
        activityPlaceApiBinding.ACPlaceRecyclerView.setAdapter(adapter);

        placeApiViewModel.GetFavListData();
    }

    @Override
    public PlaceApiViewModel getViewModel() {
        return placeApiViewModel;
    }

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_place_api;
    }

    @Override
    public SharedPrefence getSharedPrefence() {
        return sharedPrefence;
    }

    /** Populate the list which contains {@link Favplace}
     * @param favplace {@link Favplace} model **/
    @Override
    public void addList(List<Favplace> favplace) {
       // adapter.addList(favplace);
    }

    /** Show/hide circular progress bar to indicate loading
     * @param show true/false **/
    @Override
    public void showProgress(boolean show) {
        if (show)
            placeApiViewModel.setIsLoading(true);
        else
            placeApiViewModel.setIsLoading(false);
    }

    /** Show/hide clear button on Autocomplete search box
     * @param show true/false **/
    @Override
    public void showclearButton(boolean show) {
        if (show) {
            if (activityPlaceApiBinding.ACPlaceClear.getVisibility() == View.GONE)
                activityPlaceApiBinding.ACPlaceClear.setVisibility(View.VISIBLE);
        } else {
            if (activityPlaceApiBinding.ACPlaceClear.getVisibility() == View.VISIBLE)
                activityPlaceApiBinding.ACPlaceClear.setVisibility(View.GONE);
        }
    }

    /** Call to terminate current activity **/
    @Override
    public void FinishAct() {
        finish();
    }

    /** Clears all text on search box when clear button is clicked **/
    @Override
    public void clearText() {
        activityPlaceApiBinding.ACPlaceEdit.setText("");
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

}
