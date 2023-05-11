package com.example.amzoodmart.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.amzoodmart.R;
import com.example.amzoodmart.adapters.AddressAdapter;
import com.example.amzoodmart.models.AddressModel;
import com.example.amzoodmart.models.MyCartModel;
import com.example.amzoodmart.models.NewProductsModel;
import com.example.amzoodmart.models.PopularProductsModel;
import com.example.amzoodmart.models.ShowAllModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AddressActivity extends AppCompatActivity implements AddressAdapter.SelectedAddress {

    Button addAddress;
    String cartProductName ="",cartProductImgUrl ="";
    int cartProductPrice =0;

     String userName =" ",userNumber =" ",userDistict =" ",userAddDeatail =" ",userCity =" ",userCode="";
    RecyclerView recyclerView;
    private AddressAdapter addressAdapter;
    private List<AddressModel> addressModelList;
    FirebaseFirestore firestore;
    FirebaseAuth auth;
    Button addAddressBtn,paymentBtn;
    Toolbar toolbar ;
    String mAddress ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);

        toolbar =findViewById(R.id.address_toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //get data form detailed activity
        Object obj =getIntent().getSerializableExtra("item");
        userName= getIntent().getStringExtra("userName");
        userNumber= getIntent().getStringExtra("userNumber");
        userDistict= getIntent().getStringExtra("userDistict");
        userAddDeatail= getIntent().getStringExtra("userAddDeatail");
        userCity= getIntent().getStringExtra("userCity");
        userCode= getIntent().getStringExtra("userCode");


        // get data form cart
        cartProductName =getIntent().getStringExtra("cartProductName");
        cartProductImgUrl =getIntent().getStringExtra("cartProductImg");
        cartProductPrice =getIntent().getIntExtra("cartProductPrice",0);

        firestore =FirebaseFirestore.getInstance();


        auth =FirebaseAuth.getInstance();

        recyclerView = findViewById(R.id.address_recycler);
        paymentBtn =findViewById(R.id.payment_btn);
        addAddress =findViewById(R.id.add_address_btn);

        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        addressModelList =new ArrayList<>();
        addressAdapter =new AddressAdapter(getApplicationContext(),addressModelList,this);
        recyclerView.setAdapter(addressAdapter);

        firestore.collection("CurrentUser.").document(Objects.requireNonNull(auth.getCurrentUser()).getUid())
                        .collection("Address").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for (DocumentSnapshot doc :task.getResult().getDocuments()){

                                AddressModel addressModel =doc.toObject(AddressModel.class);
                                addressModelList.add(addressModel);

                                addressAdapter.notifyDataSetChanged();

                                userName =addressModel.getUserName();
                                userNumber =addressModel.getUserNumber();
                                userCity =addressModel.getUserCity();
                                userDistict =addressModel.getUserDistict();
                                userAddDeatail =addressModel.getUserAddress_detailed();
                                userCode =addressModel.getUserCode();
                            }
                        }
                    }
                });
        paymentBtn.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                double amount =0.0;
                String  productName ="";
                String productImgUrl ="";
                String productDesc ="";
                if(obj instanceof NewProductsModel){
                    NewProductsModel newProductsModel =(NewProductsModel) obj;
                    amount =newProductsModel.getPrice();
                    productName =newProductsModel.getName();
                    productImgUrl =newProductsModel.getImg_url();
                    productDesc =newProductsModel.getDescription();

                }
              else if(obj instanceof PopularProductsModel){
                    PopularProductsModel popularProductsModel =(PopularProductsModel) obj;
                    amount =popularProductsModel.getPrice();
                    productName =popularProductsModel.getName();
                    productImgUrl =popularProductsModel.getImg_url();
                    productDesc =popularProductsModel.getDescription();
                }
               else if(obj instanceof ShowAllModel){
                    ShowAllModel showAllModel =(ShowAllModel) obj;
                    amount =showAllModel.getPrice();
                    productName =showAllModel.getName();
                    productImgUrl =showAllModel.getImg_url();
                    productDesc =showAllModel.getDescription();
                }else{
                    productName =cartProductName;
                    amount =cartProductPrice;
                    productImgUrl =cartProductImgUrl;
                }

                Intent intent =new Intent(AddressActivity.this,PaymentActivity.class);
                intent.putExtra("amount",amount);
                intent.putExtra("productName",productName);
                intent.putExtra("productImgUrl",productImgUrl);
                intent.putExtra("productDesc",productDesc);
                intent.putExtra("userName",userName);
                intent.putExtra("userNumber",userNumber);
                intent.putExtra("userDistict",userDistict);
                intent.putExtra("userAddDeatail",userAddDeatail);
                intent.putExtra("userCity",userCity);
                intent.putExtra("userCode",userCode);
                startActivity(intent);


            }
        });

        addAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AddressActivity.this,AddAddressActivity.class));
                finish();
            }
        });
    }

    @Override
    public void setAddress(String address) {
        mAddress =address;
    }
}