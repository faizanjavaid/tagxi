package com.tyt.client.ui.homeScreen.profile.profileEdit;

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
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.tyt.client.BR;
import com.tyt.client.R;
import com.tyt.client.databinding.ActivityProfileEditBinding;
import com.tyt.client.retro.responsemodel.TranslationModel;
import com.tyt.client.ui.base.BaseActivity;
import com.tyt.client.ui.base.BaseFragment;
import com.tyt.client.ui.country.Countrylistdialog;
import com.tyt.client.ui.homeScreen.HomeAct;
import com.tyt.client.ui.homeScreen.mapFrag.MapFrag;
import com.tyt.client.ui.homeScreen.profile.ProfileFrag;
import com.tyt.client.utilz.Constants;
import com.tyt.client.utilz.SharedPrefence;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import javax.inject.Inject;

/**
 * Fragment which is used to update the user details.
 *
* */

public class ProfileEditFrag extends BaseFragment<ActivityProfileEditBinding, ProfileEditViewModel> implements ProfileEditNavigator {

    public static final String TAG = "ProfileEditFrag";
    @Inject
    public ProfileEditViewModel mViewModel;
    public ActivityProfileEditBinding mBinding;

    @Inject
    SharedPrefence sharedPrefence;
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

    @Override
    public void openImageChooseAlert() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !((BaseActivity) requireActivity()).checkGranted(Constants.storagePermission)) {
            ((BaseActivity) requireActivity()).requestPermissionsSafely(Constants.storagePermission, Constants.REQUEST_PERMISSION);
            return;
        }

        AlertDialog.Builder gcbuilder = new AlertDialog.Builder(getBaseActivity());

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
            } else cameraIntent();
        });

        galleryic.setOnClickListener(view -> {
            galleryIntent();
        });

    }

    @Override
    public void openHomeAct() {
        ((HomeAct) getActivity()).onClickHome();
    }

    @Override
    public void onResume() {
        super.onResume();
        sharedPrefence.saveBoolean(SharedPrefence.dont_refresh, true);
        mViewModel.countryCode.set(Constants.defaultCountryCode);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(
                countryChoose, new IntentFilter(Constants.INTENT_COUNTRY_CHOOSE));
    }

    @Override
    public void clickBack() {
        getBaseAct().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_frame, ProfileFrag.newInstance(), ProfileFrag.TAG)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commitNow();
    }


    private void galleryIntent() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(Intent.createChooser(intent, "Select File"), Constants.SELECT_FILE);
    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, Constants.REQUEST_CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        sharedPrefence.saveBoolean(SharedPrefence.dont_refresh, true);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Constants.SELECT_FILE) {
                gallerywithcameradialog.dismiss();
                mViewModel.onSelectFromGalleryResult(data);
            } else if (requestCode == Constants.REQUEST_CAMERA) {
                gallerywithcameradialog.dismiss();
                if (Build.VERSION.SDK_INT >= 30) {
                    mViewModel.bitmap_profilePicture.set(CurrentPhotoPath);
                    mViewModel.realPath = CurrentPhotoPath;
                } else {
                    mViewModel.onCaptureImageResult(data);
                }
            }
        }
    }

    private final BroadcastReceiver countryChoose = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                Log.e("CountryCode==", "Code==" + mViewModel.countryCode.get());
                mViewModel.countryCode.set(intent.getStringExtra(Constants.countryCode));
            }
        }
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        sharedPrefence.saveBoolean(SharedPrefence.dont_refresh, false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        sharedPrefence.saveBoolean(SharedPrefence.dont_refresh, false);
    }

    private void dispatchTakePictureIntent() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !getBaseActivity().checkGranted(Constants.storagePermission)) {
            getBaseActivity().requestPermissionsSafely(Constants.storagePermission, Constants.REQUEST_PERMISSION);
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
                        Uri photoURI = FileProvider.getUriForFile(getBaseActivity(),
                                "com.tyt.client.fileprovider",
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
        File storageDir = getBaseActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        CurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

}
