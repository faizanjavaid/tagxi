package com.tyt.driver.ui.docsEdit;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.tyt.driver.R;
import com.tyt.driver.app.MyApp;
import com.tyt.driver.retro.GitHubCountryCode;
import com.tyt.driver.retro.GitHubService;
import com.tyt.driver.retro.base.BaseNetwork;
import com.tyt.driver.retro.base.BaseResponse;
import com.tyt.driver.utilz.CommonUtils;
import com.tyt.driver.utilz.Constants;
import com.tyt.driver.utilz.RealPathUtil;
import com.tyt.driver.utilz.SharedPrefence;
import com.tyt.driver.utilz.exception.CustomException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import javax.inject.Inject;
import javax.inject.Named;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by root on 10/7/17.
 */

public class DocsEditViewModel extends BaseNetwork<BaseResponse, DocsEditNavigator> {

    @Inject
    HashMap<String, String> Map;


    SharedPrefence sharedPrefence;
    GitHubService gitHubService;
    GitHubCountryCode gitHubCountryCode;

    public ObservableBoolean hasExpiry = new ObservableBoolean();
    public ObservableBoolean hasIdNumber = new ObservableBoolean();
    public ObservableField<String> idNUmber = new ObservableField<>("");
    public ObservableField<String> expDate = new ObservableField<>("");
    public ObservableField<String> year = new ObservableField<>();
    public ObservableField<String> DocsID = new ObservableField<>();
    public ObservableField<String> bitmap_profilePicture = new ObservableField<>("");
    String RealPath = null;
    File RealFile = null;

    File camera_pic;

    public ObservableField<String> camera = new ObservableField<>();
    public ObservableField<String> gallery = new ObservableField<>();
    public ObservableField<String> choose = new ObservableField<>();


    HashMap<String, String> map = new HashMap<>();

    @Inject

    public DocsEditViewModel(@Named(Constants.ourApp) GitHubService gitHubService,
                             @Named(Constants.countryMap) GitHubCountryCode gitHubCountryCode,
                             SharedPrefence sharedPrefence, Gson gson) {
        super(gitHubService, sharedPrefence, gson);
        this.gitHubService = gitHubService;
        this.gitHubCountryCode = gitHubCountryCode;
        this.sharedPrefence = sharedPrefence;
        this.gson = gson;

        camera.set(translationModel.text_camera);
        gallery.set(translationModel.text_galary);
        choose.set(translationModel.txt_Choose);
    }


    /**
     * Callback for successful API calls
     *
     * @param taskId   ID of the API task
     * @param response {@link BaseResponse} model
     **/
    @Override
    public void onSuccessfulApi(long taskId, BaseResponse response) {
        setIsLoading(false);
        if (response.success) {
            getmNavigator().closeAct();
        }
    }

    /**
     * Callback for failed API calls
     *
     * @param taskId ID of the API task
     * @param e      {@link CustomException}
     **/
    @Override
    public void onFailureApi(long taskId, CustomException e) {
        setIsLoading(false);

    }

    /**
     * Returns {@link HashMap} with query parameters to call APIs
     **/
    @Override
    public HashMap<String, String> getMap() {
        return Map;
    }

    public void onIdNumberChanged(Editable e) {
        idNUmber.set(e.toString());
    }

    public void onClickExpiry(View v) {
        getmNavigator().onClickExpiry();
    }

    public void onClickImage(View v) {
        getmNavigator().onClickImage();
    }


    public void onSelectFromGalleryResult(Intent data) {
        if (data != null) {
                RealPath = RealPathUtil.getRealPath(MyApp.getmContext(), data.getData());
                bitmap_profilePicture.set(RealPath);
        }
    }

    /**
     * @param data gets the path from Camera that the driver choose.
     */
    public void onCaptureImageResult(Intent data)  {
        if (data != null && data.getExtras().get("data") != null) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
        }
        RealPath = CommonUtils.getImageUri((Bitmap) data.getExtras().get("data"));
        bitmap_profilePicture.set(RealPath);
//        file_profile_pic=new File(RealPath);

        //File camfile = new File(mCurrentPhotoPath);

        //Bitmap bitmap = MediaStore.Images.Media.getBitmap( getmNavigator().getBaseAct().getBaseContext().getContentResolver(), Uri.fromFile(camfile));

      /*  if (bitmap != null) {

        }*/

        //RealPath = (createFile((Bitmap) data.getExtras().get("data")));
    }

    String mCurrentPhotoPath;

    private String createFile(Bitmap bitmap) {
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root);
        if (myDir.isDirectory())
            if (myDir.isFile())
                myDir.mkdirs();
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmm", Locale.ENGLISH).format(new Date());
        String imageFileName = "";

        imageFileName = "JPEG_" + timeStamp + "_";

        File file = new File(myDir, imageFileName);

        if (file.exists()) {
            Log.d("xxxTripFrag", "createFile: Delete");
            file.delete();
            file.deleteOnExit();
        }

        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 0, out);
            out.flush();
            out.close();

            RealFile = file;

            return file.getAbsolutePath();

        } catch (Exception e) {
            e.printStackTrace();
            Log.d("xxxTripFrag", "createFile: Error=" + e.getMessage());
        }
        return file.getAbsolutePath();
    }


    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getmNavigator().getBaseAct().getContentResolver().query(uri, projection, null, null, null);
        if (cursor == null) return null;
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String s = cursor.getString(column_index);
        cursor.close();
        return s;
    }

    @BindingAdapter("imageUrlProfile")
    public static void setImageUrl(ImageView imageView, String url) {
        Context context = imageView.getContext();
        Glide.with(context).load(url).
                apply(RequestOptions.circleCropTransform().error(R.drawable.ic_camera).
                        placeholder(R.drawable.ic_camera)).into(imageView);
    }

    public void onClickUpload(View v) {

        if (hasExpiry.get()) {
            if (expDate.get().isEmpty()) {
                getmNavigator().showMessage(getmNavigator().getBaseAct().getTranslatedString(R.string.txt_pls_choose_expiry));
                return;
            }
        }

        if (hasIdNumber.get()) {
            if (idNUmber.get().isEmpty()) {
                getmNavigator().showMessage(getmNavigator().getBaseAct().getTranslatedString(R.string.txt_pls_enter_id));
                return;
            }
        }

        Log.e("validlm--", "dargker");
        requestbody.clear();
        requestbody.put(Constants.NetworkParameters.IdentifyNumber, RequestBody.create(MediaType.parse("text/plain"), idNUmber.get()));
        requestbody.put(Constants.NetworkParameters.ExpiryNumber, RequestBody.create(MediaType.parse("text/plain"), expDate.get() + " " + "00:00:00"));
        requestbody.put(Constants.NetworkParameters.DocumentId, RequestBody.create(MediaType.parse("text/plain"), DocsID.get()));

        if (!CommonUtils.IsEmpty(RealPath)) {
            setIsLoading(true);
            RequestBody reqFile = RequestBody.create(MediaType.parse("multipart/form-data"), new File(RealPath));
            body_profile_pic = MultipartBody.Part.createFormData(Constants.NetworkParameters.DocsIMage, new File(RealPath).getName(), reqFile);
        } else {
            getmNavigator().showMessage(getmNavigator().getBaseAct().getTranslatedString(R.string.txt_pls_choose_doc));
            return;
        }
        setIsLoading(true);
        DocumentUpload();
    }

    public void onClickBack(View v) {
        getmNavigator().onClickBack();
    }

//    private boolean validation() {
//        boolean isValid = false;
//        if (TextUtils.isEmpty(idNUmber.get())) {
//            if (hasIdNumber.get())
//                getmNavigator().showMessage("Please enter Identification number");
//            else if(TextUtils.isEmpty(bitmap_profilePicture.get()))
//                getmNavigator().showMessage("Please select the documnet image");
//        } else if (TextUtils.isEmpty(bitmap_profilePicture.get()))
//            getmNavigator().showMessage("Please select the documnet image");
//        else if (TextUtils.isEmpty(expDate.get())) {
//            if (hasExpiry.get())
//                getmNavigator().showMessage("Please choose the expiry date");
//        } else isValid = true;
//
//        return isValid;
//    }

}

