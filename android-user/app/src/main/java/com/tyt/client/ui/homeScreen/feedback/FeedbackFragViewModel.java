package com.tyt.client.ui.homeScreen.feedback;

import android.net.Uri;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableFloat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.tyt.client.R;
import com.tyt.client.app.MyApp;
import com.tyt.client.retro.GitHubService;
import com.tyt.client.retro.base.BaseNetwork;
import com.tyt.client.retro.base.BaseResponse;
import com.tyt.client.retro.responsemodel.ProfileModel;
import com.tyt.client.utilz.Constants;
import com.tyt.client.utilz.SharedPrefence;
import com.tyt.client.utilz.exception.CustomException;

import java.util.HashMap;
import java.util.Objects;

/**
 * Created by Mahi in 2021.
 */

public class FeedbackFragViewModel extends BaseNetwork<BaseResponse, FeedbackNavigator> {
    private static final String TAG = "AddCardViewModel";

    public ObservableField<String> feedBack = new ObservableField<>("");

    public ObservableField<String> DriverName = new ObservableField<>();
    public ObservableField<String> DriverPic = new ObservableField<>();
    public ObservableField<String> REQID = new ObservableField<>();

    public ObservableFloat userReview = new ObservableFloat();
    HashMap<String, String> hashMap = new HashMap<>();


    public ObservableBoolean enablefeed_btn = new ObservableBoolean(true);

    public FeedbackFragViewModel(GitHubService gitHubService, SharedPrefence sharedPrefence, Gson gson) {
        super(gitHubService, sharedPrefence, gson);
    }


    /**
     * called when API call is successful
     **/
    @Override
    public void onSuccessfulApi(long taskId, BaseResponse response) {
        setIsLoading(false);
        enablefeed_btn.set(true);
        if (response.message.equalsIgnoreCase("rated_successfully")) {
            getmNavigator().openHomePage();
        }
    }

    /**
     * called when API call fails
     **/
    @Override
    public void onFailureApi(long taskId, CustomException e) {
        setIsLoading(false);
        enablefeed_btn.set(true);
        getmNavigator().showMessage(e.getMessage());
    }

    /**
     * adds client_id, client_token to {@link HashMap} used for API calls
     **/
    @Override
    public HashMap<String, String> getMap() {
        return null;
    }


    public void onUserFeedback(Editable e) {
        feedBack.set(e.toString());
    }


    public void onClickSubmitFeedback(View v) {

        if (enablefeed_btn.get()) {
            enablefeed_btn.set(false);
            if (getmNavigator().isNetworkConnected()) {
                setIsLoading(true);
                hashMap.clear();
                Log.d(TAG, "onClickSubmitFeedback: ReqId="+REQID.get());

                hashMap.put(Constants.NetworkParameters.request_id, sharedPrefence.Getvalue(SharedPrefence.REQUEST_ID));

                if (userReview.get() > 0) {
                    hashMap.put(Constants.NetworkParameters.Rating, "" + userReview.get());
                } else {
                    getmNavigator().showMessage("Please rate a driver");
                    return;
                }
                if (!Objects.requireNonNull(feedBack.get()).isEmpty())
                    hashMap.put(Constants.NetworkParameters.Comment, feedBack.get());
                RateDriver(hashMap);
            } else {
                setIsLoading(false);
                getmNavigator().showNetworkMessage();
            }
        }
    }

    @BindingAdapter("app:feeddriverImg")
    public static void loadUserImg(ImageView billimageView, String fuserimgurl) {
        if (fuserimgurl != null) {
            Glide.with(billimageView.getContext()).load(Uri.parse(fuserimgurl)).into(billimageView);
        }
    }

    public void setValues(ProfileModel tripResponse, ImageView imagevw) {
        REQID.set(tripResponse.getOnTripRequest().getData().getId());
        sharedPrefence.savevalue(SharedPrefence.REQUEST_ID,tripResponse.getOnTripRequest().getData().getId());
        Log.d(TAG, "setValues: REQID="+tripResponse.getOnTripRequest().getData().getId());
        Log.d(TAG, "setValues: REQID="+sharedPrefence.Getvalue(SharedPrefence.REQUEST_ID));
        DriverName.set(tripResponse.getOnTripRequest().getData().getDriverDetail().getData().getName());
        DriverPic.set(tripResponse.getOnTripRequest().getData().getDriverDetail().getData().getProfilePicture());

        Glide.with(MyApp.getmContext()).load(DriverPic).
                apply(RequestOptions.circleCropTransform().error(R.drawable.profile_place_holder).
                        placeholder(R.drawable.profile_place_holder)).into(imagevw);
    }


    public void setValues(BaseResponse tripResponse, ImageView imagevw) {

        REQID.set(tripResponse.result.getData().getId());

        DriverName.set(tripResponse.result.getData().getUserDetail().getData().getName());
        DriverPic.set(tripResponse.result.getData().getUserDetail().getData().getProfilePicture());

        Glide.with(MyApp.getmContext()).load(DriverPic).
                apply(RequestOptions.circleCropTransform().error(R.drawable.profile_place_holder).
                        placeholder(R.drawable.profile_place_holder)).into(imagevw);
    }
}
