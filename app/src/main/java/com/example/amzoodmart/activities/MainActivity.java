package com.example.amzoodmart.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.app.AlertDialog;
import android.content.DialogInterface;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.amzoodmart.R;
import com.example.amzoodmart.adapters.ShowAllAdapter;
import com.example.amzoodmart.fragments.HomeFragment;
import com.example.amzoodmart.models.ShowAllModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;


public class MainActivity extends AppCompatActivity {

    Fragment homeFragment;
    Toolbar toolbar;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    FirebaseAuth auth;
    ShowAllModel model;
    ShowAllAdapter adapter;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        auth =FirebaseAuth.getInstance();
        toolbar =findViewById(R.id.nav_toolbar);
        drawerLayout =findViewById(R.id.drawerLayout);
        navigationView=findViewById(R.id.navigationView);

        setSupportActionBar(toolbar);


        ActionBarDrawerToggle toggle =new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.openDrawer,R.string.closeDrawer);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int id =item.getItemId();
                if(id ==R.id.nav_home_items){
                    loadFragment(homeFragment);
                } else if (id ==R.id.nav_category_items) {
                    startActivity(new Intent(MainActivity.this, ShowAllActivity.class));
                }else if (id ==R.id.nav_cart_items) {
                    startActivity(new Intent(MainActivity.this,CartActivity.class));


                }else if (id ==R.id.nav_order_items) {
                    startActivity(new Intent(MainActivity.this,MyOrderActivity.class));

                }else if (id ==R.id.nav_about_items) {
                    startActivity(new Intent(MainActivity.this,AboutUsActivity.class));

                }else if (id ==R.id.nav_logout_items) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(MainActivity.this, R.style.AlertDialogCustom));
                    builder.setTitle("Logout");
                    builder.setMessage("Are you sure you want to logout?");

                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            auth.signOut();
                            startActivity(new Intent(MainActivity.this,RegistationActivity.class));
                            finish();
                            // Perform delete operation or other actions here
                        }
                    });

                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // User canceled the delete operation
                            Toast.makeText(MainActivity.this, "Logout  cancel successfully", Toast.LENGTH_SHORT).show();
                        }
                    });

                    AlertDialog dialog = builder.create();
                    dialog.show();

                }
                drawerLayout.closeDrawer(GravityCompat.START);

                return true;
            }
        });




       homeFragment =new HomeFragment();
        loadFragment(homeFragment);

    }

    @Override
    public void onBackPressed() {
       if( drawerLayout.isDrawerOpen(GravityCompat.START)){
           drawerLayout.closeDrawer(GravityCompat.START);
       }else {
           super.onBackPressed();
       }
    }

    private void loadFragment(Fragment homeFragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main_container,homeFragment);
        transaction.commit();
   }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
      getMenuInflater().inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }



    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id =item.getItemId();
        if(id == R.id.menu_search){
            startActivity(new Intent(MainActivity.this,ShowAllActivity.class));
        }
        else if (id == R.id.menu_my_cart) {
            startActivity(new Intent(MainActivity.this,CartActivity.class));

        }
        return  true;
    }
}