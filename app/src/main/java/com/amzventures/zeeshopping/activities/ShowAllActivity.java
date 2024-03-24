package com.amzventures.zeeshopping.activities;

import android.annotation.SuppressLint;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amzventures.zeeshopping.R;
import com.amzventures.zeeshopping.Utility.NetworkChangeListener;
import com.amzventures.zeeshopping.adapters.ShowAllAdapter;
import com.amzventures.zeeshopping.models.ShowAllModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ShowAllActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ShowAllAdapter showAllAdapter;
    List<ShowAllModel> showAllModelList;
    Toolbar toolbar;
    FirebaseFirestore firestore;
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_all);


        String type = getIntent().getStringExtra("type");
        String title = getIntent().getStringExtra("title");
        firestore = FirebaseFirestore.getInstance();
        recyclerView = findViewById(R.id.show_all_rec);


        toolbar = findViewById(R.id.show_all_toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        showAllModelList = new ArrayList<>();
        showAllAdapter = new ShowAllAdapter(this, showAllModelList);
        recyclerView.setAdapter(showAllAdapter);

        if (type == null || type.isEmpty()) {
            retrieveAllData();
            toolbar.setTitle(title);

        } else {
            retrieveDataByType(type);
            toolbar.setTitle(title);
        }


    }


    // Retrieve all data from Firestore
    private void retrieveAllData() {
        firestore.collection("ShowAll")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            showAllModelList.clear();
                            for (DocumentSnapshot doc : task.getResult().getDocuments()) {
                                ShowAllModel showAllModel = doc.toObject(ShowAllModel.class);
                                showAllModelList.add(showAllModel);
                            }
                            showAllAdapter.notifyDataSetChanged();
                        } else {
                            // Handle errors
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ShowAllActivity.this, "Restart or Please wait ...", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // Retrieve data from Firestore based on type
    private void retrieveDataByType(String type) {
        firestore.collection("ShowAll")
                .whereEqualTo("type", type.toLowerCase())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            showAllModelList.clear();
                            for (DocumentSnapshot doc : task.getResult().getDocuments()) {
                                ShowAllModel showAllModel = doc.toObject(ShowAllModel.class);
                                showAllModelList.add(showAllModel);
                            }
                            showAllAdapter.notifyDataSetChanged();
                        } else {
                            // Handle errors
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ShowAllActivity.this, "Restart or Please wait ...", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search, menu);
        MenuItem item = menu.findItem(R.id.all_search_menu);
        changeMenuItemIconColor(item, Color.WHITE);
        SearchView searchView = (SearchView) item.getActionView();
        // Get the search icon and set its tint mode to SRC_IN
        ImageView searchIcon = searchView.findViewById(androidx.appcompat.R.id.search_button);
        Drawable iconDrawable = searchIcon.getDrawable();
        if (iconDrawable != null) {
            iconDrawable.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
        }
        // Set the text color of the search view
        int searchEditTextId = androidx.appcompat.R.id.search_src_text;
        EditText searchEditText = searchView.findViewById(searchEditTextId);
        if (searchEditText != null) {
            searchEditText.setTextColor(Color.WHITE);
            searchEditText.setHintTextColor(Color.WHITE);
        }
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mySearch(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mySearch(newText);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    private void changeMenuItemIconColor(MenuItem menuItem, int colorResId) {
        Drawable icon = menuItem.getIcon();
        if (icon != null) {
            icon.mutate();
            icon.setColorFilter(colorResId, PorterDuff.Mode.SRC_ATOP);
            menuItem.setIcon(icon);
        }
    }


    private void mySearch(String newText) {
        firestore.collection("ShowAll")
                .whereEqualTo("type", newText.toLowerCase())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            showAllModelList.clear(); // Clear the existing list
                            for (DocumentSnapshot doc : task.getResult().getDocuments()) {
                                ShowAllModel showAllModel = doc.toObject(ShowAllModel.class);
                                showAllModelList.add(showAllModel); // Add the search results to the list
                            }
                            showAllAdapter.notifyDataSetChanged(); // Notify the adapter about the changes
                        } else {
                            // Handle any errors that occurred during the query
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ShowAllActivity.this, "Restart or Please wait ...", Toast.LENGTH_SHORT).show();
                    }
                });
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