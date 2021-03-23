package com.example.contactinformation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class VerifyPhoneNumberActivity extends AppCompatActivity {

    Animation topAnim,bottomAnim,fadein,bounce;
    TextInputEditText textInputEditTextVerifyPhone;
    MaterialButton btnGoAhead;
    CardView cardView;
    TextView textViewConfirmPhone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_phone_number);

        topAnim= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.top_animation);
        bottomAnim=AnimationUtils.loadAnimation(getApplicationContext(),R.anim.bottom_animation);
        fadein=AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_in);
        bounce=AnimationUtils.loadAnimation(getApplicationContext(),R.anim.bounce_animation);

        cardView=(CardView)findViewById(R.id.cardViewVerifyPhone) ;
        textViewConfirmPhone=(TextView)findViewById(R.id.textViewConfirmPhone);
        textInputEditTextVerifyPhone=(TextInputEditText)findViewById(R.id.textInputEditTextVerifyPhone);
        btnGoAhead=(MaterialButton)findViewById(R.id.btnGoAhead);

        cardView.setAnimation(bounce);
        textInputEditTextVerifyPhone.setAnimation(bounce);
        btnGoAhead.setAnimation(bottomAnim);
        textViewConfirmPhone.setAnimation(topAnim);

        btnGoAhead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!validatePhoneNo()){
                    return;
                }
                else{
                    String phoneNumber = textInputEditTextVerifyPhone.getText().toString(); 
                    Intent intent = new Intent(getApplicationContext(),OTPActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("Phone",phoneNumber);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    finish();
                }

            }
        });
    }

    private Boolean validatePhoneNo(){
        String phoneNumber = textInputEditTextVerifyPhone.getText().toString();

        if(phoneNumber.isEmpty()){
            textInputEditTextVerifyPhone.setError("Field can't be empty");
            return false;
        }
        else if(phoneNumber.length() != 11){
            textInputEditTextVerifyPhone.setError("Phone must be 11 character");
            return false;
        }
        else{
            textInputEditTextVerifyPhone.setError(null);
            return  true;
        }
    }
}