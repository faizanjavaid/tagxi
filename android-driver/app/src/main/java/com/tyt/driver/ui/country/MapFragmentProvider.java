package com.tyt.driver.ui.country;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class MapFragmentProvider {

    @ContributesAndroidInjector(modules = MapDaggerModel.class)
    abstract Countrylistdialog  provideSheduleCancelDialogFrag();
}
