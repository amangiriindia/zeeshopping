package com.example.amzoodmart.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.amzoodmart.R;
import com.example.amzoodmart.models.NewProductsModel;
import com.google.firebase.firestore.FirebaseFirestore;


public class DetailedActivity extends AppCompatActivity {

    ImageView detailedImg;
    TextView rating,name,description,price;
    Button addToCart,buyNow;
    ImageView addItems,removeItems;

    // New Product
    NewProductsModel newProductsModel =null;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);

        firestore =FirebaseFirestore.getInstance();
        final Object obj =getIntent().getSerializableExtra("detailed");
        if(obj instanceof NewProductsModel){
            newProductsModel =(NewProductsModel) obj;
        }

        detailedImg =findViewById(R.id.detailed_img);
        name =findViewById(R.id.detailed_name);
        rating =findViewById(R.id.rating);
        description=findViewById(R.id.detailed_desc);
        price =findViewById(R.id.detailed_price);

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
            price.setText(String.valueOf(newProductsModel.getPrice()));

        }

    }
}