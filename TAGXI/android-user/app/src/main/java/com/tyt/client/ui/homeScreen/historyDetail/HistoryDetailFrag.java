package com.tyt.client.ui.homeScreen.historyDetail;

import android.app.Dialog;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.tyt.client.BR;
import com.tyt.client.R;
import com.tyt.client.databinding.HistoryDetailsBinding;
import com.tyt.client.retro.responsemodel.CancelReasonModel;
import com.tyt.client.ui.base.BaseActivity;
import com.tyt.client.ui.base.BaseFragment;
import com.tyt.client.ui.homeScreen.HomeAct;
import com.tyt.client.ui.homeScreen.tripscreen.CancelReasonAdapter;

import java.util.List;
import java.util.Objects;

import javax.inject.Inject;


/**
 * Fragment class to show the details of the particular trip in history page.
 *
* */

public class HistoryDetailFrag extends BaseFragment<HistoryDetailsBinding, HistoryDetailViewModel> implements HistoryDetailNavigator {

    public static final String TAG = "HistoryDetailFrag";
    @Inject
    public HistoryDetailViewModel mViewModel;
    public HistoryDetailsBinding mBinding;
    public static String IDD = "";
    ShimmerFrameLayout shimmerFrameLayout;

    // TODO: Rename and change types and number of parameters
    public static HistoryDetailFrag newInstance(String id) {
        IDD = id;
        return new HistoryDetailFrag();
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

        shimmerFrameLayout=view.findViewById(R.id.detail_shim);

        mViewModel.getSingleHistoryApi(IDD);
        mViewModel.IDD.set(IDD);
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
    public HistoryDetailViewModel getViewModel() {
        return mViewModel;
    }

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.history_details;
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
        if (getActivity().getSupportFragmentManager().findFragmentByTag(TAG) != null) {
            ((HomeAct) getActivity()).ShowHideBar(true);
            Fragment fragment = getActivity().getSupportFragmentManager().findFragmentByTag(TAG);
            getActivity().getSupportFragmentManager().beginTransaction().remove(fragment).commit();
        }
    }


    Dialog cancelReasonDialog;
    EditText editText;
    CancelReasonAdapter cancelReasonAdapter;

    @Override
    public void loadCancelAlert(List<CancelReasonModel> reasonModels) {

        reasonModels.add(new CancelReasonModel(0, "Others"));

        if (cancelReasonDialog != null)
            if (!cancelReasonDialog.isShowing()) {
                cancelReasonDialog.show();
                return;
            }

        cancelReasonDialog = new Dialog(getActivity());
        cancelReasonDialog.setContentView(R.layout.cancel_reason_dialog);

        TextView tTitle = cancelReasonDialog.findViewById(R.id.choose_reason);
        TextView tConfirm = cancelReasonDialog.findViewById(R.id.txt_confirm);
        editText = cancelReasonDialog.findViewById(R.id.cancel_reason_edit);

        tTitle.setText(getBaseActivity().getTranslatedString(R.string.txt_choose_reason));
        tConfirm.setText(getBaseActivity().getTranslatedString(R.string.txt_confirm));
        editText.setHint(getBaseActivity().getTranslatedString(R.string.txt_raise_reason));

        editText = cancelReasonDialog.findViewById(R.id.cancel_reason_edit);
        RelativeLayout relativeLayout = cancelReasonDialog.findViewById(R.id.confirm);
        RecyclerView recyclerView = cancelReasonDialog.findViewById(R.id.recycle);
        cancelReasonAdapter = new CancelReasonAdapter(getActivity(), reasonModels, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(cancelReasonAdapter);

        relativeLayout.setOnClickListener(v -> {
            if (!editText.getText().toString().isEmpty())
                mViewModel.reasonText.set(editText.getText().toString());

            mViewModel.cancelApi();
        });

        cancelReasonDialog.show();

    }

    @Override
    public void selectedReason(boolean clickedStatus) {
        if (clickedStatus)
            editText.setVisibility(View.VISIBLE);
        else {
            if (!editText.getText().toString().isEmpty())
                editText.setText("");
            editText.setVisibility(View.GONE);
        }
    }

    @Override
    public String getItemPosition() {
        return cancelReasonAdapter != null ? cancelReasonAdapter.getSelectPosition() : "";
    }

    @Override
    public View getdriverimg() {
        return getViewDataBinding().userImg;
    }

    @Override
    public View getCarImg() {
        return getViewDataBinding().carimg;
    }

    @Override
    public void startshim() {
        shimmerFrameLayout.startShimmer();
        getViewDataBinding().detailShim.setVisibility(View.VISIBLE);
        getViewDataBinding().hisDetailMainLay.setVisibility(View.GONE);
    }

    @Override
    public void stopshim() {
        shimmerFrameLayout.startShimmer();
        getViewDataBinding().detailShim.setVisibility(View.GONE);
        getViewDataBinding().hisDetailMainLay.setVisibility(View.VISIBLE);
    }


    @Override
    public void onResume() {
        super.onResume();
        ((HomeAct) requireActivity()).ShowHideBar(false);
    }
}
