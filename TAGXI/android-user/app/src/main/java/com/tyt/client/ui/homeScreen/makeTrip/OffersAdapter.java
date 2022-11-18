package com.tyt.client.ui.homeScreen.makeTrip;


import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tyt.client.R;
import com.tyt.client.retro.responsemodel.CardListModel;
import com.tyt.client.retro.responsemodel.OffersResponseData;
import com.tyt.client.ui.homeScreen.manageCard.ManageCardNavigator;

import java.util.List;

/**
* Adapter class to show Promo Code Details in booking page.
* */

public class OffersAdapter extends RecyclerView.Adapter<OffersAdapter.ViewHolder> {

    Context context;
    MakeTripNavigator navigator;
    List<OffersResponseData> offersResponseData;
    int pos = -1;

    public OffersAdapter(Context context, List<OffersResponseData> offersResponseData, MakeTripNavigator navigator) {
        this.context = context;
        this.navigator = navigator;
        this.offersResponseData = offersResponseData;
    }

    @NonNull
    @Override
    public OffersAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.offer_list_item, viewGroup, false);
        return new OffersAdapter.ViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull OffersAdapter.ViewHolder viewHolder, final int i) {
        viewHolder.card_value.setText(offersResponseData.get(i).getDiscountPercent() + "% Offer");
        viewHolder.code.setText(offersResponseData.get(i).getCode());

        viewHolder.removecouponic.setVisibility(offersResponseData.get(i).getIsApplied()?View.VISIBLE:View.GONE);

        viewHolder.radioButton.setChecked(pos == i);

        if (offersResponseData.get(i).getIsApplied()) {
            viewHolder.applyCouponTxt.setText("Promo Applied");
            viewHolder.applyCouponTxt.setTextColor(context.getResources().getColor(R.color.clr_green));
            viewHolder.applyCouponTxt.setBackgroundResource(R.drawable.green_rect_border);
        } else {
            viewHolder.applyCouponTxt.setText("Apply Promo");
            viewHolder.applyCouponTxt.setBackgroundResource(R.drawable.orange_rect_border);
            viewHolder.applyCouponTxt.setTextColor(context.getResources().getColor(R.color.clr_FF6E1D));
        }

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView card_value, code, applyCouponTxt;
        RadioButton radioButton;
        ImageView removecouponic;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            card_value = itemView.findViewById(R.id.offers_percentage);
            code = itemView.findViewById(R.id.code);
            applyCouponTxt = itemView.findViewById(R.id.apply_coupon);
            radioButton = itemView.findViewById(R.id.radio_choose_offers);
            removecouponic=itemView.findViewById(R.id.remove_coupon);

            radioButton.setOnClickListener(v -> {
                pos = getAdapterPosition();
                navigator.clickedOferItem(offersResponseData.get(getAdapterPosition()));
                notifyDataSetChanged();
            });

            applyCouponTxt.setOnClickListener(v -> navigator.clickedOferItem(offersResponseData.get(getAdapterPosition())));

            removecouponic.setOnClickListener(view -> {
                navigator.removeclickedpromo(offersResponseData.get(getAdapterPosition()));
                notifyDataSetChanged();
            });

        }
    }

    @Override
    public int getItemCount() {
        return offersResponseData.size();
    }
}
