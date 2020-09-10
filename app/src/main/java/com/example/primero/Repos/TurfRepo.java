package com.example.primero.Repos;

import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.primero.Models.FavModel;
import com.example.primero.Models.TurfModel;
import com.example.primero.Room.AppDatabase;
import com.example.primero.SessionManager;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class TurfRepo {

    private static final String TAG = "TurfRepo";

    SessionManager sessionManager;
    AppDatabase mDb;
    private CollectionReference turfRef;

    public List<TurfModel> turfList = new ArrayList<>();
    TurfModel model;

    public TurfRepo() {
        sessionManager = SessionManager.getInstance();
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        turfRef = firestore.collection("Turfs");
        mDb = AppDatabase.getDatabase();
    }

    public void getOnlineTurfList(){

        Log.d(TAG, "getOnlineTurfList:FRESH WORK GETTING TURFS init");

        turfRef
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        turfList.clear();
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots){
                            model = document.toObject(TurfModel.class);
                            Log.d(TAG, "onSuccess: GETTING TURFS turf " + model.getSlots().get(1));
                            turfList.add(model);
                        }
                        if (turfList.size() > 0){
                            setTurfListToRoom(turfList);
                        }

                    }
                });
    }



    public void setTurfListToRoom(List<TurfModel> list){
        mDb.turfDao().insertTurfList(list);
        sessionManager.FETCH_STATUS = "REMOTE";
        sessionManager.setFRESH_STATUS("NOT FRESH");
    }


}
