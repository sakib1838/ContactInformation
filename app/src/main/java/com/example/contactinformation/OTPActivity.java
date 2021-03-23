package com.example.contactinformation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.icu.text.UnicodeSetSpanner;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.chaos.view.PinView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class OTPActivity extends AppCompatActivity {
    PinView pinView;
    MaterialButton materialButtonConfirm;
    String verificationCodeBySystem;
    String phoneNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_o_t_p);

        pinView=(PinView)findViewById(R.id.pin_view);
        materialButtonConfirm=(MaterialButton)findViewById(R.id.buttonConfirm);

        Bundle bundle = getIntent().getExtras();
        phoneNumber=bundle.getString("Phone");
        System.out.println(phoneNumber);

        sendVerificationCodeToUser(phoneNumber);

        materialButtonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String code = pinView.getText().toString();
                if(code.length()!=6){
                    pinView.setError("PIN must be 6 character");
                    pinView.requestFocus();
                    return;
                }
                verifyCode(code);
            }
        });

    }

    private void sendVerificationCodeToUser(String phoneNumber){

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        PhoneAuthOptions phoneAuthOptions = PhoneAuthOptions.newBuilder(firebaseAuth).setPhoneNumber("+88"+phoneNumber).setTimeout(60L, TimeUnit.SECONDS).setActivity(this).setCallbacks(mCallBacks).build();
        PhoneAuthProvider.verifyPhoneNumber(phoneAuthOptions);

    }//sends verification Code

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationCodeBySystem = s;
        }

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if(code!=null){
                pinView.setText(code);
                verifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(OTPActivity.this,e.toString(), Toast.LENGTH_SHORT).show();
        }
    };// code sends is successful or not

    private  void verifyCode(String codeByUser){
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCodeBySystem,codeByUser);
        provideSignUpInfo(credential);
    }

    private void provideSignUpInfo(PhoneAuthCredential credential){
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Bundle bundle = new Bundle();
                    bundle.putString("Phone",phoneNumber);
                    Intent intent = new Intent(getApplicationContext(),SignUpActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    finish();
                }else{
                    new MaterialAlertDialogBuilder(getApplicationContext()).setTitle("Failed").setMessage("Something Wrong!").setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });//materialDialog Ends here
                }
            }// on Complete Ends Here
        }); //signInWithCredential ends here

    }




}