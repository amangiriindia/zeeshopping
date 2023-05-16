package com.example.amzoodmart.activities;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.amzoodmart.R;
import com.example.amzoodmart.adapters.myOrderAdapter;
import com.example.amzoodmart.models.MyOrderModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MyOrderActivity extends AppCompatActivity {


    LinearLayout linearLayout;
    Toolbar toolbar;
    RecyclerView recyclerView;
    myOrderAdapter OrderAdapter ;
    List<MyOrderModel> myOrderModelList;
    FirebaseFirestore firestore;
    FirebaseAuth auth;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order);


        // Create an instance of the broadcast receiver
        IntentFilter intentFilter = new IntentFilter("MyTotalAmount");
        LocalBroadcastManager.getInstance(this).registerReceiver(totalAmountReceiver, intentFilter);


        linearLayout =findViewById(R.id.layout_order_item);
        toolbar =findViewById(R.id.myOrder_toolbar);
        recyclerView =findViewById(R.id.myorder_rec);

        firestore =FirebaseFirestore.getInstance();
        auth =FirebaseAuth.getInstance();


        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        myOrderModelList =new ArrayList<>();
        OrderAdapter =new myOrderAdapter(this,myOrderModelList);
        recyclerView.setAdapter(OrderAdapter);

        firestore.collection("OrderDetail").document(auth.getCurrentUser().getUid())
                .collection("User").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot doc : task.getResult().getDocuments()) {

                                String documentId = doc.getId();

                                MyOrderModel myOrderModel = doc.toObject(MyOrderModel.class);
                                myOrderModel.setDocumentId(documentId);
                                myOrderModelList.add(myOrderModel);
                                OrderAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                });

    }

    private BroadcastReceiver totalAmountReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if ("MyTotalAmount".equals(intent.getAction())) {

                Intent i =new Intent(MyOrderActivity.this,OrderTrackingActivity.class);
                i.putExtra("orderId",intent.getStringExtra("orderId"));
                i.putExtra("orderName",intent.getStringExtra("orderName"));
                i.putExtra("orderPrice",intent.getStringExtra("orderPrice"));
                i.putExtra("orderQty",intent.getStringExtra("orderQty"));
                i.putExtra("orderPayment",intent.getStringExtra("orderPayment"));
                i.putExtra("orderStatus",intent.getStringExtra("orderStatus"));
                i.putExtra("orderDate",intent.getStringExtra("orderDate"));
                i.putExtra("orderImgUrl",intent.getStringExtra("orderImgUrl"));
                i.putExtra("orderAddress",intent.getStringExtra("orderAddress"));
                i.putExtra("documentId",intent.getStringExtra("documentId"));


                startActivity(i);

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