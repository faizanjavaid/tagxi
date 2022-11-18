package com.tyt.client.ui.homeScreen.faq;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tyt.client.R;
import com.tyt.client.retro.responsemodel.Faqmodel;

import java.util.List;

/**
 * Adapter class which is used to show the Faq questions with answers.
 *
* */



public class FaqAdapter extends RecyclerView.Adapter<FaqAdapter.ViewHolder> {

    List<Faqmodel> faqmodels;
    Context context;
    FaqNavigator faqNavigator;
    boolean isUpArrow = false;

    public FaqAdapter(Context context, List<Faqmodel> faqmodels, FaqNavigator faqNavigator) {
        this.context = context;
        this.faqmodels = faqmodels;
        this.faqNavigator = faqNavigator;
    }


    @NonNull
    @Override
    public FaqAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.faq_item, viewGroup, false);

        return new FaqAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final FaqAdapter.ViewHolder viewHolder, final int i) {
        viewHolder.question.setText(faqmodels.get(i).getQuestion());
        viewHolder.answer.setText(faqmodels.get(i).getAnswer());

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isUpArrow) {
                    isUpArrow = true;
                    viewHolder.answer.setVisibility(View.VISIBLE);
                    viewHolder.view.setVisibility(View.VISIBLE);
                    viewHolder.updownImg.setImageDrawable(context.getResources().getDrawable(R.drawable.up_arrow));
                } else {
                    isUpArrow = false;
                    viewHolder.answer.setVisibility(View.GONE);
                    viewHolder.view.setVisibility(View.GONE);
                    viewHolder.updownImg.setImageDrawable(context.getResources().getDrawable(R.drawable.down_arrow));
                }
            }
        });
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView question, answer;
        ImageView updownImg;
        View view;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            question = itemView.findViewById(R.id.question);
            answer = itemView.findViewById(R.id.answer);
            updownImg = itemView.findViewById(R.id.img);
            view = itemView.findViewById(R.id.vw);
        }
    }

    @Override
    public int getItemCount() {
        return faqmodels.size();
    }
}

