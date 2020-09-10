package com.example.primero.UI;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.Animation;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextSwitcher;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.lifecycle.Observer;

import com.example.primero.R;
import com.example.primero.SessionManager;
import com.example.primero.Utils.SoftInputDealer;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class AuthActivity extends AppCompatActivity {

    private static final String TAG = "AuthActivity";

    TextSwitcher authTxtSwitcher;
    TextInputEditText authInput;
    TextInputLayout authInpLayout;

    RelativeLayout authBtn,btnRevealer;
    ProgressBar authProgress;

    TextView authBtnTxt;

    String authWindow = "phone";

    //Firebase
    PhoneAuthProvider phoneAuthProvider;
    PhoneAuthCredential mCredential;
    FirebaseAuth mAuth;

    //Local
    String phone = "",otp = "";
    String verficationId = "";

    //UTILS
    SoftInputDealer softInputDealer;
    SessionManager sessionManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        //SESSION INIT
        sessionManager = SessionManager.getInstance();
        sessionManager.authStatus().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (s.equals("AUTHENTICATED")){
                    Intent intent = new Intent(AuthActivity.this, IntroActivity.class);
                    ActivityOptionsCompat options = ActivityOptionsCompat
                            .makeSceneTransitionAnimation(AuthActivity.this, authBtn, "auth_reveal");
                    getWindow().setExitTransition(null);
                    startActivity(intent,options.toBundle());
                }
            }
        });

        authTxtSwitcher = findViewById(R.id.auth_text_switcher);
        authInput = findViewById(R.id.auth_input);
        authInpLayout = findViewById(R.id.auth_input_layout);
        authBtn = findViewById(R.id.auth_btn);
        btnRevealer = findViewById(R.id.auth_btn_revealer);
        authProgress = findViewById(R.id.auth_btn_progress);
        authBtnTxt = findViewById(R.id.auth_btn_txt);

        //Auth INIT
        phoneAuthProvider = PhoneAuthProvider.getInstance();
        mAuth = FirebaseAuth.getInstance();

        authBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                authBtnClick();
            }
        });

        //Anim SETTING
        authTxtSwitcher.setInAnimation(this,android.R.anim.slide_in_left);
        authTxtSwitcher.setOutAnimation(this,android.R.anim.slide_out_right);


        softInputDealer = SoftInputDealer.getInstance();
    }


    private void authBtnClick() {

        if (authWindow.equals("phone")){

            phone = Objects.requireNonNull(authInput.getText()).toString();
            if (phone.equals("") || phone.length() < 10){
                authInput.setError("10 digit number required !");

            }
            else if (phone.length() == 10){
                authWindow = "loading";
                sendOtpAnim();
                sendOtp(phone);
            }
            else if (phone.equals("0134")){
                phone ="65055 53434";
                sendOtpAnim();
                sendOtp(phone);
            }


        }

        else if (authWindow.equals("otp")){
            otp = Objects.requireNonNull(authInput.getText()).toString();
            if (otp != null && otp.length() >=6){
                verifyOtp(otp);
            }
        }

    }

    private void sendOtp(String phone) {

        phoneAuthProvider.verifyPhoneNumber(
                "+91" + phone,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallbacks);
    }

    private void sendOtpAnim() {

        authBtnTxt.setVisibility(View.GONE);
        authProgress.setVisibility(View.VISIBLE);
        SoftInputDealer.forceHide(this,authInput);
    }


    private void verifyOtp(String otp){

        verifyOtpAnim();
        firebaseOtpVerification(otp);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void verifyOtpAnim(){

        authBtnTxt.setVisibility(View.GONE);
        authProgress.setVisibility(View.VISIBLE);

        int cx = btnRevealer.getWidth() / 2;
        int cy = btnRevealer.getHeight() / 2;

        float finalRadius = (float) Math.hypot(cx,cy);

        Animator reveal = ViewAnimationUtils.createCircularReveal(btnRevealer,cx,cy,finalRadius,0);
        reveal.start();
        btnRevealer.setVisibility(View.GONE);

        authWindow = "loading";



    }

    @SuppressLint({"ResourceAsColor", "UseCompatLoadingForDrawables"})
    private  void codeSendAnim(){

        authBtnTxt.setVisibility(View.VISIBLE);
        authBtnTxt.setText("VERIFY");
        authProgress.setVisibility(View.GONE);

        authBtnTxt.setTextColor(R.color.black);

        int cx = btnRevealer.getWidth() / 2;
        int cy = btnRevealer.getHeight() / 2;

        float finalRadius = (float) Math.hypot(cx,cy);

        Animator reveal = ViewAnimationUtils.createCircularReveal(btnRevealer,cx,cy,0,finalRadius);
        reveal.start();
        btnRevealer.setVisibility(View.VISIBLE);

        authTxtSwitcher.setText("An OTP had sent to +91 "+ phone);
        authInpLayout.setStartIconDrawable(getDrawable(R.drawable.ic_baseline_lock_24));
        authInput.setText("");
        authInput.clearFocus();
        authInput.setInputType(InputType.TYPE_CLASS_NUMBER);
        authWindow = "otp";

    }

    //CALBACK
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            Log.d(TAG, "onVerificationCompleted: ");
            if (authWindow.equals("otp")){
                otp = phoneAuthCredential.getSmsCode();
                authInput.setText(otp);
                verifyOtp(otp);
            }
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Log.e(TAG, "onVerificationFailed: ",e );
        }

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            Log.d(TAG, "onCodeSent: ");
            verficationId = s;
            codeSendAnim();
        }

        @Override
        public void onCodeAutoRetrievalTimeOut(@NonNull String s) {
            super.onCodeAutoRetrievalTimeOut(s);
            Log.d(TAG, "onCodeAutoRetrievalTimeOut: ");
        }
    };

    private void firebaseOtpVerification(String OTP){

        if (!verficationId.equals("")){

            mCredential = PhoneAuthProvider.getCredential(verficationId,OTP);
            Log.d(TAG, "firebaseOtpVerification: " + mCredential);

            mAuth.signInWithCredential(mCredential)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                Log.d(TAG, "onComplete: FIREBSE AUTH SUCCESSFULL");
                                onVerificationComplete();
                            }
                        }
                    });
        }
    }

    private void onVerificationComplete(){

        sessionManager.setIS_LOGGED_IN(true);
        sessionManager.setAUTH_STATUS("AUTHENTICATED");

    }
}
