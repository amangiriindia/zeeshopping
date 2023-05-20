package com.example.amzoodmart.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.amzoodmart.R;

import java.util.Objects;

public class ReturnActivity extends AppCompatActivity {

    Toolbar toolbar;
    TextView returnOrderId,returnProduct,returnPrice,returnOutput;
    EditText returnProblem,returnproblemDesc,returnPhoneNumber,returnAccountNumber,returnIFSCcode,returnAccName;
    Button btnReqReturn;
    ImageView returnImg;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_return);


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

        toolbar = findViewById(R.id.return_toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}