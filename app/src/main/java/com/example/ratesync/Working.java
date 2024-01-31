package com.example.ratesync;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Working extends AppCompatActivity {

    private Spinner from,to;
    private EditText inputtt;
    private TextView result;
    private String ip;
    private Button convert;
    private DatabaseReference RootRef;

    Button prof;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_working);
        prof = findViewById(R.id.prof);

        from = findViewById(R.id.from);
        to = findViewById(R.id.to);
        inputtt = findViewById(R.id.input);
        result = findViewById(R.id.result);
        convert = findViewById(R.id.convert);

        RootRef = FirebaseDatabase.getInstance().getReference();

        mAuth = FirebaseAuth.getInstance();


        method();
        convert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input = inputtt.getText().toString();
                String s1 = from.getSelectedItem().toString();
                String s2 = to.getSelectedItem().toString();
                String real1 = s1.substring(s1.length()-3);
                String real2 = s2.substring(s2.length()-3);
                if(TextUtils.isEmpty(input))
                {
                    Toast.makeText(Working.this, "Enter Some Value", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    FetchData(input,real1,real2);
                }
            }
        });



        prof.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Intent intent = new Intent(Working.this, MainActivity.class);
                startActivity(intent);
                finish();
                Toast.makeText(Working.this, "Logout Successful!", Toast.LENGTH_SHORT).show();
            }
        });

    }



    private void method() {
        RootRef.child("Country").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                final List<String> list = new ArrayList<String>();

                for(DataSnapshot dataSnapshot: snapshot.getChildren())
                {
                    String name2 = dataSnapshot.child("code").getValue(String.class);
                    String name1 = dataSnapshot.child("name").getValue(String.class);
                    String finalstr = name1+" \t "+name2;
                    list.add(finalstr);
                }

                ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<String>(Working.this,android.R.layout.simple_spinner_item,list);
                arrayAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                from.setAdapter(arrayAdapter1);

                ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<String>(Working.this,android.R.layout.simple_spinner_item,list);
                arrayAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                to.setAdapter(arrayAdapter2);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void FetchData(String input, String real1, String real2) {

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://api.freecurrencyapi.com/v1/latest?apikey=fca_live_aC0sEXaJXKoMVhO6tKcYzeryOOE8L6ka5xuRHJYt&currencies="+real2+"&base_currency="+real1+"";

// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        float igo = Float.parseFloat(input);

                        try {
                            JSONObject jsonObject = new JSONObject(response.toString());
                            float er = Float.parseFloat(jsonObject.getString(real1+"_"+real2));
                            float rs = er*igo;
                            result.setText(String.valueOf(rs));

                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Working.this, "error", Toast.LENGTH_SHORT).show();
            }
        });

// Add the request to the RequestQueue.
        queue.add(stringRequest);

    }

}
