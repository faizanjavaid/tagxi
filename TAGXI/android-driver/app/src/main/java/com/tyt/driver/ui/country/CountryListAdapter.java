package com.tyt.driver.ui.country;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.tyt.driver.R;
import com.tyt.driver.retro.responsemodel.CountryListModel;
import com.tyt.driver.utilz.CommonUtils;

import java.util.ArrayList;

public class CountryListAdapter extends RecyclerView.Adapter<CountryListAdapter.ViewHolder> implements Filterable {

    Context context;
    ArrayList<CountryListModel> dataList = new ArrayList<>();
    ArrayList<CountryListModel> filtereddataList = new ArrayList<>();
    Countrylistnavigator countrylistnavigator;


    public CountryListAdapter(Context context, String data, Countrylistnavigator countrylistnavigator) {
        this.countrylistnavigator = countrylistnavigator;
        this.context = context;
        dataList.addAll(CommonUtils.stringToArray(data, CountryListModel[].class));
        filtereddataList.addAll(CommonUtils.stringToArray(data, CountryListModel[].class));
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.countrylist_item, viewGroup, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        // if (!TextUtils.isEmpty(filtereddataList.get(i).getFlag()) && filtereddataList.get(i).getFlag() != null) {
        viewHolder.countryName.setText(filtereddataList.get(i).getName());
        viewHolder.countryCode.setText(filtereddataList.get(i).getCallingCode());
        Glide.with(context).load(filtereddataList.get(i).getFlag()).into(viewHolder.countryFlag);
        // }

    }

    @Override
    public int getItemCount() {
        return filtereddataList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    filtereddataList = dataList;
                } else {
                    ArrayList<CountryListModel> filteredList = new ArrayList<>();
                    for (CountryListModel row : dataList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getName().toLowerCase().contains(charString.toLowerCase()) || row.getCallingCode().contains(charSequence)) {
                            filteredList.add(row);
                        }
                    }

                    filtereddataList = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = filtereddataList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filtereddataList = (ArrayList<CountryListModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView countryName, countryCode;
        ImageView countryFlag;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            countryName = itemView.findViewById(R.id.country_name);
            countryCode = itemView.findViewById(R.id.countryCode);
            countryFlag = itemView.findViewById(R.id.flag_img);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        if (filtereddataList.size() > 0) {
                            String flag = filtereddataList.get(getAdapterPosition()).getFlag();
                            String code = filtereddataList.get(getAdapterPosition()).getCallingCode();
                            String name = filtereddataList.get(getAdapterPosition()).getName();
                            String countryId = filtereddataList.get(getAdapterPosition()).getId();
                            countrylistnavigator.clickedItem(flag, code, name, countryId);
                        } else {
                            countrylistnavigator.showMessage("Try Again Later");
                        }
                    } catch (ArrayIndexOutOfBoundsException e) {
                        countrylistnavigator.showMessage(e.getMessage());
                    }
                }
            });

        }
    }
}
