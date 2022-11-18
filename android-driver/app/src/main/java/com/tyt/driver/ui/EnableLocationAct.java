package com.tyt.driver.ui;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tyt.driver.R;
import com.tyt.driver.app.MyApp;
import com.tyt.driver.retro.responsemodel.TranslationModel;
import com.tyt.driver.ui.homeScreen.HomeAct;
import com.tyt.driver.ui.login.LoginAct;
import com.tyt.driver.ui.loginOrSign.LoginOrSignAct;
import com.tyt.driver.utilz.CommonUtils;
import com.tyt.driver.utilz.Constants;
import com.tyt.driver.utilz.SharedPrefence;

public class EnableLocationAct extends AppCompatActivity {

    RelativeLayout enableLoc;
    TranslationModel translationModel;

    SharedPreferences sharedPreferences;

    TextView trackHint,trackHead,enableLocTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enable_location);
       enableLoc = findViewById(R.id.enableloc);

      /*   trackHead = findViewById(R.id.track_loc);
        trackHint = findViewById(R.id.track_hint);
        enableLocTxt = findViewById(R.id.enableLocTxt);*/

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        if (!CommonUtils.IsEmpty(sharedPreferences.getString(SharedPrefence.LANGUANGE,""))) {
            translationModel = new Gson().fromJson(sharedPreferences.getString(sharedPreferences.getString(SharedPrefence.LANGUANGE,""),""), TranslationModel.class);
        }

      /*  trackHead.setText(translationModel.txt_track_loc);
        trackHint.setText(translationModel.txt_live_ride);
        enableLocTxt.setText(translationModel.txt_enable_loc);*/

    }

    @Override
    protected void onResume() {
        super.onResume();
        enableLoc.setOnClickListener(v -> {
            if (CommonUtils.isGpscheck(EnableLocationAct.this)) {
                RedirectPages();
            } else CommonUtils.Show_enableLoc_Dialog(EnableLocationAct.this);
        });
    }

    private void RedirectPages() {
        if ((sharedPreferences.getString(SharedPrefence.LANGUANGE,"") == null || sharedPreferences.getString(SharedPrefence.LANGUANGE,"").isEmpty() || sharedPreferences.getString(SharedPrefence.LANGUANGES,"") == null || sharedPreferences.getString(SharedPrefence.LANGUANGES,"").isEmpty())) {
            startActivity(new Intent(EnableLocationAct.this, LoginOrSignAct.class));
        } else if (CommonUtils.IsEmpty(sharedPreferences.getString(SharedPrefence.AccessToken,""))) {
            startActivity(new Intent(EnableLocationAct.this, LoginAct.class));
        }else startActivity(new Intent(EnableLocationAct.this, HomeAct.class));
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_LOCATION_DIALOG) {
            if (resultCode != RESULT_CANCELED) {
                if (resultCode == RESULT_OK) {
                    RedirectPages();
                }
            }else Toast.makeText(this, "Enable location to Move Further", Toast.LENGTH_SHORT).show();
        }
    }

}
