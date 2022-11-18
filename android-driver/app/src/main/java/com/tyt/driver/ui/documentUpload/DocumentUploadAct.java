package com.tyt.driver.ui.documentUpload;

import android.content.Intent;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;
import com.tyt.driver.BR;
import com.tyt.driver.R;
import com.tyt.driver.databinding.DocumentUploadBinding;
import com.tyt.driver.databinding.LoginSignBinding;
import com.tyt.driver.retro.responsemodel.Documentdata;
import com.tyt.driver.ui.base.BaseActivity;
import com.tyt.driver.ui.docsEdit.DocsEditAct;
import com.tyt.driver.ui.homeScreen.HomeAct;
import com.tyt.driver.utilz.SharedPrefence;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by naveen on 8/24/17.
 */

public class DocumentUploadAct extends BaseActivity<DocumentUploadBinding, DocumentUploadViewModel> implements DocumentUploadNavigator {
    @Inject
    SharedPrefence sharedPrefence;
    @Inject
    DocumentUploadViewModel mViewModel;
    DocumentUploadBinding documentUploadBinding;
    String from = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        documentUploadBinding = getViewDataBinding();
        mViewModel.setNavigator(this);

        if (getIntent() != null)
            from = getIntent().getStringExtra("fromm");

    }

    @Override
    protected void onResume() {
        super.onResume();
        mViewModel.documentgetApi();
    }

    @Override
    public BaseActivity getBaseAct() {
        return this;
    }

    @Override
    public void loadDocuments(List<Documentdata> documentdata) {
        DocumentUploadAdapter uploadAdapter = new DocumentUploadAdapter(this, documentdata, this);
        documentUploadBinding.recycle.setLayoutManager(new LinearLayoutManager(this));
        documentUploadBinding.recycle.setAdapter(uploadAdapter);
    }

    @Override
    public void onCLickDocsItem(Documentdata documentdata) {
        Intent docsIntent = new Intent(this, DocsEditAct.class);
        docsIntent.putExtra("DocsData", new Gson().toJson(documentdata));
        startActivity(docsIntent);
    }

    @Override
    public void onCLickSubmit() {
        startActivity(new Intent(this, HomeAct.class));
    }

    @Override
    public DocumentUploadViewModel getViewModel() {
        return mViewModel;
    }

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.document_upload;
    }

    @Override
    public SharedPrefence getSharedPrefence() {
        return sharedPrefence;
    }

    @Override
    public void onBackPressed() {
        if (from != null) {
            if (from.equalsIgnoreCase("1")) {
                super.onBackPressed();
            }
        }
    }
}