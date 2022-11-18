package com.tyt.driver.ui.carDetails.carmake;

import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.tyt.driver.BR;
import com.tyt.driver.R;
import com.tyt.driver.databinding.CarMakeLayBinding;
import com.tyt.driver.databinding.MakeTripLayBinding;
import com.tyt.driver.retro.responsemodel.CarMakeModel;
import com.tyt.driver.ui.base.BaseActivity;
import com.tyt.driver.ui.base.BaseFragment;
import com.tyt.driver.ui.carDetails.carmodel.CarModelFrag;
import com.tyt.driver.utilz.Constants;

import java.util.List;

import javax.inject.Inject;

public class CarMakeFrag extends BaseFragment<CarMakeLayBinding, CarMakeViewModel> implements CarMakeNavigator {

    public static final String TAG = "CarMakeFrag";
    @Inject
    public CarMakeViewModel viewModel;
    public CarMakeLayBinding binding;
    CarMakeAdapter carMakeAdapter;
    LinearLayoutManager linearLayoutManager;

    public static Fragment newInstance() {
        return new CarMakeFrag();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = getViewDataBinding();
        viewModel.setNavigator(this);
        viewModel.carMakeApi();
    }


    @Override
    public CarMakeViewModel getViewModel() {
        return viewModel;
    }

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.car_make_lay;
    }

    /**
     * returns reference of {@link BaseActivity}
     **/
    @Override
    public BaseActivity getBaseAct() {
        return ((BaseActivity) getActivity());
    }

    @Override
    public void loadCarMake(List<CarMakeModel> carMakeModels) {
        carMakeAdapter = new CarMakeAdapter(getActivity(), carMakeModels, this);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        binding.recylerview.setLayoutManager(linearLayoutManager);
        binding.recylerview.setAdapter(carMakeAdapter);
        binding.searchEdit.addTextChangedListener(carMakeAdapter);
    }

    @Override
    public void onClickSelectedMake(String name, int id) {
        viewModel.sharedPrefence.savevalue(Constants.selectedMakeName, name);
        viewModel.sharedPrefence.savevalue(Constants.selectedMakeID, "" + id);
    }

    @Override
    public void onClickContiue() {
        if (!viewModel.sharedPrefence.Getvalue(Constants.selectedMakeID).isEmpty())
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .disallowAddToBackStack()
                    .setCustomAnimations(R.anim.slide_left, R.anim.slide_right)
                    .add(R.id.car_details_frame, CarModelFrag.newInstance(viewModel.sharedPrefence.Getvalue(Constants.selectedMakeID)), CarModelFrag.TAG)
                    .commitAllowingStateLoss();
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


}
