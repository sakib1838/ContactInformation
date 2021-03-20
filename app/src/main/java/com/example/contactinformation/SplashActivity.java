package com.example.contactinformation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import java.nio.InvalidMarkException;

public class SplashActivity extends AppCompatActivity {
    Animation topAnimation, bottomAnimation;
    ImageView imageView;
    TextView textViewTitle, textViewSubTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);


        topAnimation= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.top_animation);
        bottomAnimation= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.bottom_animation);

        imageView=(ImageView)findViewById(R.id.imageView);
        textViewTitle=(TextView)findViewById(R.id.textView);
        textViewSubTitle=(TextView)findViewById(R.id.textView2);

        imageView.setAnimation(topAnimation);
        textViewTitle.setAnimation(bottomAnimation);
        textViewSubTitle.setAnimation(bottomAnimation);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this,SignUpActivity.class));
                finish();
            }
        },5000);


    }
}