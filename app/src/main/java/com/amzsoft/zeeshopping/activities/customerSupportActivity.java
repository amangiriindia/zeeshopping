package com.amzsoft.zeeshopping.activities;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.amzsoft.zeeshopping.Utility.NetworkChangeListener;
import com.amzsoft.zeeshopping.databinding.ActivityCustmerSupportBinding;

public class customerSupportActivity extends AppCompatActivity {

    ActivityCustmerSupportBinding binding;
    Button sendEmailBtn;
    EditText problemEditText, messageEditText;
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityCustmerSupportBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbarCustomer);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        binding.toolbarCustomer.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        sendEmailBtn = binding.sendEmailBtn;
        problemEditText = binding.problemEditText;
        messageEditText = binding.messageEditText;

        sendEmailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = "zeeshopping2014@gmail.com";
                String subject = problemEditText.getText().toString();
                String message = messageEditText.getText().toString();

                // Check if there is a mail sender app installed
                if (isIntentAvailable(Intent.ACTION_SENDTO)) {
                    // Create an intent to send an email
                    Intent intent = new Intent(Intent.ACTION_SENDTO);
                    intent.setData(Uri.parse("mailto:" + email));
                    intent.putExtra(Intent.EXTRA_SUBJECT, subject);
                    intent.putExtra(Intent.EXTRA_TEXT, message);

                    // Start the activity to send the email
                    startActivity(intent);
                } else {
                    // No mail sender app installed, show a toast
                    Toast.makeText(customerSupportActivity.this, "We do not have any mail sender application", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Register the network change listener
        registerReceiver(networkChangeListener, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    protected void onStop() {
        // Unregister the network change listener
        unregisterReceiver(networkChangeListener);
        super.onStop();
    }

    // Check if there is an app installed that can handle the given intent
    private boolean isIntentAvailable(String action) {
        Intent intent = new Intent(action);
        return getPackageManager().queryIntentActivities(intent, 0).size() > 0;
    }
}
