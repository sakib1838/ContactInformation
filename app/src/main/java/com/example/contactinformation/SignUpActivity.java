package com.example.contactinformation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class SignUpActivity extends AppCompatActivity {

    Animation topAnimation,bottomAnimation,bounce;
    TextInputEditText textInputEditTextPhone,textInputEditTextUserName,textInputEditTextPassword;
    TextView textViewGoToSignIn,textViewSignUp;
    MaterialButton btnSignUp;
    CardView cardView;
    ProgressDialog progressDialog;
    String phoneNumber;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        firebaseDatabase=FirebaseDatabase.getInstance();

        topAnimation= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.top_animation);
        bottomAnimation= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.bottom_animation);
        bounce = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.bounce_animation);

        cardView=(CardView)findViewById(R.id.cardView);
        textInputEditTextPhone=(TextInputEditText)findViewById(R.id.textInputEditTextSignUpPhone);
        textInputEditTextUserName=(TextInputEditText)findViewById(R.id.textInputEditTextSignUpUserName);
        textInputEditTextPassword=(TextInputEditText)findViewById(R.id.textInputEditTextSignUpPassword);
        textViewGoToSignIn=(TextView)findViewById(R.id.textViewGoToSingIn);
        textViewSignUp=(TextView)findViewById(R.id.textViewSignUp);
        btnSignUp=(MaterialButton)findViewById(R.id.btnSignUp);


        cardView.setAnimation(bounce);
        textViewSignUp.setAnimation(topAnimation);
        textViewGoToSignIn.setAnimation(bottomAnimation);
        btnSignUp.setAnimation(bottomAnimation);

        Bundle bundle = getIntent().getExtras();
        phoneNumber=bundle.getString("Phone");
        System.out.println(phoneNumber);
        textInputEditTextPhone.setText(phoneNumber);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getCredentials();
            }
        });


    }


    private Boolean validateUserName(){
        if(textInputEditTextUserName.getText().toString().isEmpty()){
            textInputEditTextUserName.setError("Must have a username");
            return false;
        }
        else if(textInputEditTextUserName.getText().toString().length()<4){
            textInputEditTextUserName.setError("Username have at least 4 characters");
            return false;
        }
        else{
            textInputEditTextUserName.setError(null);
            return true;
        }

    }

    private Boolean validatePassword(){
        String passwordPattern = "^" +
                "(?=.*[@#$%^&+=])" +     // at least 1 special character
                "(?=\\S+$)" +            // no white spaces
                ".{4,}" +                // at least 4 characters
                "$";

        if(textInputEditTextPassword.getText().toString().isEmpty()){
            textInputEditTextPassword.setError("Password can't be empty");
            return false;
        }
        else if(!textInputEditTextPassword.getText().toString().matches(passwordPattern)){
            textInputEditTextPassword.setError("Password is not strong");
            return false;
        }
        else{
            textInputEditTextPassword.setError(null);
            return true;
        }
    }


    private void getCredentials(){

        if(!validateUserName() || !validatePassword()){
            return;
        }

        String phone = textInputEditTextPhone.getText().toString();
        String username = textInputEditTextUserName.getText().toString();
        String password = textInputEditTextPassword.getText().toString();

        registration(phone,username,password);

    }

    private void registration(String phone, String username, String password){
        showDialog();

        databaseReference=firebaseDatabase.getReference("Users");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.child(phone).exists()){
                    HashMap<String,Object> regCredential= new HashMap<>();
                    regCredential.put("PhoneNumber",phone);
                    regCredential.put("UserName",username);
                    regCredential.put("Password",password);
                    regCredential.put("Role","User");

                    databaseReference.child(phone).updateChildren(regCredential).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                new MaterialAlertDialogBuilder(SignUpActivity.this)
                                        .setTitle("Done")
                                        .setMessage("You registration completed")
                                        .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                Intent intent = new Intent(SignUpActivity.this,SignInActivity.class);
                                                startActivity(intent);
                                                finish();
                                            }
                                        })
                                        .show();
                            }else{
                                new MaterialAlertDialogBuilder(SignUpActivity.this)
                                        .setTitle("Error")
                                        .setMessage("Cannot Register at this moment")
                                        .setNegativeButton("Ok",null)
                                        .show();
                            }
                            progressDialog.dismiss();
                        }
                    });
                }else{
                    new MaterialAlertDialogBuilder(SignUpActivity.this)
                            .setTitle("Error")
                            .setMessage("This Phone number already registered,Sign In please ")
                            .setNegativeButton("Ok",null)
                            .show();
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                new MaterialAlertDialogBuilder(SignUpActivity.this)
                        .setTitle("Error")
                        .setMessage(error.toString())
                        .setNegativeButton("Ok",null)
                        .show();
                progressDialog.dismiss();
            }
        });

    }

    private void showDialog(){
        this.progressDialog= new ProgressDialog(SignUpActivity.this);
        progressDialog.setCancelable(true);
        progressDialog.setTitle("This takes a few moments");
        progressDialog.show();
    }

}