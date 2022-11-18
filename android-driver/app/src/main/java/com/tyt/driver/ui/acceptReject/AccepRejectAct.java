package com.tyt.driver.ui.acceptReject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.animation.Animation;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.tyt.driver.BR;
import com.tyt.driver.R;
import com.tyt.driver.databinding.AcceptRejectBinding;
import com.tyt.driver.retro.base.BaseResponse;
import com.tyt.driver.retro.responsemodel.MetaRequest;
import com.tyt.driver.retro.responsemodel.ProfileModel;
import com.tyt.driver.retro.responsemodel.tripRequest.ReqData;
import com.tyt.driver.ui.base.BaseActivity;
import com.tyt.driver.ui.homeScreen.HomeAct;
import com.tyt.driver.utilz.CommonUtils;
import com.tyt.driver.utilz.Constants;
import com.tyt.driver.utilz.SharedPrefence;

import javax.inject.Inject;

/**
 * Created by naveen on 8/24/17.
 */

public class AccepRejectAct extends BaseActivity<AcceptRejectBinding, AcceptRejectViewModel> implements Animation.AnimationListener, AcceptRejectNavigator, OnMapReadyCallback {
    public final static String TAG = "xxxAcceptRejAct";

    @Inject
    SharedPrefence sharedPrefence;
    @Inject
    AcceptRejectViewModel mViewMOdel;
    AcceptRejectBinding mBinding;
    CountDownTimer mCountDownTimer;
    int i = 0;
    MediaPlayer mMediaPlayer;
    String reqestData;
    Animation animBlink;
    private Handler pulseHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG, "AcceptRejectSc onStart Called");
        mBinding = getViewDataBinding();
        mViewMOdel.setNavigator(this);

        sharedPrefence.saveBoolean(SharedPrefence.AcceptRejProc, true);

        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        audioManager.adjustVolume(AudioManager.ADJUST_RAISE, AudioManager.FLAG_PLAY_SOUND);

       /* // load the animation
        animBlink = AnimationUtils.loadAnimation(this,
                R.anim.blink);

        // set animation listener
        animBlink.setAnimationListener(this);*/

        if (getIntent() != null) {
            if (getIntent().getStringExtra(Constants.IntentExtras.ACCEPT_REJECT_DATA) != null) {
                reqestData = getIntent().getStringExtra(Constants.IntentExtras.ACCEPT_REJECT_DATA);
                BaseResponse response = CommonUtils.getSingleObject(reqestData, BaseResponse.class);
                if (response != null) {
                    ReqData req = response.getResult().getData();
                    mViewMOdel.setDetails(req, this, mBinding);
                }
            } else if (getIntent().getStringExtra(Constants.IntentExtras.MetAcceptRejData) != null) {
                reqestData = getIntent().getStringExtra(Constants.IntentExtras.MetAcceptRejData);
                ProfileModel response = CommonUtils.getSingleObject(reqestData, ProfileModel.class);
                Log.e("reponse---", "reponse---" + new Gson().toJson(response));
                if (response != null) {
                    MetaRequest req = response.getMetaRequest();
                    mViewMOdel.setDetails(req.getData(), this, mBinding);
                }
            }

        }


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null)
            mapFragment.getMapAsync(this);

        mBinding.progressbar.setProgress(i);
        mCountDownTimer = new CountDownTimer(30000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                if (!mViewMOdel.stopTimer.get()) {

                    if (mMediaPlayer == null) {
                        mMediaPlayer = MediaPlayer.create(AccepRejectAct.this, R.raw.beep);
                        if (!mMediaPlayer.isPlaying()) {
                            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                            mMediaPlayer.setLooping(true);
                            mMediaPlayer.start();
                        }
                    }

                    Log.v("xxxAcceptReject", "Tick of Progress" + i + millisUntilFinished);
                    i++;
                    mBinding.progressbar.setProgress(i * 100 / (30000 / 1000));
                    int millisRemaining = (int) (millisUntilFinished / 1000);
                    mViewMOdel.counterText.set("" + millisRemaining);

                    pulseHandler = new Handler(Looper.getMainLooper());
                    pulseHandler.postDelayed(pulseAnimRunnable, 200);
                    pulseAnimRunnable.run();
                }

            }

            @Override
            public void onFinish() {
                if (!mViewMOdel.stopTimer.get()) {
                    Log.d("xxxAcceptRejectAct", "onFinish: ");
                    if (mMediaPlayer != null) {
                        mMediaPlayer.stop();
                        mMediaPlayer.reset();
                    }
                    i++;
                    mBinding.progressbar.setProgress(100);
                    mViewMOdel.RejectApi();
                }
            }
        };
        mCountDownTimer.start();
    }

    Runnable pulseAnimRunnable = new Runnable() {
        @Override
        public void run() {
            mBinding.blinkTimeImge.animate().scaleX(4.0f).scaleY(4.0f).alpha(0f).setDuration(200).withEndAction(pulseAnimRunnable1);
            mBinding.blinkTimeImage2.animate().scaleX(4.0f).scaleY(4.0f).alpha(0f).setDuration(100).withEndAction(pulseAnimRunnable1);
        }
    };

    Runnable pulseAnimRunnable1 = new Runnable() {
        @Override
        public void run() {
            mBinding.blinkTimeImge.animate().scaleX(1f).scaleY(1f).alpha(1f);
            mBinding.blinkTimeImage2.animate().scaleX(1f).scaleY(1f).alpha(1f);
        }
    };

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {

    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }


    // Receieve the broadcast if the user cancel the trip
    BroadcastReceiver cancelReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e(TAG, "cancel receiver method called");
            finishAct();
        }
    };


    @Override
    public BaseActivity getBaseAct() {
        return this;
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e(TAG, "AcceptRejectSc onStart Called");
        LocalBroadcastManager.getInstance(this).registerReceiver(cancelReceiver, new IntentFilter(Constants.BroadcastsActions.CANCEL_RECEIVER));

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e(TAG, "AcceptRejectSc onPause Called");
        if (mMediaPlayer != null) {
            if (mMediaPlayer.isPlaying()) {
                mMediaPlayer.stop();
            }
            mMediaPlayer.reset();
            Log.d("xxxTAG", "onPause: ");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG, "AcceptRejectSc onResume Called");
        mMediaPlayer = null;
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e(TAG, "AcceptRejectSc onStop Called");
        sharedPrefence.saveBoolean(SharedPrefence.AcceptRejProc, false);
        if (mMediaPlayer != null) {
            Log.d("xxxTAG", "onStop: ");
            mMediaPlayer.stop();
            mMediaPlayer.reset();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "AcceptRejectSc onDestroy Called");
        sharedPrefence.saveBoolean(SharedPrefence.AcceptRejProc, false);
        Log.d("xxxTAG", "onDestroy:1 ");
        if (mMediaPlayer != null) {
            if (mMediaPlayer.isPlaying()) {
                mMediaPlayer.stop();
                Log.d("xxxTAG", "onDestroy:2 ");
            }
            mMediaPlayer.reset();
        }
    }

    @Override
    public void finishAct() {
        Log.d("xxxTAG", "finishAct: ");
        sharedPrefence.saveBoolean(SharedPrefence.AcceptRejProc, false);
        if (mMediaPlayer != null) {
            if (mMediaPlayer.isPlaying()) {
                mMediaPlayer.stop();
                mMediaPlayer.reset();
            }
        }
        pulseHandler.removeCallbacks(pulseAnimRunnable);
        mMediaPlayer = null;
        startActivity(new Intent(this, HomeAct.class));
        //finish();
        finishAndRemoveTask();
    }

    @Override
    public AcceptRejectViewModel getViewModel() {
        return mViewMOdel;
    }

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.accept_reject;
    }

    @Override
    public SharedPrefence getSharedPrefence() {
        return sharedPrefence;
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        Log.d("", "onMapReady:->290 ");
        mViewMOdel.googleMap = googleMap;
        if (mViewMOdel.pickLat != null && mViewMOdel.pickLng != null && mViewMOdel.pickLat.get() != null && mViewMOdel.pickLng.get() != null) {
            googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(mViewMOdel.pickLat.get(), mViewMOdel.pickLng.get()))
                    .title(getTranslatedString(R.string.txt_my_location))
                    .anchor(0.5f, 0.5f)
                    .icon(CommonUtils.getBitmapDescriptor(this, R.drawable.pick_marker, Constants.Pick_Marker)));

            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(mViewMOdel.pickLat.get(), mViewMOdel.pickLng.get()), 16));
        }


        googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.uber_map));
        googleMap.setTrafficEnabled(true);
    }


}