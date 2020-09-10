package com.example.primero.Repos;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.primero.Models.FavModel;
import com.example.primero.Models.TurfModel;
import com.example.primero.Models.UserModel;
import com.example.primero.R;
import com.example.primero.Room.AppDatabase;
import com.example.primero.SessionManager;
import com.example.primero.UI.AuthActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserRepo {

    private static final String TAG = "UserRepo";

    SessionManager sessionManager;
    AppDatabase mDb;

    FirebaseUser mUser;
    FirebaseFirestore firestore;
    CollectionReference userCollection;

    Context context;
    String uId ="";
    Map<String,Object> userMap = new HashMap<>();
    List<FavModel> favList = new ArrayList<>();
    UserModel userModel;


    String defProfileImg = "https://static.dribbble.com/users/5746/screenshots/4143186/dribbble-salvatier-avatar.jpg";
    public UserRepo(Context context) {
        this.context = context;
        sessionManager = SessionManager.getInstance();
        mDb = AppDatabase.getDatabase();
        mUser =FirebaseAuth.getInstance().getCurrentUser();
        firestore = FirebaseFirestore.getInstance();
        userCollection = firestore.collection("Users");
        if (mUser != null)
            uId = mUser.getUid();
    }


    public void setNewUserToFirestore(){

        Log.d(TAG, "setNewUserToFirestore: FRESH WORK new User setting up");

        final String uid = mUser.getUid();
        String phone = mUser.getPhoneNumber();
        String name = context.getResources().getString(R.string.def_avatar_name);

        Log.d(TAG, "setNewUserToFirestore: FRESH WORK setting user with UID : "+ uid);

        userMap.put("uId",uid);
        userMap.put("phone",phone);
        userMap.put("name",name);
        userMap.put("profileImg",defProfileImg);

        userCollection
                .document(uid)
                .set(userMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Log.d(TAG, "onComplete: FRESH WORK new user setted up with "+ task.getResult());
                            getUserFromFirestore();
                        }
                    }
                });

    }

    public void getUserFromFirestore(){

        if (uId!=null){
            Log.d(TAG, "getUserFromFirestore: FRESH WORK getting user init");
            userCollection.document(uId)
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {

                            if (documentSnapshot != null && documentSnapshot.exists()){
                                Log.d(TAG, "onSuccess: FRESH WORK GOT USER WITH "+ documentSnapshot.getData());
                                userModel = documentSnapshot.toObject(UserModel.class);
                                getFavListOnline();
                                setUserToRoom(userModel);
                            }
                            else {
                                setNewUserToFirestore();
                            }
                        }
                    });

        } else {
            Toast.makeText(context, "Authentication failed ", Toast.LENGTH_SHORT).show();
        }

    }

    public void getFavListOnline(){
        Log.d(TAG, "getFavListOnline: FRESH WORK fav list iinit with " + uId);
        userCollection.document(uId).collection("FavTurfs")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots != null && queryDocumentSnapshots.size() > 0){
                            favList.clear();
                            for(DocumentSnapshot document : queryDocumentSnapshots){
                                Log.d(TAG, "onSuccess: FRESH WORK FAV got fav list with " + document.getData());
                                favList.add(document.toObject(FavModel.class));
                            }
                            if (favList.size() > 0){
                                setFavToRoom(favList);
                            }
                        }
                    }
                });
    }

    public void setUserToRoom(UserModel model){

        Log.d(TAG, "setUserToRoom: FRESH WORK SETTING user to room init");

        userModel = model;
        if (userModel != null){
            mDb.userDao().insUser(userModel);
            sessionManager.setCurrentUser(userModel);
        }
    }

    public void setFavToRoom(List<FavModel> models){
        Log.d(TAG, "setFavToRoom: FRESH WORK FAv setting FAV to Room");
        mDb.favTurfDao().insertFavList(models);
    }

    public void setUserName(String name){
        userCollection.document(uId)
                .update("name",name)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        sessionManager.setGLOBAL_TRIGGER("NAME CHANGED");
                        getUserFromFirestore();
                    }
                });
    }


}