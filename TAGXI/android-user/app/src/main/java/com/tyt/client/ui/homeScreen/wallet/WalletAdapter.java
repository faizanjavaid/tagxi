package com.tyt.client.ui.homeScreen.wallet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tyt.client.R;
import com.tyt.client.retro.responsemodel.WalletHistoryModel;
import com.tyt.client.utilz.CommonUtils;

import java.util.List;


/**
* Adapter File which is used to store all wallet transactions.
* */


public class WalletAdapter extends RecyclerView.Adapter<WalletAdapter.ViewHolder> {

    List<WalletHistoryModel.WalletHistoryData> walletHistoryData;
    Context context;
    String currencySymbol = "";

    WalletAdapter(Context context, List<WalletHistoryModel.WalletHistoryData> walletHistoryData, String currencySymbol) {
        this.context = context;
        this.walletHistoryData = walletHistoryData;
        this.currencySymbol = currencySymbol;
    }


    @NonNull
    @Override
    public WalletAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.wallet_items, viewGroup, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull WalletAdapter.ViewHolder viewHolder, final int i) {

        viewHolder.name.setText(walletHistoryData.get(i).getRemarks());
        viewHolder.date.setText(walletHistoryData.get(i).getCreatedAt());
        if (walletHistoryData.get(i).getIsCredit() == 1) {
            viewHolder.amount.setText("+" + currencySymbol + CommonUtils.doubleDecimalFromat(walletHistoryData.get(i).getAmount()));
            viewHolder.amount.setTextColor(context.getResources().getColor(R.color.clr_green));
        } else {
            viewHolder.amount.setText("-" + currencySymbol + CommonUtils.doubleDecimalFromat(walletHistoryData.get(i).getAmount()));
            viewHolder.amount.setTextColor(context.getResources().getColor(R.color.bt_error_red));
        }

    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, date, amount;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.title);
            date = itemView.findViewById(R.id.title_desc);
            amount = itemView.findViewById(R.id.amount);
        }
    }

    @Override
    public int getItemCount() {
        return walletHistoryData.size();
    }
}
