package com.amzventures.zeeshopping.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amzventures.zeeshopping.R;
import com.amzventures.zeeshopping.Utility.NetworkChangeListener;
import com.amzventures.zeeshopping.adapters.ShowAllAdapter;
import com.amzventures.zeeshopping.models.ShowAllModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
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
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();
    ShowAllAdapter showAllAdapter;
    List<ShowAllModel> showAllModelList;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categoty);
        Intent intent = getIntent();
        firestore = FirebaseFirestore.getInstance();
        recyclerView = findViewById(R.id.cate_all_rec);

        toolbar = findViewById(R.id.cate_all_toolbar);
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
        String title =intent.getStringExtra("title");
        int off =intent.getIntExtra("off",0);
        if(!TextUtils.isEmpty(title)){
            toolbar.setTitle(title);
        }

        if (!TextUtils.isEmpty(productStatus)) {
            retrieveAllDataByStatus(productStatus);
        } else if (off !=0) {
            retrieveAllDataByOffer(off);
        } else {
            retrieveAllDataByRating(4);

        }

    }

    private void retrieveAllDataByOffer(int i) {
        firestore.collection("ShowAll")
                .whereGreaterThan("offer", i)
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
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(CategotyActivity.this, "Please Wait....", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void retrieveAllDataByRating(int i) {
        firestore.collection("ShowAll")
                .whereGreaterThan("rating", i)
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
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(CategotyActivity.this, "Restart or Please Wait....", Toast.LENGTH_SHORT).show();
                    }
                });
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
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(CategotyActivity.this, "Restart or Please Wait....", Toast.LENGTH_SHORT).show();
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