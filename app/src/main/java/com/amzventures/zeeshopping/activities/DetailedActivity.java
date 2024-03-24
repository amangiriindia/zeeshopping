package com.amzventures.zeeshopping.activities;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
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

import com.amzventures.zeeshopping.R;
import com.amzventures.zeeshopping.Utility.NetworkChangeListener;
import com.amzventures.zeeshopping.adapters.ShowAllAdapter;
import com.amzventures.zeeshopping.models.ShowAllModel;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
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


    TextView  name, description, price, quantity,out_of_stock;
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
    TextView colorBoxLabel,colorBox0,colorBox1,colorBox2,colorBox3,colorBox4,colorBox5,colorBox6,colorBox7,colorBox8,colorBox9;
    TextView sizeBoxLabel,sizeBox0,sizeBox1,sizeBox2,sizeBox3,sizeBox4,sizeBox5,sizeBox6,sizeBox7,sizeBox8,sizeBox9;
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
    private View[] colorBoxes = new View[10];
    private int lastClickedColorBoxIndex = -1;
    boolean colorFlag ,sizeFlag ;
    String  colorBoxArray[] = new String[10] ;
    String  sizeBoxArray[] = new String[10];
    FirebaseFirestore db;

    public static String colorTextOutput = "N/A",productId="";
    public static String sizeTextOutput = "N/A";
    private String productname="";
    private String product_desc="";
    private String delevary_time="";


    @SuppressLint({"MissingInflatedId", "WrongViewCast"})
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
        colorBoxLabel =findViewById(R.id.color_box_label);
        colorBox0 =findViewById(R.id.color_box0);
        colorBox1 =findViewById(R.id.color_box1);
        colorBox2 =findViewById(R.id.color_box2);
        colorBox3 =findViewById(R.id.color_box3);
        colorBox4 =findViewById(R.id.color_box4);
        colorBox5 =findViewById(R.id.color_box5);
        colorBox6 =findViewById(R.id.color_box6);
        colorBox7 =findViewById(R.id.color_box7);
        colorBox8 =findViewById(R.id.color_box8);
        colorBox9 =findViewById(R.id.color_box9);
        sizeBoxLabel =findViewById(R.id.size_box_label);
        sizeBox0 = findViewById(R.id.size_box0);
        sizeBox1 = findViewById(R.id.size_box1);
        sizeBox2 = findViewById(R.id.size_box2);
        sizeBox3 = findViewById(R.id.size_box3);
        sizeBox4 = findViewById(R.id.size_box4);
        sizeBox5 = findViewById(R.id.size_box5);
        sizeBox6 = findViewById(R.id.size_box6);
        sizeBox7 = findViewById(R.id.size_box7);
        sizeBox8 = findViewById(R.id.size_box8);
        sizeBox9 = findViewById(R.id.size_box9);
        TextView[] sizeBoxes = new TextView[]{sizeBox0, sizeBox1, sizeBox2, sizeBox3, sizeBox4, sizeBox5, sizeBox6, sizeBox7, sizeBox8, sizeBox9};
        TextView[] colorBoxes = new TextView[]{colorBox0, colorBox1, colorBox2, colorBox3, colorBox4, colorBox5, colorBox6, colorBox7, colorBox8, colorBox9};
        addToCart = findViewById(R.id.add_to_cart);
        buyNow = findViewById(R.id.buy_now);
        addItems = findViewById(R.id.add_item);
        removeItems = findViewById(R.id.remove_item);
        toolbar = findViewById(R.id.detailed_toolbar);

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });




        db =FirebaseFirestore.getInstance();
        // Retrieve the values from the Intent
        String sliderProductId = getIntent().getStringExtra("productid");


//        productid
        if (sliderProductId != null) {
            db.collection("ShowAll")
                    .whereEqualTo("productId", sliderProductId)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                // Directly retrieve data from the document
                                description.setText(document.getString("description"));
                                name.setText(document.getString("name"));
                                ratingTextView.setText(String.valueOf(document.getDouble("rating")));
                                double rating = document.getDouble("rating");
                                setRating((float) rating);
                                productPrice = document.getLong("price").intValue();
                                ImgUrl = document.getString("img_url");
                                imageSliderFromFirestore( ImgUrl);
                                delevaryCharge = document.getLong("delivery").intValue();
                                deliveryTimeTextView.setText(document.getString("delivery_time"));
                                replacment = document.getString("replace");
                                returnPolicy = document.getString("return1");
                                offerPercent = document.getLong("offer").intValue();
                                type = document.getString("type");
                                outOfStock = document.getBoolean("outOfStock");
                                colorFlag = document.getBoolean("colorFlag");
                                sizeFlag = document.getBoolean("sizeFlag");
                                productId = document.getString("productId");
                                productname =document.getString("name");
                                product_desc =document.getString("description");
                                delevary_time =document.getString("delivery_time");

                                if (delevaryCharge > 0) {
                                    delevaryFree.setText("Delivery charge : ₹" + delevaryCharge);
                                }
                                if (returnPolicy.equals("yes")) {
                                    returnHeadTextView.setVisibility(View.VISIBLE);
                                    returnDataTextView.setVisibility(View.VISIBLE);
                                }
                                if (replacment.equalsIgnoreCase("yes")) {
                                    replaceHeadTextView.setVisibility(View.VISIBLE);
                                    replaceDataTextView.setVisibility(View.VISIBLE);
                                }

                                afterofferPrice = (productPrice * (100 - offerPercent)) / 100;
                                if (offerPercent > 0) {
                                    productOffer.setVisibility(View.VISIBLE);
                                    price.setVisibility(View.VISIBLE);
                                    price.setPaintFlags(price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                                    productOffer.setText(offerPercent + "% off");
                                    price.setText(String.valueOf(productPrice));
                                    offerPrice.setText("₹" + afterofferPrice);
                                } else {
                                    offerPrice.setText("₹" + afterofferPrice);
                                }

                                if (outOfStock) {
                                    out_of_stock.setVisibility(View.VISIBLE);
                                }

        if(sizeFlag){
            sizeBoxLabel.setVisibility(View.VISIBLE);
            firestore.collection("ShowAll")
                    .whereEqualTo("productId", productId)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                List<String> sizeBoxList = new ArrayList<>();
                                for (DocumentSnapshot doc : task.getResult().getDocuments()) {
                                    List<String> sizeBox = (List<String>) doc.get("sizeBoxList");
                                    if (sizeBox != null) {
                                        sizeBoxList.addAll(sizeBox);
                                    }
                                }
                                // Convert colorBoxList to an array if needed
                                sizeBoxArray = sizeBoxList.toArray(new String[0]);
                                // Use the colorBoxArray as needed

                                for (int i = 0; i < sizeBoxArray.length; i++) {
                                    TextView box = sizeBoxes[i];
                                    box.setVisibility(View.VISIBLE);
                                    box.setText(sizeBoxArray[i]);
                                }


                            } else {
                                // Handle errors
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(DetailedActivity.this, "Please Wait....", Toast.LENGTH_SHORT).show();
                        }
                    });

        }

        if(colorFlag){
            colorBoxLabel.setVisibility(View.VISIBLE);
            firestore.collection("ShowAll")
                    .whereEqualTo("productId", productId)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                List<String> colorBoxList = new ArrayList<>();
                                for (DocumentSnapshot doc : task.getResult().getDocuments()) {
                                    List<String> colorBox = (List<String>) doc.get("colorBoxList");
                                    if (colorBox != null) {
                                        colorBoxList.addAll(colorBox);
                                    }
                                }
                                // Convert colorBoxList to an array if needed
                                colorBoxArray = colorBoxList.toArray(new String[0]);
                                // Use the colorBoxArray as needed

                                for (int i = 0; i < colorBoxArray.length; i++) {
                                    TextView box = colorBoxes[i];
                                    box.setVisibility(View.VISIBLE);
                                    box.setText(colorBoxArray[i]);
                                }

                            } else {
                                // Handle errors
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(DetailedActivity.this, "Please Wait....", Toast.LENGTH_SHORT).show();
                        }
                    });





        }
        setupColorBoxListeners();
        setupSizeBoxListeners();


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
              buyNow();
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

        //similar Product
        similarProductRecyclarview.setLayoutManager(new GridLayoutManager(this, 2));
        showAllModelList =new ArrayList<>();
        showAllAdapter =new ShowAllAdapter(this,showAllModelList);
        similarProductRecyclarview.setAdapter(showAllAdapter);


        firestore.collection("ShowAll")
                .whereEqualTo("type", type.toLowerCase())  // Add your where condition here
                .limit(6)  // Set the maximum limit to 6
                .get()
                .addOnCompleteListener(task1 -> {
                    if (task1.isSuccessful()) {
                        QuerySnapshot querySnapshot = task1.getResult();
                        int count = 0;
                        for (QueryDocumentSnapshot document1 : querySnapshot) {
                            if (count >= 6) {
                                break;  // Exit the loop if 6 items have been added
                            }
                            ShowAllModel showAllModel = document1.toObject(ShowAllModel.class);
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
                        } else {
                            // Handle the task failure
                            Toast.makeText(DetailedActivity.this, "Restart or Please Wait...", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(DetailedActivity.this, "Restart or Please Wait....", Toast.LENGTH_SHORT).show();
                    });

        }




    }



    private void imageSliderFromFirestore(String imgUrl) {
        // Show the URL being fetched in a Toast message
//        Toast.makeText(this, "Fetching images for URL: " + imgUrl, Toast.LENGTH_SHORT).show();

        // Create a list to hold the SlideModels
        List<SlideModel> slideModels = new ArrayList<>();
        slideModels.add(new SlideModel(imgUrl, ScaleTypes.FIT));

        // Get a reference to the Firestore collection
        CollectionReference collectionRef = firestore.collection("ShowAll");

        // Query Firestore to find documents with the given imgUrl
        collectionRef.whereEqualTo("img_url", imgUrl)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String documentId = document.getId();

                            // Execute the second query to retrieve data from the "slider" collection
                            firestore.collection("ShowAll")
                                    .document(documentId)
                                    .collection("slider")
                                    .get()
                                    .addOnCompleteListener(sliderTask -> {
                                        if (sliderTask.isSuccessful()) {
                                            for (QueryDocumentSnapshot queryDocumentSnapshot : sliderTask.getResult()) {
                                                String imageUrl = queryDocumentSnapshot.getString("img_url");
                                                if (imageUrl != null && !imageUrl.isEmpty()) {
                                                    slideModels.add(new SlideModel(imageUrl, ScaleTypes.FIT));
                                                }
                                            }
                                            // Set image list for the slider
                                            imageSlider.setImageList(slideModels);
                                        } else {
                                            // Handle exceptions or errors in retrieving the documents from "slider" collection
                                            Log.e(TAG, "Error fetching slider images: ", sliderTask.getException());
                                        }
                                    });
                        }
                    } else {
                        // Handle exceptions or errors in retrieving the document
                        Log.e(TAG, "Error fetching document: ", task.getException());
                        Toast.makeText(DetailedActivity.this, "Failed to fetch document", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    // Handle failures in Firestore queries
                    Log.e(TAG, "Firestore query failed: ", e);
                    Toast.makeText(DetailedActivity.this, "Firestore query failed", Toast.LENGTH_SHORT).show();
                });
    }


    private TextView setupColorBoxListeners() {
        for (int i = 0; i < 10; i++) {
            int colorBoxId = getResources().getIdentifier("color_box" + i, "id", getPackageName());
            colorBoxes[i] = findViewById(colorBoxId);
            final int index = i;

            colorBoxes[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    handleColorBoxClick(index);
                }
            });
        }

        // Return the last clicked color box TextView
        if (lastClickedColorBoxIndex != -1) {
            return (TextView) colorBoxes[lastClickedColorBoxIndex];
        } else {
            return null;
        }
    }

    private void handleColorBoxClick(int clickedIndex) {
        // Get the clicked color text
        String colorOutput = colorBoxArray[clickedIndex];
        colorTextOutput = colorOutput;
        // Update the colorBoxLabel with the clicked color text
       // colorBoxLabel.setText(colorTextOutput);


        // Reset the background color of the previously clicked box
        if (lastClickedColorBoxIndex != -1) {
            colorBoxes[lastClickedColorBoxIndex].setBackgroundResource(R.drawable.product_box_boder);
        }

        // Change the background color of the clicked box (if desired)
        colorBoxes[clickedIndex].setBackgroundResource(R.drawable.product_color_box_output);

        // Update the last clicked box index
        lastClickedColorBoxIndex = clickedIndex;
    }



    private View[] sizeBoxes = new View[10];
    private int lastClickedSizeBoxIndex = -1;

    private TextView setupSizeBoxListeners() {
        for (int i = 0; i < 10; i++) {
            int sizeBoxId = getResources().getIdentifier("size_box" + i, "id", getPackageName());
            sizeBoxes[i] = findViewById(sizeBoxId);
            final int index = i;

            sizeBoxes[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    handleSizeBoxClick(index);
                }
            });
        }

        // Return the last clicked size box TextView
        if (lastClickedSizeBoxIndex != -1) {
            return (TextView) sizeBoxes[lastClickedSizeBoxIndex];
        } else {
            return null;
        }
    }

    private void handleSizeBoxClick(int clickedIndex) {
        // Reset the background color of the previously clicked box
        String sizeOutput = sizeBoxArray[clickedIndex];
        sizeTextOutput  =sizeOutput;
      //  sizeBoxLabel.setText(sizeTextOutput);
        if (lastClickedSizeBoxIndex != -1) {
            sizeBoxes[lastClickedSizeBoxIndex].setBackgroundResource(R.drawable.product_size_box_border);
        }

        // Change the background color of the clicked box
        sizeBoxes[clickedIndex].setBackgroundResource(R.drawable.product_size_box_output);

        // Update the last clicked box index
        lastClickedSizeBoxIndex = clickedIndex;
    }


    public void setRating(float ratingValue) {
        RatingBar ratingBar = findViewById(R.id.my_rating);
        if (ratingBar != null) {
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

                // Set the color filter for filled, half-filled, and empty stars
                int color = ContextCompat.getColor(this, R.color.green);
                filledStars.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
                halfFilledStars.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
                emptyStars.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
            }

            // Set the progress drawable back to the RatingBar
            ratingBar.setProgressDrawable(progressDrawable);
        } else {
            Log.e(TAG, "RatingBar not found");
        }
    }



    private void addToCart() {

        if(outOfStock){
            Toast.makeText(DetailedActivity.this, "Product is out of stock", Toast.LENGTH_SHORT).show();
            return;
        }
        if(colorFlag || sizeFlag){
            Toast.makeText(this, "You can easily order this product without adding a card. Thank you!", Toast.LENGTH_SHORT).show();
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

    public void buyNow() {
        if (outOfStock) {
            Toast.makeText(this, "Product is out of stock", Toast.LENGTH_SHORT).show();
            return;
        }

        if (colorFlag) {
            if (colorTextOutput.equalsIgnoreCase("N/A")) {
                Toast.makeText(this, "Please select any color,Thanks!", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        if (sizeFlag) {
            if (sizeTextOutput.equalsIgnoreCase("N/A")) {
                Toast.makeText(this, "Please select any size,Thanks!", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        double totalamount = (afterofferPrice * totalQuantity );
        SharedPreferences sharedPreferences = getSharedPreferences("MyPreferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("productName", productname);
        editor.putInt("productamount", (int) afterofferPrice);
        editor.putString("productImgUrl", ImgUrl);
        editor.putString("productDesc", product_desc);
        editor.putInt("delivaryCharge", delevaryCharge);
        editor.putString("returnPolicy", returnPolicy);
        editor.putString("replacement", replacment);
        editor.putString("delevryTime", delevary_time);
        editor.putString("productColor", colorTextOutput);
        editor.putString("productSize", sizeTextOutput);
        editor.putInt("totalQuantity", totalQuantity);
        editor.putFloat("totalAmount", (float) totalamount);

        editor.apply();

        // Start the new activity
        Intent intent = new Intent(this, AddressActivity.class);
        startActivity(intent);

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

