package com.tyt.driver.ui.homeScreen.mapscrn.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.tyt.driver.R;
import com.tyt.driver.retro.responsemodel.Type;
import com.tyt.driver.ui.homeScreen.makeTrip.MakeTripNavigator;

import java.util.List;

public class CarsTypesAdapter extends RecyclerView.Adapter<CarsTypesAdapter.ViewHolder> {

    Context context;
    MakeTripNavigator makeTripNavigator;
    List<Type> typeList;
    int row_index = -1;

    public CarsTypesAdapter(Context context, List<Type> typeList, MakeTripNavigator makeTripNavigator) {
        this.makeTripNavigator = makeTripNavigator;
        this.typeList = typeList;
        this.context = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.child_cartypes, viewGroup, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        viewHolder.typeName.setText(typeList.get(i).getName());
        Glide.with(context).load(typeList.get(i).getIcon()).into(viewHolder.typeImg);

        viewHolder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                row_index = i;
                notifyDataSetChanged();
            }
        });

        if (row_index == i) {
            viewHolder.view.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
            makeTripNavigator.clickedTypes(typeList.get(i));
        } else {
            viewHolder.view.setBackgroundColor(context.getResources().getColor(R.color.clr_white));
        }

    }

    @Override
    public int getItemCount() {
        return typeList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView typeName;
        ImageView typeImg;
        RelativeLayout relativeLayout;
        View view;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            typeName = itemView.findViewById(R.id.model_name);
            typeImg = itemView.findViewById(R.id.types_img);
            relativeLayout = itemView.findViewById(R.id.main_lay);
            view = itemView.findViewById(R.id.view);
        }
    }
}
