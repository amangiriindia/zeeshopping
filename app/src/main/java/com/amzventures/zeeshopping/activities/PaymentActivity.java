package com.amzventures.zeeshopping.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
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

import com.amzventures.zeeshopping.R;
import com.amzventures.zeeshopping.Utility.NetworkChangeListener;
import com.bumptech.glide.Glide;
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
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;
import java.util.Random;

public class PaymentActivity extends AppCompatActivity implements PaymentResultListener {

    Toolbar toolbar;
    double productAmount = 0.0,
            totalAmount =0.0;
    int productQty = 0;
    String productName = "";
    String productImgUrl = "";
    String productDesc = "";
    static String orderId;
    String userName = " ", userNumber = " ", userDistict = " ", userAddDeatail = " ", userCity = " ", userCode = "",productColor="",productSize="";
    String delivaryTime ="",returnData ="",replaceData ="";
    int delevaryCharge =0;
    Button paymentBtn, cashOnDel;
    String selectedAddress="";
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

        SharedPreferences sharedPreferences = getSharedPreferences("MyPreferences", MODE_PRIVATE);

        productName = sharedPreferences.getString("productName", "");
         productImgUrl = sharedPreferences.getString("productImgUrl", "");
        productDesc = sharedPreferences.getString("productDesc", "");
        delevaryCharge = sharedPreferences.getInt("delivaryCharge", 0);
        int productorgprice = sharedPreferences.getInt("productamount",0);
         returnData = sharedPreferences.getString("returnPolicy", "");
         replaceData = sharedPreferences.getString("replacement", "");
         delivaryTime = sharedPreferences.getString("delevryTime", "");
         productColor = sharedPreferences.getString("productColor", "");
         productSize = sharedPreferences.getString("productSize", "");
        productQty = sharedPreferences.getInt("totalQuantity", 0);
        productAmount = sharedPreferences.getFloat("totalAmount", 0.0f);
        selectedAddress = sharedPreferences.getString("selectedAddress", "");


       // Now you can use these values in your AddressActivity

        //data base
        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        //geting value for the intent

        userName = getIntent().getStringExtra("userName");
        userNumber = getIntent().getStringExtra("userNumber");
        userDistict = getIntent().getStringExtra("userDistict");
        userAddDeatail = getIntent().getStringExtra("userAddDeatail");
        userCity = getIntent().getStringExtra("userCity");
        userCode = getIntent().getStringExtra("userCode");



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

        paymentBtn.setVisibility(View.GONE);

        paymentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                paymentMethod(totalAmount);

            }
        });


    }

    private void comfirmOrderOnline() {
        orderId =  randomOrderId();
        String saveCurrentTime, saveCurrentDate;
        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MM dd, yyyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss");
        saveCurrentTime = currentTime.format(calForDate.getTime());



        final HashMap<String, Object> orderMap = new HashMap<>();
        orderMap.put("productName", productName);
        orderMap.put("productPrice", totalAmount + "");
        orderMap.put("productQuantity", productQty + "");
        orderMap.put("productDesc", productDesc);
        orderMap.put("productImgUrl", productImgUrl);
        orderMap.put("Method", "Online Payment Mode");
        orderMap.put("orderStatus", "Ordered");
        orderMap.put("userName", userName);
        orderMap.put("userNumber", userNumber);
        orderMap.put("userDistict", userDistict);
        orderMap.put("userAddress_detailed", selectedAddress);
        orderMap.put("orderId", orderId);
        orderMap.put("userCity", userCity);
        orderMap.put("userCode", userCode);
        orderMap.put("currentTime", saveCurrentTime);
        orderMap.put("currentDate", saveCurrentDate);
        orderMap.put("delivery_time", delivaryTime);
        orderMap.put("delivery", delevaryCharge);
        orderMap.put("returnData", returnData);
        orderMap.put("replaceData", replaceData);
        orderMap.put("productColor",productColor);
        orderMap.put("productSize",productSize);
        orderMap.put("flag", false);
        orderMap.put("rnFlag", false);

        firestore.collection("orderDetailedUpdate")
                .document(orderId)
                .set(orderMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // Data added successfully
                            // Handle the success scenario here
                        } else {
                            // Failed to add data
                            // Handle the failure scenario here
                            Toast.makeText(PaymentActivity.this, "Restart or Please wait ...", Toast.LENGTH_SHORT).show();

                        }
                    }
                });




        final HashMap<String, Object> cartMap = new HashMap<>();
        cartMap.put("productName", productName);
        cartMap.put("productPrice", productAmount + "");
        cartMap.put("productQuantity", productQty + "");
        cartMap.put("productImgUrl", productImgUrl);
        cartMap.put("Method", "Online Payment Mode");
        cartMap.put("orderStatus", "Ordered");
        cartMap.put("userName", userName);
        cartMap.put("userNumber", userNumber);
        cartMap.put("orderId", orderId);
        cartMap.put("currentDate", saveCurrentDate);




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
        orderId =  randomOrderId();
        final HashMap<String, Object> orderMap = new HashMap<>();
        orderMap.put("productName", productName);
        orderMap.put("productPrice", totalAmount + "");
        orderMap.put("productDesc", productDesc);
        orderMap.put("productQuantity", productQty + "");
        orderMap.put("productImgUrl", productImgUrl);
        orderMap.put("Method", "Cash On Delevery");
        orderMap.put("orderStatus", "Ordered");
        orderMap.put("userName", userName);
        orderMap.put("userNumber", userNumber);
        orderMap.put("userDistict", userDistict);
        orderMap.put("userAddress_detailed", selectedAddress);
        orderMap.put("orderId", orderId);
        orderMap.put("userCity", userCity);
        orderMap.put("userCode", userCode);
        orderMap.put("currentTime", saveCurrentTime);
        orderMap.put("currentDate", saveCurrentDate);
        orderMap.put("delivery_time",delivaryTime);
        orderMap.put("productColor",productColor);
        orderMap.put("productSize",productSize);
        orderMap.put("delivery",delevaryCharge);
        orderMap.put("returnData",returnData);
        orderMap.put("replaceData",replaceData);
        orderMap.put("flag",false);
        orderMap.put("rnFlag",false);

        firestore.collection("orderDetailedUpdate")
                .document(orderId)
                .set(orderMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // Success
                            orderSoundFlag();
                            // startActivity(new Intent(PaymentActivity.this, OrderConfirmActivity.class));
                            //  finish();
                        } else {
                            // Failure
                            Toast.makeText(PaymentActivity.this, "Restart or Please wait...", Toast.LENGTH_SHORT).show();
                        }
                    }
                });



        final HashMap<String, Object> cartMap = new HashMap<>();
        cartMap.put("productName", productName);
        cartMap.put("productPrice", totalAmount + "");
        cartMap.put("productQuantity", productQty + "");
        cartMap.put("productImgUrl", productImgUrl);
        cartMap.put("Method", "Cash on delivery");
        cartMap.put("orderStatus", "Ordered");
        cartMap.put("userName", userName);
        cartMap.put("userNumber", userNumber);
        cartMap.put("orderId", orderId);
        cartMap.put("currentDate", saveCurrentDate);



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
        String orderId = "";
        Random random = new Random();

        // Generate a timestamp
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyMMddHHmmssS");
        String timestamp = dateFormat.format(new Date());

        // Calculate the remaining length to achieve a total length of 16
        int remainingLength = 16 - timestamp.length();

        // Adjust the length to a minimum of 0
        remainingLength = Math.max(0, remainingLength);

        // Generate a random number with enough digits to complete the total length
        int maxRandomNumber = (int) Math.pow(10, remainingLength) - 1;
        int randomNumber = random.nextInt(maxRandomNumber);

        // Combine timestamp and random number to achieve a total length of 16
        orderId = timestamp + String.format("%0" + remainingLength + "d", randomNumber);

        return orderId;
    }








        private static MediaPlayer mediaPlayer;

        public static void playSound(Context context) {
            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
                mediaPlayer.release();
            }

            // Request audio focus
            AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
            int result = audioManager.requestAudioFocus(
                    new AudioManager.OnAudioFocusChangeListener() {
                        @Override
                        public void onAudioFocusChange(int focusChange) {
                            // Handle audio focus changes appropriately
                            // (e.g., pause/resume playback, duck volume, etc.)
                        }
                    },
                    AudioManager.STREAM_MUSIC,
                    AudioManager.AUDIOFOCUS_GAIN_TRANSIENT
            );

            if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                mediaPlayer = MediaPlayer.create(context, R.raw.orderconfirmed); // Replace with your sound file
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        mp.release();
                        audioManager.abandonAudioFocus(null);
                    }
                });
                mediaPlayer.start();
            }
        }





























    private void orderSoundFlag() {
        final HashMap<String, Object> dataMap = new HashMap<>();
        dataMap.put("soundFlag", true);


        firestore.collection("tempdata").document("ZAlWZ3wsbRgsRJzVQPyM").set(dataMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(PaymentActivity.this, "Confirmed order", Toast.LENGTH_SHORT).show();
                        playSound(PaymentActivity.this);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(PaymentActivity.this, "Failed to update sound flag", Toast.LENGTH_SHORT).show();
                    }
                });
    }









    private void paymentMethod(double amount) {
        Checkout checkout = new Checkout();

        final Activity activity = PaymentActivity.this;

        try {
            JSONObject options = new JSONObject();
            //Set Company Name
            options.put("name", "Zee Shopping");
            //Ref no
            options.put("description", productName +" , orderId  :  "+orderId);
            //Image to be display
            options.put("image", productImgUrl);
            options.put("orderId",orderId+"");
            // Currency type
            options.put("currency", "INR");
            //amount
            amount = amount * 100;
            options.put("amount", amount);
            JSONObject preFill = new JSONObject();
            //email
            preFill.put("name", userName);
            //contact
            preFill.put("contact", userNumber);

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
