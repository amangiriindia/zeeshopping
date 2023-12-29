package com.amzsoft.zeeshopping.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentFilter;
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

import com.amzsoft.zeeshopping.R;
import com.amzsoft.zeeshopping.Utility.NetworkChangeListener;
import com.amzsoft.zeeshopping.adapters.AddressAdapter;
import com.amzsoft.zeeshopping.models.AddressModel;
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
    String cartProductName = "", cartProductImgUrl = "",productColor="",productSize ="";
    int cartProductPrice = 0, cartProductQty = 0, qty = 0;

    String userName = " ", userNumber = " ", userDistict = " ", userAddDeatail = " ", userCity = " ", userCode = "";
    RecyclerView recyclerView;
    String productName = "";
    String productImgUrl = "";
    private AddressAdapter addressAdapter;
    private List<AddressModel> addressModelList;
    FirebaseFirestore firestore;
    FirebaseAuth auth;
    Button addAddressBtn, paymentBtn;
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

        //get data form detailed activity
        Object obj = getIntent().getSerializableExtra("item");
        qty = getIntent().getIntExtra("Qty", 0);
        userName = getIntent().getStringExtra("userName");
        userNumber = getIntent().getStringExtra("userNumber");
        userDistict = getIntent().getStringExtra("userDistict");
        userAddDeatail = getIntent().getStringExtra("userAddDeatail");
        userCity = getIntent().getStringExtra("userCity");
        userCode = getIntent().getStringExtra("userCode");
        productSize =getIntent().getStringExtra("productSize");
        productColor =getIntent().getStringExtra("productColor");




        // get data form cart
        cartProductName = getIntent().getStringExtra("cartProductName");
        cartProductImgUrl = getIntent().getStringExtra("cartProductImg");
        cartProductPrice = getIntent().getIntExtra("cartProductPrice", 0);
        cartProductQty = getIntent().getIntExtra("cartProductQty", 0);

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

                                addressAdapter.notifyDataSetChanged();

                                userName = addressModel.getUserName();
                                userNumber = addressModel.getUserNumber();
                                userCity = addressModel.getUserCity();
                                userDistict = addressModel.getUserDistict();
                                userAddDeatail = addressModel.getUserAddress_detailed();
                                userCode = addressModel.getUserCode();
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
    public void setAddress(String address) {
        mAddress = address;
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