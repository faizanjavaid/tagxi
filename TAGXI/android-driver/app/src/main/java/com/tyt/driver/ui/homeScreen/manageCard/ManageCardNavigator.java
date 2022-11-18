package com.tyt.driver.ui.homeScreen.manageCard;

import com.tyt.driver.retro.responsemodel.CardListModel;
import com.tyt.driver.ui.base.BaseActivity;
import com.tyt.driver.ui.base.BaseView;

import java.util.List;

/**
 * Created by root on 11/13/17.
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
