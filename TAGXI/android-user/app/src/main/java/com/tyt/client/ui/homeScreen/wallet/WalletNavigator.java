package com.tyt.client.ui.homeScreen.wallet;

import com.tyt.client.retro.responsemodel.WalletHistoryModel;
import com.tyt.client.ui.base.BaseActivity;
import com.tyt.client.ui.base.BaseView;

import java.util.List;

/**
 * Created by Mahi in 2021.
 */

public interface WalletNavigator extends BaseView {
    BaseActivity getBaseAct();

    boolean isNetworkConnected();

    void openHomePage();

    void onCLickAddCard();

    void onClickAddMoney();

    void loadWalletHistoryData(List<WalletHistoryModel.WalletHistoryData> walletHistoryData, String currencySymbol);

    void onCLickBack();
}
