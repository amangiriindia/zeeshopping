package com.example.amzoodmart.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.amzoodmart.R;
import com.example.amzoodmart.models.NewProductsModel;
import com.example.amzoodmart.models.PopularProductsModel;
import com.example.amzoodmart.models.ShowAllModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Objects;


public class DetailedActivity extends AppCompatActivity {

    ImageView detailedImg;
    TextView rating,name,description,price,quantity;
    Button addToCart,buyNow;
    ImageView addItems,removeItems;
    Toolbar toolbar;
    String ImgUrl="";
    int totalQuantity =1;
    int totalPrice =0;

    // New Product
    NewProductsModel newProductsModel =null;
    //Popular Products
    PopularProductsModel popularProductsModel = null;
    //show All
    ShowAllModel showAllModel =null;
    FirebaseAuth auth;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);

        toolbar =findViewById(R.id.detailed_toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        firestore =FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        final Object obj =getIntent().getSerializableExtra("detailed");
        if(obj instanceof NewProductsModel){
            newProductsModel =(NewProductsModel) obj;
        } else if (obj instanceof PopularProductsModel) {
            popularProductsModel =(PopularProductsModel) obj;
        } else if (obj instanceof ShowAllModel) {
            showAllModel =(ShowAllModel) obj;
        }

        detailedImg =findViewById(R.id.detailed_img);
        quantity =findViewById(R.id.quantity);
        name =findViewById(R.id.detailed_name);
        rating =findViewById(R.id.rating);
        description=findViewById(R.id.detailed_desc);
        price =findViewById(R.id.detailed_price);

        Intent intent = getIntent();
        String slider_imgUrl = intent.getStringExtra("img_url");
        String slider_name = intent.getStringExtra("name");
        double slider_price = intent.getDoubleExtra("price", 0.0); // Provide a default value if needed
        String slider_productStatus = intent.getStringExtra("product_status"); // Provide a default value if needed
        String slider_rating = intent.getStringExtra("rating"); // Provide a default value if needed
        String slider_description = intent.getStringExtra("description");

        Glide.with(this).load(slider_imgUrl).into(detailedImg); // Assuming you are using Glide for image loading
        name.setText(slider_name);
        price.setText("₹ "+String.valueOf(slider_price));
        rating.setText(slider_rating);
        description.setText(slider_description);



        addToCart =findViewById(R.id.add_to_cart);
        buyNow =findViewById(R.id.buy_now);

        addItems =findViewById(R.id.add_item);
        removeItems =findViewById(R.id.remove_item);

        //New Products
        if(newProductsModel != null){
            Glide.with(getApplicationContext()).load(newProductsModel.getImg_url()).into(detailedImg);
            name.setText(newProductsModel.getName());
            rating.setText(newProductsModel.getRating());
            description.setText(newProductsModel.getDescription());
            price.setText("₹ "+String.valueOf(newProductsModel.getPrice()));

           ImgUrl = newProductsModel.getImg_url();
            totalPrice =newProductsModel.getPrice() *totalQuantity;

        }
        //popular Products
        if(popularProductsModel != null){
            Glide.with(getApplicationContext()).load(popularProductsModel.getImg_url()).into(detailedImg);
            name.setText(popularProductsModel.getName());
            rating.setText(popularProductsModel.getRating());
            description.setText(popularProductsModel.getDescription());
            price.setText("₹ "+String.valueOf(popularProductsModel.getPrice()));
           ImgUrl = popularProductsModel.getImg_url();
            totalPrice =popularProductsModel.getPrice() *totalQuantity;
        }
        //Show All
        if(showAllModel != null){
            Glide.with(getApplicationContext()).load(showAllModel.getImg_url()).into(detailedImg);
            name.setText(showAllModel.getName());
            rating.setText(showAllModel.getRating());
            description.setText(showAllModel.getDescription());
            price.setText("₹ "+String.valueOf(showAllModel.getPrice()));
             ImgUrl =showAllModel.getImg_url();
            totalPrice =showAllModel.getPrice() *totalQuantity;



        }

        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addToCart();
            }
        });


        //Buy Now
        buyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(DetailedActivity.this,AddressActivity.class);
                if(newProductsModel !=null){
                    intent.putExtra("item",newProductsModel);
                    intent.putExtra("Qty",totalQuantity);
                }
                if(popularProductsModel !=null){
                    intent.putExtra("item",popularProductsModel);
                    intent.putExtra("Qty",totalQuantity);
                }
                if(showAllModel != null){
                    intent.putExtra("item",showAllModel);
                    intent.putExtra("Qty",totalQuantity);
                }
                startActivity(intent);
            }
        });
        //Add To Cart
        addItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(totalQuantity <10){
                    totalQuantity++;
                    quantity.setText(String.valueOf(totalQuantity));

                    if(newProductsModel != null){
                        totalPrice =newProductsModel.getPrice() *totalQuantity;
                    }
                    if(popularProductsModel != null){
                        totalPrice =popularProductsModel.getPrice() *totalQuantity;
                    }
                    if(showAllModel != null){
                        totalPrice =showAllModel.getPrice() *totalQuantity;
                    }
                }

            }
        });
        removeItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(totalQuantity > 1){
                    totalQuantity--;

                    quantity.setText(String.valueOf(totalQuantity));

                    if(newProductsModel != null){
                        totalPrice =newProductsModel.getPrice() *totalQuantity;
                    }
                    if(popularProductsModel != null){
                        totalPrice =popularProductsModel.getPrice() *totalQuantity;
                    }
                    if(showAllModel != null){
                        totalPrice =showAllModel.getPrice() *totalQuantity;
                    }
                }

            }
        });

    }

    private void addToCart() {
        String saveCurrentTime,saveCurrentDate;
        Calendar calForDate =Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MM dd, yyyy");
        saveCurrentDate =currentDate.format(calForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss");
        saveCurrentTime =currentTime.format(calForDate.getTime());

        final HashMap<String,Object> cartMap = new HashMap<>();
        cartMap.put("productName" ,name.getText().toString());
        cartMap.put("productPrice",price.getText());
        cartMap.put("currentTime" ,saveCurrentTime);
        cartMap.put("currentDate",saveCurrentDate);
        cartMap.put("totalQuantity" ,totalQuantity);
        cartMap.put("totalPrice",totalPrice);
        cartMap.put("productImgurl",ImgUrl);

        firestore.collection("AddToCart").document(auth.getCurrentUser().getUid())
                .collection("User").add(cartMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        Toast.makeText(DetailedActivity.this, "Added To A Cart", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });

    }
}