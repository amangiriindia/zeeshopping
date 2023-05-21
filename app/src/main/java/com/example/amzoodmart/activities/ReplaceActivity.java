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

public class ReplaceActivity extends AppCompatActivity {
    Toolbar toolbar;
    TextView replaceOrderId,replaceProduct,replacePrice,replaceOutput,replaceOutputProblem,replaceOutputDesc;
    EditText replaceProblem,replaceproblemDesc;
    Button btnReqReplace;
    ImageView replaceImg;
    String problem="",problemDesc="",replaceData="",orderId;
    FirebaseFirestore firestore;
    FirebaseAuth auth;
    LinearLayout replaceLInput,replaceLOutput;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_replace);

        firestore =FirebaseFirestore.getInstance();
        auth =FirebaseAuth.getInstance();

        toolbar = findViewById(R.id.replace_toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        String imgUrl = getIntent().getStringExtra("productUrl");
        replaceProduct =findViewById(R.id.replace_order_name);
        replacePrice =findViewById(R.id.replace_order_price);
        replaceOutput =findViewById(R.id.replace_reply);
        replaceOrderId =findViewById(R.id.replace_order_id);
        replaceProblem =findViewById(R.id.replace_problem);
        replaceproblemDesc =findViewById(R.id.replace_description);
        btnReqReplace =findViewById(R.id.replace_submit_btn);
        replaceImg =findViewById(R.id.replace_product_img);
        replaceLOutput =findViewById(R.id.replace_linear_output);
        replaceLInput =findViewById(R.id.replace_linear_input);
        replaceOutputProblem =findViewById(R.id.replace_output_problem);
        replaceOutputDesc =findViewById(R.id.replace_output_desc);

        orderId =getIntent().getStringExtra("OrderId");
        replaceProduct.setText(getIntent().getStringExtra("productName"));
        replacePrice.setText(getIntent().getStringExtra("productPrice"));
        replaceOrderId.setText(orderId);
        Glide.with(getApplicationContext()).load(imgUrl).into(replaceImg);

        btnReqReplace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                problem =replaceProblem.getText().toString();
                problemDesc =replaceproblemDesc.getText().toString();
                replaceData =replaceOutput.getText().toString();

                if (TextUtils.isEmpty(problem)) {
                    replaceProblem.setError("Enter your Problem!");
                    return;
                }
                if (TextUtils.isEmpty(problemDesc)) {
                    replaceproblemDesc.setError("Enter your Problem Description!");
                    return;
                }

                final HashMap<String, Object> cartMap = new HashMap<>();
                cartMap.put("rMethod","replace");
                cartMap.put("rProblem",problem);
                cartMap.put("rProblemDesc",problemDesc);
                cartMap.put("rMessage",replaceData);
                cartMap.put("flag",true);


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
                                                        replaceLInput.setVisibility(View.GONE);
                                                        replaceLOutput.setVisibility(View.VISIBLE);
                                                        replacementOutput();
                                                        Toast.makeText(ReplaceActivity.this, "Your request has been submitted", Toast.LENGTH_SHORT).show();
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

    private void replacementOutput() {

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
                                String rProblem = document.getString("rProblem");
                                String rProblemDesc = document.getString("rProblemDesc");
                                String rMessage = document.getString("rMessage");

                                // Do something with the retrieved data
                                // For example, display it in a TextView
                                replaceOutput.setText(rMessage);
                                replaceOutputProblem.setText(rProblem);
                                replaceOutputDesc.setText(rProblemDesc);

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
                                boolean flag = document.getBoolean("flag");
                                if(flag) {
                                    replaceLInput.setVisibility(View.GONE);
                                    replaceLOutput.setVisibility(View.VISIBLE);
                                    replacementOutput();
                                }


                            }
                        } else {

                        }
                    }
                });
    }
}