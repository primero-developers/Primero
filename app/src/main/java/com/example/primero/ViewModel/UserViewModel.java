package com.example.primero.ViewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.primero.Models.UserModel;
import com.example.primero.Repos.UserRepo;
import com.example.primero.SessionManager;
import com.google.firebase.firestore.auth.User;

public class UserViewModel extends ViewModel {

    UserRepo mRepo;
    SessionManager sessionManager;
    MutableLiveData<UserModel> user = new MutableLiveData<>();

    public UserViewModel() {
        sessionManager = SessionManager.getInstance();
    }

    public MutableLiveData<UserModel> getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        if (user != null)
            this.user.setValue(user);
    }
}
