package com.tyt.client.ui.homeScreen.makeComplaint;

import com.tyt.client.retro.responsemodel.ComplaintTypesModel;
import com.tyt.client.ui.base.BaseView;

import java.util.List;

public interface MakeCompNavigator extends BaseView {
    void loadComplaintTypes(List<ComplaintTypesModel> complaintTypesModels);

    void showAlertMsg(String msg,String Title);

    void CloseIcClicked();

    void clickBack();
}
