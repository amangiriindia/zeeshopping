package com.amzsoft.zeeshopping.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Paint;
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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.amzsoft.zeeshopping.R;
import com.amzsoft.zeeshopping.Utility.NetworkChangeListener;
import com.amzsoft.zeeshopping.adapters.ShowAllAdapter;
import com.amzsoft.zeeshopping.models.DiscountModel;
import com.amzsoft.zeeshopping.models.NewProductsModel;
import com.amzsoft.zeeshopping.models.PopularProductsModel;
import com.amzsoft.zeeshopping.models.ShowAllModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
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
    TextView rating, name, description, price, quantity,out_of_stock;
    Button addToCart, buyNow;
    ImageView addItems, removeItems;
    Toolbar toolbar;
    String ImgUrl = "";
    int totalQuantity = 1;
    int totalPrice = 0;
    int offerPercent =0,afterofferPrice =0,productPrice=0;
    int delevaryCharge =0;
    String replacment ="";
    String returnPolicy ="",type="";

    // New Product
    NewProductsModel newProductsModel = null;
    //Popular Products
    PopularProductsModel popularProductsModel = null;
    //show All
    ShowAllModel showAllModel = null;
    DiscountModel discountModel =null;
    FirebaseAuth auth;
    private FirebaseFirestore firestore;
    static float ratingValue = 0;
    ImageSlider imageSlider;
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();
    TextView returnHeadTextView ;
    TextView returnDataTextView ;
    TextView replaceHeadTextView ;
    TextView replaceDataTextView ;
    TextView deliveryTimeTextView,productOffer,offerPrice;
    TextView delevaryFree ;
    RecyclerView similarProductRecyclarview;
    ShowAllAdapter showAllAdapter;
    List<ShowAllModel> showAllModelList;
    boolean outOfStock =false;





    @SuppressLint("MissingInflatedId")
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
        productOffer = findViewById(R.id.product_offer);
        offerPrice =findViewById(R.id.detailed_off_price);
        similarProductRecyclarview =findViewById(R.id.similar_product_rec);
        out_of_stock =findViewById(R.id.out_of_stock);






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
        }else if(obj instanceof DiscountModel){
            discountModel =(DiscountModel) obj;
        }


        addToCart = findViewById(R.id.add_to_cart);
        buyNow = findViewById(R.id.buy_now);

        addItems = findViewById(R.id.add_item);
        removeItems = findViewById(R.id.remove_item);


        //New Products
        if (newProductsModel != null) {

            type =newProductsModel.getType();
            name.setText(newProductsModel.getName());
            ratingTextView.setText(String.valueOf(newProductsModel.getRating()));
            description.setText(newProductsModel.getDescription());
            ImgUrl = newProductsModel.getImg_url();
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

            productPrice =newProductsModel.getPrice();
            offerPercent = newProductsModel.getOffer();
             afterofferPrice =(productPrice * (100 - offerPercent)) / 100;
            newProductsModel.setPrice(afterofferPrice);
            if(offerPercent>0){
                productOffer.setVisibility(View.VISIBLE);
                price.setVisibility(View.VISIBLE);
                price.setPaintFlags(price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                productOffer.setText(offerPercent+"% off");
                price.setText(""+productPrice);
                offerPrice.setText("₹"+afterofferPrice);
            }else {
                offerPrice.setText("₹ " +afterofferPrice);
            }
            outOfStock = newProductsModel.isOutOfStock();
            if(outOfStock){
                out_of_stock.setVisibility(View.VISIBLE);
            }



        }
        //popular Products
        if (popularProductsModel != null) {
            type =popularProductsModel.getType();
            name.setText(popularProductsModel.getName());
            ratingTextView.setText(String.valueOf(popularProductsModel.getRating()));
            description.setText(popularProductsModel.getDescription());
            ImgUrl = popularProductsModel.getImg_url();
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
            //PRICE
            productPrice =popularProductsModel.getPrice();
            offerPercent = popularProductsModel.getOffer();
            afterofferPrice =(productPrice * (100 - offerPercent)) / 100;
            popularProductsModel.setPrice(afterofferPrice);
            if(offerPercent>0){
                productOffer.setVisibility(View.VISIBLE);
                price.setVisibility(View.VISIBLE);
                price.setPaintFlags(price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                productOffer.setText(offerPercent+"% off");
                price.setText(""+productPrice);
                offerPrice.setText("₹"+afterofferPrice);
            }else {
                offerPrice.setText("₹ " +afterofferPrice);
            }
            outOfStock = popularProductsModel.isOutOfStock();
            if(outOfStock){
                out_of_stock.setVisibility(View.VISIBLE);
            }


        }
        //Show All
        if (showAllModel != null) {

            type =   showAllModel.getType();
            name.setText(showAllModel.getName());
            ratingTextView.setText(String.valueOf(showAllModel.getRating()));
            description.setText(showAllModel.getDescription());
            ImgUrl = showAllModel.getImg_url();
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
            productPrice =showAllModel.getPrice();
            offerPercent =showAllModel.getOffer();
            afterofferPrice =(productPrice * (100 - offerPercent)) / 100;
            showAllModel.setPrice(afterofferPrice);
            if(offerPercent>0){
                productOffer.setVisibility(View.VISIBLE);
                price.setVisibility(View.VISIBLE);
                price.setPaintFlags(price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                productOffer.setText(offerPercent+"% off");
                price.setText(""+productPrice);
                offerPrice.setText("₹"+afterofferPrice);
            }else {
                offerPrice.setText("₹ " +afterofferPrice);
            }
            outOfStock = showAllModel.isOutOfStock();
            if(outOfStock){
                out_of_stock.setVisibility(View.VISIBLE);
            }

        }
        //discount
        if (discountModel != null) {

            type =   discountModel.getType();
            name.setText(discountModel.getName());
            ratingTextView.setText(String.valueOf(discountModel.getRating()));
            description.setText(discountModel.getDescription());
            ImgUrl = discountModel.getImg_url();
            ratingValue = (float) +discountModel.getRating();
            setRating(ratingValue);

            deliveryTimeTextView.setText(discountModel.getDelivery_time());
            delevaryCharge = discountModel.getDelivery();
            if(delevaryCharge>0){
                delevaryFree.setText("Delivery charge : ₹"+delevaryCharge);
            }

            returnPolicy = discountModel.getReturn1();
            if(returnPolicy.equals("yes")){
                returnHeadTextView.setVisibility(View.VISIBLE);
                returnDataTextView.setVisibility(View.VISIBLE);
            }

            replacment =discountModel.getReplace();
            if(replacment.equalsIgnoreCase("yes")){
                replaceHeadTextView.setVisibility(View.VISIBLE);
                replaceDataTextView.setVisibility(View.VISIBLE);
            }
            productPrice =discountModel.getPrice();
            offerPercent =discountModel.getOffer();
            afterofferPrice =(productPrice * (100 - offerPercent)) / 100;
            discountModel.setPrice(afterofferPrice);
            if(offerPercent>0){
                productOffer.setVisibility(View.VISIBLE);
                price.setVisibility(View.VISIBLE);
                price.setPaintFlags(price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                productOffer.setText(offerPercent+"% off");
                price.setText(""+productPrice);
                offerPrice.setText("₹"+afterofferPrice);
            }else {
                offerPrice.setText("₹ " +afterofferPrice);
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
                if(outOfStock){
                    Toast.makeText(DetailedActivity.this, "Product is out of stock", Toast.LENGTH_SHORT).show();
                    return;
                }
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
                if (discountModel != null) {
                    intent.putExtra("item", discountModel);
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
                }

            }
        });
        removeItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (totalQuantity > 1) {
                    totalQuantity--;

                    quantity.setText(String.valueOf(totalQuantity));

                }

            }
        });

        //  for img slider

        List<SlideModel> slideModels = new ArrayList<>();
        if (ImgUrl != "") {
            slideModels.add(new SlideModel(ImgUrl, ScaleTypes.FIT));
        }
//        slideModels.add(new SlideModel("https://cdn.pixabay.com/photo/2015/04/23/22/00/tree-736885_640.jpg", ScaleTypes.FIT));
//        slideModels.add(new SlideModel("https://img.freepik.com/free-photo/space-background-realistic-starry-night-cosmos-shining-stars-milky-way-stardust-color-galaxy_1258-154643.jpg", ScaleTypes.FIT));
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
                            Toast.makeText(DetailedActivity.this, "Restart or Please wait ...", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(DetailedActivity.this, "Restart or Please wait ...", Toast.LENGTH_SHORT).show();
                    }
                });


        //similar Product
        similarProductRecyclarview.setLayoutManager(new GridLayoutManager(this, 2));
        showAllModelList =new ArrayList<>();
        showAllAdapter =new ShowAllAdapter(this,showAllModelList);
        similarProductRecyclarview.setAdapter(showAllAdapter);


        firestore.collection("ShowAll")
                .whereEqualTo("type", type.toLowerCase())  // Add your where condition here
                .limit(6)  // Set the maximum limit to 6
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        int count = 0;
                        for (QueryDocumentSnapshot document : querySnapshot) {
                            if (count >= 6) {
                                break;  // Exit the loop if 6 items have been added
                            }
                            ShowAllModel showAllModel = document.toObject(ShowAllModel.class);
                            showAllModelList.add(showAllModel);
                            showAllAdapter.notifyDataSetChanged();
                            count++;
                        }
                    } else {
                        // Handle the task failure
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(DetailedActivity.this, "Restart or Please wait ...", Toast.LENGTH_SHORT).show();
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

            filledStars.setColorFilter(ContextCompat.getColor(this, R.color.green), PorterDuff.Mode.SRC_ATOP);
            halfFilledStars.setColorFilter(ContextCompat.getColor(this, R.color.green), PorterDuff.Mode.SRC_ATOP);
            emptyStars.setColorFilter(ContextCompat.getColor(this, R.color.green), PorterDuff.Mode.SRC_ATOP);
        }

// Set the progress drawable back to the RatingBar
        ratingBar.setProgressDrawable(progressDrawable);
    }


    private void addToCart() {
        if(outOfStock){
            Toast.makeText(DetailedActivity.this, "Product is out of stock", Toast.LENGTH_SHORT).show();
            return;
        }
        String saveCurrentTime, saveCurrentDate;
        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MM dd, yyyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss");
        saveCurrentTime = currentTime.format(calForDate.getTime());

        final HashMap<String, Object> cartMap = new HashMap<>();
        cartMap.put("productName", name.getText().toString());
        cartMap.put("productPrice",afterofferPrice+"");
        cartMap.put("currentTime", saveCurrentTime);
        cartMap.put("currentDate", saveCurrentDate);
        cartMap.put("totalQuantity", totalQuantity);
        cartMap.put("totalPrice", (afterofferPrice*totalQuantity));
        cartMap.put("productImgurl", ImgUrl);

        firestore.collection("AddToCart").document(auth.getCurrentUser().getUid())
                .collection("User").add(cartMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        Toast.makeText(DetailedActivity.this, "Added To A Cart", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(DetailedActivity.this, "Restart or Please wait ...", Toast.LENGTH_SHORT).show();
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