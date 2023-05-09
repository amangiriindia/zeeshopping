package com.example.amzoodmart.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.amzoodmart.R;
import com.example.amzoodmart.adapters.MyCartAdapter;
import com.example.amzoodmart.adapters.myOrderAdapter;
import com.example.amzoodmart.models.MyCartModel;
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
    ImageView productImage;
    TextView productName,productDesc,productQuantity,productPrice;
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

        productImage =findViewById(R.id.order_item_image);
        productName =findViewById(R.id.order_item_name);
        //productDesc =findViewById(R.id.order_item_description);
        productPrice =findViewById(R.id.order_item_price);
        productQuantity=findViewById(R.id.order_item_quantity);
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
                        if(task.isSuccessful()){
                            for (DocumentSnapshot doc :task.getResult().getDocuments()){

                                MyOrderModel myOrderModel =doc.toObject(MyOrderModel.class);
                                myOrderModelList.add(myOrderModel);
                                OrderAdapter.notifyDataSetChanged();
                            }
                        }


                    }
                });

    }
}