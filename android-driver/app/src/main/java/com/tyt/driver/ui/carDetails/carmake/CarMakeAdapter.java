package com.tyt.driver.ui.carDetails.carmake;


import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.tyt.driver.R;
import com.tyt.driver.retro.responsemodel.CarMakeModel;

import java.util.ArrayList;
import java.util.List;

public class CarMakeAdapter extends RecyclerView.Adapter<CarMakeAdapter.ViewHolder> implements TextWatcher {

    Context context;
    List<CarMakeModel> data;
    public List<CarMakeModel> filteredDataList;
    public List<CarMakeModel> primaryDataList;
    CarMakeNavigator carMakeNavigator;
    int rowIndex = -1;

    public CarMakeAdapter(Context context, List<CarMakeModel> data, CarMakeNavigator carMakeNavigator) {
        this.carMakeNavigator = carMakeNavigator;
        this.context = context;
        this.data = data;
        primaryDataList = data;
        filteredDataList = data;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.car_make_item, viewGroup, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.countryName.setText(filteredDataList.get(i).getName());

        if (rowIndex == filteredDataList.get(i).getId())
            viewHolder.tickImg.setVisibility(View.VISIBLE);
        else viewHolder.tickImg.setVisibility(View.GONE);

    }

    @Override
    public int getItemCount() {
        return filteredDataList.size();
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (primaryDataList != null && primaryDataList.size() > 0 && editable != null) {
            String strFilterable = editable.toString();
            List<CarMakeModel> newFilteredList = new ArrayList<>();
            if (!TextUtils.isEmpty(editable.toString())) {
                for (CarMakeModel row : primaryDataList) {
                    if (row.getName().toLowerCase().startsWith(strFilterable)) {
                        newFilteredList.add(row);
                    }
                }
                filteredDataList = newFilteredList;
                notifyDataSetChanged();
            } else {
                filteredDataList = primaryDataList;
                notifyDataSetChanged();
            }
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView countryName, countryCode;
        ImageView tickImg;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            countryName = itemView.findViewById(R.id.carMakeName);
            tickImg = itemView.findViewById(R.id.tick_img);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String name = filteredDataList.get(getAdapterPosition()).getName();
                    int ID = filteredDataList.get(getAdapterPosition()).getId();
                    carMakeNavigator.onClickSelectedMake(name, ID);

                    rowIndex = filteredDataList.get(getAdapterPosition()).getId();
                    notifyDataSetChanged();
                }
            });

        }
    }
}