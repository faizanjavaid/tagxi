package com.tyt.driver.ui.forgot;

import android.content.Context;
import androidx.databinding.ObservableField;
import android.text.Editable;
import android.view.View;

import com.google.gson.Gson;
import com.tyt.driver.R;
import com.tyt.driver.retro.base.BaseNetwork;
import com.tyt.driver.retro.GitHubCountryCode;
import com.tyt.driver.retro.GitHubService;
import com.tyt.driver.retro.responsemodel.User;
import com.tyt.driver.utilz.CommonUtils;
import com.tyt.driver.utilz.Constants;
import com.tyt.driver.utilz.exception.CustomException;
import com.tyt.driver.utilz.SharedPrefence;

import java.util.HashMap;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by root on 10/9/17.
 */

public class ForgotViewModel extends BaseNetwork<User, ForgotNavigator> {

    @Inject
    HashMap<String, String> Map;

    @Inject
    Context context;
    public ObservableField<String> EmailorPhone = new ObservableField<>();

    SharedPrefence sharedPrefence;
    GitHubService gitHubService;
    GitHubCountryCode gitHubCountryCodeService;

    @Inject
    public ForgotViewModel(@Named(Constants.ourApp) GitHubService gitHubService,
                           @Named(Constants.countryMap) GitHubCountryCode gitHubCountryCodeService,
                           SharedPrefence sharedPrefence, Gson gson) {
        super(gitHubService, sharedPrefence, gson);
        this.gitHubService = gitHubService;
        this.sharedPrefence = sharedPrefence;
        this.gitHubCountryCodeService = gitHubCountryCodeService;
        // setBinding();
    }

    /** Click listener for submit button. After validating input ic calls Token Generator API **/
    public void onclickSumbit(View view) {

        if (getmNavigator().isNetworkConnected()) {
            if (CommonUtils.IsEmpty(EmailorPhone.get())) {
                getmNavigator().showSnackBar(view, getmNavigator().getAttachedContext().getTranslatedString(R.string.text_mail_phNum));
            } else {

                TokenGeneratorcall();

            }


        } else {
            getmNavigator().showNetworkMessage();
        }

    }

    /** Calls API to generate token **/
    public void TokenGeneratorcall() {

        if (getmNavigator().isNetworkConnected()) {
            setIsLoading(true);
            TokenGeneratorNetcall();
        } else
            getmNavigator().showNetworkMessage();
    }

    /** Called whenever text changes on the phone number field and assigns it to EmailorPhone
     * @param e {@link Editable} **/
    public void ForgotuserChanged(Editable e) {
        EmailorPhone.set(e.toString());
        if (e.toString().contains("@") || e.toString().contains(".co") || e.toString().contains(".com")) {
            getmNavigator().OpenCountrycodeINvisible();

        } else {

            if (e.toString().matches("[a-zA-Z]")) //only alphanumeric
                getmNavigator().OpenCountrycodeINvisible();
            else if (e.toString().isEmpty())
                getmNavigator().OpenCountrycodeINvisible();
            else if (e.toString().matches("[0-9]+")) {//only number
                getmNavigator().OpenCountrycodevisible();
            }
        }

    }

    /** Calls {@link GitHubService} to generate token **/
    public void TokenGeneratorNetcall() {
        if(Map==null)
            Map=new HashMap<>();
        Map.put(Constants.NetworkParameters.client_id, sharedPrefence.getCompanyID());
        Map.put(Constants.NetworkParameters.client_token, sharedPrefence.getCompanyToken());

    }


    /** Callback for successful API calls
     * @param taskId Id of the API task
     * @param response {@link User} response model **/
    @Override
    public void onSuccessfulApi(long taskId, User response) {
        Map.clear();
        setIsLoading(false);
       /* if (response.successMessage != null && response.successMessage.equalsIgnoreCase("forgot_password")) {
            getmNavigator().showMessage(getmNavigator().getAttachedContext().getTranslatedString(R.string.Toast_Forgot_Password));
            getmNavigator().openLoginActivity();
        } else if (response.successMessage != null && response.successMessage.equalsIgnoreCase("forgot_password_sms")) {
            getmNavigator().showMessage(getmNavigator().getAttachedContext().getTranslatedString(R.string.text_pass_sent_sms));
            getmNavigator().openLoginActivity();
        } else if (response.successMessage != null && response.successMessage.equalsIgnoreCase("forgot_password_email")) {
            getmNavigator().showMessage(getmNavigator().getAttachedContext().getTranslatedString(R.string.text_pass_sent_email));
            getmNavigator().openLoginActivity();
        }*/

    }

    /** Callback for failed API calls
     * @param taskId Id of the API task
     * @param e {@link CustomException} **/
    @Override
    public void onFailureApi(long taskId, CustomException e) {
        setIsLoading(false);

        getmNavigator().showMessage(e);
    }

    @Override
    public HashMap<String, String> getMap() {
        return Map;
    }
}
