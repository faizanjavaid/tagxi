package com.tyt.driver.ui.homeScreen.makeComplaint;

import com.tyt.driver.retro.responsemodel.ComplaintTypesModel;
import com.tyt.driver.ui.base.BaseView;

import java.util.List;

public interface MakeCompNavigator extends BaseView {
    void loadComplaintTypes(List<ComplaintTypesModel> complaintTypesModels);

    void showAlertMsg(String msg,String Title);

    void CloseIcClicked();

    void clickBack();
}
