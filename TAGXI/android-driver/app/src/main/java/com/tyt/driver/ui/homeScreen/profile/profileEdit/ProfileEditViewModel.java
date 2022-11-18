package com.tyt.driver.ui.homeScreen.profile.profileEdit;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;
import androidx.databinding.ObservableField;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.tyt.driver.R;
import com.tyt.driver.app.MyApp;
import com.tyt.driver.retro.GitHubService;
import com.tyt.driver.retro.base.BaseNetwork;
import com.tyt.driver.retro.base.BaseResponse;
import com.tyt.driver.retro.responsemodel.ProfileModel;
import com.tyt.driver.utilz.CommonUtils;
import com.tyt.driver.utilz.Constants;
import com.tyt.driver.utilz.RealPathUtil;
import com.tyt.driver.utilz.SharedPrefence;
import com.tyt.driver.utilz.exception.CustomException;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import javax.inject.Inject;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by root on 11/13/17.
 */

public class ProfileEditViewModel extends BaseNetwork<BaseResponse, ProfileEditNavigator> {
    private static final String TAG = "ProfileEditViewModel";

    HashMap<String, String> Map = new HashMap<>();

    public ObservableField<String> fName = new ObservableField<>();
    public ObservableField<String> lName = new ObservableField<>();
    public ObservableField<String> email = new ObservableField<>();
    public ObservableField<String> phone = new ObservableField<>();
    public ObservableField<String> countryCode = new ObservableField<>();
    public ObservableField<String> bitmap_profilePicture = new ObservableField<String>();

    String realPath = null;
    File realFile;
    @Inject
    HashMap<String, String> hashMap;
    GitHubService gitHubService;
    //String genericsType = new GenericsType();

    public ObservableField<String> camera = new ObservableField<>();
    public ObservableField<String> gallery = new ObservableField<>();
    public ObservableField<String> choose = new ObservableField<>();

    public ProfileEditViewModel(GitHubService gitHubService, SharedPrefence sharedPrefence, Gson gson) {
        super(gitHubService, sharedPrefence, gson);

        fName.set(sharedPrefence.Getvalue(SharedPrefence.Name));
        countryCode.set(sharedPrefence.Getvalue(SharedPrefence.CountryCode));
        email.set(sharedPrefence.Getvalue(SharedPrefence.Email));
        phone.set(sharedPrefence.Getvalue(SharedPrefence.PhoneNumber));

        camera.set(translationModel.text_camera);
        gallery.set(translationModel.text_galary);
        choose.set(translationModel.txt_Choose);

        //genericsType.set(sharedPrefence.Getvalue(SharedPrefence.Profile));
        bitmap_profilePicture.set(sharedPrefence.Getvalue(SharedPrefence.Profile));
    }

    @BindingAdapter("imageUrlProfile")
    public static void setImageUrl(ImageView imageView, String url) {
        if (url != null) {
            Context context = imageView.getContext();
            Glide.with(context).load(url).
                    apply(RequestOptions.circleCropTransform().error(R.drawable.profile_place_holder).
                            placeholder(R.drawable.profile_place_holder)).into(imageView);
        }
    }

    /**
     * called when API call is successful
     **/
    @Override
    public void onSuccessfulApi(long taskId, BaseResponse response) {
        setIsLoading(false);
        if (response.success) {
            getmNavigator().showMessage(getmNavigator().getBaseAct().getTranslatedString(R.string.txt_updated));
            ProfileModel model = CommonUtils.getSingleObject(new Gson().toJson(response.data), ProfileModel.class);
            sharedPrefence.savevalue(SharedPrefence.Name, model.getName());
            sharedPrefence.savevalue(SharedPrefence.Email, model.getEmail());
            sharedPrefence.savevalue(SharedPrefence.PhoneNumber, model.getMobile());
            sharedPrefence.savevalue(SharedPrefence.Profile, model.getProfilePicture());

            getmNavigator().openHomeAct();
        }
    }

    /**
     * called when API call fails
     **/
    @Override
    public void onFailureApi(long taskId, CustomException e) {
        setIsLoading(false);
    }

    /**
     * adds client_id, client_token to {@link HashMap} used for API calls
     **/
    @Override
    public HashMap<String, String> getMap() {
        return null;
    }

    public void onClickCountryChoose(View v) {
        getmNavigator().chooseCountryCode();
    }

    public void onFirstNameChanged(Editable e) {
        fName.set(e.toString());
    }

    public void onLatnameChanged(Editable e) {
        lName.set(e.toString());
    }


    public void onEmailChnaged(Editable e) {
        email.set(e.toString());
    }

    public void onPhoneChnaged(Editable e) {
        phone.set(e.toString());
    }

    public void onClickUpdate(View v) {

        if (getmNavigator().isNetworkConnected()) {
            setIsLoading(true);
            requestbody.put(Constants.NetworkParameters.country, RequestBody.create(MediaType.parse("text/plain"), countryCode.get()));
            requestbody.put(Constants.NetworkParameters.email, RequestBody.create(MediaType.parse("text/plain"), email.get()));
            requestbody.put(Constants.NetworkParameters.phoneNumber, RequestBody.create(MediaType.parse("text/plain"), phone.get()));
            requestbody.put(Constants.NetworkParameters.name, RequestBody.create(MediaType.parse("text/plain"), fName.get()));
            if (realPath != null) {
                RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), new File(realPath));
                body = MultipartBody.Part.createFormData("profile_picture", new File(realPath).getName(), reqFile);
            }
            UpdateUserProfile();
        } else getmNavigator().showNetworkMessage();
    }

    void onSelectFromGalleryResult(Intent data) {
        if (data != null) {
            realPath = RealPathUtil.getRealPath(MyApp.getmContext(), data.getData());
//            compressFile(realFile.getPath());
            bitmap_profilePicture.set(realPath);
          /*  realPath = RealPathUtil.getRealPath(MyApp.getmContext(), data.getData());
            Log.d("RealPath", realPath);
            realFile = new File(realPath == null ? "" : realPath);
            genericsType = new GenericsType();
            genericsType.set(realFile);
            compressFile(realFile.getPath());
            bitmap_profilePicture.set(genericsType);*/
        }
    }

    void onCaptureImageResult(Intent data) {
        if (data.getData() == null) {
            realPath = CommonUtils.getImageUri((Bitmap) data.getExtras().get("data"));
            bitmap_profilePicture.set(realPath);
        }
            /*realPath = createFile((Bitmap) data.getExtras().get("data"));
            *//*genericsType = new GenericsType();
            genericsType.set(realFile);
            Log.e("Image--", "image==" + realPath);
            compressFile(realFile.getPath());*//*
            bitmap_profilePicture.set(realPath);
            //return;
        }*/
       /* realPath = getRealPathFromURI(data.getData());
        Log.d("RealPath", realPath);
        realFile = new File(realPath == null ? "" : realPath);
        genericsType.set(realFile);
        compressFile(realFile.getPath());
        bitmap_profilePicture.set(genericsType);*/
    }

    private void compressFile(String path) {
        int quality = 70;
        Bitmap gallerybitmap = BitmapFactory.decodeFile(path);

        if (gallerybitmap.getByteCount() >= 1024*2) {
            quality = 50;
        } else if (gallerybitmap.getByteCount()>52428800) {
            quality=40;
        }

        gallerybitmap=getResizedBitmapLessThanMaxSize(gallerybitmap,500);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        boolean compressed = gallerybitmap.compress(Bitmap.CompressFormat.PNG,quality,out);
        try {
            out.flush();
            out.close();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        if (compressed) {
            getmNavigator().showMessage("Image Compressed");
        }
    }

    private String createFile(Bitmap bitmap) {
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root);
        myDir.mkdirs();
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmm", Locale.ENGLISH).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File file = new File(myDir, imageFileName);
        if (file.exists()) file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
            realFile = file;
            return file.getAbsolutePath();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return file.getAbsolutePath();
    }

    private String getRealPathFromURI(Uri uri) {
        Cursor cursor = MyApp.getmContext().getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }


    public void onClickChooseImage(View v) {
        getmNavigator().openImageChooseAlert();
    }

    public void onClickBack(View v) {
        getmNavigator().clickBack();
    }

    public class GenericsType<T> {
        private T t;

        public T get() {
            return this.t;
        }

        public void set(T t1) {
            this.t = t1;
        }
    }

    final static int COMPRESSED_RATIO = 13;
    final static int perPixelDataSize = 4;

    public Bitmap getResizedBitmapLessThanMaxSize(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();
        float bitmapRatio = (float)width / (float) height;

        // For uncompressed bitmap, the data size is:
        // H * W * perPixelDataSize = H * H * bitmapRatio * perPixelDataSize
        //
        height = (int) Math.sqrt(maxSize * 1024 * COMPRESSED_RATIO / perPixelDataSize / bitmapRatio);
        width = (int) (height * bitmapRatio);
        Bitmap reduced_bitmap = Bitmap.createScaledBitmap(image, width, height, true);
        return reduced_bitmap;
    }

}
