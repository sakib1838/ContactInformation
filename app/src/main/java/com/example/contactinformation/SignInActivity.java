package com.example.contactinformation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class SignInActivity extends AppCompatActivity {

    TextView textViewGoToSignUp,textViewForgotPassword;
    MaterialButton btnSignIn;
    TextInputEditText textInputEditTextSignInPhone,textInputEditTextSignInPassword;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);


        textViewGoToSignUp=(TextView)findViewById(R.id.textViewSignUpInSignIn);
        textViewForgotPassword=(TextView)findViewById(R.id.textViewForgotPassword);

        textInputEditTextSignInPhone=(TextInputEditText)findViewById(R.id.textInputEditTextSignInPhone);
        textInputEditTextSignInPassword=(TextInputEditText)findViewById(R.id.textInputEditTextSignInPassword);
        btnSignIn=(MaterialButton)findViewById(R.id.btnSignIn);



        textViewGoToSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getApplicationContext(),VerifyPhoneNumberActivity.class));
                finish();
            }
        });


    }
}