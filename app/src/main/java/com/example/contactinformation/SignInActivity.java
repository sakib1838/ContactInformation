package com.example.contactinformation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.example.contactinformation.FirebaseModel.Users;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignInActivity extends AppCompatActivity {

    TextView textViewGoToSignUp,textViewForgotPassword;
    MaterialButton btnSignIn;
    TextInputEditText textInputEditTextSignInPhone,textInputEditTextSignInPassword;
    ProgressDialog progressDialog;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        firebaseDatabase= FirebaseDatabase.getInstance();

        textViewGoToSignUp=(TextView)findViewById(R.id.textViewSignUpInSignIn);
        textViewForgotPassword=(TextView)findViewById(R.id.textViewForgotPassword);

        textInputEditTextSignInPhone=(TextInputEditText)findViewById(R.id.textInputEditTextSignInPhone);
        textInputEditTextSignInPassword=(TextInputEditText)findViewById(R.id.textInputEditTextSignInPassword);
        btnSignIn=(MaterialButton)findViewById(R.id.btnSignIn);


        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCredentials();
            }
        });


        textViewGoToSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getApplicationContext(),VerifyPhoneNumberActivity.class));
                finish();
            }
        });


    }

    private void getCredentials(){
        if(!textInputEditTextSignInPhone.getText().toString().isEmpty() && !textInputEditTextSignInPassword.getText().toString().isEmpty()){

            String phone = textInputEditTextSignInPhone.getText().toString();
            String password = textInputEditTextSignInPassword.getText().toString();

            logIn(phone,password);

        }
        else{
            if(textInputEditTextSignInPhone.getText().toString().isEmpty()){
                textInputEditTextSignInPhone.setError("Field can't be empty");
            }
            else{
                textInputEditTextSignInPassword.setError("Password can't be empty");
            }
        }
    }

    private void logIn(String phone,String password){
        showDialog();
        databaseReference = firebaseDatabase.getReference("Users");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(phone).exists()){
                    Users users = snapshot.child(phone).getValue(Users.class);

                    if(users.getPhoneNumber().equals(phone) && users.getPassword().equals(password)){
                        startActivity(new Intent(SignInActivity.this,MainActivity.class));
//                        new MaterialAlertDialogBuilder(SignInActivity.this)
//                                .setTitle("Success")
//                                .setMessage("User Exists")
//                                .setNegativeButton("Ok",null)
//                                .show();
                        progressDialog.dismiss();
                    }else{
                        new MaterialAlertDialogBuilder(SignInActivity.this)
                                .setTitle("Error")
                                .setMessage("Login Information not match")
                                .setNegativeButton("Ok",null)
                                .show();
                        progressDialog.dismiss();
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                new MaterialAlertDialogBuilder(SignInActivity.this)
                        .setTitle("Error")
                        .setMessage(error.getMessage().toString())
                        .setNegativeButton("Ok",null)
                        .show();
                progressDialog.dismiss();
            }
        });

    }

    private  void showDialog(){
        this.progressDialog=new ProgressDialog(SignInActivity.this);
        progressDialog.setCancelable(true);
        progressDialog.setTitle("Checking Info...");
        progressDialog.show();
    }

}