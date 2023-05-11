package com.example.amzoodmart.activities;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.TextView;

import com.example.amzoodmart.R;
import com.example.amzoodmart.adapters.MyCartAdapter;
import com.example.amzoodmart.models.MyCartModel;
import com.example.amzoodmart.models.ShowAllModel;
import com.google.android.gms.tasks.OnCompleteListener;
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
    TextView overAllAmount;
    Toolbar toolbar;

    RecyclerView recyclerView;
    List<MyCartModel> cartModelsList;
    MyCartAdapter cartAdapter;
    private FirebaseAuth auth;
    private FirebaseFirestore firestore;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        auth =FirebaseAuth.getInstance();
        firestore =FirebaseFirestore.getInstance();

        toolbar =findViewById(R.id.my_cart_toolbar_menu);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        overAllAmount =findViewById(R.id.my_cart_total_price);

       // GET DATA FROM MY CLASS ADAPTER
        IntentFilter intentFilter = new IntentFilter("MyTotalAmount");
        LocalBroadcastManager.getInstance(this).registerReceiver(totalAmountReceiver, intentFilter);





        recyclerView =findViewById(R.id.cart_rec);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        cartModelsList =new ArrayList<>();
        cartAdapter =new MyCartAdapter(this,cartModelsList);
        recyclerView.setAdapter(cartAdapter);

        firestore.collection("AddToCart").document(auth.getCurrentUser().getUid())
                .collection("User").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for (DocumentSnapshot doc :task.getResult().getDocuments()){

                                String documentId =doc.getId();

                                MyCartModel myCartModel =doc.toObject(MyCartModel.class);
                                myCartModel.setDocumentId(documentId);
                                cartModelsList.add(myCartModel);
                                cartAdapter.notifyDataSetChanged();
                            }
                        }


                    }
                });
    }
private BroadcastReceiver totalAmountReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if ("MyTotalAmount".equals(intent.getAction())) {
                int totalAmount = intent.getIntExtra("totalAmount", 0);
                overAllAmount.setText(String.valueOf(totalAmount));
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

}