package com.tyt.client.ui.homeScreen.mapFrag.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.tyt.client.R;
import com.tyt.client.retro.responsemodel.EtaModel;
import com.tyt.client.retro.responsemodel.RentalPackage;
import com.tyt.client.ui.homeScreen.mapFrag.MapFragNavigator;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Adapter File Class to show the available type of cars when the rental package is picked.
 *
* */

public class RentalVehicAdapter extends RecyclerView.Adapter<RentalVehicAdapter.ViewHolder> {

    Context context;
    List<EtaModel> rentalVehicleList;
    MapFragNavigator mapFragNavigator;
    int row_index = -1;
    public String selectedTypeId;


    public RentalVehicAdapter(Context context, List<EtaModel> rentalVehicleList, MapFragNavigator mapFragNavigator) {
        this.context = context;
        this.rentalVehicleList = rentalVehicleList;
        this.mapFragNavigator = mapFragNavigator;
    }

    @NonNull
    @NotNull
    @Override
    public RentalVehicAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.child_cartypes, parent, false);
        return new RentalVehicAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RentalVehicAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        EtaModel type = rentalVehicleList.get(position);

        if (type != null) {
            holder.rentcar_name.setText(type.getName());
            holder.rentcar_Desc.setText(type.getShort_description());

            if (type.getDriverArivalEstimation().equalsIgnoreCase("--"))
                holder.rent_eta_time.setText("NA");
            else
                holder.rent_eta_time.setText(type.getDriverArivalEstimation());

            holder.rentcar_price.setText(type.getCurrency() + type.getFare_amount());
            holder.rentcar_Discount.setVisibility(View.GONE);

            if (type.getIcon() != null){
                Glide.with(context).load(type.getIcon()).into(holder.rentcar_img);
            }

            holder.rentcar_main_lay.setOnClickListener(view -> {
                row_index = position;
                selectedTypeId = type.getTypeId();
                mapFragNavigator.onClickEtaItemR(type);
                notifyDataSetChanged();
            });

            holder.rentcar_name.setOnClickListener(view -> mapFragNavigator.onClickInfoButtonR(type));

            if (row_index == position) {
                holder.rent_eta_time.setTextColor(context.getResources().getColor(R.color.clr_black));
                holder.rentcar_Card.setCardElevation(10.0f);
            } else {
                holder.rent_eta_time.setTextColor(context.getResources().getColor(R.color.gray_holo_light));
                holder.rentcar_Card.setCardElevation(0.1f);
            }

        }

    }

    @Override
    public int getItemCount() {
        return rentalVehicleList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView rentcar_img;
        TextView rentcar_name, rentcar_price, rentcar_Desc, rent_eta_time, rentcar_Discount;
        CardView rentcar_Card;
        RelativeLayout rentcar_main_lay;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            rentcar_img = itemView.findViewById(R.id.type_car);
            rentcar_name = itemView.findViewById(R.id.typeName);
            rentcar_price = itemView.findViewById(R.id.eta_price);
            rent_eta_time = itemView.findViewById(R.id.eta_time);
            rentcar_Desc = itemView.findViewById(R.id.typeDesc);
            rentcar_Card = itemView.findViewById(R.id.cartype_card);
            rentcar_Discount = itemView.findViewById(R.id.discount_eta);

            rentcar_main_lay = itemView.findViewById(R.id.main_lay);

            rentcar_main_lay.setOnClickListener(view -> {

            });

        }

    }

    public EtaModel getSelectedCar() {
        for (EtaModel obj : rentalVehicleList) {
            if ((obj.getTypeId() + "").equalsIgnoreCase(selectedTypeId))
                return obj;
        }
        return null;
    }

    public void refreshData(List<EtaModel> etaModels) {
        this.rentalVehicleList = etaModels;
        notifyDataSetChanged();
    }

    public void defaultSelection(int i) {
        row_index = i;
        selectedTypeId = rentalVehicleList.get(i).getTypeId();
        mapFragNavigator.onClickEtaItemR(rentalVehicleList.get(i));
        notifyDataSetChanged();
    }

    public void updateEta(String typeId, String eta) {
        for (EtaModel etaObj : rentalVehicleList) {
            if (etaObj.getTypeId().equalsIgnoreCase(typeId)) {
                etaObj.setDriverArivalEstimation(eta);
                notifyDataSetChanged();
                break;
            }
        }
    }

}
