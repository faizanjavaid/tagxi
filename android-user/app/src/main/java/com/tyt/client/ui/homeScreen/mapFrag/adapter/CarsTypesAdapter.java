package com.tyt.client.ui.homeScreen.mapFrag.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Paint;
import android.util.Log;
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
import com.tyt.client.retro.responsemodel.Type;
import com.tyt.client.ui.homeScreen.makeTrip.MakeTripFrag;
import com.tyt.client.ui.homeScreen.makeTrip.MakeTripNavigator;
import com.tyt.client.ui.homeScreen.mapFrag.MapFragNavigator;
import com.tyt.client.utilz.CommonUtils;

import java.util.Iterator;
import java.util.List;

/**
 * Adapter File to show the types of the car in the booking page.
 *
* */

public class CarsTypesAdapter extends RecyclerView.Adapter<CarsTypesAdapter.ViewHolder> {
    
    Context context;
    MakeTripNavigator makeTripNavigator;
    List<EtaModel> etaModel;
    int row_index = -1;
    public String selectedTypeId;

    public CarsTypesAdapter(Context context, List<EtaModel> etaModel, MakeTripNavigator makeTripNavigator) {
        Log.e("dataReceieved---", "onAdapter");
        this.makeTripNavigator = makeTripNavigator;
        this.etaModel = etaModel;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.child_cartypes, viewGroup, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder,
                                 @SuppressLint("RecyclerView") int i) {

        if (etaModel.get(i) != null) {

            viewHolder.typeName.setText(etaModel.get(i).getName());

            viewHolder.typedesc.setText(etaModel.get(i).getShort_description());

            viewHolder.cartypecard.setCardElevation(0.1f);

            if (etaModel.get(i).getHasDiscount()) {
                viewHolder.discountETA.setVisibility(View.VISIBLE);
                viewHolder.discountETA.setText(etaModel.get(i).getCurrency() + CommonUtils.doubleDecimalFromat(etaModel.get(i).getDiscountAmount()));
                viewHolder.price.setText(etaModel.get(i).getCurrency() + CommonUtils.doubleDecimalFromat(etaModel.get(i).getTotal()));
                viewHolder.price.setPaintFlags(viewHolder.price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            } else {
                viewHolder.discountETA.setVisibility(View.GONE);
                viewHolder.price.setText(etaModel.get(i).getCurrency() + CommonUtils.doubleDecimalFromat(etaModel.get(i).getTotal()));
            }
            if (etaModel.get(i).getDriverArivalEstimation().equalsIgnoreCase("--"))
                viewHolder.etaTime.setText("NA");
            else
                viewHolder.etaTime.setText(etaModel.get(i).getDriverArivalEstimation());

            if (etaModel.get(i).getIcon() != null)
                Glide.with(context).load(etaModel.get(i).getIcon()).into(viewHolder.typeImg);

            viewHolder.relativeLayout.setOnClickListener(view -> {
                row_index = i;
                selectedTypeId = etaModel.get(i).getTypeId();
                makeTripNavigator.onClickEtaItem(etaModel.get(i));
                notifyDataSetChanged();
            });

            viewHolder.typeName.setOnClickListener(v -> makeTripNavigator.onCLickInfoButon(etaModel.get(i)));

            if (row_index == i) {
                viewHolder.etaTime.setTextColor(context.getResources().getColor(R.color.clr_black));
                viewHolder.typeImg.setAlpha(1f);
                viewHolder.cartypecard.setCardElevation(10.0f);

            } else {
                viewHolder.etaTime.setTextColor(context.getResources().getColor(R.color.gray_holo_light));
                viewHolder.typeImg.setAlpha(0.5f);
                viewHolder.cartypecard.setCardElevation(0.1f);

            }

        }
    }

    @Override
    public int getItemCount() {
        return etaModel.size();
    }

    public EtaModel getSelectedCar() {
        for (EtaModel obj : etaModel) {
            if ((obj.getTypeId() + "").equalsIgnoreCase(selectedTypeId))
                return obj;
        }
        return null;
    }

    public void updateEta(String typeId, String eta) {
        for (EtaModel etaObj : etaModel) {
            if (etaObj.getTypeId().equalsIgnoreCase(typeId)) {
                etaObj.setDriverArivalEstimation(eta);
                notifyDataSetChanged();
                break;
            }
        }
    }

    public void refreshData(List<EtaModel> etaModels) {
        this.etaModel = etaModels;
        notifyDataSetChanged();
    }

    public void defaultSelection(int i) {
        row_index = i;
        selectedTypeId = etaModel.get(i).getTypeId();
        makeTripNavigator.onClickEtaItem(etaModel.get(i));
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView typeName, price, etaTime, discountETA;
        ImageView typeImg;
        RelativeLayout relativeLayout;
        CardView cartypecard;
        TextView typedesc;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            typeName = itemView.findViewById(R.id.typeName);
            typeImg = itemView.findViewById(R.id.type_car);
            price = itemView.findViewById(R.id.eta_price);
            etaTime = itemView.findViewById(R.id.eta_time);
            discountETA = itemView.findViewById(R.id.discount_eta);
            cartypecard = itemView.findViewById(R.id.cartype_card);
            typedesc=itemView.findViewById(R.id.typeDesc);
            relativeLayout = itemView.findViewById(R.id.main_lay);

            relativeLayout.setOnClickListener(v -> makeTripNavigator.onClickEtaItem(etaModel.get(getAdapterPosition())));

        }
    }
}
