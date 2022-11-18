package com.tyt.driver.ui.homeScreen.profile.profileEdit;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.tyt.driver.BR;
import com.tyt.driver.R;
import com.tyt.driver.databinding.ActivityProfileEditBinding;
import com.tyt.driver.ui.base.BaseActivity;
import com.tyt.driver.ui.base.BaseFragment;
import com.tyt.driver.ui.country.Countrylistdialog;
import com.tyt.driver.ui.homeScreen.HomeAct;
import com.tyt.driver.ui.homeScreen.profile.ProfileFrag;
import com.tyt.driver.utilz.CommonUtils;
import com.tyt.driver.utilz.Constants;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.inject.Inject;

public class ProfileEditFrag extends BaseFragment<ActivityProfileEditBinding, ProfileEditViewModel> implements ProfileEditNavigator {

    public static final String TAG = "ProfileEditFrag";
    @Inject
    public ProfileEditViewModel mViewModel;
    public ActivityProfileEditBinding mBinding;
    private String CurrentPhotoPath;


    // TODO: Rename and change types and number of parameters
    public static ProfileEditFrag newInstance() {
        return new ProfileEditFrag();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBinding = getViewDataBinding();
        mViewModel.setNavigator(this);
    }


    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(countryChoose);
    }


    @Override
    public ProfileEditViewModel getViewModel() {
        return mViewModel;
    }

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_profile_edit;
    }

    /**
     * returns reference of {@link BaseActivity}
     **/
    @Override
    public BaseActivity getBaseAct() {
        return ((BaseActivity) getActivity());
    }

    @Override
    public void chooseCountryCode() {
        Countrylistdialog newInstance = Countrylistdialog.newInstance();
        newInstance.show(getActivity().getSupportFragmentManager());
    }

    AlertDialog gallerywithcameradialog;

    @SuppressLint("NewApi")
    @Override
    public void openImageChooseAlert() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !((BaseActivity) getActivity()).checkGranted(Constants.storagePermission)) {
            ((BaseActivity) getActivity()).requestPermissionsSafely(Constants.storagePermission, Constants.REQUEST_PERMISSION);
            return;
        }

        AlertDialog.Builder gcbuilder = new AlertDialog.Builder(getBaseActivity(), R.style.RIDEOTP_Theme_Dialog);

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

        /*String option1 = ((BaseActivity) getActivity()).getTranslatedString(R.string.text_camera);
        String option2 = ((BaseActivity) getActivity()).getTranslatedString(R.string.text_galary);
        String[] options = new String[]{option1, option2};

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(((BaseActivity) getActivity()).getTranslatedString(R.string.txt_Choose));
        builder.setCancelable(true);
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    cameraIntent();
                } else if (which == 1) {
                    galleryIntent();
                }
            }
        }).show();*/
    }

    @Override
    public void openHomeAct() {
        ((HomeAct) getActivity()).onClickHome();
        // startActivity(new Intent(getActivity(),HomeAct.class));
    }


    @SuppressLint("NewApi")
    @Override
    public void onResume() {
        super.onResume();
        mViewModel.countryCode.set(Constants.defaultCountryCode);

        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(
                countryChoose, new IntentFilter(Constants.INTENT_COUNTRY_CHOOSE));

        ((HomeAct) getActivity()).ShowOnloneOffline(false);
    }

    @Override
    public void clickBack() {
        Fragment fragment = getActivity().getSupportFragmentManager().findFragmentByTag(ProfileEditFrag.TAG);
        if (fragment != null)
            getBaseAct().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_frame, ProfileFrag.newInstance(), ProfileFrag.TAG)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .commit();
    }


    @SuppressLint("NewApi")
    private void galleryIntent() {
        Intent gintent;
        if (Build.VERSION.SDK_INT >= 30) {
            gintent = CommonUtils.android11GalIntent();
        } else {
            gintent = new Intent()
                    .setType("image/*")
                    .setAction(Intent.ACTION_GET_CONTENT);
        }
        startActivityForResult(Intent.createChooser(gintent, "Select File"), Constants.SELECT_FILE);

    }


    @SuppressLint("NewApi")
    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, Constants.REQUEST_CAMERA);
    }


    @SuppressLint("NewApi")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            gallerywithcameradialog.dismiss();
            if (requestCode == Constants.SELECT_FILE) {
                mViewModel.onSelectFromGalleryResult(data);
            } else if (requestCode == Constants.REQUEST_CAMERA) {
                if (Build.VERSION.SDK_INT >= 30) {
                    getViewModel().realPath = CurrentPhotoPath;
                    getViewModel().bitmap_profilePicture.set(CurrentPhotoPath);
                } else {
                    mViewModel.onCaptureImageResult(data);
                }
            }
        }
    }


    @SuppressLint("NewApi")
    private final BroadcastReceiver countryChoose = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                Log.e("CountryCode==", "Code==" + mViewModel.countryCode.get());
                mViewModel.countryCode.set(intent.getStringExtra(Constants.countryCode));
            }
        }
    };

    @SuppressLint("NewApi")
    private void dispatchTakePictureIntent() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !getBaseAct().checkGranted(Constants.storagePermission)) {
            getBaseAct().requestPermissionsSafely(Constants.storagePermission, Constants.REQUEST_PERMISSION);
        } else {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            if (takePictureIntent.resolveActivity(getBaseAct().getPackageManager()) != null) {

                File photoFile = null;
                try {
                    photoFile = CreateImageFile();
                } catch (IOException ex) {
                    Log.d("xxxDocsEdit", "dispatchTakePictureIntent: Error==>" + ex.getMessage());
                }
                if (photoFile != null) {
                    try {
                        Uri photoURI = FileProvider.getUriForFile(getBaseAct(),
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
        @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getBaseAct().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        CurrentPhotoPath = image.getAbsolutePath();
        //getViewModel().mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

}
