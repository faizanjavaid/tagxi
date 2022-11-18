package com.tyt.client.ui.homeScreen.profile;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;
import androidx.databinding.ObservableField;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.tyt.client.R;
import com.tyt.client.app.MyApp;
import com.tyt.client.retro.GitHubService;
import com.tyt.client.retro.base.BaseNetwork;
import com.tyt.client.retro.base.BaseResponse;
import com.tyt.client.utilz.Constants;
import com.tyt.client.utilz.SharedPrefence;
import com.tyt.client.utilz.exception.CustomException;
import com.tyt.client.utilz.language.LanguageUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Created by Mahi in 2021.
 */

public class ProfileViewModel extends BaseNetwork<BaseResponse, ProfileNavigator> {
    private static final String TAG = "AddCardViewModel";

    HashMap<String, String> map = new HashMap<>();

    public ObservableField<String> userName = new ObservableField<>();
    public ObservableField<String> userEmail = new ObservableField<>();
    public ObservableField<String> selectedLanguage = new ObservableField<>();

    public ProfileViewModel(GitHubService gitHubService, SharedPrefence sharedPrefence, Gson gson) {
        super(gitHubService, sharedPrefence, gson);
    }


    /**
     * called when API call is successful
     **/
    @Override
    public void onSuccessfulApi(long taskId, BaseResponse response) {
        setIsLoading(false);
        if (response.success) {
            sharedPrefence.savevalue(SharedPrefence.AccessToken, "");
            sharedPrefence.savevalue(SharedPrefence.Name, "");
            sharedPrefence.savevalue(SharedPrefence.Email, "");
            sharedPrefence.savevalue(SharedPrefence.Password, "");
            sharedPrefence.savevalue(SharedPrefence.CountryCode, "");
            sharedPrefence.savevalue(SharedPrefence.ConfirmPassword, "");
            sharedPrefence.savevalue(SharedPrefence.PhoneNumber, "");
            sharedPrefence.savevalue(SharedPrefence.Profile, "");
            getmNavigator().openFromStart();
        }
    }

    /**
     * called when API call fails
     **/
    @Override
    public void onFailureApi(long taskId, CustomException e) {
        setIsLoading(false);
        getmNavigator().showMessage(e.getMessage());

        if (e.getCode() == 401) {
            sharedPrefence.savevalue(SharedPrefence.AccessToken, "");
            sharedPrefence.savevalue(SharedPrefence.Name, "");
            sharedPrefence.savevalue(SharedPrefence.Email, "");
            sharedPrefence.savevalue(SharedPrefence.Password, "");
            sharedPrefence.savevalue(SharedPrefence.CountryCode, "");
            sharedPrefence.savevalue(SharedPrefence.ConfirmPassword, "");
            sharedPrefence.savevalue(SharedPrefence.PhoneNumber, "");
            sharedPrefence.savevalue(SharedPrefence.Profile, "");
            sharedPrefence.savevalue(SharedPrefence.ID, "");
            getmNavigator().openFromStart();
        }
    }

    /**
     * adds client_id, client_token to {@link HashMap} used for API calls
     **/
    @Override
    public HashMap<String, String> getMap() {
        return null;
    }


    public void onClickEditProfile(View v) {
        getmNavigator().openEditProfile();
    }


    public void detailsSet(ImageView profImg) {
        userName.set(sharedPrefence.Getvalue(SharedPrefence.Name));
        userEmail.set(sharedPrefence.Getvalue(SharedPrefence.Email));

        Glide.with(MyApp.getmContext()).load(sharedPrefence.Getvalue(SharedPrefence.Profile)).
                apply(RequestOptions.circleCropTransform().error(R.drawable.profile_place_holder).
                        placeholder(R.drawable.profile_place_holder)).into(profImg);
    }

    public void onClickChangePwd(View v) {
        getmNavigator().opneChangePasswordAlert();
    }

    public void updatePwdApi(String pass, String confPass, String oldPass) {
        if (getmNavigator().isNetworkConnected()) {
            map.clear();
            map.put(Constants.NetworkParameters.password, pass);
            map.put(Constants.NetworkParameters.confpwd, confPass);
            map.put(Constants.NetworkParameters.OldPwd, oldPass);
            UpdatePwd(map);
        } else
            getmNavigator().showNetworkMessage();
    }

    public void onClickManageCard(View v) {
        getmNavigator().onClickManageCard();
    }

    public void onClickHistory(View v) {
        getmNavigator().onClickHistory();
    }

    public void onCLickLogout(View v) {
        getmNavigator().logoutApi();

    }

    public void logoutApiCall() {
        if (getmNavigator().isNetworkConnected()) {
            LogoutApi();
        } else getmNavigator().showNetworkMessage();
    }

    public void onCLickWallet(View v) {
        getmNavigator().onClickWallet();
    }

    public void onClickRefferal(View v) {
        getmNavigator().onClickRefferal();
    }

    public void onClickSOS(View v) {
        getmNavigator().onClickSOS();
    }

    public void onclickFAQ(View v) {
        getmNavigator().onClickFAQ();
    }

    public void onClickChangeLanguage(final View view) {
        final List<String> items = new ArrayList<>();
        if (sharedPrefence.Getvalue(SharedPrefence.LANGUANGES) != null) {
            for (String key : gson.fromJson(sharedPrefence.Getvalue(SharedPrefence.LANGUANGES), String[].class))
                switch (key) {
                    case "en":
                        items.add("English");
                        break;
                    case "ar":
                        items.add("عربى");
                        break;
                    case "ta":
                        items.add("தமிழ்");
                        break;
                    case "es":
                        items.add("español");
                        break;
                    case "ja":
                        items.add("日本語");
                        break;
                    case "ko":
                        items.add("한국어");
                        break;
                    case "pt":
                        items.add("português");
                        break;
                    case "zh":
                        items.add("中国人");
                        break;
                    case "hi":
                        items.add("हिंदी");
                        break;
                }
        }

        Log.e("xxxLang"," total items in profile"+items);

        final AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setTitle(translationModel.txt_choose_language);
        builder.setItems(items.toArray(new String[items.size()]), (dialog, item) -> {
            selectedLanguage.set(items.get(item));
            String language;
            language = items.get(item);
            String loc="";
            if (language.equalsIgnoreCase("தமிழ்")) {
                loc = "ta";
            } else if (language.equalsIgnoreCase("عربى")) {
                loc = "ar";
            } else if (language.equalsIgnoreCase("English")) {
                loc = "en";
            }else if (language.equalsIgnoreCase("español")){
                loc="es";
            }else if (language.equalsIgnoreCase("日本語")){
                loc="ja";
            }else if (language.equalsIgnoreCase("한국어")){
                loc="ko";
            }else if (language.equalsIgnoreCase("português")){
                loc="pt";
            }else if (language.equalsIgnoreCase("中国人")){
                loc="zh";
            }else if (language.equalsIgnoreCase("हिंदी")){
                loc="hi";
            }

            sharedPrefence.savevalue(SharedPrefence.LANGUANGE, loc);
            selectedLanguage.set(loc);
            if ("ta".equalsIgnoreCase(loc)) {
                Locale locale = Locale.getDefault();
                Locale.setDefault(new Locale(loc));
                LanguageUtil.changeLanguageType(view.getContext(), locale);
            } else if (!TextUtils.isEmpty(loc)) {
                Locale locale = Locale.getDefault();
                Locale.setDefault(new Locale(loc));
                LanguageUtil.changeLanguageType(view.getContext(), locale);
            } else {
                LanguageUtil.changeLanguageType(view.getContext(), Locale.ENGLISH);
            }
            getmNavigator().refreshScreen();
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void onCLickBack(View v) {
        getmNavigator().onCLickBack();
    }

    public void onClickCheckUpdate(View v) {
        getmNavigator().oncheckupdate();
    }

    public void onCLickAbout(View v) {
        getmNavigator().onabout();
    }

    public void onclickFavourites(View v) {
        getmNavigator().onfav();
    }


    public void onClickMakeComp(View v) {
        getmNavigator().profileComplaintDialog();
    }
}
