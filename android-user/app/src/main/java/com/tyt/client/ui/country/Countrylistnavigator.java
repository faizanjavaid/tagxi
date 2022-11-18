package com.tyt.client.ui.country;


import com.tyt.client.ui.base.BaseView;

interface Countrylistnavigator extends BaseView {
   // void countryResponse(ArrayList<Object> data);

     void countryResponse(Object data);

    void dismissDialg();

    void clickedItem(String flag, String code, String name, String ID);
}
