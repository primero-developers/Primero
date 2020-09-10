package com.example.primero.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.primero.Models.FavModel;
import com.example.primero.Models.TurfModel;
import com.example.primero.SessionManager;

import java.util.ArrayList;
import java.util.List;

public class MainViewModel extends ViewModel {

    SessionManager sessionManager;

    MutableLiveData<List<TurfModel>> turfList = new MutableLiveData<>();
    MutableLiveData<List<FavModel>> favList = new MutableLiveData<>();

    public MainViewModel() {
        sessionManager = SessionManager.getInstance();
    }

    //GETTER
    public LiveData<List<TurfModel>> getTurfLive(){ return turfList; }
    public LiveData<List<FavModel>> getFavTurf(){ return favList; }

    //SETTER
    public void setTurfList(List<TurfModel> list){
        if (list != null && list != turfList.getValue()){
            turfList.setValue(list);
        }
    }
    public void setFavList(List<FavModel> list){
        if (list != null && list != favList.getValue()){
            favList.setValue(list);
        }
    }
}
