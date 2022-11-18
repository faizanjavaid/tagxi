package com.tyt.driver.ui.documentUpload;

import com.tyt.driver.retro.responsemodel.Documentdata;
import com.tyt.driver.ui.base.BaseActivity;
import com.tyt.driver.ui.base.BaseView;

import java.util.List;

/**
 * Created by root on 10/7/17.
 */

public interface DocumentUploadNavigator extends BaseView {

    BaseActivity getBaseAct();

    void loadDocuments(List<Documentdata> documentdata);

    void onCLickDocsItem(Documentdata documentdata);

    void onCLickSubmit();
}
