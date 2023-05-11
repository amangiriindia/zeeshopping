package com.example.amzoodmart.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;

import com.example.amzoodmart.R;


import java.util.Objects;

public class OrderTrackingActivity extends AppCompatActivity {

    Toolbar toolbar;
    String productName,productQuantity,productPrice,productimgUrl;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_tracking);


       // productName =getIntent().getStringExtra("ProductName");
       // productPrice = getIntent().getStringExtra("ProductPrice");
     //   productQuantity = getIntent().getStringExtra("ProductQuantity");
    //    productimgUrl = getIntent().getStringExtra("productImgUrl");

        //Toolbar
        toolbar =findViewById(R.id.order_detailed_toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}