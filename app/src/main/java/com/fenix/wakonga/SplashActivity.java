package com.fenix.wakonga;

import android.content.Intent;
import android.os.Handler;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;

import com.fenix.wakonga.slide.Slide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //hide title bar of activity
       // getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        ActionBar actionBar=getSupportActionBar();
        actionBar.hide();
        //making acitivity full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        mAuth= FirebaseAuth.getInstance();
        user=mAuth.getCurrentUser();
        int t =3000;//tempo de espera para abertura da tela principal
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {



                if (user!=null){
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    finish();
                    if (user.isEmailVerified()){
                        startActivity(new Intent(SplashActivity.this, MainActivity.class));
                        finish();
                    }

                }else {
                    mAuth.signOut();
                    startActivity(new Intent(SplashActivity.this, Slide.class));
                    finish();
                }
            }
        },t);
    }
}
