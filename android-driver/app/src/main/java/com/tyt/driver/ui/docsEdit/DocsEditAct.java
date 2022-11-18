package com.tyt.driver.ui.docsEdit;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.content.FileProvider;

import com.tyt.driver.BR;
import com.tyt.driver.R;
import com.tyt.driver.app.MyApp;
import com.tyt.driver.databinding.DocsEditBinding;
import com.tyt.driver.retro.responsemodel.Documentdata;
import com.tyt.driver.ui.base.BaseActivity;
import com.tyt.driver.utilz.CommonUtils;
import com.tyt.driver.utilz.Constants;
import com.tyt.driver.utilz.SharedPrefence;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.inject.Inject;

/**
 * Created by naveen on 8/24/17.
 */

public class DocsEditAct extends BaseActivity<DocsEditBinding, DocsEditViewModel> implements DocsEditNavigator, DatePickerDialog.OnDateSetListener {
    @Inject
    SharedPrefence sharedPrefence;
    @Inject
    DocsEditViewModel mViewModel;
    DocsEditBinding docsEditBinding;
    int day, month, year, hour, minute;
    int myday, myMonth, myYear, myHour, myMinute;
    private String CurrentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        docsEditBinding = getViewDataBinding();
        mViewModel.setNavigator(this);

        if (getIntent() != null) {
            String data = getIntent().getStringExtra("DocsData");
            Documentdata docsData = CommonUtils.getSingleObject(data, Documentdata.class);

            mViewModel.hasExpiry.set(docsData.getHasExpiryDate());
            mViewModel.hasIdNumber.set(docsData.getHasIdentifyNumber());
            mViewModel.DocsID.set("" + docsData.getId());

            Log.e("data---", "dd---" + docsData.getHasExpiryDate());
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
    public void onClickExpiry() {
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, this, year, month, day);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }

    AlertDialog gallerywithcameradialog;

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void onClickImage() {
        AlertDialog.Builder gcbuilder = new AlertDialog.Builder(this,R.style.RIDEOTP_Theme_Dialog);

        View gcview = getLayoutInflater().inflate(R.layout.layout_show_gal_cam, null);

        gcbuilder.setView(gcview);
        gcbuilder.setCancelable(true);

        gallerywithcameradialog = gcbuilder.create();

        gcbuilder.setTitle(mViewModel.choose.get());

        if (gallerywithcameradialog != null && !gallerywithcameradialog.isShowing())
            gallerywithcameradialog.show();

        String option1 = mViewModel.camera.get();
        String option2 = mViewModel.gallery.get();

        ImageView cameric = gallerywithcameradialog.findViewById(R.id.prof_camera_img);
        ImageView galleryic = gallerywithcameradialog.findViewById(R.id.prof_gallery_img);
        TextView camerct = gallerywithcameradialog.findViewById(R.id.tv_camera);
        TextView galleryt = gallerywithcameradialog.findViewById(R.id.tv_gallery);
        TextView choose_one = gallerywithcameradialog.findViewById(R.id.prof_choose);
        choose_one.setText(mViewModel.choose.get());

        camerct.setText(option1);
        galleryt.setText(option2);

        cameric.setOnClickListener(view -> {
            if (Build.VERSION.SDK_INT >= 30) {
                dispatchTakePictureIntent();
            } else {
                cameraIntent();
            }
        });

        galleryic.setOnClickListener(view -> {
            galleryIntent();
        });
       /* androidx.appcompat.app.AlertDialog.Builder builder1 = new androidx.appcompat.app.AlertDialog.Builder(this);
        builder1.setMessage(R.string.txt_Choose);
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                R.string.text_camera,
                (dialog, id) -> {
                    dialog.cancel();
                    *//*if (Build.VERSION.SDK_INT == 30) {
                        Android11Intent();
                    }else{*//*
                    dispatchTakePictureIntent();
                    //}
                });

        builder1.setNegativeButton(
                R.string.text_galary,
                (dialog, id) -> {
                    dialog.cancel();
                    galleryIntent();
                });

        androidx.appcompat.app.AlertDialog alert11 = builder1.create();
        alert11.show();*/
    }

    @Override
    public void closeAct() {
        finish();
    }

    @Override
    public void onClickBack() {
        finish();
    }


    @Override
    public DocsEditViewModel getViewModel() {
        return mViewModel;
    }

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.docs_edit;
    }

    @Override
    public SharedPrefence getSharedPrefence() {
        return sharedPrefence;
    }


    String monthparse, dayPArse;

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        myYear = year;
        myday = dayOfMonth;
        myMonth = month + 1;

        if (myMonth < 10)
            monthparse = "0" + myMonth;
        else monthparse = String.valueOf(myMonth);

        if (myday < 10)
            dayPArse = "0" + myday;
        else dayPArse = String.valueOf(myday);

        Log.e("yeearr---", "year--" + Calendar.getInstance().get(Calendar.YEAR) + "____" + Integer.valueOf(myYear));

        if (myYear >= Calendar.getInstance().get(Calendar.YEAR)) {
            mViewModel.year.set("" + myYear);
            mViewModel.expDate.set(myYear + "-" + monthparse + "-" + dayPArse);
        } else {
            Toast.makeText(this, "Please choose future date", Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * Open gallery {@link Intent}
     **/
    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void galleryIntent() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !checkGranted(Constants.storagePermission)) {
            requestPermissionsSafely(Constants.storagePermission, Constants.REQUEST_PERMISSION);
        } else {
            Intent intent = CommonUtils.android11GalIntent();
            startActivityForResult(Intent.createChooser(intent, "Select File"), Constants.SELECT_FILE);
        }
    }

    /**
     * Open camera {@link Intent}
     **/

    Uri Imageuri;

    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void cameraIntent() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !checkGranted(Constants.storagePermission)) {
            requestPermissionsSafely(Constants.storagePermission, Constants.REQUEST_PERMISSION);
        } else {

            ContentValues values = new ContentValues();

            values.put(MediaStore.Images.Media.TITLE, "Camera Picture");
            values.put(MediaStore.Images.Media.DESCRIPTION, "From My Camera");
            Imageuri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

            Intent cameraintent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            cameraintent.putExtra(MediaStore.EXTRA_OUTPUT, Imageuri);
            startActivityForResult(cameraintent, Constants.REQUEST_CAMERA);

//        pictureUri=getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
//        intent.putExtra(MediaStore.EXTRA_OUTPUT,pictureUri);

         /*   if (cameraintent.resolveActivity(getPackageManager())!= null) {
                File photofile = null;
                try {
                    photofile=CreateImageFile();

                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                if (photofile != null) {
                    Uri photouri = FileProvider.getUriForFile(this,"com.tyt.driver.fileprovider",photofile);
                    cameraintent.putExtra(MediaStore.EXTRA_OUTPUT,photouri);
                    startActivityForResult(cameraintent, Constants.REQUEST_CAMERA);
                }
            }*/

        }

    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void dispatchTakePictureIntent() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !checkGranted(Constants.storagePermission)) {
            requestPermissionsSafely(Constants.storagePermission, Constants.REQUEST_PERMISSION);
        } else {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {

                File photoFile = null;
                try {
                    photoFile = CreateImageFile();
                } catch (IOException ex) {
                    Log.d("xxxDocsEdit", "dispatchTakePictureIntent: Error==>" + ex.getMessage());
                }
                if (photoFile != null) {
                    try {
                        Uri photoURI = FileProvider.getUriForFile(this,
                                "com.tyt.driver.fileprovider",
                                photoFile);
                        Log.d("xxxDocsEdit", "dispatchTakePictureIntent: photoURI =" + photoURI);
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                        takePictureIntent.putExtra("android.intent.extras.CAMERA_FACING", 1);
                    } catch (Exception e) {
                        Log.e("xxxDocsEdit", "dispatchTakePictureIntent: Error==" + e.getMessage());
                    }
                    Log.e("xxxDocsEdit", "dispatchTakePictureIntent: " + takePictureIntent.getDataString());
                    startActivityForResult(takePictureIntent, Constants.REQUEST_CAMERA);
                }
            }
        }
    }

    public File CreateImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = this.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        CurrentPhotoPath = image.getAbsolutePath();
        //getViewModel().mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void dispatchGalleryIntent() {
        Intent gallery11intent = new Intent("android.intent.action.GET_CONTENT");
        gallery11intent.setType("image/*");

    }

    private boolean hasExternalStoragePrivatePicture() {
        File path = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if (path != null) {
            File file = new File(CurrentPhotoPath);
            Log.d("xxxDocsEdit", "hasExternalStoragePrivatePicture: path =" + path + "  pathname =" + path.getName());
            Log.d("xxxDocsEdit", "hasExternalStoragePrivatePicture: file =" + file.getAbsolutePath() + "  filename =" + file.getName());

            return file.exists();
        }
        return false;
    }

    private void savePic() {
        int targetW = docsEditBinding.backImag.getWidth();
        int targetH = docsEditBinding.backImag.getHeight();

        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;

        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        int scaleFactor = Math.max(1, Math.min(photoW / targetW, photoH / targetH));

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(CurrentPhotoPath, bmOptions);
        // DocsEditViewModel.getImageUriDOC(bitmap);
        docsEditBinding.image.setImageBitmap(bitmap);
        Log.d("xxxDocsEdit", "savePic: ");
    }

    public final void notifyMediaStoreScanner(final File file) {
        try {
            Log.d("xxxDocsEdit", "notifyMediaStoreScanner: ");
            MediaStore.Images.Media.insertImage(MyApp.getmContext().getContentResolver(),
                    file.getAbsolutePath(), file.getName(), null);
            MyApp.getmContext().sendBroadcast(new Intent(
                    Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.e("xxxDocsEdit", "notifyMediaStoreScanner: Error==" + e.getMessage());

        }
    }

    private void galleryAddPic() {
        Log.d("xxxDocsEdit", "galleryAddPic: ");
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(CurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

    private void scanGallery(String path) {
        try {
            Log.d("xxxDocsEdit", "scanGallery: ");
            MediaScannerConnection.scanFile(MyApp.getmContext(), new String[]{path}, null, (path1, uri) -> {
                //unimplemeted method
                Log.d("xxxDocsEdit", "onScanCompleted() called with: path = [" + path1 + "], uri = [" + uri.getPath() + "]");
            });
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("xxxDocsEdit", "scanGallery: Error =" + e.getMessage());
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            gallerywithcameradialog.dismiss();
            if (requestCode == Constants.SELECT_FILE) {
                mViewModel.onSelectFromGalleryResult(data);
            } else if (requestCode == Constants.REQUEST_CAMERA) {
                //
            /*    if (Build.VERSION.SDK_INT == Build.VERSION_CODES.R) {
                    //saveForAndroid11(data);
                    //mViewModel.onCaptureImageResult(data);
                   *//* if (CurrentPhotoPath != null && !CurrentPhotoPath.isEmpty()) {
                        getViewModel().RealPath=CurrentPhotoPath;
                        getViewModel().bitmap_profilePicture.set(CurrentPhotoPath);
                    }
                    showMessage(getViewModel().RealPath=CurrentPhotoPath);*//*
                }else{*/
                getViewModel().RealPath = CurrentPhotoPath;
                getViewModel().bitmap_profilePicture.set(CurrentPhotoPath);
                //}

            }
        }
    }

  /*  private void saveForAndroid11(Intent data) {
        Bitmap bitmap = (Bitmap) data.getExtras().get("data");

        OutputStream fos;

        try {
            if (bitmap != null) {
                ContentResolver resolver = getBaseAct().getContentResolver();
                ContentValues contentValues = new ContentValues();
                contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME,"Image_11"+".jpg");
                contentValues.put(MediaStore.MediaColumns.MIME_TYPE,"image/jpeg");
                contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH,Environment.DIRECTORY_PICTURES+File.separator+"TYT Driver");
                Uri ImageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,contentValues);
                fos = (FileOutputStream) resolver.openOutputStream(Objects.requireNonNull(ImageUri));
                bitmap.compress(Bitmap.CompressFormat.JPEG,100,fos);
                Objects.requireNonNull(fos);

                showMessage("Image Saved");
            }else showMessage("Bitmap is Null");
        } catch (Exception e) {
            showMessage("Image Not Saved");
        }

    }*/

}