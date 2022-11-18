package com.tyt.driver.ui.loginOrSign;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import com.google.android.material.textview.MaterialTextView;
import com.tyt.driver.R;

import java.util.List;

public class LanguageAdapter extends ArrayAdapter<ChooseLanguage<Integer,String>> {
    private static final String TAG = "LanguageAdapter";
    List<ChooseLanguage<Integer,String>> objects;
    Context context;
    SetTextListener setTextListener;
    public LanguageAdapter(@NonNull Context context, int resource, @NonNull List<ChooseLanguage<Integer,String>> objects) {
        super(context, resource, objects);
        for (ChooseLanguage j:objects
             ) {
            Log.d(TAG, "LanguageAdapter: "+j.getLanguageName());
        }
   this.context=context;
   this.setTextListener= (SetTextListener) context;
   this.objects=objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Log.d(TAG, "getView: "+position+" view ="+convertView);

        return initView(position, convertView, parent);
    }

    public void refresh(List<ChooseLanguage<Integer,String>> objects){
        this.objects=objects;
        notifyDataSetChanged();
    }
    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Log.d(TAG, "getDropDownView: "+position+" view ="+convertView);

        return initView(position, convertView, parent);
    }

    @Override
    public int getItemViewType(int position) {
        return objects.size();
    }

    private View initView(int position, View convertView,
                          ViewGroup parent)
    {

        // It is used to set our custom view.
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_of_language, parent, false);
        }
        ChooseLanguage<Integer,String> currentItem = (ChooseLanguage<Integer,String>) objects.get(position);
        Log.d(TAG, "initView:getLanguageName "+objects.get(position).getLanguageName());
        Log.d(TAG, "initView: currentItem "+currentItem.getLanguageName());
        MaterialTextView textViewName = convertView.findViewById(R.id.languagetext);
        AppCompatImageView iconLang = convertView.findViewById(R.id.iconLang);
        // It is used the name to the TextView when the
        // current item is not null.
        if (currentItem != null) {
            textViewName.setText(currentItem.getLanguageName());
            iconLang.setImageResource(currentItem.getIcon());
//            convertView.setOnClickListener(v->{
//                setTextListener.onSetTextClickListener(currentItem.getIcon(),currentItem.getLanguageName());
//            });

        }



        return convertView;
    }

    interface SetTextListener{
        void onSetTextClickListener(int res,String name);
    }
}
