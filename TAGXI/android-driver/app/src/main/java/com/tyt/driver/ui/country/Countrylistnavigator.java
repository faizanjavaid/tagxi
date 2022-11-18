package com.tyt.driver.ui.country;


import com.tyt.driver.ui.base.BaseView;

interface Countrylistnavigator extends BaseView {
   // void countryResponse(ArrayList<Object> data);

     void countryResponse(Object data);

    void dismissDialg();

    void clickedItem(String flag, String code, String name, String ID);
}
