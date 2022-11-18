package com.tyt.client.ui.homeScreen.getRefferal;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.tyt.client.BR;
import com.tyt.client.R;
import com.tyt.client.databinding.AddMoneyBinding;
import com.tyt.client.databinding.GetRefferalLayBinding;
import com.tyt.client.ui.base.BaseActivity;
import com.tyt.client.ui.base.BaseFragment;
import com.tyt.client.ui.homeScreen.HomeAct;
import com.tyt.client.ui.homeScreen.profile.ProfileFrag;

import javax.inject.Inject;

import static android.content.Context.CLIPBOARD_SERVICE;


/**
 * Refferal Activity to Apply the Referal Code From the user.
 *
* */

public class GetRefferalFrag extends BaseFragment<GetRefferalLayBinding, GetRefferalViewModel> implements GetRefferalNavigator {

    public static final String TAG = "GetRefferalFrag";
    @Inject
    public GetRefferalViewModel mViewModel;
    public GetRefferalLayBinding mBinding;


    // TODO: Rename and change types and number of parameters
    public static GetRefferalFrag newInstance() {
        return new GetRefferalFrag();
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

        mViewModel.getRefferalAPi();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }


    @Override
    public GetRefferalViewModel getViewModel() {
        return mViewModel;
    }

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.get_refferal_lay;
    }

    /**
     * returns reference of {@link BaseActivity}
     **/
    @Override
    public BaseActivity getBaseAct() {
        return ((BaseActivity) getActivity());
    }


    @Override
    public void onClickBack() {
        if (getActivity().getSupportFragmentManager().findFragmentByTag(GetRefferalFrag.TAG) != null) {
            ((HomeAct) getActivity()).ShowHideBar(true);
            Fragment frag = getActivity().getSupportFragmentManager().findFragmentByTag(GetRefferalFrag.TAG);
            //getActivity().getSupportFragmentManager().beginTransaction().remove(frag).commit();
            if (getActivity().getSupportFragmentManager().findFragmentByTag(ProfileFrag.TAG) == null)
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .disallowAddToBackStack()
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .add(R.id.fragment_frame, ProfileFrag.newInstance(), ProfileFrag.TAG)
                        .commit();
        } else {
            Log.e("elkfjefjke", "lejfioe");
        }
    }

    @Override
    public void onClickinvite() {
        Intent myIntent = new Intent(Intent.ACTION_SEND);
        myIntent.setType("text/plain");
        String firstLine = "Join me on TYT! using my code :" + " " + mBinding.refferalCode.getText().toString();
        String secondline = "To make easy your ride with TYT";
        myIntent.putExtra(Intent.EXTRA_TEXT, firstLine + secondline);
        startActivity(Intent.createChooser(myIntent, "Share using"));
    }

    @Override
    public void onClickCopy() {
        ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("label", mBinding.refferalCode.getText().toString());
        clipboard.setPrimaryClip(clip);
        Toast.makeText(getActivity(), "Code Copied!!", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onResume() {
        super.onResume();
        ((HomeAct) getActivity()).ShowHideBar(false);
    }
}
