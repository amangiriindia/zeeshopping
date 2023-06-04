package com.amzsoft.zeeshopping.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.amzsoft.zeeshopping.R;

public class Splash_screen extends AppCompatActivity {
    private static final long SPLASH_DURATION = 3000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Start your main activity
                Intent intent = new Intent(Splash_screen.this, loginActivity.class);
                startActivity(intent);

                // Close the splash activity
                finish();
            }
        }, SPLASH_DURATION);

    }
}