package com.tyt.driver.ui.homeScreen.instantRide;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.tyt.driver.R;
import com.tyt.driver.retro.responsemodel.EtaModel;
import com.tyt.driver.utilz.CommonUtils;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class InsEtaAdapter extends RecyclerView.Adapter<InsEtaAdapter.ViewHolder> {

    Context context;
    List<EtaModel> etaModelList;
    InstantRideNavigator navigator;

    public InsEtaAdapter(Context context, List<EtaModel> etaModelList, InstantRideNavigator navigator) {
        this.context = context;
        this.etaModelList = etaModelList;
        this.navigator = navigator;
    }

    @NotNull
    @Override
    public InsEtaAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.insr_eta_item,parent,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull InsEtaAdapter.ViewHolder holder, int position) {
        EtaModel etaitem = etaModelList.get(position);
        if (etaitem != null){

            holder.typename.setText(etaitem.getName());

            holder.typedesc.setText(etaitem.getShort_description());

            holder.type_longdesc.setText(etaitem.getDescription());

            holder.type_price.setText(etaitem.getCurrency()+ CommonUtils.doubleDecimalFromat(etaitem.getTotal()));

            Glide
                    .with(context)
                    .load(etaitem.getIcon())
                    .into(holder.typeimg);
        }

    }

    @Override
    public int getItemCount() {
        return etaModelList.size();
    }

    public  class ViewHolder extends RecyclerView.ViewHolder {
        ImageView typeimg;
        TextView typename;
        TextView typedesc;
        TextView type_longdesc;
        TextView type_price;
        LinearLayout detaillay;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            typeimg=itemView.findViewById(R.id.insr_type_img);
            typename=itemView.findViewById(R.id.insr_type_name);
            typedesc=itemView.findViewById(R.id.insr_type_desc);
            type_longdesc=itemView.findViewById(R.id.insr_type_long_desc);
            type_price=itemView.findViewById(R.id.insr_type_price);
            detaillay=itemView.findViewById(R.id.insr_detail_lay);

            detaillay.setOnClickListener(view -> {
                //set eta type and allow user to book the ride
                navigator.onclick_insr_eta_item(etaModelList.get(getAdapterPosition()));
            });

        }
    }
}
