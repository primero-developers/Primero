package com.example.primero.UI;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.primero.R;
import com.example.primero.SessionManager;

public class SplashActivity extends AppCompatActivity {

    private static final String TAG = "SplashActivity";

    ImageView splashLogo;
    SessionManager sessionManager;
    Boolean IS_LOGGED;


    private Integer SPLASH_TIME_OUT = 1000;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //VARIABLES
        sessionManager = SessionManager.getInstance();
        IS_LOGGED = sessionManager.isLoggedin().getValue();
        Log.d(TAG, "onCreate: SLPASH SCREEN >> IS_LOGGED " + IS_LOGGED);

        splashLogo = findViewById(R.id.splash_logo);


        Handler logoHandler = new Handler();
        Handler switchHandler = new Handler();

        logoHandler.postDelayed(new Runnable() {
            @Override
            public void run() {

                splashLogo.setVisibility(View.VISIBLE);
                Animation expandIn = AnimationUtils.loadAnimation(SplashActivity.this, R.anim.splash_pop_up);
                splashLogo.startAnimation(expandIn);

            }
        },100);

        switchHandler.postDelayed(new Runnable() {
            @Override
            public void run() {

                if (IS_LOGGED){
                    Intent intent = new Intent(SplashActivity.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                else {
                    Intent intent = new Intent(SplashActivity.this,AuthActivity.class);
                    startActivity(intent);
                    finish();
                }


            }
        },SPLASH_TIME_OUT);
    }
}
