package com.tyt.driver.utilz;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Utils;
import com.tyt.driver.R;
import com.tyt.driver.ui.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

public class ChartMarker extends MarkerView {

    private TextView tvContent;
    private List<BarEntry> entries;
    String currencySymbol;

    public ChartMarker(BaseActivity baseAct, int layoutResource, ArrayList<BarEntry> entries, String currencySymbol) {
        super(baseAct, layoutResource);
        tvContent = findViewById(R.id.tvContent);
        this.entries = entries;
        this.currencySymbol = currencySymbol;
    }

    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        tvContent.setText(currencySymbol + e.getY());
        super.refreshContent(e, highlight);
    }

    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(getWidth() / 2), -getHeight());
    }

}
