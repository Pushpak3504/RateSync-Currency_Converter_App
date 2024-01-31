package com.example.ratesync;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    Button dhacc,loginbtn;
    TextView forgot;
    EditText lgnemail,lgnpass;
    String txtemail, txtpass;

    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private FirebaseAuth mAuth;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dhacc = findViewById(R.id.dhacc);
        loginbtn = findViewById(R.id.loginbtn);
        lgnemail = findViewById(R.id.lgnemail);
        lgnpass = findViewById(R.id.lgpass);
        forgot = findViewById(R.id.forgot);

        mAuth = FirebaseAuth.getInstance();


        dhacc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SignUp.class);
                startActivity(intent);
                finish();
            }
        });

        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,ForgotPassword.class);
                startActivity(i);
                finish();
            }
        });

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtemail = lgnemail.getText().toString().trim();
                txtpass = lgnpass.getText().toString().trim();

                if(!TextUtils.isEmpty(txtemail))
                {
                    if(txtemail.matches(emailPattern))
                    {
                        if(!TextUtils.isEmpty(txtpass))
                        {
                            SignInUser();
                        }
                        else
                        {
                            lgnpass.setError("Password Field Can't be Empty");
                        }
                    }
                    else
                    {
                        lgnemail.setError("Enter Valid Email Address");
                    }
                }
                else
                {
                    lgnemail.setError("Email Field Can't be Empty");
                }


            }
        });
    }

    private void SignInUser() {
        loginbtn.setVisibility(View.INVISIBLE);

        mAuth.signInWithEmailAndPassword(txtemail,txtpass).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Toast.makeText(MainActivity.this, "Login Successful !", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this,Working.class);
                startActivity(intent);
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this, "Error "+ e.getMessage() , Toast.LENGTH_SHORT).show();
                loginbtn.setVisibility(View.VISIBLE);
            }
        });
    }
}