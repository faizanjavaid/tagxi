package com.tyt.driver.ui.homeScreen.userHistory;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.tyt.driver.R;

import com.tyt.driver.retro.responsemodel.HistoryCard;
import com.tyt.driver.retro.responsemodel.HistoryModel;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    Context context;
    HistoryNavigator navigator;
    List<HistoryCard> historyModels;
    Animation cardanim;

    public HistoryAdapter(Context context, List<HistoryCard> historyModels, HistoryNavigator navigator) {
        this.context = context;
        this.navigator = navigator;
        this.historyModels = historyModels;
    }

    @NonNull
    @Override
    public HistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.history_item, viewGroup, false);

        return new HistoryAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryAdapter.ViewHolder viewHolder, final int i) {

        cardanim= AnimationUtils.loadAnimation(context,R.anim.adapter_anim);

        viewHolder.cardViewhis.setAnimation(cardanim);

        viewHolder.tripId.setText("#" + historyModels.get(i).getRequestNumber());
        viewHolder.pickupAddress.setText(historyModels.get(i).getPickAddress());
        viewHolder.dropAddress.setText(historyModels.get(i).getDropAddress());

        viewHolder.driver_name.setVisibility(View.GONE);
        viewHolder.driver_rating.setVisibility(View.GONE);
        viewHolder.driver_img.setVisibility(View.GONE);

        if (historyModels.get(i).getIsLater() == 1) {
            //Upcoming Type
            viewHolder.end_time.setVisibility(View.GONE);
            viewHolder.arrive_cancel_time.setVisibility(View.GONE);
            viewHolder.arrival_cancel_txts.setVisibility(View.GONE);
            viewHolder.symbol.setVisibility(View.GONE);
            viewHolder.total_fare.setVisibility(View.GONE);
            viewHolder.tripStatus.setText("Upcoming");
            viewHolder.start_time.setText(historyModels.get(i).getTripStartTime().substring(9));
            viewHolder.tripStatus.setBackground(context.getResources().getDrawable(R.drawable.upcoming_bg));
        } else if (historyModels.get(i).getIsCancelled() == 1) {
            //Cancelled Type
            viewHolder.total_fare.setVisibility(View.GONE);
            viewHolder.symbol.setVisibility(View.GONE);
            viewHolder.start_time.setVisibility(View.GONE);
            viewHolder.end_time.setVisibility(View.GONE);
            viewHolder.tripStatus.setText("Cancelled");
            viewHolder.arrival_cancel_txts.setText("Cancelled at");
            viewHolder.arrive_cancel_time.setText(historyModels.get(i).getUpdated_at().substring(9));
            viewHolder.tripStatus.setBackground(context.getResources().getDrawable(R.drawable.cancelled_bg));
        } else if (historyModels.get(i).getIsCompleted() == 1) {
            //Completed Type
            if (historyModels.get(i).getRequestBill().getData() != null) {
                viewHolder.total_fare.setText(historyModels.get(i).getRequestBill().getData().getRequested_currency_symbol() + " " + historyModels.get(i).getRequestBill().getData().getTotalAmount().toString());
            } else {
                viewHolder.total_fare.setVisibility(View.GONE);
            }
            //viewHolder.driver_name.setText(historyModels.get(i).getDriverDetail().getDriverdata().getName());
            //Glide.with(context).load(historyModels.get(i).getDriverDetail().getDriverdata().getProfile_picture()).into(viewHolder.driver_img);
            viewHolder.tripStatus.setText("Completed");
            viewHolder.arrival_cancel_txts.setText("Arrival Time");
            viewHolder.arrive_cancel_time.setText(historyModels.get(i).getArrivedAt().toString());
            //viewHolder.driver_rating.setRating((float) historyModels.get(i).getDriverDetail().getDriverdata().rating);
            viewHolder.start_time.setText(historyModels.get(i).getTripStartTime().substring(9));
            viewHolder.end_time.setText(historyModels.get(i).getCompletedAt().substring(9));
            viewHolder.tripStatus.setBackground(context.getResources().getDrawable(R.drawable.completed_bg));
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tripId, tripStatus, pickupAddress, dropAddress;

        TextView start_time, end_time, driver_name, trip_date;
        TextView arrival_cancel_txts, arrive_cancel_time;
        TextView total_fare, symbol;
        ImageView driver_img;
        RatingBar driver_rating;
        CardView cardViewhis;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardViewhis=itemView.findViewById(R.id.history_card_Ad);
            tripId = itemView.findViewById(R.id.trip_id);
            tripStatus = itemView.findViewById(R.id.trip_status);
            pickupAddress = itemView.findViewById(R.id.pickup_address);
            dropAddress = itemView.findViewById(R.id.drop_address);

            start_time = itemView.findViewById(R.id.trip_start_time);
            end_time = itemView.findViewById(R.id.trip_end_time);

            driver_img = itemView.findViewById(R.id.driverimg);
            driver_name = itemView.findViewById(R.id.driver_name);
            driver_rating = itemView.findViewById(R.id.driver_rating);
            trip_date = itemView.findViewById(R.id.trip_date);
            arrival_cancel_txts = itemView.findViewById(R.id.details_arrive_cancel);
            arrive_cancel_time = itemView.findViewById(R.id.arrival_cancel_times);

            total_fare = itemView.findViewById(R.id.total_fare);
            symbol = itemView.findViewById(R.id.symbol);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!(historyModels.get(getAdapterPosition()).getIsLater() == 1) && !(historyModels.get(getAdapterPosition()).getIsCancelled() == 1)) {
                        navigator.onClickItem(historyModels.get(getAdapterPosition()).getId());
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return historyModels.size();
    }
}
