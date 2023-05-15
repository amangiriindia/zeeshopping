package com.example.amzoodmart.activities;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.amzoodmart.R;
import com.example.amzoodmart.Users;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class loginActivity extends AppCompatActivity {


    EditText email,password;
    FirebaseAuth mAuth;
    Button signUp_google;
    GoogleSignInClient mgoogleSignInClient;
    ProgressDialog progressDialog ;
    SharedPreferences sharedPreferences;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth =FirebaseAuth.getInstance();
        email = findViewById(R.id.email);
        password =findViewById(R.id.password);
        signUp_google =findViewById(R.id.sign_up_google);

        progressDialog =new ProgressDialog(loginActivity.this);
        progressDialog.setTitle("Creating account");
        progressDialog.setMessage("we are creating your account");

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mgoogleSignInClient =GoogleSignIn.getClient(this,gso);
        signUp_google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();

            }
        });

      // FOR ONBORDING SCREEN
        sharedPreferences =getSharedPreferences("onBoardingScreen",MODE_PRIVATE);
        boolean isFirstTime =sharedPreferences.getBoolean("firstTime",true);
        if(isFirstTime){
            SharedPreferences.Editor editor =sharedPreferences.edit();
            editor.putBoolean("firstTime",false);
            editor.commit();
            Intent intent =new Intent(loginActivity.this,OnBoardingActivity.class);
            startActivity(intent);
            finish();
        }
    }



    int RC_SIGN_IN =40;

    private  void  signIn(){
        Intent intent =mgoogleSignInClient.getSignInIntent();
        startActivityForResult(intent,RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC_SIGN_IN){
            Task<GoogleSignInAccount>task =GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account =task.getResult(ApiException.class);
                firebaseAuth(account.getIdToken());
            } catch (ApiException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void firebaseAuth(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken,null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){
                            FirebaseUser user =mAuth.getCurrentUser();
                            Users users =new Users();
                            users.setUserId(user.getUid());
                            users.setUserName(user.getDisplayName());
                            users.setProfile(user.getPhotoUrl().toString());
                            Toast.makeText(loginActivity.this, "login successfully", Toast.LENGTH_SHORT).show();

                          Intent intent =new Intent(loginActivity.this,MainActivity.class);
                           startActivity(intent);
                        }else {
                            Toast.makeText(loginActivity.this, "error", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
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