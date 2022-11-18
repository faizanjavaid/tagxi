package com.tyt.client.ui.homeScreen.inAppChat;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseUser;
import com.tyt.client.R;
import com.tyt.client.ui.homeScreen.inAppChat.chatmodel.Chat;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Adapter Class to hold the chat messages from user and driver
* */

public class AppChatAdapter extends RecyclerView.Adapter<AppChatAdapter.AppChatHolder> {
    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;
    Typeface MR, MRR;
    private Context mContext;
    private List<Chat> mChat;
    private String imageurl;
    View mRootView;
    private FirebaseUser fuser;

    public AppChatAdapter(Context mContext, List<Chat> mChat, String imageurl) {
        this.mChat = mChat;
        this.mContext = mContext;
        this.imageurl = imageurl;

        MRR = Typeface.createFromAsset(mContext.getAssets(), "fonts/Poppins.ttf");
        MR = Typeface.createFromAsset(mContext.getAssets(), "fonts/Padauk.ttf");
    }

    @NonNull
    @Override
    public AppChatHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewDataBinding mViewDataBinding;
        Log.d("--AppChatAdapterTAG", "onCreateViewHolder() called with:  viewType = [" + viewType + "]");
        if (viewType == MSG_TYPE_RIGHT) {
              View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_right, parent, false);
            return new AppChatHolder(view);
        } else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_left, parent, false);
            return new AppChatHolder(view);  }
    }

    @Override
    public void onBindViewHolder(@NonNull AppChatHolder holder, int position) {
        Log.d("--AppChatAdapterTAG", "onBindViewHolder() called with: holder = [" + mChat.get(position) + "], position = [" + position + "]");
        Chat chat = mChat.get(position);
        holder.show_message.setTypeface(MRR);
        holder.time_tv.setTypeface(MRR);
        holder.show_message.setText(chat.getMessage());

        holder.time_tv.setText(holder.convertTime(chat.getTime()));
    }

    @Override
    public int getItemCount() {
       return  (null != mChat ? mChat.size() : 0);
    }

    public void smsNotify(List<Chat> chatList){
        this.mChat=chatList;
        notifyDataSetChanged();

    }

    public void smsDelete(List<Chat> chatList,int position){
        if(position!=-1 && position<mChat.size())
        {
            mChat.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, getItemCount());
        }
    }
    public static class AppChatHolder extends RecyclerView.ViewHolder {
        TextView show_message, time_tv;

        public AppChatHolder(@NonNull View itemView) {
            super(itemView);
            show_message = itemView.findViewById(R.id.show_message);
            time_tv = itemView.findViewById(R.id.time_tv);
        }

        public String convertTime(String time){
            Log.d("--TAG", "convertTime: before ="+time);
            try{
                SimpleDateFormat formatter = new SimpleDateFormat("h:mm a");
                String dateString = formatter.format(new Date(Long.parseLong(time)));
                Log.d("--TAG", "convertTime: after ="+time);
                return dateString;
            }catch (Exception e){
               return dateFormate(time);
            }

        }

        public String dateFormate(String date){
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
            SimpleDateFormat outputFormat = new SimpleDateFormat("h:mm a");
            try { Date parsedDate = inputFormat.parse(date);
                String formattedDate = outputFormat.format(parsedDate);
                Log.d("--TAG", "dateFormate: after ="+formattedDate);
                return formattedDate;
            }
            catch (Exception e){

            }
            return date;
        }
    }



    public static boolean isValidFormat(String format, String value) {
        Date date = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            date = sdf.parse(value);
            if (!value.equals(sdf.format(date))) {
                date = null;
            }
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        return date != null;
    }
    @Override
    public int getItemViewType(int position) {
        if (mChat.get(position).getMessageStatus().equalsIgnoreCase("send")){
            return MSG_TYPE_RIGHT;
        } else {
            return MSG_TYPE_LEFT;
        }
    }
}
