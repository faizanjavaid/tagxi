package com.tyt.driver.ui.carDetails.carmodel;

import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.tyt.driver.BR;
import com.tyt.driver.R;
import com.tyt.driver.databinding.CarMakeLayBinding;
import com.tyt.driver.databinding.CarModelLayBinding;
import com.tyt.driver.retro.responsemodel.CarModelData;
import com.tyt.driver.ui.base.BaseActivity;
import com.tyt.driver.ui.base.BaseFragment;
import com.tyt.driver.ui.carDetails.servicelocation.ServiceLocationFrag;
import com.tyt.driver.ui.homeScreen.HomeAct;
import com.tyt.driver.utilz.Constants;

import java.util.List;

import javax.inject.Inject;

public class CarModelFrag extends BaseFragment<CarModelLayBinding, CarModelViewModel> implements CarModelNavigator {

    public static final String TAG = "CarModelFrag";
    @Inject
    public CarModelViewModel viewModel;
    public CarModelLayBinding binding;
    CarModelAdapter carMakeAdapter;
    LinearLayoutManager linearLayoutManager;
    public static String makeId;

    public static Fragment newInstance(String getvalue) {
        makeId = getvalue;
        return new CarModelFrag();
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
        viewModel.carMakeId.set(makeId);

        viewModel.carModelApi();
    }


    @Override
    public CarModelViewModel getViewModel() {
        return viewModel;
    }

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.car_model_lay;
    }


    /**
     * returns reference of {@link BaseActivity}
     **/
    @Override
    public BaseActivity getBaseAct() {
        return ((BaseActivity) getActivity());
    }

    @Override
    public void loadCarMake(List<CarModelData> carModelData) {
        carMakeAdapter = new CarModelAdapter(getActivity(), carModelData, this);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        binding.recylerview.setLayoutManager(linearLayoutManager);
        binding.recylerview.setAdapter(carMakeAdapter);
        binding.searchEdit.addTextChangedListener(carMakeAdapter);
    }


    @Override
    public void onClickContiue() {
        if (!viewModel.sharedPrefence.Getvalue(Constants.selectedModelName).isEmpty())
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .disallowAddToBackStack()
                    .setCustomAnimations(R.anim.slide_left, R.anim.slide_right)
                    .add(R.id.car_details_frame, ServiceLocationFrag.newInstance(), ServiceLocationFrag.TAG)
                    .commitAllowingStateLoss();
    }

    @Override
    public void onClickSelectedModel(String name, int id) {
        viewModel.sharedPrefence.savevalue(Constants.selectedModelName, name);
        viewModel.sharedPrefence.savevalue(Constants.selectedModelID, "" + id);
    }

    @Override
    public void onCLickBack() {
        if (getActivity().getSupportFragmentManager().findFragmentByTag(CarModelFrag.TAG) != null) {
            Fragment fragment = getActivity().getSupportFragmentManager().findFragmentByTag(CarModelFrag.TAG);
            getActivity().getSupportFragmentManager().beginTransaction().remove(fragment).commit();
        }
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
