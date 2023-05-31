package com.example.amzoodmart.activities;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.amzoodmart.R;
import com.example.amzoodmart.Utility.NetworkChangeListener;
import com.example.amzoodmart.adapters.MyCartAdapter;
import com.example.amzoodmart.models.MyCartModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class CartActivity extends AppCompatActivity {

    //int overAllTotalAmount;
    String cartProductName = "", cartProductImg;
    int carttotalPrice = 0, cartTotalQty = 0;
    TextView overAllAmount, emptyCartText;
    Toolbar toolbar;
    Button buyNow;

    RecyclerView recyclerView;
    List<MyCartModel> cartModelsList;
    MyCartAdapter cartAdapter;
    private FirebaseAuth auth;
    private FirebaseFirestore firestore;
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        toolbar = findViewById(R.id.my_cart_toolbar_menu);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        overAllAmount = findViewById(R.id.my_cart_total_price);
        buyNow = findViewById(R.id.cart_buy_now);
        cartProductImg = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSdQ-pKb8VBKhKYBj-6o2mM0h3i6dzV6tGbrGykMoC0Hw&usqp=CAU&ec=48665699";


        // GET DATA FROM MY CLASS ADAPTER
        IntentFilter intentFilter = new IntentFilter("MyTotalAmount");
        LocalBroadcastManager.getInstance(this).registerReceiver(totalAmountReceiver, intentFilter);


        buyNow.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if (carttotalPrice <= 0) {
                    Toast.makeText(CartActivity.this, "Please Add Item On Cart", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(CartActivity.this, AddressActivity.class);
                intent.putExtra("cartProductName", cartProductName);
                intent.putExtra("cartProductImg", cartProductImg);
                intent.putExtra("cartProductPrice", carttotalPrice);
                intent.putExtra("cartProductQty", cartTotalQty);
                startActivity(intent);
            }
        });

        recyclerView = findViewById(R.id.cart_rec);
        emptyCartText = findViewById(R.id.empty_cart_text);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        cartModelsList = new ArrayList<>();
        cartAdapter = new MyCartAdapter(this, cartModelsList);
        recyclerView.setAdapter(cartAdapter);

        firestore.collection("AddToCart").document(auth.getCurrentUser().getUid())
                .collection("User").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot doc : task.getResult().getDocuments()) {
                                String documentId = doc.getId();
                                MyCartModel myCartModel = doc.toObject(MyCartModel.class);
                                myCartModel.setDocumentId(documentId);
                                cartModelsList.add(myCartModel);
                            }
                            cartAdapter.notifyDataSetChanged();
                            // Check if the data set is empty and set the visibility of the empty text
                            if (cartAdapter.getItemCount() == 0) {
                                emptyCartText.setVisibility(View.VISIBLE);
                            } else {
                                emptyCartText.setVisibility(View.GONE);
                            }
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(CartActivity.this, "Restart or Please wait ...", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private BroadcastReceiver totalAmountReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if ("MyTotalAmount".equals(intent.getAction())) {
                int totalAmount = intent.getIntExtra("totalAmount", 0);
                int qty = intent.getIntExtra("Qty", 0);
                String productName = intent.getStringExtra("pname");
                overAllAmount.setText("₹ " + String.valueOf(totalAmount));
                cartProductName = productName;
                carttotalPrice = totalAmount;
                cartTotalQty = qty;
                // Handle the received totalAmount value
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Unregister the BroadcastReceiver
        LocalBroadcastManager.getInstance(this).unregisterReceiver(totalAmountReceiver);
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