package com.amzventures.zeeshopping.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amzventures.zeeshopping.R;
import com.amzventures.zeeshopping.Utility.NetworkChangeListener;
import com.amzventures.zeeshopping.adapters.AddressAdapter;
import com.amzventures.zeeshopping.models.AddressModel;
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

public class AddressActivity extends AppCompatActivity implements AddressAdapter.SelectedAddress {

    Button addAddress;

    String userName = " ", userNumber = " ", userDistict = " ", userAddDeatail = " ", userCity = " ", userCode = "";
    RecyclerView recyclerView;

    private AddressAdapter addressAdapter;
    private List<AddressModel> addressModelList;
    FirebaseFirestore firestore;
    FirebaseAuth auth;
    private SharedPreferences sharedPreferences;
    Button  paymentBtn;
    boolean flagAddress;
    Toolbar toolbar;
    String mAddress = "";
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);

        toolbar = findViewById(R.id.address_toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);






        firestore = FirebaseFirestore.getInstance();


        auth = FirebaseAuth.getInstance();

        recyclerView = findViewById(R.id.address_recycler);
        paymentBtn = findViewById(R.id.payment_btn);
        addAddress = findViewById(R.id.add_address_btn);

        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        addressModelList = new ArrayList<>();
        addressAdapter = new AddressAdapter(getApplicationContext(), addressModelList, this);
        recyclerView.setAdapter(addressAdapter);

        firestore.collection("CurrentUser.").document(Objects.requireNonNull(auth.getCurrentUser()).getUid())
                .collection("Address").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot doc : task.getResult().getDocuments()) {

                                AddressModel addressModel = doc.toObject(AddressModel.class);
                                addressModelList.add(addressModel);
                                userName = addressModel.getUserName();
                                userNumber = addressModel.getUserNumber();
                                userCity = addressModel.getUserCity();
                                userDistict = addressModel.getUserDistict();
                                userAddDeatail = addressModel.getUserAddress_detailed();
                                userCode = addressModel.getUserCode();
                                addressAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddressActivity.this, "Restart or Please wait ...", Toast.LENGTH_SHORT).show();
                    }
                });

        addAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AddressActivity.this, AddAddressActivity.class));
                finish();
            }
        });


        paymentBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {





                if (flagAddress) {
                    Intent intent = new Intent(AddressActivity.this, PaymentActivity.class);

                    intent.putExtra("userName", userName);
                    intent.putExtra("userNumber", userNumber);
                    intent.putExtra("userDistict", userDistict);
                    intent.putExtra("userAddDeatail", userAddDeatail);
                    intent.putExtra("userCity", userCity);
                    intent.putExtra("userCode", userCode);
                    startActivity(intent);
                } else {
                    Toast.makeText(AddressActivity.this, "Please Add Address!", Toast.LENGTH_SHORT).show();
                }


            }
        });


    }

    @Override
    public void onRadioButtonSelected(boolean flag) {
        // Handle the flag value here
        flagAddress = flag;

    }

    @Override
    public void onAddressSelected(String address) {

        // Save the selected address in SharedPreferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("selectedAddress", address);
        editor.apply();


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