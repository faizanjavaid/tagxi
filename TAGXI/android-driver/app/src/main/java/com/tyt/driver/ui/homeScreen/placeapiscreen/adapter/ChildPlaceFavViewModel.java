package com.tyt.driver.ui.homeScreen.placeapiscreen.adapter;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.appcompat.widget.PopupMenu;

import android.view.View;

import com.tyt.driver.retro.base.BaseNetwork;
import com.tyt.driver.retro.base.BaseResponse;
import com.tyt.driver.retro.GitHubService;
import com.tyt.driver.retro.responsemodel.Favplace;
import com.tyt.driver.ui.base.BaseActivity;
import com.tyt.driver.ui.base.BaseView;
import com.tyt.driver.ui.homeScreen.placeapiscreen.PlaceApiNavigator;
import com.tyt.driver.utilz.Constants;
import com.tyt.driver.utilz.exception.CustomException;
import com.tyt.driver.utilz.SharedPrefence;

import java.util.HashMap;

/**
 * Created by root on 11/30/17.
 */

public class ChildPlaceFavViewModel extends BaseNetwork<BaseResponse, ChildPlaceFavViewModel.ChidPlaceItemListener> {

   public ObservableBoolean IsFavtitle,IsPlaceLayout;
   public ObservableField<String> place,NickName;
   GitHubService gitHubService;
   public Favplace favplace;
   public PopupMenu popupMenu;
   ChidPlaceItemListener adapterlister;
   HashMap<String, String> hashMap;
   SharedPrefence sharedPrefence;

   public ChildPlaceFavViewModel(GitHubService gitHubService, Favplace favplace, ChidPlaceItemListener adapterlister,
                                 HashMap<String, String> hashMap, SharedPrefence sharedPrefence) {
      super(gitHubService);
      this.favplace=favplace;
      this.gitHubService=gitHubService;
      this.adapterlister=adapterlister;
      this.hashMap=hashMap;
      this.sharedPrefence =sharedPrefence;
      place=new ObservableField<>(favplace.placeId);
      NickName=new ObservableField<>(favplace.nickName);
      IsFavtitle=new ObservableBoolean(favplace.IsFavTit);
      IsPlaceLayout=new ObservableBoolean(favplace.IsPlaceLayout);
   }

   public interface ChidPlaceItemListener extends BaseView {
      PlaceApiNavigator getListener();
      void Deleteditems(int i);
      void Favselected(Favplace favplace);
      BaseActivity getBaseAct();
   }



   /** Called when any of the Favourite address was selected **/
   public void onFavselected(View view){
      adapterlister.Favselected(favplace);
   }

   /** Callback for successful API calls
    * @param taskId ID of the API task
    * @param response Response model **/
   @Override
   public void onSuccessfulApi(long taskId, BaseResponse response) {
      adapterlister.getListener().showProgress(false);
      /*if(response.successMessage.equalsIgnoreCase("Favorite_Deleted_Successfully")){
            adapterlister.Deleteditems(response.favid);
      }*/
   }

   /** Callback for failed API calls
    * @param taskId ID of the API task
    * @param e Exception msg **/
   @Override
   public void onFailureApi(long taskId, CustomException e) {
      adapterlister.getListener().showProgress(false);
      adapterlister.getListener().showMessage(e);
      if (e.getCode() == Constants.ErrorCode.COMPANY_CREDENTIALS_NOT_VALID ||
              e.getCode() == Constants.ErrorCode.COMPANY_KEY_DATE_EXPIRED ||
              e.getCode() == Constants.ErrorCode.COMPANY_KEY_NOT_ACTIVE ||
              e.getCode() == Constants.ErrorCode.COMPANY_KEY_NOT_VALID) {
         if(getmNavigator()!=null);
         getmNavigator().refreshCompanyKey();
      }
   }

   /** Returns a {@link HashMap} with query parameters required for API calls **/
   @Override
   public HashMap<String, String> getMap() {
      return hashMap;
   }

}
