package com.example.amzoodmart.activities;

import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.amzoodmart.R;
import com.example.amzoodmart.Utility.NetworkChangeListener;
import com.example.amzoodmart.models.NewProductsModel;
import com.example.amzoodmart.models.PopularProductsModel;
import com.example.amzoodmart.models.ShowAllModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;


public class DetailedActivity extends AppCompatActivity {

    ImageView detailedImg;
    TextView rating, name, description, price, quantity;
    Button addToCart, buyNow;
    ImageView addItems, removeItems;
    Toolbar toolbar;
    String ImgUrl = "";
    int totalQuantity = 1;
    int totalPrice = 0;
    int delevaryCharge =0;
    String replacment ="";
    String returnPolicy ="";

    // New Product
    NewProductsModel newProductsModel = null;
    //Popular Products
    PopularProductsModel popularProductsModel = null;
    //show All
    ShowAllModel showAllModel = null;
    FirebaseAuth auth;
    private FirebaseFirestore firestore;
    static float ratingValue = 0;
    ImageSlider imageSlider;
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();
    TextView returnHeadTextView ;
    TextView returnDataTextView ;
    TextView replaceHeadTextView ;
    TextView replaceDataTextView ;
    TextView deliveryTimeTextView;
    TextView delevaryFree ;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);


        TextView ratingTextView = findViewById(R.id.rating);
        imageSlider = findViewById(R.id.detailed_img_slider);
        quantity = findViewById(R.id.quantity);
        name = findViewById(R.id.detailed_name);
        description = findViewById(R.id.detailed_desc);
        price = findViewById(R.id.detailed_price);
        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        returnHeadTextView = findViewById(R.id.return_head);
        returnDataTextView = findViewById(R.id.return_data);
        replaceHeadTextView = findViewById(R.id.replace_head);
        replaceDataTextView = findViewById(R.id.replace_data);
         delevaryFree =findViewById(R.id.free_delivery);
        deliveryTimeTextView = findViewById(R.id.delivery_time);




        toolbar = findViewById(R.id.detailed_toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        final Object obj = getIntent().getSerializableExtra("detailed");
        if (obj instanceof NewProductsModel) {
            newProductsModel = (NewProductsModel) obj;
        } else if (obj instanceof PopularProductsModel) {
            popularProductsModel = (PopularProductsModel) obj;
        } else if (obj instanceof ShowAllModel) {
            showAllModel = (ShowAllModel) obj;
        }

        Intent intent = getIntent();
        String slider_imgUrl = intent.getStringExtra("img_url");
        String slider_name = intent.getStringExtra("name");
        double slider_price = intent.getDoubleExtra("price", 0.0); // Provide a default value if needed
        String slider_productStatus = intent.getStringExtra("product_status"); // Provide a default value if needed
        double slider_rating = intent.getDoubleExtra("rating", 0.0); // Provide a default value if needed
        String slider_description = intent.getStringExtra("description");


        name.setText(slider_name);
        price.setText("₹ " + String.valueOf(slider_price));
        ratingTextView.setText(String.valueOf(slider_rating));
        description.setText(slider_description);
        ratingValue = (float) slider_rating;
        setRating(ratingValue);



        addToCart = findViewById(R.id.add_to_cart);
        buyNow = findViewById(R.id.buy_now);

        addItems = findViewById(R.id.add_item);
        removeItems = findViewById(R.id.remove_item);


        //New Products
        if (newProductsModel != null) {

            name.setText(newProductsModel.getName());
            ratingTextView.setText(String.valueOf(newProductsModel.getRating()));
            description.setText(newProductsModel.getDescription());
            price.setText("₹ " + String.valueOf(newProductsModel.getPrice()));

            ImgUrl = newProductsModel.getImg_url();
            totalPrice = newProductsModel.getPrice() * totalQuantity;
            ratingValue = (float) newProductsModel.getRating();
            setRating(ratingValue);
            deliveryTimeTextView.setText(newProductsModel.getDelivery_time());
            delevaryCharge = newProductsModel.getDelivery();
            if(delevaryCharge>0){
              delevaryFree.setText("Delivery charge : ₹"+delevaryCharge);
            }

            returnPolicy = newProductsModel.getReturn1();
            if(returnPolicy.equals("yes")){
                returnHeadTextView.setVisibility(View.VISIBLE);
                returnDataTextView.setVisibility(View.VISIBLE);
            }

            replacment =newProductsModel.getReplace();
            if(replacment.equalsIgnoreCase("yes")){
                replaceHeadTextView.setVisibility(View.VISIBLE);
                replaceDataTextView.setVisibility(View.VISIBLE);
            }


        }
        //popular Products
        if (popularProductsModel != null) {
            name.setText(popularProductsModel.getName());
            ratingTextView.setText(String.valueOf(popularProductsModel.getRating()));
            description.setText(popularProductsModel.getDescription());
            price.setText("₹ " + String.valueOf(popularProductsModel.getPrice()));
            ImgUrl = popularProductsModel.getImg_url();
            totalPrice = popularProductsModel.getPrice() * totalQuantity;
            ratingValue = (float) popularProductsModel.getRating();
            setRating(ratingValue);

            deliveryTimeTextView.setText(popularProductsModel.getDelivery_time());
            delevaryCharge = popularProductsModel.getDelivery();
            if(delevaryCharge>0){
                delevaryFree.setText("Delivery charge : ₹"+delevaryCharge);
            }

            returnPolicy = popularProductsModel.getReturn1();
            if(returnPolicy.equals("yes")){
                returnHeadTextView.setVisibility(View.VISIBLE);
                returnDataTextView.setVisibility(View.VISIBLE);
            }

            replacment =popularProductsModel.getReplace();
            if(replacment.equalsIgnoreCase("yes")){
                replaceHeadTextView.setVisibility(View.VISIBLE);
                replaceDataTextView.setVisibility(View.VISIBLE);
            }


        }
        //Show All
        if (showAllModel != null) {

            name.setText(showAllModel.getName());
            ratingTextView.setText(String.valueOf(showAllModel.getRating()));
            description.setText(showAllModel.getDescription());
            price.setText("₹ " + String.valueOf(showAllModel.getPrice()));
            ImgUrl = showAllModel.getImg_url();
            totalPrice = showAllModel.getPrice() * totalQuantity;
            ratingValue = (float) +showAllModel.getRating();
            setRating(ratingValue);

            deliveryTimeTextView.setText(showAllModel.getDelivery_time());
            delevaryCharge = showAllModel.getDelivery();
            if(delevaryCharge>0){
                delevaryFree.setText("Delivery charge : ₹"+delevaryCharge);
            }

            returnPolicy = showAllModel.getReturn1();
            if(returnPolicy.equals("yes")){
                returnHeadTextView.setVisibility(View.VISIBLE);
                returnDataTextView.setVisibility(View.VISIBLE);
            }

            replacment =showAllModel.getReplace();
            if(replacment.equalsIgnoreCase("yes")){
                replaceHeadTextView.setVisibility(View.VISIBLE);
                replaceDataTextView.setVisibility(View.VISIBLE);
            }

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
                Intent intent = new Intent(DetailedActivity.this, AddressActivity.class);
                if (newProductsModel != null) {
                    intent.putExtra("item", newProductsModel);
                    intent.putExtra("Qty", totalQuantity);
                }
                if (popularProductsModel != null) {
                    intent.putExtra("item", popularProductsModel);
                    intent.putExtra("Qty", totalQuantity);
                }
                if (showAllModel != null) {
                    intent.putExtra("item", showAllModel);
                    intent.putExtra("Qty", totalQuantity);
                }
                startActivity(intent);
            }
        });
        //Add To Cart
        addItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (totalQuantity < 10) {
                    totalQuantity++;
                    quantity.setText(String.valueOf(totalQuantity));

                    if (newProductsModel != null) {
                        totalPrice = newProductsModel.getPrice() * totalQuantity;
                    }
                    if (popularProductsModel != null) {
                        totalPrice = popularProductsModel.getPrice() * totalQuantity;
                    }
                    if (showAllModel != null) {
                        totalPrice = showAllModel.getPrice() * totalQuantity;
                    }
                }

            }
        });
        removeItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (totalQuantity > 1) {
                    totalQuantity--;

                    quantity.setText(String.valueOf(totalQuantity));

                    if (newProductsModel != null) {
                        totalPrice = newProductsModel.getPrice() * totalQuantity;
                    }
                    if (popularProductsModel != null) {
                        totalPrice = popularProductsModel.getPrice() * totalQuantity;
                    }
                    if (showAllModel != null) {
                        totalPrice = showAllModel.getPrice() * totalQuantity;
                    }
                }

            }
        });

        //  for img slider

        List<SlideModel> slideModels = new ArrayList<>();
        if (ImgUrl != "") {
            slideModels.add(new SlideModel(ImgUrl, ScaleTypes.FIT));
        }
        slideModels.add(new SlideModel("https://cdn.pixabay.com/photo/2015/04/23/22/00/tree-736885_640.jpg", ScaleTypes.FIT));
        slideModels.add(new SlideModel("https://img.freepik.com/free-photo/space-background-realistic-starry-night-cosmos-shining-stars-milky-way-stardust-color-galaxy_1258-154643.jpg", ScaleTypes.FIT));
        final String[] documentId = {""};

        CollectionReference collectionRef = firestore.collection("ShowAll");

        collectionRef.whereEqualTo("img_url", ImgUrl)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                documentId[0] = document.getId();

                                // Execute the second query to retrieve data from the "slider" collection
                                firestore.collection("ShowAll")
                                        .document(documentId[0])
                                        .collection("slider")
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                                                        String imgUrl = queryDocumentSnapshot.getString("img_url");
                                                        slideModels.add(new SlideModel(imgUrl, ScaleTypes.FIT));
                                                    }
                                                    imageSlider.setImageList(slideModels);
                                                } else {
                                                    // Handle exceptions or errors in retrieving the documents from "slider" collection
                                                }
                                            }
                                        });
                            }
                        } else {
                            // Handle exceptions or errors in retrieving the document
                        }
                    }
                });

    }

    public void setRating(float ratingValue) {
        RatingBar ratingBar = findViewById(R.id.my_rating);
        int maxStars = 5; // Set the maximum number of stars
        ratingBar.setMax(maxStars);

// Set the rating value for the RatingBar
        float stepSize = 1.0f; // Set the step size
        float rating = ratingValue / stepSize;
        ratingBar.setRating(rating);

// Customize the progress drawable
        Drawable progressDrawable = ratingBar.getProgressDrawable();
        if (progressDrawable instanceof LayerDrawable) {
            LayerDrawable stars = (LayerDrawable) progressDrawable;
            Drawable filledStars = stars.getDrawable(2);
            Drawable halfFilledStars = stars.getDrawable(1);
            Drawable emptyStars = stars.getDrawable(0);

            filledStars.setColorFilter(ContextCompat.getColor(this, R.color.pink), PorterDuff.Mode.SRC_ATOP);
            halfFilledStars.setColorFilter(ContextCompat.getColor(this, R.color.pink), PorterDuff.Mode.SRC_ATOP);
            emptyStars.setColorFilter(ContextCompat.getColor(this, R.color.pink), PorterDuff.Mode.SRC_ATOP);
        }

// Set the progress drawable back to the RatingBar
        ratingBar.setProgressDrawable(progressDrawable);
    }


    private void addToCart() {
        String saveCurrentTime, saveCurrentDate;
        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MM dd, yyyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss");
        saveCurrentTime = currentTime.format(calForDate.getTime());

        final HashMap<String, Object> cartMap = new HashMap<>();
        cartMap.put("productName", name.getText().toString());
        cartMap.put("productPrice", price.getText());
        cartMap.put("currentTime", saveCurrentTime);
        cartMap.put("currentDate", saveCurrentDate);
        cartMap.put("totalQuantity", totalQuantity);
        cartMap.put("totalPrice", totalPrice);
        cartMap.put("productImgurl", ImgUrl);

        firestore.collection("AddToCart").document(auth.getCurrentUser().getUid())
                .collection("User").add(cartMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        Toast.makeText(DetailedActivity.this, "Added To A Cart", Toast.LENGTH_SHORT).show();
                        finish();
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