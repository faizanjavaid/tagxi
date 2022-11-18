package com.tyt.client.ui.homeScreen.tripscreen;


import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tyt.client.R;

import com.tyt.client.retro.responsemodel.CancelReasonModel;
import com.tyt.client.ui.homeScreen.historyDetail.HistoryDetailNavigator;

import java.util.List;

/**
 * Adapter File Which stores the Cancellation Reasons from the server.
* */

public class CancelReasonAdapter extends RecyclerView.Adapter<CancelReasonAdapter.ViewHolder> {

    Context context;
    TripNavigator navigator;
    HistoryDetailNavigator mnavigator;

    public int mSelectedItem = -1;
    public String selecteID;
    List<CancelReasonModel> reasonModels;


    public CancelReasonAdapter(Context context, List<CancelReasonModel> reasonModels, TripNavigator navigator) {
        this.context = context;
        this.navigator = navigator;
        this.reasonModels = reasonModels;
    }

    public CancelReasonAdapter(Context context, List<CancelReasonModel> reasonModels, HistoryDetailNavigator navigator) {
        this.context = context;
        this.mnavigator = mnavigator;
        this.reasonModels = reasonModels;
    }

    @NonNull
    @Override
    public CancelReasonAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.cancel_reason_item, viewGroup, false);

        return new CancelReasonAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CancelReasonAdapter.ViewHolder viewHolder, final int i) {

        viewHolder.mRadio.setChecked(i == mSelectedItem);
        viewHolder.reasonText.setText(reasonModels.get(i).getReason());
        viewHolder.viewParent.setTag(reasonModels.get(i).getId());
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView reasonText;
        RadioButton mRadio;
        View viewParent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            reasonText = itemView.findViewById(R.id.cancel_txt);
            viewParent = itemView.findViewById(R.id.layout_cancel_desc);
            mRadio = itemView.findViewById(R.id.radio_cancel_desc);

            mRadio.setOnClickListener(v -> viewParent.performClick());
            viewParent.setOnClickListener(view -> {
                mSelectedItem = getAdapterPosition();
                selecteID = String.valueOf(view.getTag());
                notifyDataSetChanged();

                if (navigator != null) {
                    if (!TextUtils.isEmpty(selecteID))
                        navigator.selectedReason(selecteID.equalsIgnoreCase("0"));
                } else {
                    if (!TextUtils.isEmpty(selecteID))
                        mnavigator.selectedReason(selecteID.equalsIgnoreCase("0"));
                }

            });
        }
    }


    public String getSelectPosition() {
        return selecteID;
    }

    @Override
    public int getItemCount() {
        return reasonModels.size();
    }
}
