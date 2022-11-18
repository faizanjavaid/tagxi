package com.tyt.client.ui.homeScreen.mapFrag.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.tyt.client.R;
import com.tyt.client.retro.responsemodel.RentalPackage;
import com.tyt.client.ui.homeScreen.mapFrag.MapFragNavigator;
import com.tyt.client.ui.homeScreen.tripscreen.CancelReasonAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Adapter File to show the Rental packages list in the Map Screen Page
 */


public class RentalPackagesAdapter extends RecyclerView.Adapter<RentalPackagesAdapter.ViewHolder> {

    Context context;
    List<RentalPackage> packagesList;
    MapFragNavigator navigator;
    int row_index = -1;

    public RentalPackagesAdapter(Context context, List<RentalPackage> packagesList, MapFragNavigator navigator) {
        this.context = context;
        this.packagesList = packagesList;
        this.navigator = navigator;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rental_package_item, parent, false);

        return new RentalPackagesAdapter.ViewHolder(itemView);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        //later fix if any issues

        String[] splitted_array;

        String package_name_raw;

        RentalPackage rentalPackage = packagesList.get(position);


        package_name_raw = rentalPackage.getPackage_name();
        if (package_name_raw != null) {
            splitted_array = package_name_raw.split(" ");

            String hours = splitted_array[0] + " " + splitted_array[1];
            String kms = splitted_array[2] + " " + splitted_array[3];

            holder.package_hour.setText(hours);
            holder.package_km.setText(kms);
        }

        holder.package_card.setOnClickListener(view -> {
            row_index = position;
            navigator.onclickrentalitem(rentalPackage);
            notifyDataSetChanged();
        });

        if (row_index == position) {
            //Item Is Selected
            holder.package_card.setRadius(1.0f);
            holder.package_lay.setBackgroundResource(R.drawable.edit_text_border);
        } else {
            //Item is not Selected
            holder.package_card.setRadius(8.0f);
            holder.package_lay.setBackground(null);
        }

    }

    @Override
    public int getItemCount() {
        return packagesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout package_lay;
        CardView package_card;
        TextView package_hour;
        TextView package_km;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            package_hour = itemView.findViewById(R.id.rental_hour);

            package_km = itemView.findViewById(R.id.rental_km);

            package_card = itemView.findViewById(R.id.rental_package_card);

            package_lay = itemView.findViewById(R.id.package_lay);
        }
    }
}
