package com.example.amzoodmart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegistationActivity extends AppCompatActivity {

    EditText email,name,password;
    FirebaseAuth mAuth;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registation);
        mAuth =FirebaseAuth.getInstance();
//        if(mAuth.getCurrentUser()  != null) {
//            startActivity(new Intent(RegistationActivity.this, MainActivity2.class));
//            finish();
//        }

     name =findViewById(R.id.name);
     email = findViewById(R.id.email);
     password =findViewById(R.id.password);
     sharedPreferences =getSharedPreferences("onBoardingScreen",MODE_PRIVATE);
     boolean isFirstTime =sharedPreferences.getBoolean("firstTime",true);
     if(isFirstTime){
         SharedPreferences.Editor editor =sharedPreferences.edit();
         editor.putBoolean("firstTime",false);
         editor.commit();
         Intent intent =new Intent(RegistationActivity.this,OnBoardingActivity.class);
         startActivity(intent);
         finish();
     }
    }

    public void signup(View view){
        String userName =name.getText().toString();
        String userEmail =email.getText().toString();
        String userPassword =password.getText().toString();

        if(TextUtils.isEmpty(userName)){
            Toast.makeText(this, "Enter Name!", Toast.LENGTH_SHORT).show();
            return;
        }
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
        mAuth.createUserWithEmailAndPassword(userEmail,userPassword)
                        .addOnCompleteListener(RegistationActivity.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if(task.isSuccessful()){
                                            Toast.makeText(RegistationActivity.this, "Successfully Register", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(RegistationActivity.this,MainActivity2.class));
                                        }else {
                                            Toast.makeText(RegistationActivity.this, "Alredy Have Account", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                        );


    }
    public void signin(View view){
        startActivity(new Intent(RegistationActivity.this,loginActivity.class));
    }

}