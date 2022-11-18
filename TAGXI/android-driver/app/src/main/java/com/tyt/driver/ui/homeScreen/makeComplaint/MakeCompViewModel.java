package com.tyt.driver.ui.homeScreen.makeComplaint;

import android.util.Log;
import android.view.View;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;

import com.google.gson.Gson;
import com.tyt.driver.retro.GitHubService;
import com.tyt.driver.retro.base.BaseNetwork;
import com.tyt.driver.retro.base.BaseResponse;
import com.tyt.driver.retro.responsemodel.ComplaintTypesModel;
import com.tyt.driver.utilz.CommonUtils;
import com.tyt.driver.utilz.SharedPrefence;
import com.tyt.driver.utilz.exception.CustomException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

public class MakeCompViewModel extends BaseNetwork<BaseResponse, MakeCompNavigator> {
    public static final String TAG ="MakeCompViewModel";

    List<ComplaintTypesModel> complaintTModels = new ArrayList<>();
    public ObservableBoolean showprocessImg = new ObservableBoolean(false);
    public ObservableBoolean showdonesImg = new ObservableBoolean(false);
    public ObservableBoolean show_status = new ObservableBoolean(false);

    public ObservableField<String> process_txt = new ObservableField<>("Processing Your Submission");
    public ObservableField<String> done_txt  = new ObservableField<>("DONE");

    @Inject
    public MakeCompViewModel(GitHubService gitHubService, SharedPrefence sharedPrefence, Gson gson) {
        super(gitHubService, sharedPrefence, gson);
    }

    @Override
    public HashMap<String, String> getMap() {
        return new HashMap<>();
    }

    @Override
    public void onSuccessfulApi(long taskId, BaseResponse response) {
        setIsLoading(false);
        complaintTModels.clear();

        if (response.success) {
            if (response.message.equalsIgnoreCase("complaint_titles_listed")) {
                String complainttypeData = CommonUtils.arrayToString((ArrayList<Object>) response.data);
                complaintTModels.addAll(CommonUtils.stringToArray(complainttypeData, ComplaintTypesModel[].class));
                Log.e("xxxComplaint Types", complaintTModels.toString());
                getmNavigator().loadComplaintTypes(complaintTModels);
            }
            else if (response.message.equalsIgnoreCase("complaint_posted_successfully")){
                getmNavigator().showAlertMsg("We Have Successfully Received Your Complaint..","Submission Success");
                showdonesImg.set(true);
            }
        }
    }

    @Override
    public void onFailureApi(long taskId, CustomException e) {
        setIsLoading(false);
        getmNavigator().showMessage(e);
    }

    public void getcomplaint_types(String compType) {
        if (getmNavigator().isNetworkConnected()) {
            setIsLoading(true);
            getMap();
            //while (complaintTModels != null) {
            GetComplaintTypes(compType,getMap());
            // }
            getmNavigator().loadComplaintTypes(complaintTModels);
        } else getmNavigator().showNetworkMessage();
    }

    public void submitComplaints(HashMap<String, String> cmap) {
        if (getmNavigator().isNetworkConnected()) {
            setIsLoading(true);
            //showprocessImg.set(true);
            getMap();
            MakeComplaint(cmap);
        } else getmNavigator().showNetworkMessage();
    }

    public void onClosearrowclicked(View view) {
        getmNavigator().CloseIcClicked();
    }
}
