package com.tyt.driver.ui.homeScreen.placeapiscreen;


import android.content.Context;
import androidx.databinding.ObservableField;
import android.text.Editable;
import android.view.View;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.tyt.driver.retro.base.BaseNetwork;
import com.tyt.driver.retro.base.BaseResponse;
import com.tyt.driver.retro.GitHubMapService;
import com.tyt.driver.retro.GitHubService;
import com.tyt.driver.retro.responsemodel.Favplace;
import com.tyt.driver.utilz.Constants;
import com.tyt.driver.utilz.exception.CustomException;
import com.tyt.driver.utilz.SharedPrefence;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by root on 11/30/17.
 */


public class PlaceApiViewModel extends BaseNetwork<BaseResponse, PlaceApiNavigator> {


    @Inject

    SharedPrefence sharedPrefence;


    public ObservableField<String> title;


    @Named(Constants.googleMap)

    @Inject

    GitHubMapService mapService;


    @Inject

    Context context;


    List<Favplace> favplaces = new ArrayList<>();

    @Inject

    HashMap<String, String> hashMap;


    @Inject

    Gson gson;


    @Inject

    public PlaceApiViewModel(@Named(Constants.ourApp) GitHubService gitHubService,
                             @Named("HashMapData") HashMap<String, String> stringHashMap,
                             SharedPrefence sharedPrefence, Gson gson) {

        super(gitHubService, sharedPrefence, gson);

        hashMap = stringHashMap;

        title = new ObservableField<>();

    }

    /** Calls favourites list API to get saved locations **/
    public void GetFavListData() {
        title.set(hashMap.get(Constants.Extra_identity));

        setIsLoading(true);

        hashMap.clear();

        hashMap.put(Constants.NetworkParameters.client_id, sharedPrefence.getCompanyID());
        hashMap.put(Constants.NetworkParameters.client_token, sharedPrefence.getCompanyToken());



    }

    /** Back button listener **/
    public void onClickBack(View view) {

        getmNavigator().FinishAct();

    }

    /** Search box clear button listener **/
    public void onClickClearBtn(View view) {
        getmNavigator().clearText();
    }





  /*  public void onPlaceonTextChanged(CharSequence charSequence, int i, int i1, int i2){

        if(charSequence.length()==0){

            getmNavigator().showclearButton(false);

            BaseResponse baseResponse = gson.fromJson(sharedPrefence.Getvalue(SharedPrefence.FAVLIST), BaseResponse.class);

            getmNavigator().addList(baseResponse.getFavplace());

            System.out.println("++onPlaceonTextChanged+++");

        }

    }

*/

  /** Listener for search box text change
   * @param editable used to search locations matching given search term **/
    public void onPlaceEditTextchanged(Editable editable) {

        if (editable.toString().isEmpty() && editable.length() == 0) {

            System.out.println("++onPlaceEditTextchanged+++");

            getmNavigator().showclearButton(false);

           // if (baseResponse != null)

               /* if (baseResponse.getFavplace() != null)

                    getmNavigator().addList(baseResponse.getFavplace());*/


        } else if (!editable.toString().isEmpty() && editable.length() >= 3) {

            getmNavigator().showclearButton(true);

            getPlaceApicall(editable.toString());

        } else {

            getmNavigator().showclearButton(false);

            /*if (baseResponse != null && baseResponse.getFavplace() != null)
                getmNavigator().addList(baseResponse.getFavplace());*/

        }

    }


    /** Call places search autocomplete API using given search query string
     * @param s search query **/
    private void getPlaceApicall(String s) {

        LatLng latLng;


        String input = null;

       /* try {
            if (Locale.getDefault().getLanguage().equalsIgnoreCase("ar"))
                input = s;
            else
                input = URLEncoder.encode(s, "utf8");

        } catch (UnsupportedEncodingException e) {

            e.printStackTrace();

        }*/

        input = s;

        favplaces.clear();

        /*  "country:IN","500"*/

        Callback placesapi_callback = new Callback<JsonObject>() {

            @Override

            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {


                if (response.isSuccessful() && response.body().get("status").getAsString().equalsIgnoreCase("OK")) {

                    JsonArray predsJsonArray = response.body().getAsJsonArray("predictions");


                    for (int i = 0; i < predsJsonArray.size(); i++) {

                        Favplace favplace = new Favplace();

                        favplace.IsPlaceLayout = true;


                        String address = predsJsonArray.get(i).getAsJsonObject().get("description").getAsString();

                        favplace.PlaceApiOGaddress = address;

                        favplace.IsPlaceLayout = true;

                        if (address.contains(",")) {


                            favplace.nickName = address.substring(0, address.indexOf(","));

                            int firstIndex = address.indexOf(",");

                            favplace.placeId = address.substring(firstIndex).replace(",", "").trim();

                        } else if (!address.contains(",")) {

                            favplace.nickName = address;

                            favplace.placeId = "";

                        }


                        favplaces.add(favplace);


                    }


                    getmNavigator().addList(favplaces);


                }


            }


            @Override

            public void onFailure(Call<JsonObject> call, Throwable t) {

                System.out.println("++" + t.toString());

            }

        };
    }

    /** Callback for successful API calls
     * @param taskId ID of the API task
     * @param response {@link BaseResponse} model **/
    @Override
    public void onSuccessfulApi(long taskId, BaseResponse response) {

        setIsLoading(false);

       /* if (response.successMessage.equalsIgnoreCase("Favorite_Added_Successfully")) {

            if (response.getFavplace().size() > 0) {

                response.getFavplace().get(0).IsFavTit = true;

                getmNavigator().addList(response.getFavplace());

                String Favlist = gson.toJson(response);

                sharedPrefence.savevalue(SharedPrefence.FAVLIST, Favlist);

            }


        }*/


    }

    /** Callback for API call fails
     * @param taskId Id of the API call
     * @param e Exception msg. **/
    @Override
    public void onFailureApi(long taskId, CustomException e) {
        if (e.getCode() != Constants.ErrorCode.EMPTY_FAV_LIST)
            getmNavigator().showMessage(e);
        else if (e.getCode() == Constants.ErrorCode.COMPANY_CREDENTIALS_NOT_VALID ||
                e.getCode() == Constants.ErrorCode.COMPANY_KEY_DATE_EXPIRED ||
                e.getCode() == Constants.ErrorCode.COMPANY_KEY_NOT_ACTIVE ||
                e.getCode() == Constants.ErrorCode.COMPANY_KEY_NOT_VALID) {
            getmNavigator().showMessage(e);
            getmNavigator().refreshCompanyKey();
        }
    }

    /** Returns {@link HashMap} with query parameters used for API calls **/
    @Override
    public HashMap<String, String> getMap() {
        return hashMap;
    }

}