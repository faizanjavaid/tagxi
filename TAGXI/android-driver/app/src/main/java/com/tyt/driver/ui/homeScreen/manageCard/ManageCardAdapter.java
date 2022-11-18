package com.tyt.driver.ui.homeScreen.manageCard;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tyt.driver.R;
import com.tyt.driver.retro.responsemodel.CardListModel;

import java.util.List;

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

        viewHolder.radioButton.setChecked(cardListModels.get(i).getIsDefault() == 1);

        viewHolder.delete.setText(navigator.getBaseAct().getTranslatedString(R.string.txt_remove));
        viewHolder.card_text.setText(navigator.getBaseAct().getTranslatedString(R.string.txt_card_num));
        viewHolder.card_valid.setText(navigator.getBaseAct().getTranslatedString(R.string.txt_valid_tru));
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView card_value, validTrough, delete, card_text, card_valid;
        ImageView cardTypeImg;
        RadioButton radioButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            card_value = itemView.findViewById(R.id.card_value);
            validTrough = itemView.findViewById(R.id.valid_value);
            cardTypeImg = itemView.findViewById(R.id.card_type_img);
            delete = itemView.findViewById(R.id.remove_butt);
            radioButton = itemView.findViewById(R.id.radio_choose);
            card_text = itemView.findViewById(R.id.card_num_txt);
            card_valid = itemView.findViewById(R.id.valid_thru);

            radioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (cardListModels.get(getAdapterPosition()).getIsDefault() != 1) {
                        id = cardListModels.get(getAdapterPosition()).getId();
                        navigator.clickCardItem(cardListModels.get(getAdapterPosition()).getId());
                    }

                    // notifyDataSetChanged();
                }
            });

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    navigator.deleteClick(cardListModels.get(getAdapterPosition()).getId());
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return cardListModels.size();
    }
}
