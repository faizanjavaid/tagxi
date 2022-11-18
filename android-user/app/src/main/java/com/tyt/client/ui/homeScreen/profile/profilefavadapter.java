package com.tyt.client.ui.homeScreen.profile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tyt.client.R;
import com.tyt.client.retro.responsemodel.FavouriteLocations;
import com.tyt.client.ui.homeScreen.FavouitesLocAdapter;
import com.tyt.client.ui.homeScreen.faq.FaqNavigator;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class profilefavadapter extends RecyclerView.Adapter<profilefavadapter.ViewHolder> {
    Context context;
    List<FavouriteLocations.FavLocData> favLocDataList;
    FaqNavigator faqNavigator;

    public profilefavadapter(Context context, List<FavouriteLocations.FavLocData> favLocDataList, FaqNavigator faqNavigator) {
        this.context = context;
        this.favLocDataList = favLocDataList;
        this.faqNavigator = faqNavigator;
    }

    @NonNull
    @Override
    public profilefavadapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.favloc_item, parent, false);
        return new profilefavadapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull profilefavadapter.ViewHolder holder, int i) {
        if (favLocDataList.get(i).getAddress_name().equalsIgnoreCase("Home")) {
            //set Home Icon
            holder.addresstype.setImageResource(R.drawable.ic_home);
        } else if (favLocDataList.get(i).getAddress_name().equalsIgnoreCase("Work")) {
            //set Suitcase Icon
            holder.addresstype.setImageResource(R.drawable.ic_work);
        } else {
            //set Heart Icon
            holder.addresstype.setImageResource(R.drawable.ic_full_heart);
        }

        if (favLocDataList.get(i).getPick_address() != null) {
            holder.Address.setText(favLocDataList.get(i).getPick_address());
        } else if (favLocDataList.get(i).getDrop_address() != null) {
            holder.Address.setText(favLocDataList.get(i).getDrop_address());
        }

        holder.Addressname.setText(favLocDataList.get(i).getAddress_name());
    }

    @Override
    public int getItemCount() {
        return favLocDataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView addresstype;
        TextView Addressname;
        TextView Address;
        LinearLayout mainlayfav;
        ImageView deletebtn;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            addresstype = itemView.findViewById(R.id.img_favaddresstype);
            Addressname = itemView.findViewById(R.id.tv_fav_addressname);
            Address = itemView.findViewById(R.id.tv_fav_address_data);
            mainlayfav = itemView.findViewById(R.id.favrcyclemainlay);
            deletebtn = itemView.findViewById(R.id.img_del_fav);

            deletebtn.setOnClickListener(view -> {
                //call the delete API
                faqNavigator.deleteitem(String.valueOf(favLocDataList.get(getAdapterPosition()).getId()));

            });

        }
    }
}
