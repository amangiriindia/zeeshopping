package com.example.amzoodmart.activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.amzoodmart.R;
import com.example.amzoodmart.Utility.NetworkChangeListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class OrderTrackingActivity extends AppCompatActivity {

    Toolbar toolbar;
    Button btn_cancel;
    ImageView imageView;
    String doucmentId = "";
    TextView order_name, order_id, order_price, order_qty, order_payment, order_status, order_date, order_address, order_total;
    FirebaseFirestore firestore;
    FirebaseAuth auth;
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_tracking);

        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

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

        String imgUrl = getIntent().getStringExtra("orderImgUrl");
        order_id.setText(getIntent().getStringExtra("orderId"));
        order_total.setText("₹ " + getIntent().getStringExtra("orderPrice"));
        order_name.setText(getIntent().getStringExtra("orderName"));
        order_price.setText("₹ " + getIntent().getStringExtra("orderPrice"));
        order_qty.setText(getIntent().getStringExtra("orderQty"));
        order_payment.setText(getIntent().getStringExtra("orderPayment"));
        order_status.setText(getIntent().getStringExtra("orderStatus"));
        order_date.setText(getIntent().getStringExtra("orderDate"));
        order_address.setText(getIntent().getStringExtra("orderAddress"));
        Glide.with(getApplicationContext()).load(imgUrl).into(imageView);
        doucmentId = getIntent().getStringExtra("documentId");


        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(OrderTrackingActivity.this, R.style.AlertDialogCustom));
                builder.setTitle("Order Cancel");
                builder.setMessage("Are you sure you want to cancel order?");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        // Perform delete operation or other actions here
                        DocumentReference documentRef = firestore.collection("OrderDetail")
                                .document(Objects.requireNonNull(auth.getCurrentUser()).getUid())
                                .collection("User")
                                .document(doucmentId); // Replace 'documentId' with the ID of the specific document you want to update


                        documentRef.update("orderStatus", "cancel")
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(OrderTrackingActivity.this, "Order Canceled", Toast.LENGTH_SHORT).show();
                                        order_status.setText("cancel");
                                        finish();
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