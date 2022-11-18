package com.tyt.client.ui.homeScreen.faq;

import com.tyt.client.retro.responsemodel.Faqmodel;
import com.tyt.client.retro.responsemodel.FavouriteLocations;
import com.tyt.client.ui.base.BaseActivity;
import com.tyt.client.ui.base.BaseView;

import java.util.List;

/**
 * Created by Mahi in 2021.
 */

public interface FaqNavigator extends BaseView {
    boolean isNetworkConnected();

    void loadFaq(List<Faqmodel> faqmodels);

    void loadFavourites(List<FavouriteLocations.FavLocData> favouriteLocations);

    void onClickBAck();

    public BaseActivity getAttachedContext();

    void deleteitem(String id);
}
