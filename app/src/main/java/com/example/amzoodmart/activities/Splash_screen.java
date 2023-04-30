package com.example.amzoodmart.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;

import com.example.amzoodmart.R;

public class Splash_screen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        SystemClock.sleep(3000);
        Intent i;
        i = new Intent(Splash_screen.this,RegistationActivity.class);
        startActivity(i);
        finish();
    }
}