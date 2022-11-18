package com.tyt.driver.ui.carDetails.carTypes;

import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.tyt.driver.BR;
import com.tyt.driver.R;
import com.tyt.driver.databinding.CarTypeLayBinding;
import com.tyt.driver.databinding.ServiceLocLayBinding;
import com.tyt.driver.retro.responsemodel.CarTypeModel;
import com.tyt.driver.ui.base.BaseActivity;
import com.tyt.driver.ui.base.BaseFragment;
import com.tyt.driver.ui.carDetails.carColor.CarColorFrag;
import com.tyt.driver.utilz.Constants;

import java.util.List;

import javax.inject.Inject;

public class CarTypeFrag extends BaseFragment<CarTypeLayBinding, CarTypeViewModel> implements CarTypeNavigator {

    public static final String TAG = "CarTypeFrag";
    @Inject
    public CarTypeViewModel viewModel;
    public CarTypeLayBinding binding;
    CarTypeAdapter serviceLocationAdapter;
    LinearLayoutManager linearLayoutManager;
    public static String AreaId;

    public static Fragment newInstance(String ID) {
        AreaId = ID;
        return new CarTypeFrag();
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
        viewModel.TypesApi(AreaId);
    }


    @Override
    public CarTypeViewModel getViewModel() {
        return viewModel;
    }

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.car_type_lay;
    }


    /**
     * returns reference of {@link BaseActivity}
     **/
    @Override
    public BaseActivity getBaseAct() {
        return ((BaseActivity) getActivity());
    }


    @Override
    public void onClickContiue() {

        if (!viewModel.sharedPrefence.Getvalue(Constants.SelectedTypeID).isEmpty())
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .disallowAddToBackStack()
                    .setCustomAnimations(R.anim.slide_left, R.anim.slide_right)
                    .add(R.id.car_details_frame, CarColorFrag.newInstance(), CarColorFrag.TAG)
                    .commitAllowingStateLoss();
    }

    @Override
    public void loadTypes(List<CarTypeModel> carTypeModels) {
        serviceLocationAdapter = new CarTypeAdapter(getActivity(), carTypeModels, this);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        binding.recylerview.setLayoutManager(linearLayoutManager);
        binding.recylerview.setAdapter(serviceLocationAdapter);
        binding.searchEdit.addTextChangedListener(serviceLocationAdapter);
    }

    @Override
    public void onClickSelectedType(String name, String id) {
        viewModel.sharedPrefence.savevalue(Constants.SelectedTypeName, name);
        viewModel.sharedPrefence.savevalue(Constants.SelectedTypeID, id);
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
