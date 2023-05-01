package com.example.amzoodmart.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.amzoodmart.R;
import com.example.amzoodmart.adapters.CategoryAdapter;
import com.example.amzoodmart.adapters.NewProductAdapter;
import com.example.amzoodmart.models.CategoryModel;
import com.example.amzoodmart.models.NewProductsModel;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    RecyclerView catRecyclerView,newProductRecyclerView;
    //Category recycleview
    CategoryAdapter categoryAdapter;
    List<CategoryModel> categoryModelList;

    //New product Recyclerview
    NewProductAdapter newProductAdapter;
    List<NewProductsModel> newProductsModelList;

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


        catRecyclerView =root.findViewById(R.id.rec_category);
        newProductRecyclerView =root.findViewById(R.id.new_product_rec);
        db =FirebaseFirestore.getInstance();


        //image slider
        ImageSlider imageSlider =root.findViewById(R.id.image_slider);
        List<SlideModel> slideModels =new ArrayList<>();

        slideModels.add(new SlideModel(R.drawable.banner1,"Discount On Shoes Items", ScaleTypes.CENTER_CROP));
        slideModels.add(new SlideModel(R.drawable.banner2,"Discount On Perfume", ScaleTypes.CENTER_CROP));
        slideModels.add(new SlideModel(R.drawable.banner3,"70% OFF", ScaleTypes.CENTER_CROP));

        imageSlider.setImageList(slideModels);


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

        db.collection("NewProduct")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        for (QueryDocumentSnapshot document : querySnapshot) {

                            NewProductsModel newProductsModel =document.toObject(NewProductsModel.class);
                            newProductsModelList.add(newProductsModel);
                            newProductAdapter.notifyDataSetChanged();
                        }
                    }
                     else{
                       // Toast.makeText(getActivity(), ""+task.getException(), Toast.LENGTH_SHORT).show();
                    }
                });

        return root;
    }
}