package com.tyt.driver.ui.carDetails.carColor;

import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.tyt.driver.BR;
import com.tyt.driver.R;
import com.tyt.driver.databinding.CarColorLayBinding;
import com.tyt.driver.databinding.CarTypeLayBinding;
import com.tyt.driver.ui.base.BaseActivity;
import com.tyt.driver.ui.base.BaseFragment;
import com.tyt.driver.ui.carDetails.carNumber.VehNumberFrag;
import com.tyt.driver.utilz.Constants;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class CarColorFrag extends BaseFragment<CarColorLayBinding, CarColorViewModel> implements CarColorNavigator {

    public static final String TAG = "CarColorFrag";
    @Inject
    public CarColorViewModel viewModel;
    public CarColorLayBinding binding;
    CarColorAdapter carColorAdapter;
    LinearLayoutManager linearLayoutManager;

    List<String> carColor = new ArrayList<>();

    public static Fragment newInstance() {
        return new CarColorFrag();
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

        carColor.add("Dark blue");
        carColor.add("Blue");
        carColor.add("Dark green");
        carColor.add("Green");
        carColor.add("Brown");
        carColor.add("Orange");
        carColor.add("Red");
        carColor.add("Pink");
        carColor.add("Yellow");
        carColor.add("White");
        carColor.add("Black");

        carColorAdapter = new CarColorAdapter(getActivity(), carColor, this);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        binding.recylerview.setLayoutManager(linearLayoutManager);
        binding.recylerview.setAdapter(carColorAdapter);
        binding.searchEdit.addTextChangedListener(carColorAdapter);

    }


    @Override
    public CarColorViewModel getViewModel() {
        return viewModel;
    }

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.car_color_lay;
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
        if (!viewModel.sharedPrefence.Getvalue(Constants.selectedColor).isEmpty())
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .disallowAddToBackStack()
                    .setCustomAnimations(R.anim.slide_left, R.anim.slide_right)
                    .add(R.id.car_details_frame, VehNumberFrag.newInstance(), VehNumberFrag.TAG)
                    .commitAllowingStateLoss();
    }

    @Override
    public void onClickSelectedColor(String s) {
        viewModel.sharedPrefence.savevalue(Constants.selectedColor, s);
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
