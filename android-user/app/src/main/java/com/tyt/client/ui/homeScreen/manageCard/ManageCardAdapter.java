package com.tyt.client.ui.homeScreen.manageCard;


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

import java.util.List;

/**
 * Adapter File to store the saved and new card details.
 *
* */

public class ManageCardAdapter extends RecyclerView.Adapter<ManageCardAdapter.ViewHolder> {

    Context context;
    ManageCardNavigator navigator;
    List<CardListModel> cardListModels;
    String id = "-1";

    public ManageCardAdapter(Context context, List<CardListModel> cardListModels, ManageCardNavigator navigator) {
        this.context = context;
        this.navigator = navigator;
        this.cardListModels = cardListModels;
    }


    @NonNull
    @Override
    public ManageCardAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.manage_card_item, viewGroup, false);

        return new ManageCardAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ManageCardAdapter.ViewHolder viewHolder, final int i) {
        viewHolder.card_value.setText("**** **** **** " + cardListModels.get(i).getLastNumber().replace(".", ""));
        if (cardListModels.get(i).getCardType().equalsIgnoreCase("VISA"))
            viewHolder.cardTypeImg.setImageDrawable(context.getResources().getDrawable(R.drawable.visa_img));
        else
            viewHolder.cardTypeImg.setImageDrawable(context.getResources().getDrawable(R.drawable.card_img_black));

        if (cardListModels.get(i).getValidThru() != null)
            viewHolder.validTrough.setText(cardListModels.get(i).getValidThru());

        if (cardListModels.get(i).getIsDefault() == 1)
            viewHolder.radioButton.setChecked(true);
        else viewHolder.radioButton.setChecked(false);

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView card_value, validTrough, delete;
        ImageView cardTypeImg;
        RadioButton radioButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            card_value = itemView.findViewById(R.id.card_value);
            validTrough = itemView.findViewById(R.id.valid_value);
            cardTypeImg = itemView.findViewById(R.id.card_type_img);
            delete = itemView.findViewById(R.id.remove_butt);
            radioButton = itemView.findViewById(R.id.radio_choose);

            delete.setOnClickListener(v -> navigator.deleteClick(cardListModels.get(getAdapterPosition()).getId()));

            radioButton.setOnClickListener(v -> {
                if (cardListModels.get(getAdapterPosition()).getIsDefault() != 1) {
                    id = cardListModels.get(getAdapterPosition()).getId();
                    navigator.clickCardItem(cardListModels.get(getAdapterPosition()).getId());
                }

            });
        }
    }

    @Override
    public int getItemCount() {
        return cardListModels.size();
    }
}
