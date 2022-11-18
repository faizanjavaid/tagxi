package com.tyt.client.ui;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.tyt.client.R;
import com.tyt.client.ui.homeScreen.HomeAct;
import com.tyt.client.ui.login.LoginAct;
import com.tyt.client.ui.loginOrSign.LoginOrSignAct;
import com.tyt.client.utilz.CommonUtils;
import com.tyt.client.utilz.Constants;
import com.tyt.client.utilz.SharedPrefence;
/**
* It is Activity which is used to enable location if it is in off state.
* */

public class EnableLocationAct extends AppCompatActivity {

    RelativeLayout enableLoc;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enable_location);
        enableLoc = findViewById(R.id.enableloc);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

    }

    @Override
    protected void onResume() {
        super.onResume();
        enableLoc.setOnClickListener(v -> {
            if (CommonUtils.isGpscheck(EnableLocationAct.this)) {
                RedirectPages();
            } else  CommonUtils.Show_enableLoc_Dialog(EnableLocationAct.this);
        });
    }

    private void RedirectPages(){

        if ((sharedPreferences.getString(SharedPrefence.LANGUANGE,"") == null || sharedPreferences.getString(SharedPrefence.LANGUANGE,"").isEmpty() || sharedPreferences.getString(SharedPrefence.LANGUANGES,"") == null || sharedPreferences.getString(SharedPrefence.LANGUANGES,"").isEmpty())) {
            startActivity(new Intent(EnableLocationAct.this, LoginOrSignAct.class));
        } else if (CommonUtils.IsEmpty(sharedPreferences.getString(SharedPrefence.AccessToken,""))) {
            startActivity(new Intent(EnableLocationAct.this, LoginAct.class));
        }else startActivity(new Intent(EnableLocationAct.this, HomeAct.class));

    }

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
