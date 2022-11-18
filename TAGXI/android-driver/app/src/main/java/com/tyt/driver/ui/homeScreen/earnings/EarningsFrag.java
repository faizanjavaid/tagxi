package com.tyt.driver.ui.homeScreen.earnings;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;

import androidx.databinding.library.baseAdapters.BR;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.tyt.driver.R;
import com.tyt.driver.databinding.EarningsLayBinding;
import com.tyt.driver.retro.responsemodel.WeekDays;
import com.tyt.driver.retro.responsemodel.WeeklyModel;
import com.tyt.driver.ui.base.BaseActivity;
import com.tyt.driver.ui.base.BaseFragment;
import com.tyt.driver.ui.homeScreen.HomeAct;
import com.tyt.driver.ui.homeScreen.profile.ProfileFrag;
import com.tyt.driver.utilz.ChartMarker;
import com.tyt.driver.utilz.CustomBarChartRender;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import javax.inject.Inject;

public class EarningsFrag extends BaseFragment<EarningsLayBinding, EarningsViewModel> implements EarningsNavigator, DatePickerDialog.OnDateSetListener {

    public static final String TAG = "EarningsFrag";
    @Inject
    public EarningsViewModel mViewModel;
    public EarningsLayBinding mBinding;

    int startDay, startMonth, startYear;
    int toDay, toMonth, toYear;


    // TODO: Rename and change types and number of parameters
    public static EarningsFrag newInstance() {
        return new EarningsFrag();
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

        setupChart();

        mViewModel.todayEarningsApi();
    }

    private void setupChart() {
        mBinding.chart.setPinchZoom(false);
        mBinding.chart.setDoubleTapToZoomEnabled(false);
        mBinding.chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        mBinding.chart.getAxisLeft().setEnabled(false);
        mBinding.chart.getAxisRight().setEnabled(false);
        mBinding.chart.getAxisRight().setDrawLabels(true);
        mBinding.chart.getXAxis().setDrawGridLines(false);
        mBinding.chart.getAxisLeft().setDrawGridLines(false);
        mBinding.chart.getAxisRight().setDrawGridLines(false);
        mBinding.chart.getDescription().setEnabled(false);
        mBinding.chart.setFitBars(true);
        mBinding.chart.getLegend().setEnabled(false);
        mBinding.chart.getXAxis().setTextSize(15f);
        mBinding.chart.setExtraOffsets(0, 20, 0, 20);

        mBinding.chart.getXAxis().setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                int index = (int) value;
                if (index == 0)
                    return "S";
                if (index == 1)
                    return "M";
                if (index == 2)
                    return "T";
                if (index == 3)
                    return "W";
                if (index == 4)
                    return "T";
                if (index == 5)
                    return "F";
                if (index == 6)
                    return "S";
                return super.getFormattedValue(value);
            }
        });

        mBinding.chart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {

            }

            @Override
            public void onNothingSelected() {

            }
        });
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
    public EarningsViewModel getViewModel() {
        return mViewModel;
    }

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.earnings_lay;
    }

    /**
     * returns reference of {@link BaseActivity}
     **/

    @Override
    public void onClickBack() {
        if (getActivity().getSupportFragmentManager().findFragmentByTag(EarningsFrag.TAG) != null) {
            ((HomeAct) getActivity()).ShowHideBar(true);
            Fragment frag = getActivity().getSupportFragmentManager().findFragmentByTag(EarningsFrag.TAG);
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_frame, ProfileFrag.newInstance(), ProfileFrag.TAG)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .commit();

            ((HomeAct) getActivity()).ShowOnloneOffline(true);
        } else {
            Log.e("elkfjefjke", "lejfioe");
        }
    }

    @Override
    public void onClickToDate() {
        mViewModel.fromDateClick.set(false);
        mViewModel.toDateClick.set(true);

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), this, year, month, day);
        //datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }

    @Override
    public void onClickStartDate() {
        mViewModel.fromDateClick.set(true);
        mViewModel.toDateClick.set(false);
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), this, year, month, day);
        // datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }

    @Override
    public BaseActivity getBaseAct() {
        return getBaseActivity();
    }

    @Override
    public void loadChartData(WeeklyModel response) {
        mBinding.chart.clear();

        CustomBarChartRender barChartRender = new CustomBarChartRender(mBinding.chart, mBinding.chart.getAnimator(), mBinding.chart.getViewPortHandler());
        barChartRender.setRadius(30);
        mBinding.chart.setRenderer(barChartRender);
        mBinding.chart.setHighlightFullBarEnabled(false);

        if (response != null && response.getWeekDays() != null) {
            ArrayList<BarEntry> entries = new ArrayList<>();

            WeekDays weekDays = response.getWeekDays();
            if (weekDays.getSun() != null)
                entries.add(new BarEntry(0, weekDays.getSun().floatValue()));
            if (weekDays.getMon() != null)
                entries.add(new BarEntry(1, weekDays.getMon().floatValue()));
            if (weekDays.getTues() != null)
                entries.add(new BarEntry(2, weekDays.getTues().floatValue()));
            if (weekDays.getWed() != null)
                entries.add(new BarEntry(3, weekDays.getWed().floatValue()));
            if (weekDays.getThurs() != null)
                entries.add(new BarEntry(4, weekDays.getThurs().floatValue()));
            if (weekDays.getFri() != null)
                entries.add(new BarEntry(5, weekDays.getFri().floatValue()));
            if (weekDays.getSat() != null)
                entries.add(new BarEntry(6, weekDays.getSat().floatValue()));

//            entries.add(new BarEntry(0, 5));
//            entries.add(new BarEntry(1, 7));
//            entries.add(new BarEntry(2, 3));
//            entries.add(new BarEntry(3, 2));
//            entries.add(new BarEntry(4, 9));
//            entries.add(new BarEntry(5, 10));
//            entries.add(new BarEntry(6, 25));

            ChartMarker mv = new ChartMarker(getBaseAct(), R.layout.layout_chart_marker, entries, response.getCurrencySymbol());
            mv.setChartView(mBinding.chart);
            mBinding.chart.setMarker(mv);

            BarDataSet set = new BarDataSet(entries, "");
            set.setColor(Color.parseColor("#F2F5F7"));
            set.setHighlightEnabled(true);
            set.setHighLightColor(Color.parseColor("#FF6E1D"));
            set.setHighLightAlpha(175);

            Typeface font = Typeface.createFromAsset(getContext().getAssets(), "fonts/Poppins.ttf");
            BarData data = new BarData(set);
            data.setValueTextSize(10f);
            data.setValueTypeface(font);
            data.setDrawValues(false);
            data.setBarWidth(0.8f);

            mBinding.chart.setData(data);
            mBinding.chart.invalidate();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ((HomeAct) getActivity()).ShowHideBar(false);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        if (mViewModel.fromDateClick.get()) {
            startYear = year;
            startDay = dayOfMonth;
            startMonth = month;
            Calendar calendar = Calendar.getInstance();
            calendar.set(startYear, startMonth, startDay);

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            String strDate = format.format(calendar.getTime());
            mViewModel.fromDateValue.set(strDate);

        } else {
            toYear = year;
            toDay = dayOfMonth;
            toMonth = month;
            Calendar calendar = Calendar.getInstance();
            calendar.set(toYear, toMonth, toDay);

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            String strDate = format.format(calendar.getTime());
            mViewModel.toDateValue.set(strDate);
        }
    }
}
