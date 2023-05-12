package com.example.amzoodmart.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.amzoodmart.R;

public class OrderConfirmActivity extends AppCompatActivity {
    ImageView imageView;
    String img ="";
    Button orderBtn,homeBtn;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_confirm);

        img ="https://firebasestorage.googleapis.com/v0/b/amzoodmart.appspot.com/o/order_thumb.jpg?alt=media&token=1a21d634-7f0c-4b5e-a5e9-9a710bd23083";
        orderBtn =findViewById(R.id.goto_myorder_btn);
        homeBtn =findViewById(R.id.goto_home_btn);
        imageView =findViewById(R.id.order_success_image);

        Glide.with(getApplicationContext()).load(img).into(imageView);
        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(OrderConfirmActivity.this,MainActivity.class));
                finish();
            }
        });
        orderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(OrderConfirmActivity.this,MyOrderActivity.class));
                finish();
            }
        });

    }
}