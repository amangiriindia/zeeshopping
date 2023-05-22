package com.example.amzoodmart.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.amzoodmart.R;
import com.example.amzoodmart.Utility.NetworkChangeListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Objects;
import java.util.Random;

public class PaymentActivity extends AppCompatActivity implements PaymentResultListener {

    Toolbar toolbar;
    double productAmount = 0.0,totalAmount =0.0;
    int productQty = 0;
    String productName = "";
    String productImgUrl = "";
    String productDesc = "";
    static String orderId = randomOrderId();
    String userName = " ", userNumber = " ", userDistict = " ", userAddDeatail = " ", userCity = " ", userCode = "";
    String delivaryTime ="",returnData ="",replaceData ="";
    int delevaryCharge =0;
    Button paymentBtn, cashOnDel;
    TextView subTotal, name, total,delivaryChargeText;
    ImageView pro_img;
    FirebaseFirestore firestore;
    FirebaseAuth auth;
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        toolbar = findViewById(R.id.payment_toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        //data base
        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        //geting value for the intent
        productAmount = getIntent().getDoubleExtra("amount", 0.0);
        productName = getIntent().getStringExtra("productName");
        productImgUrl = getIntent().getStringExtra("productImgUrl");
        productDesc = getIntent().getStringExtra("productDesc");
        productQty = getIntent().getIntExtra("productQty", 0);
        userName = getIntent().getStringExtra("userName");
        userNumber = getIntent().getStringExtra("userNumber");
        userDistict = getIntent().getStringExtra("userDistict");
        userAddDeatail = getIntent().getStringExtra("userAddDeatail");
        userCity = getIntent().getStringExtra("userCity");
        userCode = getIntent().getStringExtra("userCode");
        delivaryTime = getIntent().getStringExtra("delivaryTime");
        delevaryCharge = getIntent().getIntExtra("delivaryCharge",0);
        returnData =getIntent().getStringExtra("returnData");
        replaceData = getIntent().getStringExtra("replaceData");


        subTotal = findViewById(R.id.sub_total);
        name = findViewById(R.id.pro_name);
        total = findViewById(R.id.total_amt);
        pro_img = findViewById(R.id.product_img);
        cashOnDel = findViewById(R.id.cod_btn);
        paymentBtn = findViewById(R.id.pay_btn);
        delivaryChargeText =findViewById(R.id.delivary_Charge);

       totalAmount =productAmount +delevaryCharge;
        subTotal.setText("₹ " + productAmount);
        total.setText("₹ " + totalAmount);
        if(delevaryCharge >0){
            delivaryChargeText.setText("₹ "+delevaryCharge);
        }
        name.setText(productName.toString());
        Glide.with(this).load(productImgUrl).into(pro_img);


        cashOnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                comfirmOrderCod();

            }
        });


        paymentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                paymentMethod(totalAmount);

            }
        });


    }

    private void comfirmOrderOnline() {

        String saveCurrentTime, saveCurrentDate;
        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MM dd, yyyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss");
        saveCurrentTime = currentTime.format(calForDate.getTime());

        final HashMap<String, Object> cartMap = new HashMap<>();
        cartMap.put("productName", productName);
        cartMap.put("productPrice", productAmount + "");
        cartMap.put("productQuantity", productQty + "");
        cartMap.put("productDesc", productDesc);
        cartMap.put("productImgUrl", productImgUrl);
        cartMap.put("Method", "Online Payment Mode");
        cartMap.put("orderStatus", "Ordered");
        cartMap.put("userName", userName);
        cartMap.put("userNumber", userNumber);
        cartMap.put("userDistict", userDistict);
        cartMap.put("userAddress_detailed", userAddDeatail);
        cartMap.put("orderId", orderId);
        cartMap.put("userCity", userCity);
        cartMap.put("userCode", userCode);
        cartMap.put("currentTime", saveCurrentTime);
        cartMap.put("currentDate", saveCurrentDate);
        cartMap.put("delivaryTime",delivaryTime);
        cartMap.put("delivaryCharge",delevaryCharge);
        cartMap.put("returnData",returnData);
        cartMap.put("replaceData",replaceData);
        cartMap.put("flag",false);
        cartMap.put("rnFlag",false);




        firestore.collection("OrderDetail").document(Objects.requireNonNull(auth.getCurrentUser()).getUid())
                .collection("User").add(cartMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        // Toast.makeText(PaymentActivity.this, "Comfirm Ordered", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(PaymentActivity.this, OrderConfirmActivity.class));
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(PaymentActivity.this, "Restart or Please wait ...", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void comfirmOrderCod() {
        String saveCurrentTime, saveCurrentDate;
        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MM dd, yyyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss");
        saveCurrentTime = currentTime.format(calForDate.getTime());

        final HashMap<String, Object> cartMap = new HashMap<>();
        cartMap.put("productName", productName);
        cartMap.put("productPrice", productAmount + "");
        cartMap.put("productDesc", productDesc);
        cartMap.put("productQuantity", productQty + "");
        cartMap.put("productImgUrl", productImgUrl);
        cartMap.put("Method", "Cash On Delevery");
        cartMap.put("orderStatus", "Ordered");
        cartMap.put("userName", userName);
        cartMap.put("userNumber", userNumber);
        cartMap.put("userDistict", userDistict);
        cartMap.put("userAddress_detailed", userAddDeatail);
        cartMap.put("orderId", orderId);
        cartMap.put("userCity", userCity);
        cartMap.put("userCode", userCode);
        cartMap.put("currentTime", saveCurrentTime);
        cartMap.put("currentDate", saveCurrentDate);
        cartMap.put("delivaryTime",delivaryTime);
        cartMap.put("delivaryCharge",delevaryCharge);
        cartMap.put("returnData",returnData);
        cartMap.put("replaceData",replaceData);
        cartMap.put("flag",false);
        cartMap.put("rnFlag",false);



        firestore.collection("OrderDetail").document(Objects.requireNonNull(auth.getCurrentUser()).getUid())
                .collection("User").add(cartMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        // Toast.makeText(PaymentActivity.this, "Comfirm Ordered", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(PaymentActivity.this, OrderConfirmActivity.class));
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(PaymentActivity.this, "Restart or Please wait ...", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public static String randomOrderId() {
        String s = "";
        Random random = new Random();
        s += "ZEE";
        s += "-";
        for (int i = 0; i < 3; i++) {
            int randomNumber = random.nextInt(9);
            s += randomNumber;
        }
        s += "-";
        for (int i = 0; i < 4; i++) {
            int randomNumber = random.nextInt(9);
            s += randomNumber;
        }
        return s;
    }

    private void paymentMethod(double amount) {
        Checkout checkout = new Checkout();

        final Activity activity = PaymentActivity.this;

        try {
            JSONObject options = new JSONObject();
            //Set Company Name
            options.put("name", "Zee Shopping");
            //Ref no
            options.put("description", productName);
            //Image to be display
            options.put("image", productImgUrl);
            //options.put("order_id", orderId);
            // Currency type
            options.put("currency", "INR");
            //double total = Double.parseDouble(mAmountText.getText().toString());
            //amount
            amount = amount * 100;
            options.put("amount", amount);
            JSONObject preFill = new JSONObject();
            //email
            preFill.put("name", userName);
            //contact
            preFill.put("contact", userNumber);
            preFill.put("orderId", orderId);

            options.put("prefill", preFill);

            checkout.open(activity, options);
        } catch (Exception e) {
            Log.e("TAG", "Error in starting Razorpay Checkout", e);
        }
    }


    @Override
    public void onPaymentSuccess(String s) {
        comfirmOrderOnline();

    }

    @Override
    public void onPaymentError(int i, String s) {
        Toast.makeText(this, "Payment Cancel", Toast.LENGTH_SHORT).show();
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
