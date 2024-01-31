package com.example.ratesync;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignUp extends AppCompatActivity {

    EditText edtfullname, edtemail,edtpassword, edtcpass;
    Button signupbtn;
    String txtfullname, txtemail,txtpassword, txtcpass;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        signupbtn = findViewById(R.id.signupbtn);
        edtfullname = findViewById(R.id.fullname);
        edtemail = findViewById(R.id.email);
        edtpassword = findViewById(R.id.password);
        edtcpass = findViewById(R.id.confirmpassword);

        signupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtfullname = edtfullname.getText().toString();
                txtemail = edtemail.getText().toString().trim();
                txtpassword = edtpassword.getText().toString().trim();
                txtcpass = edtcpass.getText().toString().trim();
                mAuth = FirebaseAuth.getInstance();
                db = FirebaseFirestore.getInstance();

                if(!TextUtils.isEmpty(txtfullname))
                {
                    if(!TextUtils.isEmpty(txtemail))
                    {
                        if(txtemail.matches(emailPattern))
                        {
                            if(!TextUtils.isEmpty(txtpassword))
                            {
                                if(!TextUtils.isEmpty(txtcpass))
                                {
                                    if(txtcpass.equals(txtpassword))
                                    {
                                        SignUpUser();
                                    }
                                    else
                                    {
                                        edtcpass.setError("Confirm Password Should be Same As Password");
                                    }
                                }
                                else
                                {
                                    edtcpass.setError("Confirm Password Field Can't Be Empty");
                                }
                            }
                            else
                            {
                                edtpassword.setError("Password Field Can't Be Empty");
                            }
                        }
                        else
                        {
                            edtemail.setError("Enter Valid Email Address");
                        }
                    }
                    else
                    {
                        edtemail.setError("Email Field Can't Be Empty");
                    }
                }
                else
                {
                    edtfullname.setError("Username Field Can't Be Empty");
                }
            }
        });
    }

    private void SignUpUser() {
        signupbtn.setVisibility(View.INVISIBLE);

        mAuth.createUserWithEmailAndPassword(txtemail,txtpassword).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {

                Map<String, Object> user = new HashMap<>();
                user.put("FullName", txtfullname);
                user.put("Email", txtemail);
                user.put("PassWord", txtpassword);

                db.collection("users")
                        .add(user)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Toast.makeText(SignUp.this, "Sign Up Successful !", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(SignUp.this,Working.class);
                                startActivity(intent);
                                finish();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(SignUp.this, "Error - "+e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(SignUp.this, "Error "+ e.getMessage() , Toast.LENGTH_SHORT).show();
                signupbtn.setVisibility(View.VISIBLE);
            }
        });
    }
}