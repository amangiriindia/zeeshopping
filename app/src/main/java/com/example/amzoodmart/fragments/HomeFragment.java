package com.example.amzoodmart.fragments;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.interfaces.ItemClickListener;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.amzoodmart.R;
import com.example.amzoodmart.activities.DetailedActivity;
import com.example.amzoodmart.activities.ShowAllActivity;
import com.example.amzoodmart.adapters.CategoryAdapter;
import com.example.amzoodmart.adapters.NewProductAdapter;
import com.example.amzoodmart.adapters.PopularProductAdapter;
import com.example.amzoodmart.models.CategoryModel;
import com.example.amzoodmart.models.NewProductsModel;
import com.example.amzoodmart.models.PopularProductsModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    TextView catShowAll,popularShowAll,newProductShowAll,cate1ShowAll;
    LinearLayout linearLayout;
    ProgressDialog progressDialog;
    RecyclerView catRecyclerView,newProductRecyclerView,popularRecyclerview,cate1RecycyclerView;

    //Category recycleview
    CategoryAdapter categoryAdapter;
    List<CategoryModel> categoryModelList;

    //New product Recyclerview
    NewProductAdapter newProductAdapter;
    List<NewProductsModel> newProductsModelList;

    //Popular  products
    PopularProductAdapter popularProductAdapter;
    List<PopularProductsModel> popularProductsModelList;
    //FireStore
    FirebaseFirestore db;
    public HomeFragment() {
        // Required empty public constructor
    }


    @SuppressLint("NotifyDataSetChanged")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =  inflater.inflate(R.layout.fragment_home2, container, false);
        db =FirebaseFirestore.getInstance();


        progressDialog =new ProgressDialog(getActivity());
        catRecyclerView =root.findViewById(R.id.rec_category);
        newProductRecyclerView =root.findViewById(R.id.new_product_rec);
        popularRecyclerview =root.findViewById(R.id.popular_rec);
        cate1RecycyclerView =root.findViewById(R.id.category_1_rec);


        catShowAll =root.findViewById(R.id.category_see_all);
        popularShowAll = root.findViewById(R.id.popular_see_all);
        newProductShowAll =root.findViewById(R.id.newProducts_see_all);


       catShowAll.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent intent =new Intent(getContext(), ShowAllActivity.class);
               startActivity(intent);
           }
       });

        popularShowAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(getContext(), ShowAllActivity.class);
                startActivity(intent);
            }
        });

        newProductShowAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(getContext(), ShowAllActivity.class);
                startActivity(intent);
            }
        });


        linearLayout =root.findViewById(R.id.home_layout);
        linearLayout.setVisibility(View.GONE);
//        ImageSlider imageSlider =root.findViewById(R.id.image_slider);
//        List<SlideModel> slideModels =new ArrayList<>();
//                slideModels.add(new SlideModel(R.drawable.banner1,"Discount On Shoes Items", ScaleTypes.CENTER_CROP));
//        slideModels.add(new SlideModel(R.drawable.banner2,"Discount On Perfume", ScaleTypes.CENTER_CROP));
//        slideModels.add(new SlideModel(R.drawable.banner3,"70% OFF", ScaleTypes.CENTER_CROP));
//
//        imageSlider.setImageList(slideModels);



        //image slider
        ImageSlider imageSlider =root.findViewById(R.id.image_slider);
        List<SlideModel> slideModels =new ArrayList<>();

        db.collection("Slider").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for (QueryDocumentSnapshot queryDocumentSnapshot :task.getResult()){
                         String imgUrl =queryDocumentSnapshot.getString("img_url");
                         String tittle =queryDocumentSnapshot.getString("sliderHeading");
                        slideModels.add(new SlideModel(imgUrl,tittle,ScaleTypes.FIT));


                    }
                    imageSlider.setImageList(slideModels,ScaleTypes.FIT);
                }
            }
        })  ;





        imageSlider.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemSelected(int i) {

            }

            @Override
            public void doubleClick(int i) {
                switch (i) {
                    case 0:
                        DocumentReference docRef = db.collection("Slider").document("BdqjN5uqG43qL8O0Vjjn").collection("product_detail").document("VCTL4l0ghPMAkXPXmmui");
                        gotoDetailed(docRef);
                        break;
                    case 1:
                        DocumentReference docRef1 = db.collection("Slider").document("BdqjN5uqG43qL8O0Vjjn").collection("product_detail").document("VCTL4l0ghPMAkXPXmmui");
                        gotoDetailed(docRef1);
                        break;
                    case 2:
                        DocumentReference docRef2 = db.collection("Slider").document("BdqjN5uqG43qL8O0Vjjn").collection("product_detail").document("VCTL4l0ghPMAkXPXmmui");
                        gotoDetailed(docRef2);
                        break;
                    default:
                        break;
                }

            }
        });





        progressDialog.setTitle("Welcome To Amzood Mart");
        progressDialog.setMessage("please wait...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();


        // Category
        catRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.HORIZONTAL,false));
        categoryModelList =new ArrayList<>();
        categoryAdapter =new CategoryAdapter(getContext(),categoryModelList);
        catRecyclerView.setAdapter(categoryAdapter);

        db.collection("Category")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        for (QueryDocumentSnapshot document : querySnapshot) {

                            CategoryModel categoryModel =document.toObject(CategoryModel.class);
                            categoryModelList.add(categoryModel);
                            categoryAdapter.notifyDataSetChanged();
                            linearLayout.setVisibility(View.VISIBLE);
                            progressDialog.dismiss();
                        }
                    }
                  else {
                       // Toast.makeText(getActivity(), ""+task.getException(), Toast.LENGTH_SHORT).show();
                    }



                });

        //new product
        newProductRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.HORIZONTAL,false));
        newProductsModelList =new ArrayList<>();
        newProductAdapter =new NewProductAdapter(getContext(), newProductsModelList);
        newProductRecyclerView.setAdapter(newProductAdapter);


        db.collection("ShowAll")
                .whereEqualTo("product_status", "newProduct")  // Add your where condition here
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        for (QueryDocumentSnapshot document : querySnapshot) {
                            NewProductsModel newProductsModel = document.toObject(NewProductsModel.class);
                            newProductsModelList.add(newProductsModel);
                            newProductAdapter.notifyDataSetChanged();
                        }
                    } else {
                        // Handle the task failure
                    }
                });


        //popular Product
        popularRecyclerview.setLayoutManager(new GridLayoutManager(getActivity(),2));
        popularProductsModelList =new ArrayList<>();
        popularProductAdapter =new PopularProductAdapter(getContext(), popularProductsModelList);
        popularRecyclerview.setAdapter(popularProductAdapter);

        db.collection("ShowAll")
                .whereEqualTo("product_status", "popular")  // Replace fieldName and fieldValue with your actual field and value
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        for (QueryDocumentSnapshot document : querySnapshot) {
                            PopularProductsModel popularProductsModel = document.toObject(PopularProductsModel.class);
                            popularProductsModelList.add(popularProductsModel);
                            popularProductAdapter.notifyDataSetChanged();
                        }
                    } else {
                        // Handle the failure case
                    }
                });

  // categoty1
        cate1RecycyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));


        return root;
    }

    private void gotoDetailed(DocumentReference docRef) {
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String imgUrl = document.getString("img_url");
                        String name = document.getString("name");
                        double price = document.getDouble("price");
                        String productStatus = document.getString("product_status");
                        double rating = document.getDouble("rating");
                        String type = document.getString("type");
                        String description =document.getString("description");

                        // Use the retrieved field values as needed
                        Intent intent =new Intent(getContext(), DetailedActivity.class);
                        intent.putExtra("img_url",imgUrl);
                        intent.putExtra("name",name);
                        intent.putExtra("price",price);
                        intent.putExtra("product_status",productStatus);
                        intent.putExtra("rating",rating);
                        intent.putExtra("description",description);
                        startActivity(intent);

                    } else {
                        // Document does not exist
                    }
                }
            }
        });
    }
}