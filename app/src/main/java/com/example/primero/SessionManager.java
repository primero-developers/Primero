package com.example.primero;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.primero.Models.UserModel;

public class SessionManager {

    private static final String TAG = "SessionManager";

    //Variables
    Context context;
    MutableLiveData<Boolean> IS_LOGGED_IN = new MutableLiveData<>();
    MutableLiveData<Boolean> IS_FRESH = new MutableLiveData<>();
    MutableLiveData<String> AUTH_STATUS = new MutableLiveData<>();
    MutableLiveData<String> FRESH_STATUS = new MutableLiveData<>();
    public String FETCH_STATUS;

    SharedPreferences sharedPreferences;

    public UserModel currentUser;

    public MutableLiveData<String> GLOBAL_TRIGGER = new MutableLiveData<>();

    public static SessionManager INSTANCE;
    //STATIC INSTANCE
    public static SessionManager getInstance(){
        if (INSTANCE == null){
            synchronized (SessionManager.class){
                INSTANCE = new SessionManager();
            }
        }
        return INSTANCE;
    }

    //Constructor
    public SessionManager(){

    }

    //IINIT SESSION
    public void initSession(Context context){

        this.context = context;
        sharedPreferences = context.getSharedPreferences("LOG_STATUS",Context.MODE_PRIVATE);
        IS_LOGGED_IN.setValue(sharedPreferences.getBoolean("IS_LOGGED_IN",false));
        IS_FRESH.setValue(sharedPreferences.getBoolean("IS_FRESH",true));
        FETCH_STATUS = "CACHED";
        if (IS_LOGGED_IN.getValue())
            AUTH_STATUS.setValue("CACHED");
        else
            AUTH_STATUS.setValue("NOT_AUTHENTICATED");

        Log.d(TAG, "initSession: SESSION >> init session with IS_LOGGED = "+ IS_LOGGED_IN.getValue() + " IS_FRESH = "+ IS_FRESH.getValue() + " Auth Status = "+ AUTH_STATUS.getValue());

    }

    //Live Observers || GETTERS
    public LiveData<Boolean> isLoggedin(){ return IS_LOGGED_IN; }
    public LiveData<Boolean> isFresh(){
        return IS_FRESH;
    }
    public LiveData<String> authStatus(){ return AUTH_STATUS; }
    public UserModel getCurrentUser(){ return currentUser; }
    public LiveData<String> getFRESH_STATUS(){ return FRESH_STATUS;}
    public LiveData<String> getGLOBAL_TRIGGER(){ return GLOBAL_TRIGGER; }

    //SETTERS
    public void setAUTH_STATUS(String status){
        AUTH_STATUS.setValue(status);
    }

    public void setIS_LOGGED_IN(Boolean is_logged_in){

        if (is_logged_in){
            IS_LOGGED_IN.setValue(true);
            setSharedPref("IS_LOGGED_IN",true);
        }
        else{
            IS_LOGGED_IN.setValue(true);
            setSharedPref("IS_LOGGED_IN",true);
            AUTH_STATUS.setValue("NOT_AUTHENTICATED");
        }
    }

    public void setSharedPref(String key, Boolean value){

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key,value);
        editor.apply();

    }

    public void setCurrentUser(UserModel user){
        Log.d(TAG, "setCurrentUser: FRESH WORK current user had set");
        currentUser = user;
        if (currentUser != null){
            setFRESH_STATUS("GOT USER");
        }
    }

    public void setFRESH_STATUS(String status){
        FRESH_STATUS.setValue(status);
    }

    public void setGLOBAL_TRIGGER(String value){ GLOBAL_TRIGGER.setValue(value);}
}
