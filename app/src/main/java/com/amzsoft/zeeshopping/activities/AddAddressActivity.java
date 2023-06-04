package com.amzsoft.zeeshopping.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.amzsoft.zeeshopping.R;
import com.amzsoft.zeeshopping.Utility.NetworkChangeListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AddAddressActivity extends AppCompatActivity {
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();
    EditText name, address, city, postalCode, phoneNumber, distict, detailed;
    String final_address = " ";
    Toolbar toolbar;
    Button addAdresssBtn;
    static boolean flag = true;
    FirebaseFirestore firestore;
    FirebaseAuth auth;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);

        toolbar = findViewById(R.id.add_address_toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        name = findViewById(R.id.ad_name);
        address = findViewById(R.id.ad_address);
        city = findViewById(R.id.ad_city);
        postalCode = findViewById(R.id.ad_code);
        phoneNumber = findViewById(R.id.ad_phone);
        distict = findViewById(R.id.ad_district);
        detailed = findViewById(R.id.ad_detailed);
        addAdresssBtn = findViewById(R.id.ad_add_address);

        addAdresssBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userName = name.getText().toString();
                String userCity = city.getText().toString();
                String userAddress = address.getText().toString();
                String userCode = postalCode.getText().toString();
                String userNumber = phoneNumber.getText().toString();
                String userDistict = distict.getText().toString();
                String userAddDeatail = detailed.getText().toString();

                if (TextUtils.isEmpty(userName)) {
                    name.setError("Enter Name!");
                    return;
                }
                if (TextUtils.isEmpty(userAddress)) {
                    address.setError("Enter your Address!");
                    return;
                }
                if (TextUtils.isEmpty(userCity)) {
                    city.setError("Enter City!");
                    return;
                }
                if (TextUtils.isEmpty(userCode)) {
                    postalCode.setError("Enter Pin Code!");
                    return;
                }
                if (TextUtils.isEmpty(userNumber)) {
                    phoneNumber.setError("Enter Phone Number!");
                    return;
                }
                if (TextUtils.isEmpty(userDistict)) {
                    distict.setError("Enter Distict!");
                    return;
                }
                if (TextUtils.isEmpty(userAddDeatail)) {
                    detailed.setError("Enter Detailed Address!");
                    return;
                }


                if (!userName.isEmpty()) {
                    final_address += userName + ", ";
                }
                if (!userCity.isEmpty()) {
                    final_address += userCity + ", ";
                }
                if (!userAddress.isEmpty()) {
                    final_address += userAddress + ", ";
                }
                if (!userCode.isEmpty()) {
                    final_address += userCode + ", ";
                }
                if (!userDistict.isEmpty()) {
                    final_address += userDistict + ", ";
                }
                if (!userAddDeatail.isEmpty()) {
                    final_address += userAddDeatail + ", ";
                }
                if (!userNumber.isEmpty()) {
                    final_address += userNumber + ".";
                }
                if (!userName.isEmpty() && !userCity.isEmpty() && !userAddress.isEmpty() && !userCode.isEmpty() && !userNumber.isEmpty() && !userDistict.isEmpty() && !userAddDeatail.isEmpty()) {
                    Map<String, String> map = new HashMap<>();

                    map.put("userName", userName);
                    map.put("userNumber", userNumber);
                    map.put("userDistict", userDistict);
                    map.put("userAddress_detailed", userAddDeatail);
                    map.put("userCity", userCity);
                    map.put("userCode", userCode);
                    map.put("userAddress", final_address);

                    firestore.collection("CurrentUser.").document(Objects.requireNonNull(auth.getCurrentUser()).getUid())
                            .collection("Address").add(map).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentReference> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(AddAddressActivity.this, "Address Added", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(AddAddressActivity.this, AddressActivity.class);
                                        intent.putExtra("userName", userName);
                                        intent.putExtra("userNumber", userNumber);
                                        intent.putExtra("userDistict", userDistict);
                                        intent.putExtra("userAdd_deatil", userAddDeatail);
                                        intent.putExtra("userCity", userCity);
                                        intent.putExtra("userCode", userCode);

                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(AddAddressActivity.this, "Restart or Please wait ...", Toast.LENGTH_SHORT).show();
                                }
                            });
                } else {
                    Toast.makeText(AddAddressActivity.this, "Kindly Fill All Field", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
    @Override
    protected void onStart() {
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeListener,filter);
        super.onStart();
    }

    @Override
    protected void onStop() {
        unregisterReceiver(networkChangeListener);
        super.onStop();
    }
}