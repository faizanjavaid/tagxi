package com.tyt.driver.ui.homeScreen.sos;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tyt.driver.R;
import com.tyt.driver.retro.responsemodel.SOSModel;

import java.util.List;


public class SosAdapter extends RecyclerView.Adapter<SosAdapter.ViewHolder> {

    List<SOSModel> sosModels;
    Context context;
    SosNavigator sosNavigator;
    boolean isUpArrow = false;

    public SosAdapter(Context context, List<SOSModel> faqmodels, SosNavigator sosNavigator) {
        this.context = context;
        this.sosModels = faqmodels;
        this.sosNavigator = sosNavigator;
    }


    @NonNull
    @Override
    public SosAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.sos_item, viewGroup, false);

        return new SosAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final SosAdapter.ViewHolder viewHolder, final int i) {
        viewHolder.number.setText(sosModels.get(i).getNumber());
        viewHolder.name.setText(sosModels.get(i).getName());

        if (!sosModels.get(i).getType().equalsIgnoreCase("admin"))
            viewHolder.delete.setVisibility(View.VISIBLE);
        else viewHolder.delete.setVisibility(View.GONE);

        viewHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sosNavigator.onClickDelete(sosModels.get(i));
            }
        });

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sosNavigator.onClickCall(sosModels.get(i));
            }
        });
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView number, name;
        ImageView delete;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            number = itemView.findViewById(R.id.number);
            name = itemView.findViewById(R.id.name);
            delete = itemView.findViewById(R.id.delete);
        }
    }

    @Override
    public int getItemCount() {
        return sosModels.size();
    }
}

