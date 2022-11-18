package com.tyt.client.ui.homeScreen.placeapiscreen.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.ViewGroup;

import com.tyt.client.retro.responsemodel.Favplace;
import com.tyt.client.ui.base.BaseActivity;
import com.tyt.client.ui.base.BaseViewHolder;

/**
 * Created by root on 11/30/17.
 */

public class PlaceandFavAdapter extends RecyclerView.Adapter<BaseViewHolder> {
    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
   /* public GitHubService gitHubService;
    public SharedPrefence sharedPrefence;
    public HashMap<String, String> hashMap;
    public List<Favplace> favList;
    public Gson gson;
    public PlaceApiNavigator listener;
    public PlaceApiAct placeApiAct;

    public PlaceandFavAdapter(ArrayList<Favplace> favplaces, GitHubService gitHubService,
                              SharedPrefence sharedPrefence, HashMap<String, String> hashMap, Gson gson,
                              PlaceApiAct placeApiAct) {
        favList = favplaces;
        this.gitHubService = gitHubService;
        this.sharedPrefence = sharedPrefence;
        this.hashMap = hashMap;
        this.gson = gson;
        this.placeApiAct=placeApiAct;
        this.listener = placeApiAct;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ChildViewHolder(childItemBinding);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        return favList.size();
    }

    *//** Populate favourite places list
     * @param favplace {@link Favplace} **//*
    public void addList(List<Favplace> favplace) {
        favList.clear();
        favList.addAll(favplace);
        notifyDataSetChanged();
    }

    public class ChildViewHolder extends BaseViewHolder implements ChildPlaceFavViewModel.ChidPlaceItemListener {
        //private ChildFavheaderBinding mBinding;

        private ChildPlaceFavViewModel childPlaceFavViewModel;

        public ChildViewHolder(ChildFavheaderBinding binding) {
            super(binding.getRoot());
            this.mBinding = binding;

        }

        @Override
        public void onBind(int position) {
            Favplace favplace = favList.get(position);

            childPlaceFavViewModel = new ChildPlaceFavViewModel(gitHubService, favplace, this, hashMap, sharedPrefence);
            mBinding.setViewModel(childPlaceFavViewModel);
            mBinding.executePendingBindings();


        }


        @Override
        public PlaceApiNavigator getListener() {
            return listener;
        }

        *//** Delete favourite at specific position
         * @param i position of clicked item **//*
        @Override
        public void Deleteditems(int i) {
            int z = 0, y = 0;
            Iterator it = favList.iterator();

            while (it.hasNext()) {
                Favplace obj = (Favplace) it.next();
                if (getAdapterPosition() == 0 && favList.size() > 1) {

                    if (y == 1) {
                        obj.IsFavTit = true;
                        favList.set(y, obj);
                        break;
                    }


                }

                y++;
            }
            Iterator itz = favList.iterator();

            while (itz.hasNext()) {
                Favplace obj = (Favplace) itz.next();
                if (obj.Favid == i) {
                    favList.remove(obj);
                 *//*   notifyItemRemoved(z);
                    notifyItemChanged(z);*//*
                 notifyDataSetChanged();
                 *//*   if(i==0&&favList.size() > 1){
                        notifyItemChanged(z+1);
                    }*//*
                    break;
                }
                z++;
            }


            BaseResponse baseResponse = gson.fromJson(sharedPrefence.Getvalue(SharedPrefence.FAVLIST), BaseResponse.class);
            baseResponse.getFavplace().clear();
            baseResponse.setFavplace(favList);
            sharedPrefence.savevalue(SharedPrefence.FAVLIST, gson.toJson(baseResponse));

        }

        *//** Called when {@link Favplace} was clicked from the list
         * @param favplace - {@link Favplace} **//*
        @Override
        public void Favselected(Favplace favplace) {
            Intent intent=new Intent();

            if(favplace.IsPlaceLayout){
                intent.putExtra(Constants.EXTRA_Data,favplace.PlaceApiOGaddress);
            }else {
                intent.putExtra(Constants.EXTRA_Data,favplace.placeId);
            }

            placeApiAct.setResult(Constants.REQUEST_CODE_AUTOCOMPLETE,intent);
            placeApiAct.finish();

        }

        *//** Returns reference of the {@link BaseActivity} **//*
        @Override
        public BaseActivity getBaseAct() {
            return getBaseAct();
        }

    }
*/
}
