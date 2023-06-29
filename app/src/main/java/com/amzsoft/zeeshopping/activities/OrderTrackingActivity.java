package com.amzsoft.zeeshopping.activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.amzsoft.zeeshopping.R;
import com.amzsoft.zeeshopping.Utility.NetworkChangeListener;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class OrderTrackingActivity extends AppCompatActivity {

    Toolbar toolbar;
    Button btn_cancel,btn_replace,btn_return;
    ImageView imageView;
   String doucmentId ,returnData ="",replaceData="",orderStatus ="",orderId,productName ="",producturl,prductPrice,orderQty ="",orderPayment="",orderDate="",orderAddress="";
    TextView order_name, order_id, order_price, order_qty, order_payment, order_status, order_date, order_address, order_total,order_delivery_time,order_time_level;
    FirebaseFirestore firestore;
    FirebaseAuth auth;
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();



    @SuppressLint({"MissingInflatedId", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_tracking);

        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        orderId=getIntent().getStringExtra("orderId");




        order_id = findViewById(R.id.order_id);
        order_name = findViewById(R.id.order_name);
        order_price = findViewById(R.id.order_price);
        order_qty = findViewById(R.id.order_qty);
        order_payment = findViewById(R.id.order_payment);
        order_status = findViewById(R.id.order_status);
        order_date = findViewById(R.id.order_date);
        order_address = findViewById(R.id.order_address);
        btn_cancel = findViewById(R.id.order_cancel_btn);
        imageView = findViewById(R.id.order_product_img);
        order_total = findViewById(R.id.order_total);
        btn_return =findViewById(R.id.order_return_btn);
        btn_replace =findViewById(R.id.order_replace_btn);
        order_delivery_time =findViewById(R.id.order_delivery_time);
        order_time_level = findViewById(R.id.order_time_label);

        //Toolbar
        toolbar = findViewById(R.id.order_detailed_toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });




        firestore.collection("orderDetailedUpdate")
                .document(orderId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                // Document exists, retrieve the data
                                productName = document.getString("productName");
                                prductPrice = document.getString("productPrice");
                                orderQty = document.getString("productQuantity");
                                producturl = document.getString("productImgUrl");
                                orderPayment = document.getString("Method") + "";
                                orderStatus = document.getString("orderStatus");
                                orderAddress += document.getString("userName") + ", ";
                                orderAddress += document.getString("userNumber") + ", ";
                                orderAddress += document.getString("userDistict") + ", ";
                                orderAddress += document.getString("userCity") + ", ";
                                orderAddress += document.getString("userCode") + ", ";
                                orderAddress += document.getString("userAddress_detailed") + ".";
                                orderDate = document.getString("currentDate");
                                String orderDeleiveryTime = document.getString("delivery_time");
                                returnData = document.getString("returnData");
                                replaceData = document.getString("replaceData");

                                // Use the retrieved data as needed
                                // ...

                                // Set the values to the corresponding views
                                String imgUrl = producturl;
                                order_id.setText(orderId);
                                order_total.setText("₹ " + prductPrice);
                                order_name.setText(productName);
                                order_price.setText("₹ " + prductPrice);
                                order_qty.setText(orderQty);
                                order_payment.setText(orderPayment);
                                order_status.setText(orderStatus);
                                order_date.setText(orderDate);
                                order_address.setText(orderAddress);
                                order_delivery_time.setText(orderDeleiveryTime);
                                Glide.with(getApplicationContext()).load(imgUrl).into(imageView);

                                if(orderStatus.equals("Delivered")){
                                    btn_cancel.setVisibility(View.GONE);
                                    order_delivery_time.setVisibility(View.GONE);
                                    order_time_level.setVisibility(View.GONE);
                                    if(replaceData.equals("yes")){
                                        btn_replace.setVisibility(View.VISIBLE);
                                    }
                                    if(returnData.equals("yes")){
                                        btn_return.setVisibility(View.VISIBLE);
                                    }
                                }

                            } else {
                                // Document doesn't exist
                                // Handle the scenario when the document doesn't exist
                                Toast.makeText(OrderTrackingActivity.this, "Restart or Please wait ...", Toast.LENGTH_SHORT).show();

                            }
                        } else {
                            // Error getting the document
                            // Handle the error scenario
                            Toast.makeText(OrderTrackingActivity.this, "Restart or Please wait ...", Toast.LENGTH_SHORT).show();
                        }
                    }
                });






        doucmentId = getIntent().getStringExtra("documentId");






        btn_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(OrderTrackingActivity.this,ReturnActivity.class);
                intent.putExtra("OrderId",orderId);
                intent.putExtra("productPrice",prductPrice);
                intent.putExtra("productUrl",producturl);
                intent.putExtra("productName",productName);
                startActivity(intent);
            }
        });
        btn_replace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(OrderTrackingActivity.this,ReplaceActivity.class);
               intent.putExtra("OrderId",orderId);
                intent.putExtra("productPrice",prductPrice);
                intent.putExtra("productUrl",producturl);
                intent.putExtra("productName",productName);
                startActivity(intent);
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(OrderTrackingActivity.this, R.style.AlertDialogCustom));
                builder.setTitle("Order Cancel");
                builder.setMessage("Are you sure you want to cancel order?");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {


                        firestore.collection("orderDetailedUpdate")
                                .document(orderId)
                                .update("orderStatus", "cancel")
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            // Update successful
                                            Toast.makeText(OrderTrackingActivity.this, "Order Canceled", Toast.LENGTH_SHORT).show();
                                            order_status.setText("cancel");
                                            finish();
                                        } else {
                                            // Update failed
                                            Toast.makeText(OrderTrackingActivity.this, "Restart or Please wait ...", Toast.LENGTH_SHORT).show();

                                        }
                                    }
                                });
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // User canceled the delete operation
                        Toast.makeText(OrderTrackingActivity.this, "Order not cancel! ", Toast.LENGTH_SHORT).show();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();


            }
        });


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