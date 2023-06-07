package com.amzsoft.zeeshopping.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.amzsoft.zeeshopping.R;
import com.amzsoft.zeeshopping.Utility.NetworkChangeListener;

import java.util.Objects;

public class custmerSupportActivity extends AppCompatActivity {

    Button sendEmailBtn;
    TextView problem, massage;
    Toolbar toolbar;
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custmer_support);

        toolbar = findViewById(R.id.toolbar_customer);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        sendEmailBtn = findViewById(R.id.send_email_btn);
        problem = findViewById(R.id.problemEditText);
        massage = findViewById(R.id.messageEditText);
        sendEmailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = "zeeshopping2014@gmail.com";
                String subject = problem.getText().toString();
                String content = massage.getText().toString();
                if(TextUtils.isEmpty(subject)){
                    problem.setError("Enter your problem");
                } else if (TextUtils.isEmpty(content)) {
                    massage.setError("Enter your Problem in description");
                }

                sendEmail(subject, content, email);
            }
        });
    }

    private void sendEmail(String subject, String content, String email) {
        Intent intent = new Intent(Intent.ACTION_SEND);
       intent.setData(Uri.parse("mailto:" + email));
        //intent.putExtra(Intent.EXTRA_EMAIL,email);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, content);

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(Intent.createChooser(intent, "Choose email client: "));
        } else {
            Toast.makeText(custmerSupportActivity.this, "You have no mail sender application", Toast.LENGTH_SHORT).show();
        }
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
