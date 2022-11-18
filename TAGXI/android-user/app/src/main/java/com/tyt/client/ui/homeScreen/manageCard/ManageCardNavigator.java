package com.tyt.client.ui.homeScreen.manageCard;

import com.tyt.client.retro.responsemodel.CardListModel;
import com.tyt.client.ui.base.BaseActivity;
import com.tyt.client.ui.base.BaseView;

import java.util.List;

/**
 * Created by Mahi in 2021.
 */

public interface ManageCardNavigator extends BaseView {
    BaseActivity getBaseAct();
    void loadManageCardList(List<CardListModel> cardListModels);

    void openAddcardFrag();

    void intiatePayment(String clientToken);

    void deleteClick(String id);

    void clickBack();

    void clickCardItem(String id);
}
