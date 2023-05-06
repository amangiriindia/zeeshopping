package com.example.amzoodmart.activities;

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
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;
import java.util.Objects;

public class PaymentActivity extends AppCompatActivity implements PaymentResultListener {

    Toolbar toolbar;
    double amount =0.0;
    String  productName ="";
    String productImgUrl ="";
    String productDesc ="";

    Button paymentBtn,cashOnDel;
    TextView subTotal,name,total;
    ImageView pro_img;
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
       //geting value for the intent
       amount=getIntent().getDoubleExtra("amount",0.0);
       productName= getIntent().getStringExtra("productName");
       productImgUrl= getIntent().getStringExtra("productImgUrl");
       productDesc= getIntent().getStringExtra("productDesc");


        subTotal =findViewById(R.id.sub_total);
        name =findViewById(R.id.pro_name);
        total = findViewById(R.id.total_amt);
        pro_img =findViewById(R.id.product_img);
        cashOnDel =findViewById(R.id.cod_btn);
        paymentBtn =findViewById(R.id.pay_btn);

        subTotal.setText(amount+"");
        total.setText(amount+"");
        name.setText(productName.toString());
        Glide.with(this).load(productImgUrl).into(pro_img);






        paymentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                paymentMethod();
            }
        });



    }

    private void paymentMethod() {
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
            //options.put("order_id", "order_9A33XWu170gUtm");
            // Currency type
            options.put("currency", "INR");
            //double total = Double.parseDouble(mAmountText.getText().toString());
            //multiply with 100 to get exact amount in rupee
            amount = amount * 100;
            //amount
            options.put("amount", amount);
            JSONObject preFill = new JSONObject();
            //email
            preFill.put("email", "developer.kharag@gmail.com");
            //contact
            preFill.put("contact", "7489347378");

            options.put("prefill", preFill);

            checkout.open(activity, options);
        } catch (Exception e) {
            Log.e("TAG", "Error in starting Razorpay Checkout", e);
        }
    }


    @Override
    public void onPaymentSuccess(String s) {
        Toast.makeText(this, "Payment Successful", Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onPaymentError(int i, String s) {
        Toast.makeText(this, "Payment Cancel", Toast.LENGTH_SHORT).show();
    }



}
