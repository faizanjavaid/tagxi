package com.tyt.client.ui.homeScreen.profile;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.browser.customtabs.CustomTabsIntent;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.textfield.TextInputEditText;
import com.tyt.client.BR;
import com.tyt.client.R;
import com.tyt.client.databinding.ProfileFragLayBinding;
import com.tyt.client.ui.base.BaseActivity;
import com.tyt.client.ui.base.BaseFragment;
import com.tyt.client.ui.homeScreen.HomeAct;
import com.tyt.client.ui.homeScreen.faq.FaqFrag;
import com.tyt.client.ui.homeScreen.getRefferal.GetRefferalFrag;
import com.tyt.client.ui.homeScreen.makeComplaint.MakeCompFrag;
import com.tyt.client.ui.homeScreen.manageCard.ManageCardFrag;
import com.tyt.client.ui.homeScreen.mapFrag.MapFrag;
import com.tyt.client.ui.homeScreen.profile.profileEdit.ProfileEditFrag;
import com.tyt.client.ui.homeScreen.sos.SosFrag;
import com.tyt.client.ui.homeScreen.userHistory.HistoryFrag;
import com.tyt.client.ui.homeScreen.wallet.WalletFrag;
import com.tyt.client.ui.loginOrSign.LoginOrSignAct;
import com.tyt.client.utilz.Constants;
import com.tyt.client.utilz.SharedPrefence;

import javax.inject.Inject;

public class ProfileFrag extends BaseFragment<ProfileFragLayBinding, ProfileViewModel> implements ProfileNavigator {

    public static final String TAG = "ProfileFrag";
    @Inject
    public ProfileViewModel mViewModel;
    @Inject
    SharedPrefence sharedPrefence;
    public ProfileFragLayBinding mBinding;

    // TODO: Rename and change types and number of parameters
    public static ProfileFrag newInstance() {
        return new ProfileFrag();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBinding = getViewDataBinding();
        mViewModel.setNavigator(this);

        AboutApp(mBinding.tvAppVersion);
    }

    public void AboutApp(TextView v) {
        String version = "";
        try {
            Context context;
            context = v.getContext();
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            version = pInfo.versionName;
            sharedPrefence.savevalue(SharedPrefence.Appversion, version);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        v.setText("v " + version);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public ProfileViewModel getViewModel() {
        return mViewModel;
    }

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.profile_frag_lay;
    }

    /**
     * returns reference of {@link BaseActivity}
     **/
    @Override
    public BaseActivity getBaseAct() {
        return ((BaseActivity) getActivity());
    }

    @Override
    public void openEditProfile() {
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .disallowAddToBackStack()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .replace(R.id.fragment_frame, ProfileEditFrag.newInstance(), ProfileEditFrag.TAG)
                .commit();
    }

    Dialog dialog_password;

    public void opneChangePasswordAlert() {
      /*  if (dialog_password != null)
            if (dialog_password.isShowing()) {
                dialog_password.dismiss();
                dialog_password = null;
            }

        dialog_password = new Dialog(getActivity());
        dialog_password.setContentView(R.layout.change_password);

        final TextInputEditText pass = dialog_password.findViewById(R.id.pwd);
        final TextInputEditText confPass = dialog_password.findViewById(R.id.conf_pwd);
        final TextInputEditText OldPwd = dialog_password.findViewById(R.id.old_pwd);
        RelativeLayout submit = dialog_password.findViewById(R.id.submit_lay);

        submit.setOnClickListener(v -> {
            if (!TextUtils.isEmpty(pass.getText().toString()) && !TextUtils.isEmpty(confPass.getText().toString())
                    && !TextUtils.isEmpty(OldPwd.getText().toString())) {
                if (pass.getText().toString().equals(confPass.getText().toString())) {
                    mViewModel.updatePwdApi(pass.getText().toString(), confPass.getText().toString(), OldPwd.getText().toString());
                } else
                    Toast.makeText(getActivity(), "Not Equal", Toast.LENGTH_SHORT).show();
            } else
                Toast.makeText(getActivity(), "Fields Should not empty", Toast.LENGTH_SHORT).show();

        });

        dialog_password.setCanceledOnTouchOutside(true);
        dialog_password.show();*/

    }

    @Override
    public void onClickManageCard() {
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .disallowAddToBackStack()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .replace(R.id.fragment_frame, ManageCardFrag.newInstance(), ManageCardFrag.TAG)
                .commit();
    }

    @Override
    public void onClickHistory() {
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .disallowAddToBackStack()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .replace(R.id.fragment_frame, HistoryFrag.newInstance(), HistoryFrag.TAG)
                .commit();
    }

    @Override
    public void logoutApi() {
        AlertDialog ad = new AlertDialog.Builder(getActivity())
                .setTitle("Logout")
                .setCancelable(false)
                .setMessage("Are you sure to logout?")
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        mViewModel.logoutApiCall();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
        if (!ad.isShowing())
            ad.show();
    }

    @Override
    public void openFromStart() {
        startActivity(new Intent(getActivity(), LoginOrSignAct.class));
    }

    @Override
    public void onClickWallet() {
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .disallowAddToBackStack()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .replace(R.id.fragment_frame, WalletFrag.newInstance(), WalletFrag.TAG)
                .commit();
    }

    @Override
    public void onClickRefferal() {
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .disallowAddToBackStack()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .replace(R.id.fragment_frame, GetRefferalFrag.newInstance(), GetRefferalFrag.TAG)
                .commit();
    }

    @Override
    public void onClickSOS() {
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .disallowAddToBackStack()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .replace(R.id.fragment_frame, SosFrag.newInstance(), SosFrag.TAG)
                .commit();
    }

    @Override
    public void onClickFAQ() {
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .disallowAddToBackStack()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .replace(R.id.fragment_frame, FaqFrag.newInstance("FAQ"), FaqFrag.TAG)
                .commit();
    }

    @Override
    public void refreshScreen() {
        Intent intent = new Intent(getActivity(), HomeAct.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        requireActivity().finish();
        requireActivity().startActivity(intent);
    }

    @Override
    public void onCLickBack() {
        getBaseAct().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_frame, MapFrag.newInstance("Profile"), MapFrag.TAG)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commitNow();
    }

    @Override
    public void oncheckupdate() {
        //pass intent to play store
        final String appPackageName = getBaseAct().getPackageName(); // getPackageName() from Context or Activity object
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }

    Dialog aboutdialog;

    @Override
    public void onabout() {
        aboutdialog = new Dialog(getBaseAct(), android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        aboutdialog.setContentView(R.layout.about_dialog_lay);

        aboutdialog.setCancelable(false);
        aboutdialog.setCanceledOnTouchOutside(false);

        ImageView Back = aboutdialog.findViewById(R.id.back_arrow_about);
        TextView version = aboutdialog.findViewById(R.id.version_txt_about);
        RelativeLayout Termscndts = aboutdialog.findViewById(R.id.termsserv_lay_about);

        String ADversion = sharedPrefence.Getvalue(SharedPrefence.Appversion);

        version.setText("Version " + ADversion);

        Back.setOnClickListener(view -> {
            //dismiss the dialog
            aboutdialog.dismiss();
            sharedPrefence.saveBoolean(SharedPrefence.dont_refresh, true);
        });

        Termscndts.setOnClickListener(view -> {
            //open terms and conditions frag
            int primary = Color.parseColor("#E70000");
            CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
            builder.setToolbarColor(primary);
            CustomTabsIntent intent = builder.build();
            intent.launchUrl(getBaseAct(), Uri.parse(Constants.URL.privacy_policy));
        });

        aboutdialog.setOnKeyListener((dialogInterface, i, keyEvent) -> {

            if (i == KeyEvent.KEYCODE_BACK) {
                aboutdialog.dismiss();
            }
            return true;
        });

        aboutdialog.show();
    }

    @Override
    public void onfav() {
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .disallowAddToBackStack()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .replace(R.id.fragment_frame, FaqFrag.newInstance("FAV"), FaqFrag.TAG)
                .commit();
    }

    @Override
    public void profileComplaintDialog() {
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .disallowAddToBackStack()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .replace(R.id.fragment_frame, MakeCompFrag.newInstance("", Constants.profile_comptype))
                .commit();
    }

    @Override
    public void onResume() {
        super.onResume();
        mViewModel.detailsSet(mBinding.profImg);
        sharedPrefence.saveBoolean(SharedPrefence.dont_refresh, false);
    }
}
