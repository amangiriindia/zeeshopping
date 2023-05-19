package com.example.amzoodmart.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.amzoodmart.R;
import com.example.amzoodmart.adapters.ShowAllAdapter;
import com.example.amzoodmart.models.ShowAllModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CategotyActivity extends AppCompatActivity {
FirebaseFirestore firestore;
RecyclerView recyclerView;
Toolbar toolbar;
    ShowAllAdapter showAllAdapter;
    List<ShowAllModel> showAllModelList;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categoty);
         Intent intent = getIntent();
        firestore =FirebaseFirestore.getInstance();
        recyclerView = findViewById(R.id.cate_all_rec);

        toolbar =findViewById(R.id.cate_all_toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });



        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
         showAllModelList = new ArrayList<>();
        showAllAdapter = new ShowAllAdapter(this, showAllModelList);
        recyclerView.setAdapter(showAllAdapter);

       //  Retrieve the value of the "product_status" parameter
        String productStatus = intent.getStringExtra("product_status");
        if (!TextUtils.isEmpty(productStatus)) {
            // productStatus is not empty
            // Perform your desired operations here
            if (productStatus.equals("newProduct")) {
                toolbar.setTitle("New Product");
            } else {
                toolbar.setTitle("Popular Product");
            }
            retrieveAllDataByStatus(productStatus);
        }else {
            Toast.makeText(this, "Product is empty", Toast.LENGTH_SHORT).show();
        }

    }

    private void retrieveAllDataByStatus(String productStatus) {
       firestore.collection("ShowAll")
                .whereEqualTo("product_status", productStatus)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            showAllModelList.clear();
                            for (DocumentSnapshot doc : task.getResult().getDocuments()) {
                                ShowAllModel showAllModel = doc.toObject(ShowAllModel.class);
                                showAllModelList.add(showAllModel);
                            }
                            showAllAdapter.notifyDataSetChanged();
                        } else {
                            // Handle errors
                        }
                    }
                });

    }
}