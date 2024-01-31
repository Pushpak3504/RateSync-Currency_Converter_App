package com.example.ratesync;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {
    EditText foredt;
    String strmail;
    Button back,reset;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        foredt = findViewById(R.id.mailforgot);
        back = findViewById(R.id.backbtn);
        reset = findViewById(R.id.resetpass);
        mAuth = FirebaseAuth.getInstance();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ForgotPassword.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strmail = foredt.getText().toString().trim();

                if(!TextUtils.isEmpty(strmail))
                {
                    ResetPassword();
                }
                else
                {
                    foredt.setError("Email Field Can't Be Empty");
                }
            }
        });
    }

    private void ResetPassword() {
        reset.setVisibility(View.INVISIBLE);

        mAuth.sendPasswordResetEmail(strmail).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(ForgotPassword.this, "Reset Password Link Has Been Sent To Your Email.!", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(ForgotPassword.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ForgotPassword.this, "Error - " + e.getMessage(), Toast.LENGTH_SHORT).show();
                reset.setVisibility(View.VISIBLE);
            }
        });
    }
}