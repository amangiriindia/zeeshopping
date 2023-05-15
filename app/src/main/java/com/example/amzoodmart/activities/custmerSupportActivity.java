package com.example.amzoodmart.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.amzoodmart.R;
import com.example.amzoodmart.databinding.ActivityCustmerSupportBinding;

import java.util.Objects;

public class custmerSupportActivity extends AppCompatActivity {


 ActivityCustmerSupportBinding binding;
    Toolbar toolbar;
    Button sendEmailBtn;
    TextView problem,massage;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custmer_support);

        toolbar =findViewById(R.id.support_toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        binding = ActivityCustmerSupportBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sendEmailBtn = findViewById(R.id.send_email_btn);
        problem =findViewById(R.id.problemEditText);
        massage = findViewById(R.id.messageEditText);



        binding.sendEmailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = "amangiri381@gmail.com";
                String subject = problem.getText().toString();
                String message = massage.getText().toString();

                String[] address = email.split(",");
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:"));
                intent.putExtra(Intent.EXTRA_EMAIL, address);
                intent.putExtra(Intent.EXTRA_SUBJECT, subject);
                intent.putExtra(Intent.EXTRA_TEXT, message);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                } else {
                    Toast.makeText(custmerSupportActivity.this, "We have not any mail sender Apptication", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
}