package com.example.primero.Worker;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.primero.Models.UserModel;
import com.example.primero.Repos.UserRepo;
import com.example.primero.SessionManager;

public class UserWorker extends Worker {

    private static final String TAG = "UserWorker";


    UserRepo userRepo;
    SessionManager sessionManager;
    UserModel model;

    public UserWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        userRepo = new UserRepo(context);
        sessionManager = SessionManager.getInstance();
    }

    @NonNull
    @Override
    public Result doWork() {

        Log.d(TAG, "doWork: FRESH WORK worker initiated");

        userRepo.getUserFromFirestore();
        return Result.success();
    }
}
