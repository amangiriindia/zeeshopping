package com.example.amzoodmart.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.amzoodmart.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class loginActivity extends AppCompatActivity {


    EditText email,password;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth =FirebaseAuth.getInstance();
        email = findViewById(R.id.email);
        password =findViewById(R.id.password);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(mAuth.getCurrentUser() != null){
            Toast.makeText(this, "Already Logged In", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(loginActivity.this,MainActivity.class));
            finish();
        }
    }

    public void signin(View view){

        String userEmail =email.getText().toString();
        String userPassword =password.getText().toString();

        if(TextUtils.isEmpty(userEmail)){
            Toast.makeText(this, "Enter Email!", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(userPassword)){
            Toast.makeText(this, "Enter Password!", Toast.LENGTH_SHORT).show();
            return;
        }
        if(userPassword.length()<6){
            Toast.makeText(this, "Password too short, enter minimum 6 chracter", Toast.LENGTH_SHORT).show();
            return;
        }
        mAuth.signInWithEmailAndPassword(userEmail,userPassword)
                .addOnCompleteListener(loginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(loginActivity.this, "Successfuly Login", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(loginActivity.this,MainActivity.class));
                                }else {
                                    Toast.makeText(loginActivity.this, "Wrong Username and  Password!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                );


    }
    public void signup(View view){
        startActivity(new Intent(loginActivity.this,RegistationActivity.class));
    }
}