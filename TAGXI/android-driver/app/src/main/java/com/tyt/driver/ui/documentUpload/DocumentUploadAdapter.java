package com.tyt.driver.ui.documentUpload;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.tyt.driver.R;
import com.tyt.driver.app.MyApp;
import com.tyt.driver.retro.responsemodel.Documentdata;

import java.util.List;


public class DocumentUploadAdapter extends RecyclerView.Adapter<DocumentUploadAdapter.ViewHolder> {

    List<Documentdata> documentdata;
    Context context;
    public DocumentUploadNavigator documentUploadNavigator;

    public DocumentUploadAdapter(Context context, List<Documentdata> documentdata, DocumentUploadNavigator documentUploadNavigator) {
        this.context = context;
        this.documentdata = documentdata;
        this.documentUploadNavigator = documentUploadNavigator;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.document_items, viewGroup, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final DocumentUploadAdapter.ViewHolder viewHolder, final int i) {
        viewHolder.docsName.setText(documentdata.get(i).getName());
        viewHolder.docsStatus.setText(documentdata.get(i).getDocumentStatusString());


        if (documentdata.get(i).getDocumentStatus() == 6 || documentdata.get(i).getDocumentStatus() == 5 || documentdata.get(i).getDocumentStatus() == 0)
            viewHolder.docsStatus.setTextColor(context.getResources().getColor(R.color.clr_E70000));
        else if (documentdata.get(i).getDocumentStatus() == 2)
            viewHolder.docsStatus.setTextColor(context.getResources().getColor(R.color.clr_EF9900));
        else if (documentdata.get(i).getDocumentStatus() == 1) {
            viewHolder.docsNotUploaded.setVisibility(View.GONE);
            viewHolder.docsStatus.setTextColor(context.getResources().getColor(R.color.clr_1EC639));
        } else if (documentdata.get(i).getDocumentStatus() == 3) {
            viewHolder.docsNotUploaded.setVisibility(View.GONE);
            viewHolder.docsStatus.setTextColor(context.getResources().getColor(R.color.clr_2361E2));
        } else {
            viewHolder.docsNotUploaded.setVisibility(View.GONE);
            viewHolder.docsStatus.setTextColor(context.getResources().getColor(R.color.clr_3DB39E));
        }


        viewHolder.itemView.setOnClickListener(v -> documentUploadNavigator.onCLickDocsItem(documentdata.get(i)));

        if (documentdata.get(i).getDriverDocument() != null)
            if (documentdata.get(i).getDriverDocument().getData().getComment() != null) {
                viewHolder.docsDescription.setText(documentdata.get(i).getDriverDocument().getData().getComment());
                viewHolder.docsDescription.setVisibility(View.VISIBLE);
            } else viewHolder.docsDescription.setVisibility(View.GONE);


        if (documentdata.get(i).getDriverDocument() != null)
            Glide.with(MyApp.getmContext()).load(documentdata.get(i).getDriverDocument().getData().getDocument()).
                    apply(RequestOptions.circleCropTransform().error(R.drawable.ic_camera).
                            placeholder(R.drawable.ic_camera)).into(viewHolder.docsImg);
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView docsName, docsStatus, docsDescription;
        ImageView docsImg, docsNotUploaded;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            docsName = itemView.findViewById(R.id.docs_name);
            docsStatus = itemView.findViewById(R.id.status);
            docsDescription = itemView.findViewById(R.id.description);
            docsImg = itemView.findViewById(R.id.docs_img);
            docsNotUploaded = itemView.findViewById(R.id.not_upload_img);
        }
    }

    @Override
    public int getItemCount() {
        return documentdata.size();
    }
}

