package com.tyt.client.ui.homeScreen.placeapiscreen;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;
import com.tyt.client.retro.GitHubService;
import com.tyt.client.ui.homeScreen.placeapiscreen.adapter.PlaceandFavAdapter;
import com.tyt.client.utilz.Constants;
import com.tyt.client.utilz.SharedPrefence;

import java.util.HashMap;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Mahi in 2021.
 */
@Module
public class PlaceApiDaggerModel {

    @Provides
    LinearLayoutManager provideLinearLayoutManage(PlaceApiAct placeApiAct) {
        return new LinearLayoutManager(placeApiAct);
    }

    @Provides
    PlaceandFavAdapter PlaceandFavAdapterAdapter(@Named(Constants.ourApp) GitHubService gitHubService, SharedPrefence sharedPrefence,
                                                 HashMap<String,String> hashMap, Gson gson, PlaceApiAct placeApiAct) {
        return new PlaceandFavAdapter();
    }

    @Provides
    @Named("HashMapData")
    static HashMap<String, String> provideData(PlaceApiAct placeApiAct) {
        return placeApiAct.Bindabledata;
    }

}
