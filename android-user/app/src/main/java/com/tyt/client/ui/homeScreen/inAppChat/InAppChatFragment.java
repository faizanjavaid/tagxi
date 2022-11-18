package com.tyt.client.ui.homeScreen.inAppChat;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.library.baseAdapters.BR;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.tyt.client.R;
import com.tyt.client.databinding.FragmentInAppChatBinding;
import com.tyt.client.retro.base.BaseResponse;
import com.tyt.client.retro.responsemodel.ProfileModel;
import com.tyt.client.ui.base.BaseActivity;
import com.tyt.client.ui.base.BaseFragment;
import com.tyt.client.ui.homeScreen.HomeAct;
import com.tyt.client.ui.homeScreen.inAppChat.chathistory.ChatHistory;
import com.tyt.client.ui.homeScreen.inAppChat.chathistory.Datum;
import com.tyt.client.ui.homeScreen.inAppChat.chatmodel.Chat;
import com.tyt.client.utilz.SharedPrefence;


import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Fragment which is used to Make Chat inside of the App
* */

public class InAppChatFragment extends BaseFragment<FragmentInAppChatBinding, AppChatViewModel> implements AppChatNavigator {
    public static final String TAG = "InAppChatFragment";
    @Inject
    SharedPrefence sharedPrefence;
    Chat chat;
    BaseActivity context;
    private static String ph;
    public static ProfileModel onTripReq;
    boolean notify = false;
    @Inject
    AppChatViewModel appChatViewModel;

    FragmentInAppChatBinding inAppChatBinding;
    AppChatAdapter chatAdapter;
    List<Chat> chatList = new ArrayList<>();
    ;

    @Override
    public AppChatViewModel getViewModel() {
        return appChatViewModel;
    }

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_in_app_chat;
    }

    public InAppChatFragment() {
        // Required empty public constructor
    }

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            Log.d("xxxTAG", "onMove() called with: recyclerView = [" + recyclerView + "], viewHolder = [" + viewHolder + "], target = [" + target + "]");
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            Log.d("xxxTAG", "onSwiped() called with: viewHolder = [" + viewHolder + "], direction = [" + direction + "]");
            Snackbar.make(inAppChatBinding.CORLayoutChats.getRootView(), "Message Deleted Successfully", Snackbar.LENGTH_SHORT).show();

            chatList.remove(viewHolder.getAdapterPosition());
            chatAdapter.notifyDataSetChanged();
        }
    };

    public static InAppChatFragment newInstance(String phone) {
        ph = phone;
        return new InAppChatFragment();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        inAppChatBinding = getViewDataBinding();
        appChatViewModel.setNavigator(this);
        appChatViewModel.SmsHistoryApiCall();
        getBaseAct().setSupportActionBar(inAppChatBinding.mainToolbar);
        getBaseAct().getSupportActionBar().setTitle("In AppChat");
        getBaseAct().getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        inAppChatBinding.smschatLayout.recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);

        inAppChatBinding.smschatLayout.recyclerView.setLayoutManager(linearLayoutManager);
        chatAdapter = new AppChatAdapter(getBaseAct(), chatList, "");
        new ItemTouchHelper(simpleCallback).attachToRecyclerView(inAppChatBinding.smschatLayout.recyclerView);
        inAppChatBinding.smschatLayout.recyclerView.setAdapter(chatAdapter);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("--InAppChatFrTAG", "onCreate: ");
        this.context = getBaseAct();
        setHasOptionsMenu(true);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (getActivity().getSupportFragmentManager().findFragmentByTag(InAppChatFragment.TAG) != null) {
            switch (item.getItemId()) {
                case R.id.call:
                    Toast.makeText(context, "Calling....", Toast.LENGTH_SHORT).show();
                    Intent callIntent = new Intent(Intent.ACTION_DIAL);
                    callIntent.setData(Uri.parse("tel:" + ph));
                    getBaseAct().getViewDataBinding().getRoot().getContext().startActivity(callIntent);
                    break;
                default:
                    ((HomeAct) getActivity()).ShowHideBar(false);
                    ((HomeAct) getActivity()).onBackPressed();
                    break;
            }

        } else {
            Log.e("elkfjefjke", "lejfioe");
        }
        return true;

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {

        super.onCreateOptionsMenu(menu, inflater);
        Log.d("--InAppChatFrTAG", "onCreateOptionsMenu: ");
        inflater.inflate(R.menu.inaachat_menu, menu);

    }

    @Override
    public void onPrepareOptionsMenu(@NonNull @NotNull Menu menu) {
        menu.clear();
        MenuInflater inflater=this.context.getMenuInflater();
        inflater.inflate(R.menu.inaachat_menu,menu);
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public BaseActivity getBaseAct() {
        return getBaseActivity() != null ? getBaseActivity() : (BaseActivity) getActivity();
    }


    @Override
    public void onSmsChat() {
        notify = true;
        String msg = inAppChatBinding.smschatLayout.textSend.getText().toString();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
        Calendar cal = Calendar.getInstance();
        String time = dateFormat.format(cal.getTime());

        if (!msg.equals("")) {
            String req_id = sharedPrefence.Getvalue(SharedPrefence.REQUEST_ID);
            chat = new Chat("send", req_id, msg, true, time);
            chatList.add(chat);
            chat.setList(chatList);
            appChatViewModel.SmsApiCall(sharedPrefence.Getvalue(SharedPrefence.REQUEST_ID), msg, time);
        } else {

            new Handler(Looper.getMainLooper()).post(() -> {
//                Toast.makeText(getBaseAct(), "You can't send empty message", Toast.LENGTH_SHORT).show();
                Snackbar.make(inAppChatBinding.CORLayoutChats.getRootView(), "You can't send empty message", Snackbar.LENGTH_SHORT).show();
            });

        }
        inAppChatBinding.smschatLayout.textSend.setText("");
    }

    @Override
    public void onSmsChatShow(boolean isSuccess, String successMsg, BaseResponse response) {

        if (isSuccess) {
            switch (successMsg) {
                case "message_sent_successfully":
                    Snackbar.make(inAppChatBinding.CORLayoutChats.getRootView(), "Message Sent Successfully", Snackbar.LENGTH_SHORT).show();
                    break;

                case "chats_listed":

                    String strJson = new Gson().toJson(response.data);
                    Log.d("xxxxTAG", "chats_listed: " + strJson);
                    ArrayList<Datum> myDat = new ArrayList<>();
                    JsonElement json = JsonParser.parseString(strJson);
                    if (json.isJsonArray()) {
                        JsonArray jsonArray = json.getAsJsonArray();
                        Datum datum;
                        for (int i = 0; i < jsonArray.getAsJsonArray().size(); i++) {
                            JsonElement data = jsonArray.getAsJsonArray().get(i);
                            if (data.isJsonObject()) {
                                //Log.d("xxxTAG2", "onSmsChatShow: isJsonObject=="+data.getAsJsonObject());
                                datum = new Gson().fromJson(data, Datum.class);
                                myDat.add(datum);
                                chatList.add(new Chat(datum.getMessageStatus(), datum.getRequestId(), datum.getMessage(), datum.getSeen() == 1, datum.getConvertedCreatedAt()));
                            } else if (data.isJsonArray()) {
                                //Log.d("xxxTAG1", "onSmsChatShow: isJsonArray =="+data.getAsJsonArray());
                            }
                        }
                        new ChatHistory().setData(myDat);
                    } else if (json.isJsonObject()) {
                        // Log.d("xxxTAG2", "onSmsChatShow: isJsonObject" + json.getAsJsonObject());
                    }

                    break;
            }

            chatAdapter.smsNotify(chatList);
            for (int i = 0; i < chatList.size(); i++) {
                Log.d("xxxTAG", "onSmsChatShow: index=" + i + " " + "message =" + chatList.get(i).getMessage() + " status =" + chatList.get(i).getMessageStatus() + " chatAdapter.getItemCount=" + chatAdapter.getItemCount());
            }
            if (chatList.size() > 0) {
                inAppChatBinding.smschatLayout.recyclerView.post(new Runnable() {
                    @Override
                    public void run() {
                        // Call smooth scroll
                        //inAppChatBinding.smschatLayout.recyclerView.scrollTo(0,20);
                        inAppChatBinding.smschatLayout.recyclerView.smoothScrollToPosition(chatAdapter.getItemCount() - 1);
                    }
                });
            }
        }

    }

    @Override
    public void onReceivedChatShow(JsonObject jsonObject) {
        ChatHistory chatHistory = new Gson().fromJson(jsonObject, ChatHistory.class);
        chatList.clear();
        for (int i = 0; i < chatHistory.getData().size(); i++) {
            Datum datum = chatHistory.getData().get(i);
            if (datum != null) {
                chatList.add(new Chat(datum.getMessageStatus(), datum.getRequestId(), datum.getMessage(), datum.getSeen() == 1, datum.getConvertedCreatedAt()));

            }
        }


        chatAdapter.smsNotify(chatList);
        for (int i = 0; i < chatList.size(); i++) {
            Log.d("xxxTAG", "onReceivedChatShow: index=" + i + " " + "message =" + chatList.get(i).getMessage() + " status =" + chatList.get(i).getMessageStatus() + " chatAdapter.getItemCount=" + chatAdapter.getItemCount());
        }
        if (chatList.size() > 0) {
            inAppChatBinding.smschatLayout.recyclerView.post(new Runnable() {
                @Override
                public void run() {
                    // Call smooth scroll
                    // inAppChatBinding.smschatLayout.recyclerView.scrollTo(0,20);
                    inAppChatBinding.smschatLayout.recyclerView.smoothScrollToPosition(chatAdapter.getItemCount() - 1);
                }
            });
        }
    }

}