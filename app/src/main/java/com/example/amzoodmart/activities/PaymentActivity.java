package com.example.amzoodmart.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.amzoodmart.R;
import com.google.android.gms.tasks.OnCompleteListener;
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
    double productAmount =0.0;
    int productQty =0;
    String  productName ="";
    String productImgUrl ="";
    String productDesc ="";
   static String orderId =randomOrderId();
    String userName =" ",userNumber =" ",userDistict =" ",userAddDeatail =" ",userCity =" ",userCode="";

    Button paymentBtn,cashOnDel;
    TextView subTotal,name,total;
    ImageView pro_img;
    FirebaseFirestore firestore ;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        toolbar =findViewById(R.id.payment_toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        //data base
        firestore =FirebaseFirestore.getInstance();
        auth =FirebaseAuth.getInstance();
       //geting value for the intent
       productAmount=getIntent().getDoubleExtra("amount",0.0);
       productName= getIntent().getStringExtra("productName");
       productImgUrl= getIntent().getStringExtra("productImgUrl");
       productDesc= getIntent().getStringExtra("productDesc");
       productQty =getIntent().getIntExtra("productQty",0);
       userName= getIntent().getStringExtra("userName");
       userNumber= getIntent().getStringExtra("userNumber");
       userDistict= getIntent().getStringExtra("userDistict");
       userAddDeatail= getIntent().getStringExtra("userAddDeatail");
       userCity= getIntent().getStringExtra("userCity");
       userCode= getIntent().getStringExtra("userCode");



        subTotal =findViewById(R.id.sub_total);
        name =findViewById(R.id.pro_name);
        total = findViewById(R.id.total_amt);
        pro_img =findViewById(R.id.product_img);
        cashOnDel =findViewById(R.id.cod_btn);
        paymentBtn =findViewById(R.id.pay_btn);

        subTotal.setText(productAmount+"");
        total.setText(productAmount+"");
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
                paymentMethod(productAmount);

            }
        });



    }

    private void comfirmOrderOnline() {

        String saveCurrentTime,saveCurrentDate;
        Calendar calForDate =Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MM dd, yyyy");
        saveCurrentDate =currentDate.format(calForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss");
        saveCurrentTime =currentTime.format(calForDate.getTime());

        final HashMap<String,Object> cartMap = new HashMap<>();
        cartMap.put("productName" ,productName);
        cartMap.put("productPrice",productAmount+"");
        cartMap.put("productQty",productQty+"");
        cartMap.put("productDesc" ,productDesc);
        cartMap.put("productImgUrl" ,productImgUrl);
        cartMap.put("Method" ,"Online Payment Mode");
        cartMap.put("orderStatus","Ordered");
        cartMap.put("userName",userName);
        cartMap.put("userNumber",userNumber);
        cartMap.put("userDistict",userDistict);
        cartMap.put("userAddress_detailed",userAddDeatail);
        cartMap.put("orderId",orderId);
        cartMap.put("userCity",userCity);
        cartMap.put("userCode",userCode);
        cartMap.put("currentTime" ,saveCurrentTime);
        cartMap.put("currentDate",saveCurrentDate);


        firestore.collection("OrderDetail").document(Objects.requireNonNull(auth.getCurrentUser()).getUid())
                .collection("User").add(cartMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        Toast.makeText(PaymentActivity.this, "Comfirm Ordered", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
    }

    private void comfirmOrderCod() {
        String saveCurrentTime,saveCurrentDate;
        Calendar calForDate =Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MM dd, yyyy");
        saveCurrentDate =currentDate.format(calForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss");
        saveCurrentTime =currentTime.format(calForDate.getTime());

        final HashMap<String,Object> cartMap = new HashMap<>();
        cartMap.put("productName" ,productName);
        cartMap.put("productPrice",productAmount+"");
        cartMap.put("productDesc" ,productDesc);
        cartMap.put("productQty",productQty+"");
        cartMap.put("productImgUrl" ,productImgUrl);
        cartMap.put("Method" ,"Cash On Delevary");
        cartMap.put("orderStatus","Ordered");
        cartMap.put("userName",userName);
        cartMap.put("userNumber",userNumber);
        cartMap.put("userDistict",userDistict);
        cartMap.put("userAddress_detailed",userAddDeatail);
        cartMap.put("orderId",orderId);
        cartMap.put("userCity",userCity);
        cartMap.put("userCode",userCode);
        cartMap.put("currentTime" ,saveCurrentTime);
        cartMap.put("currentDate",saveCurrentDate);


       firestore.collection("OrderDetail").document(Objects.requireNonNull(auth.getCurrentUser()).getUid())
                .collection("User").add(cartMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        Toast.makeText(PaymentActivity.this, "Comfirm Ordered", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
    }

    public static String randomOrderId() {
        String s ="";
        Random random = new Random();
        s += "ORDERIDAMZ";

        for (int i = 0; i < 3; i++) {
            char randomChar = (char) (random.nextInt(26) + 'A');
            s += randomChar;
        }
        for (int i = 0; i < 5; i++) {
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
            options.put("name", "Amzood Mart");
            //Ref no
            options.put("description", productName);
            //Image to be display
            options.put("image", productImgUrl);
            //options.put("order_id", orderId);
            // Currency type
            options.put("currency", "INR");
            //double total = Double.parseDouble(mAmountText.getText().toString());
            //amount
            amount =amount*100;
            options.put("amount", amount);
            JSONObject preFill = new JSONObject();
            //email
            preFill.put("name", userName);
            //contact
            preFill.put("contact", userNumber);
            preFill.put("orderId",orderId);

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



}
