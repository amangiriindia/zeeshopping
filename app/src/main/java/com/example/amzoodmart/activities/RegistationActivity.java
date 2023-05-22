package com.example.amzoodmart.activities;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.amzoodmart.R;
import com.example.amzoodmart.Utility.NetworkChangeListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegistationActivity extends AppCompatActivity {

    EditText email, name, password;
    FirebaseAuth mAuth;
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registation);
        mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() != null) {
            startActivity(new Intent(RegistationActivity.this, MainActivity.class));
            finish();
        }

        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()) {
                    email.setError("Invalid EmailAddress");
                    return;
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


    }

    public void signup(View view) {
        String userName = name.getText().toString();
        String userEmail = email.getText().toString();
        String userPassword = password.getText().toString();

        if (TextUtils.isEmpty(userName)) {
            name.setError("Enter Name!");
            return;
        }
        if (TextUtils.isEmpty(userEmail)) {
            email.setError("Enter Email!");
            return;
        }
        if (TextUtils.isEmpty(userPassword)) {
            password.setError("Enter Password!");
            return;
        }
        if (userPassword.length() < 6) {
            password.setError("Password too short!");
            return;
        }

        mAuth.createUserWithEmailAndPassword(userEmail, userPassword)
                .addOnCompleteListener(RegistationActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(RegistationActivity.this, "Successfully Register", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(RegistationActivity.this, MainActivity.class);
                                    intent.putExtra("name",userName);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(RegistationActivity.this, "Alredy Have Account", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                ).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(RegistationActivity.this, "Restart or Please wait ...", Toast.LENGTH_SHORT).show();
                    }
                });


    }

    public void signin(View view) {
        startActivity(new Intent(RegistationActivity.this, loginActivity.class));
    }

    @Override
    protected void onStart() {
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeListener, filter);
        super.onStart();
    }

    @Override
    protected void onStop() {
        unregisterReceiver(networkChangeListener);
        super.onStop();
    }

}