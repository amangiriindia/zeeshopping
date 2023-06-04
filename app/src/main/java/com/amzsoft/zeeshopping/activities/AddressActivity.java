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
import com.amzsoft.zeeshopping.models.DiscountModel;
import com.amzsoft.zeeshopping.models.NewProductsModel;
import com.amzsoft.zeeshopping.models.PopularProductsModel;
import com.amzsoft.zeeshopping.models.ShowAllModel;
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
    String cartProductName = "", cartProductImgUrl = "";
    int cartProductPrice = 0, cartProductQty = 0, qty = 0;

    String userName = " ", userNumber = " ", userDistict = " ", userAddDeatail = " ", userCity = " ", userCode = "";
    RecyclerView recyclerView;
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


        paymentBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                double amount = 0.0;
                String productName = "";
                String productImgUrl = "";
                String productDesc = "";
                int productQty = 1;
                int delivaryChage =0;
                String returnPolicy ="";
                String replacement ="";
                String delevryTime ="";
                if (obj instanceof NewProductsModel) {
                    NewProductsModel newProductsModel = (NewProductsModel) obj;
                    amount = newProductsModel.getPrice();
                    productName = newProductsModel.getName();
                    productImgUrl = newProductsModel.getImg_url();
                    productDesc = newProductsModel.getDescription();
                    productQty = qty;
                    delivaryChage = newProductsModel.getDelivery();
                    returnPolicy = newProductsModel.getReturn1();
                    replacement = newProductsModel.getReplace();
                    delevryTime = newProductsModel.getDelivery_time();

                } else if (obj instanceof PopularProductsModel) {
                    PopularProductsModel popularProductsModel = (PopularProductsModel) obj;
                    amount = popularProductsModel.getPrice();
                    productName = popularProductsModel.getName();
                    productImgUrl = popularProductsModel.getImg_url();
                    productDesc = popularProductsModel.getDescription();
                    productQty = qty;
                    delivaryChage = popularProductsModel.getDelivery();
                    returnPolicy = popularProductsModel.getReturn1();
                    replacement = popularProductsModel.getReplace();
                    delevryTime = popularProductsModel.getDelivery_time();
                } else if (obj instanceof ShowAllModel) {
                    ShowAllModel showAllModel = (ShowAllModel) obj;
                    amount = showAllModel.getPrice();
                    productName = showAllModel.getName();
                    productImgUrl = showAllModel.getImg_url();
                    productDesc = showAllModel.getDescription();
                    productQty = qty;
                    delivaryChage = showAllModel.getDelivery();
                    returnPolicy = showAllModel.getReturn1();
                    replacement = showAllModel.getReplace();
                    delevryTime = showAllModel.getDelivery_time();
                }else if (obj instanceof DiscountModel) {
                        DiscountModel discountModel = (DiscountModel) obj;
                        amount = discountModel.getPrice();
                        productName = discountModel.getName();
                        productImgUrl = discountModel.getImg_url();
                        productDesc = discountModel.getDescription();
                        productQty = qty;
                        delivaryChage = discountModel.getDelivery();
                        returnPolicy = discountModel.getReturn1();
                        replacement = discountModel.getReplace();
                        delevryTime = discountModel.getDelivery_time();
                } else {
                    productName = cartProductName;
                    amount = cartProductPrice;
                    productImgUrl = cartProductImgUrl;
                    productQty = cartProductQty;
                }

                if (flagAddress) {
                    Intent intent = new Intent(AddressActivity.this, PaymentActivity.class);
                    intent.putExtra("amount", amount*productQty);
                    intent.putExtra("productName", productName);
                    intent.putExtra("productImgUrl", productImgUrl);
                    intent.putExtra("productDesc", productDesc);
                    intent.putExtra("productQty", productQty);
                    intent.putExtra("userName", userName);
                    intent.putExtra("userNumber", userNumber);
                    intent.putExtra("userDistict", userDistict);
                    intent.putExtra("userAddDeatail", userAddDeatail);
                    intent.putExtra("userCity", userCity);
                    intent.putExtra("userCode", userCode);
                    intent.putExtra("delivaryTime",delevryTime);
                    intent.putExtra("delivaryCharge",delivaryChage);
                    intent.putExtra("returnData",returnPolicy);
                    intent.putExtra("replaceData",replacement);
                    startActivity(intent);
                } else {
                    Toast.makeText(AddressActivity.this, "Please Add Address!", Toast.LENGTH_SHORT).show();
                }


            }
        });

        addAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AddressActivity.this, AddAddressActivity.class));
                finish();
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