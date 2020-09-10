package com.example.primero.UI;

import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.Interpolator;
import android.widget.ProgressBar;
import android.widget.TextSwitcher;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import com.example.primero.Models.TurfModel;
import com.example.primero.R;
import com.example.primero.Repos.TurfRepo;
import com.example.primero.Room.AppDatabase;
import com.example.primero.SessionManager;
import com.example.primero.Worker.TurfWorker;
import com.example.primero.Worker.UserWorker;

import java.util.List;

public class IntroActivity extends AppCompatActivity {

    private static final String TAG = "IntroActivity";

    SessionManager sessionManager;
    TextSwitcher introSwitcher;
    ProgressBar introProgress;
    Handler handler;

    MutableLiveData<Integer> loadStatus;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        introSwitcher = findViewById(R.id.intro_switcher);
        introProgress = findViewById(R.id.intro_progress);
        sessionManager = SessionManager.getInstance();

        loadStatus =  new MutableLiveData<>();
        loadStatus.setValue(0);


        loadStatus.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer status) {
                if (status == 2){
                    sessionManager.FETCH_STATUS = "REMOTE";
                    Intent intent = new Intent(IntroActivity.this,MainActivity.class);
                    startActivity(intent);
                }
            }
        });

        sessionManager.getFRESH_STATUS().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (s.equals("GOT USER")){
                    Log.d(TAG, "onChanged: FRESH WORK GOT USER ");
                    loadStatus.setValue(1);

                } else if (s.equals("NOT FRESH")){
                    Log.d(TAG, "onChanged: FRESH WORK GOT TURFS ");
                    loadStatus.setValue(2);
                }
            }
        });


        Animation inAnim = AnimationUtils.loadAnimation(this,R.anim.intro_text_anim_in);
        inAnim.setDuration(500);
        Animation outAnim = AnimationUtils.loadAnimation(this,R.anim.intro_text_anim_out);
        inAnim.setDuration(500);

        introSwitcher.setInAnimation(inAnim);
        introSwitcher.setOutAnimation(outAnim);

        Handler switchHandler = new Handler();

        switchHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                introSwitcher.setText("Loading");
            }
        },1000);


        //Workers

        OneTimeWorkRequest setUpUser = new OneTimeWorkRequest.Builder(UserWorker.class).build();
        OneTimeWorkRequest getTurfList = new OneTimeWorkRequest.Builder(TurfWorker.class).build();

        WorkManager.getInstance(this)
                .beginWith(setUpUser)
                .then(getTurfList)
                .enqueue();

        WorkManager.getInstance(this).getWorkInfoByIdLiveData(setUpUser.getId())
                .observe(this, new Observer<WorkInfo>() {
                    @Override
                    public void onChanged(WorkInfo workInfo) {
                        Log.d(TAG, "onChanged: FRESH WORK user work status >> " + workInfo.getState());
                    }
                });
        WorkManager.getInstance(this).getWorkInfoByIdLiveData(getTurfList.getId())
                .observe(this, new Observer<WorkInfo>() {
                    @Override
                    public void onChanged(WorkInfo workInfo) {
                        Log.d(TAG, "onChanged: FRESH WORK turf work status >> " + workInfo.getState());
                    }
                });

    }


}
