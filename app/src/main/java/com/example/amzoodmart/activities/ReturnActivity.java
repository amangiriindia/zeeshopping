package com.example.amzoodmart.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.amzoodmart.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Objects;

public class ReturnActivity extends AppCompatActivity {

    Toolbar toolbar;
    TextView returnOrderId,returnProduct,returnPrice,returnOutput,returnOutputProblem,returnOutputDesc;
    EditText returnProblem,returnproblemDesc,returnPhoneNumber,returnAccountNumber,returnIFSCcode,returnAccName;
    Button btnReqReturn;
    ImageView returnImg;
    FirebaseFirestore firestore;
    FirebaseAuth auth;
    String problem="",problemDesc="",replaceData="",orderId;
    LinearLayout returnLInput,returnLOutput,returnLAccount;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_return);

        toolbar = findViewById(R.id.return_toolbar);
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

        String imgUrl = getIntent().getStringExtra("productUrl");
        returnProduct =findViewById(R.id.return_order_name);
        returnPrice =findViewById(R.id.return_order_price);
        returnOutput =findViewById(R.id.return_reply);
        returnOrderId=findViewById(R.id.return_order_id);
        returnProblem =findViewById(R.id.retrun_problem);
        returnproblemDesc =findViewById(R.id.return_description);
        btnReqReturn =findViewById(R.id.return_submit_btn);
        returnImg =findViewById(R.id.return_product_img);
        returnAccName =findViewById(R.id.return_account_holder_name);
        returnAccountNumber =findViewById(R.id.return_account_number);
        returnIFSCcode =findViewById(R.id.return_ifsc_code);
        returnPhoneNumber =findViewById(R.id.return_phone_number);
        returnLOutput =findViewById(R.id.return_linear_output);
        returnLInput =findViewById(R.id.return_linear_input);
        returnLAccount =findViewById(R.id.linear_cod_return);
        returnOutputProblem =findViewById(R.id.return_output_problem);
        returnOutputDesc =findViewById(R.id.return_output_desc);

        orderId =getIntent().getStringExtra("OrderId");
        returnProduct.setText(getIntent().getStringExtra("productName"));
        returnPrice.setText(getIntent().getStringExtra("productPrice"));
        returnOrderId.setText(orderId);
        Glide.with(getApplicationContext()).load(imgUrl).into(returnImg);

        btnReqReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pnumber="",accountNumber="",ifscCode="",accName="";
                pnumber =returnPhoneNumber.getText().toString();
                accountNumber =returnAccountNumber.getText().toString();
                ifscCode =returnIFSCcode.getText().toString();
                accName =returnAccName.getText().toString();
                problem =returnProblem.getText().toString();
                problemDesc =returnproblemDesc.getText().toString();
                replaceData =returnOutput.getText().toString();


                if (TextUtils.isEmpty(problem)) {
                    returnProblem.setError("Enter your Problem!");
                    return;
                }
                if (TextUtils.isEmpty(problemDesc)) {
                    returnproblemDesc.setError("Enter your Problem Description!");
                    return;
                }
                if (TextUtils.isEmpty(pnumber)) {
                    returnPhoneNumber.setError("Enter your Phone Number!");
                    return;
                }
                if (TextUtils.isEmpty(accountNumber)) {
                    returnAccountNumber.setError("Enter your Problem Account Number!");
                    return;
                }
                if (TextUtils.isEmpty(accName)) {
                    returnAccName.setError("Enter your Account Holder Name!");
                    return;
                }
                if (TextUtils.isEmpty(ifscCode)) {
                    returnIFSCcode.setError("Enter your IFSC code!");
                    return;
                }

                final HashMap<String, Object> cartMap = new HashMap<>();
                cartMap.put("rnMethod","return");
                cartMap.put("rnProblem",problem);
                cartMap.put("rnProblemDesc",problemDesc);
                cartMap.put("rnMessage",replaceData);
                cartMap.put("rnFlag",true);
                cartMap.put("rnAccountNumber",accountNumber);
                cartMap.put("rnAccName",accName);
                cartMap.put("rnPhoneNumber",pnumber);
                cartMap.put("rnIfscCode",ifscCode);



                firestore.collection("OrderDetail")
                        .document(Objects.requireNonNull(auth.getCurrentUser()).getUid())
                        .collection("User")
                        .whereEqualTo("orderId", orderId)  // Add a query to find the document with matching orderId
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        document.getReference().update(cartMap)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        returnLInput.setVisibility(View.GONE);
                                                        returnLAccount.setVisibility(View.GONE);
                                                        returnLOutput.setVisibility(View.VISIBLE);
                                                        returnMethod();
                                                        Toast.makeText(ReturnActivity.this, "Your request has been submitted", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                    }
                                } else {
                                    // Handle error
                                }
                            }
                        });

            }
        });



    }

    private void returnMethod() {

        firestore.collection("OrderDetail")
                .document(Objects.requireNonNull(auth.getCurrentUser()).getUid())
                .collection("User")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                // Get the data from the document
                                String rnProblem = document.getString("rnProblem");
                                String rnProblemDesc = document.getString("rnProblemDesc");
                                String rnMessage = document.getString("rnMessage");

                                // Do something with the retrieved data
                                // For example, display it in a TextView
                                returnOutput.setText(rnMessage);
                                returnOutputProblem.setText(rnProblem);
                                returnOutputDesc.setText(rnProblemDesc);

                            }
                        } else {

                        }
                    }
                });

    }

    @Override
    protected void onStart() {
        flagSet();
        super.onStart();
    }

    private void flagSet() {
        firestore.collection("OrderDetail")
                .document(Objects.requireNonNull(auth.getCurrentUser()).getUid())
                .collection("User")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                // Get the data from the document
                                boolean flag = document.getBoolean("rnFlag");
                                if(flag) {
                                    returnLAccount.setVisibility(View.GONE);
                                    returnLInput.setVisibility(View.GONE);
                                    returnLOutput.setVisibility(View.VISIBLE);
                                    returnMethod();
                                }


                            }
                        } else {

                        }
                    }
                });
    }
}