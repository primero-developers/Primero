package com.example.primero;

import android.app.Application;
import android.content.Context;

import com.example.primero.Room.AppDatabase;

public class BaseApplcation extends Application {

    //VARIABLES
    Context context;
    SessionManager sessionManager;
    AppDatabase appDatabase;

    @Override
    public void onCreate() {
        super.onCreate();

        context = getApplicationContext();
        sessionManager = SessionManager.getInstance();
        sessionManager.initSession(context);
        appDatabase = AppDatabase.initDatabase(context);

    }
}
