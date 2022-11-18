/*
package com.gotaxi.client.ui.homeScreen.mapscrn.adapter;

import androidx.databinding.BindingAdapter;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.gotaxi.client.retro.responsemodel.Type;

*/
/**
 * Created by root on 11/16/17.
 *//*


public class ChildCarsTypesViewModel {
    Type request;
    public ObservableField<String> name;
    public ObservableField<String> carurl;
    public ObservableBoolean Isselected;
    public ObservableBoolean isDriverAvailable=new ObservableBoolean(true);
    public CarsTypesViewModelListener mListener;
    public ChildCarsTypesViewModel(Type request,CarsTypesViewModelListener listener) {
        this.request=request;
        this.mListener=listener;
        name=new ObservableField<>(request.name);
//        duration=new ObservableField<>(request.duration);
        carurl=new ObservableField<>(request.icon);
        Isselected=new ObservableBoolean(request.isselected==null?false:request.isselected);
        isDriverAvailable.set((request.drivers!=null&&request.drivers.size()>0));
    }

    */
/** called when a car in list of cars clicked **//*

    public void onItemCarClick() {
        request.isselected=true;
        mListener.onItemCarClick(request);
    }

    */
/** custom {@link BindingAdapter} function to set image of car **//*

    @BindingAdapter("childcarimageUrl")
    public static void setImageUrl(ImageView imageView, String url) {
        Glide.with(imageView.getContext()).load(url).into(imageView);

    }

    public interface CarsTypesViewModelListener {
        void onItemCarClick(Type id);
    }
}
*/
