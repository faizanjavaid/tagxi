package com.tyt.driver.ui.approval;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.tyt.driver.BR;
import com.tyt.driver.R;
import com.tyt.driver.databinding.ApplyRefferralBinding;
import com.tyt.driver.databinding.ApprovalLayBinding;
import com.tyt.driver.ui.base.BaseActivity;
import com.tyt.driver.ui.documentUpload.DocumentUploadAct;
import com.tyt.driver.ui.homeScreen.HomeAct;
import com.tyt.driver.utilz.SharedPrefence;

import javax.inject.Inject;

/**
 * Created by naveen on 8/24/17.
 */

public class ApprovalAct extends BaseActivity<ApprovalLayBinding, ApprovalViewModel> implements ApprovalNavigator {
    @Inject
    SharedPrefence sharedPrefence;
    @Inject
    ApprovalViewModel approvalViewModel;
    ApprovalLayBinding approvalLayBinding;
    String reason;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        approvalLayBinding = getViewDataBinding();
        approvalViewModel.setNavigator(this);

        if (getIntent() != null) {
            reason = getIntent().getStringExtra("DeclinedReason");
            Log.e("resone--", "reason==" + reason);

            try {
                if (!reason.isEmpty()) {
                    approvalViewModel.reasonss.set(reason);
                    approvalViewModel.title.set(approvalViewModel.translationModel.txt_doc_declined);
                    approvalViewModel.showdocsbutton.set(true);
                } else {
                    approvalViewModel.showdocsbutton.set(true);
                    approvalViewModel.reasonss.set(approvalViewModel.translationModel.txt_approval_sent);
                    approvalViewModel.title.set(approvalViewModel.translationModel.txt_waiting_approval);
                }
            } catch (NullPointerException e) {
                showMessage(e.getLocalizedMessage());
            }

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public BaseActivity getBaseAct() {
        return this;
    }

    @Override
    public void openHomeAct() {
        startActivity(new Intent(this, HomeAct.class));
        finish();
    }

    @Override
    public void openDocumentUpload() {
        startActivity(new Intent(this, DocumentUploadAct.class));
    }

    @Override
    public ApprovalViewModel getViewModel() {
        return approvalViewModel;
    }

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.approval_lay;
    }

    @Override
    public SharedPrefence getSharedPrefence() {
        return sharedPrefence;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}