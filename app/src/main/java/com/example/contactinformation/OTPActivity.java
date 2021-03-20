package com.example.contactinformation;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.chaos.view.PinView;
import com.google.android.material.button.MaterialButton;

public class OTPActivity extends AppCompatActivity {
    PinView pinView;
    MaterialButton materialButtonConfirm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_o_t_p);

        pinView=(PinView)findViewById(R.id.pin_view);
        materialButtonConfirm=(MaterialButton)findViewById(R.id.buttonConfirm);

        pinView.setText("123456");

        materialButtonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pin = pinView.getText().toString();

                Toast.makeText(getApplicationContext(),"This pin is "+pin,Toast.LENGTH_SHORT).show();
            }
        });

    }
}