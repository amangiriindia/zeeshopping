package com.example.amzoodmart.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;

import com.example.amzoodmart.R;

public class Splash_screen extends AppCompatActivity {
    private static final long SPLASH_DURATION = 3000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
//        SystemClock.sleep(3000);
//        Intent i;
//        i = new Intent(Splash_screen.this,RegistationActivity.class);
//        startActivity(i);
//        finish();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Start your main activity
                Intent intent = new Intent(Splash_screen.this, RegistationActivity.class);
                startActivity(intent);

                // Close the splash activity
                finish();
            }
        }, SPLASH_DURATION);

    }
}