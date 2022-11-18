package com.nplus.countrylist;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pandiyan on 26/10/2016.
 */
public class DialogCountryAdapter extends BaseAdapter implements Filterable {

    Context ctx = null;
    List<Country> listarray = null;
    List<Country> Fullarray = null;
    private LayoutInflater mInflater = null;

    public DialogCountryAdapter(Activity activty, List<Country> list) {
        this.ctx = activty;
        mInflater = activty.getLayoutInflater();
        this.listarray = list;
        this.Fullarray = list;
    }

    @Override
    public int getCount() {
        return listarray!=null?listarray.size():0;
    }

    @Override
    public Object getItem(int arg0) {
        return listarray.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup arg2) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_country_drop, null);
            holder = new ViewHolder();
            holder.mImageView = convertView.findViewById(R.id.image);
            holder.mNameView = convertView.findViewById(R.id.country_name);
            holder.mCodeView = convertView.findViewById(R.id.country_code);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Country country = (Country) getItem(position);
        if (country != null) {
            holder.mNameView.setText(country.getName());
            holder.mCodeView.setText(country.getCountryCodeStr());
            holder.mImageView.setImageResource(country.getResId());
            holder.mNameView.setTag(country);
        }

        return convertView;
    }

    @Override
    public Filter getFilter() {
        return new ValueFilter();
    }

    private static class ViewHolder {
        public ImageView mImageView;
        public TextView mNameView;
        public TextView mCodeView;
    }


    private class ValueFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            if (constraint != null && constraint.toString().trim().length() > 0) {
                ArrayList<Country> filterList = new ArrayList<Country>();
                for (int i = 0; i < Fullarray.size(); i++) {
                    if ((Fullarray.get(i).getName().toUpperCase()).startsWith(constraint.toString().toUpperCase())) {
                        Country country = Fullarray.get(i);
                        filterList.add(country);
                    }
                }
                results.count = filterList.size();
                results.values = filterList;
            } else {
                results.count = Fullarray.size();
                results.values = Fullarray;
            }
            return results;

        }

        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {
            listarray = (ArrayList<Country>) results.values;
            notifyDataSetChanged();
        }

    }
}