package com.tyt.driver.ui.carDetails.servicelocation;

import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.tyt.driver.BR;
import com.tyt.driver.R;
import com.tyt.driver.databinding.CarModelLayBinding;
import com.tyt.driver.databinding.ServiceLocLayBinding;
import com.tyt.driver.retro.responsemodel.ServiceLocationModel;
import com.tyt.driver.ui.base.BaseActivity;
import com.tyt.driver.ui.base.BaseFragment;
import com.tyt.driver.ui.carDetails.carTypes.CarTypeFrag;
import com.tyt.driver.utilz.Constants;

import java.util.List;

import javax.inject.Inject;

public class ServiceLocationFrag extends BaseFragment<ServiceLocLayBinding, SeviceLocationVieModel> implements ServiceLocationNavigator {

    public static final String TAG = "ServiceLocationFrag";
    @Inject
    public SeviceLocationVieModel viewModel;
    public ServiceLocLayBinding binding;
    ServiceLocationAdapter serviceLocationAdapter;
    LinearLayoutManager linearLayoutManager;

    public static Fragment newInstance() {
        return new ServiceLocationFrag();
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
        viewModel.serviceLocApi();
    }


    @Override
    public SeviceLocationVieModel getViewModel() {
        return viewModel;
    }

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.service_loc_lay;
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
        if (!viewModel.sharedPrefence.Getvalue(Constants.AreaID).isEmpty())
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .disallowAddToBackStack()
                    .setCustomAnimations(R.anim.slide_left, R.anim.slide_right)
                    .add(R.id.car_details_frame, CarTypeFrag.newInstance(viewModel.sharedPrefence.Getvalue(Constants.AreaID)), CarTypeFrag.TAG)
                    .commitAllowingStateLoss();
    }


    @Override
    public void loadServiceLocation(List<ServiceLocationModel> serviceLocationModels) {
        serviceLocationAdapter = new ServiceLocationAdapter(getActivity(), serviceLocationModels, this);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        binding.recylerview.setLayoutManager(linearLayoutManager);
        binding.recylerview.setAdapter(serviceLocationAdapter);
        binding.searchEdit.addTextChangedListener(serviceLocationAdapter);
    }

    @Override
    public void onClickSelectArea(String name, String id) {
        viewModel.sharedPrefence.savevalue(Constants.AreaName, name);
        viewModel.sharedPrefence.savevalue(Constants.AreaID, id);
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
