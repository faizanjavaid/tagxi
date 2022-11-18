package com.tyt.driver.ui.wallet;

import com.tyt.driver.retro.responsemodel.WalletHistoryModel;
import com.tyt.driver.ui.base.BaseActivity;
import com.tyt.driver.ui.base.BaseView;

import java.util.List;

/**
 * Created by root on 11/13/17.
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
