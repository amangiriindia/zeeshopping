package com.example.amzoodmart.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.amzoodmart.R;
import com.example.amzoodmart.models.MyOrderModel;
import com.example.amzoodmart.models.NewProductsModel;


import java.util.Objects;

public class OrderTrackingActivity extends AppCompatActivity {

    Toolbar toolbar;
    Button btn_cancel;
    ImageView imageView;
    TextView order_name,order_id,order_price,order_qty,order_payment,order_status,order_date,order_address;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_tracking);


        order_id =findViewById(R.id.order_id);
        order_name =findViewById(R.id.order_name);
        order_price =findViewById(R.id.order_price);
        order_qty =findViewById(R.id.order_qty);
        order_payment =findViewById(R.id.order_payment);
        order_status =findViewById(R.id.order_status);
        order_date =findViewById(R.id.order_date);
        order_address =findViewById(R.id.order_address);
        btn_cancel =findViewById(R.id.order_cancel_btn);
        imageView =findViewById(R.id.order_product_img);

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

       String imgUrl = getIntent().getStringExtra("orderImgUrl");
        order_id.setText(getIntent().getStringExtra("orderId"));
        order_name.setText(getIntent().getStringExtra("orderName"));
        order_price.setText(getIntent().getStringExtra("orderPrice"));
        order_qty.setText(getIntent().getStringExtra("orderQty"));
        order_payment.setText(getIntent().getStringExtra("orderPayment"));
        order_status.setText(getIntent().getStringExtra("orderStatus"));
        order_date.setText(getIntent().getStringExtra("orderDate"));
        order_address.setText(getIntent().getStringExtra("orderAddress"));
        Glide.with(getApplicationContext()).load(imgUrl).into(imageView);



    }
}