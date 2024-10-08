package com.amzventures.zeeshopping.fragments;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amzventures.zeeshopping.R;
import com.amzventures.zeeshopping.activities.CategotyActivity;
import com.amzventures.zeeshopping.activities.DetailedActivity;
import com.amzventures.zeeshopping.activities.ShowAllActivity;
import com.amzventures.zeeshopping.adapters.CategoryAdapter;
import com.amzventures.zeeshopping.adapters.DiscountAdapter;
import com.amzventures.zeeshopping.adapters.NewProductAdapter;
import com.amzventures.zeeshopping.adapters.PopularProductAdapter;
import com.amzventures.zeeshopping.adapters.ShowAllAdapter;
import com.amzventures.zeeshopping.models.CategoryModel;
import com.amzventures.zeeshopping.models.DiscountModel;
import com.amzventures.zeeshopping.models.NewProductsModel;
import com.amzventures.zeeshopping.models.PopularProductsModel;
import com.amzventures.zeeshopping.models.ShowAllModel;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.interfaces.ItemClickListener;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HomeFragment extends Fragment {

    TextView catShowAll,popularShowAll,newProductShowAll,mostDemandShowAll,offerShowAll,recommendShowAll;
    LinearLayout linearLayout;
    ProgressDialog progressDialog;
    RecyclerView catRecyclerView,newProductRecyclerView,popularRecyclerview,mostDemoandRecyclerView,offerRecyclerView,recommendRecyclerView;

    //Category recycleview
    CategoryAdapter categoryAdapter;
    List<CategoryModel> categoryModelList;

    //New product Recyclerview
    NewProductAdapter newProductAdapter;
    List<NewProductsModel> newProductsModelList;

    //Popular  products
    PopularProductAdapter popularProductAdapter;
    List<PopularProductsModel> popularProductsModelList;
    //most demoand
    ShowAllAdapter showAllAdapter;
    List<ShowAllModel> showAllModelList;
    //recommand
    ShowAllAdapter recshowAllAdapter;
    List<ShowAllModel> recshowAllModelList;

    DiscountAdapter discountAdapter;
    List<DiscountModel>discountModelList;
    //FireStore
    FirebaseFirestore db;
    public HomeFragment() {
        // Required empty public constructor
    }


    @SuppressLint({"NotifyDataSetChanged", "MissingInflatedId"})
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
        mostDemoandRecyclerView =root.findViewById(R.id.most_demand_rec);
        offerRecyclerView =root.findViewById(R.id.offer_rec);
        recommendRecyclerView =root.findViewById(R.id.recommend_rec);


        catShowAll =root.findViewById(R.id.category_see_all);
        popularShowAll = root.findViewById(R.id.popular_see_all);
        newProductShowAll =root.findViewById(R.id.newProducts_see_all);
        mostDemandShowAll =root.findViewById(R.id.most_demand_see_all);
        offerShowAll =root.findViewById(R.id.offer_see_all);
        recommendShowAll =root.findViewById(R.id.recommend_see_all);



       catShowAll.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent intent =new Intent(getContext(), ShowAllActivity.class);
               intent.putExtra("title","All Category");
               startActivity(intent);
           }
       });

        popularShowAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), CategotyActivity.class);
                intent.putExtra("product_status", "popular");
                intent.putExtra("title","Popular Product");
                startActivity(intent);
            }
        });

        newProductShowAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), CategotyActivity.class);
                intent.putExtra("product_status", "newProduct");
                intent.putExtra("title","New Product");
                startActivity(intent);
            }
        });
        mostDemandShowAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), CategotyActivity.class);
                intent.putExtra("title","Most Demand Product");
                startActivity(intent);
            }
        });
        offerShowAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), CategotyActivity.class);
                intent.putExtra("off", 40);
                intent.putExtra("title","Special Offer");
                startActivity(intent);
            }
        });
        recommendShowAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ShowAllActivity.class);
                intent.putExtra("title","Recommend for you");
                startActivity(intent);
            }
        });



        linearLayout =root.findViewById(R.id.home_layout);
        linearLayout.setVisibility(View.GONE);


          //image slider

        ImageSlider imageSlider = root.findViewById(R.id.image_slider);
        List<SlideModel> slideModels = new ArrayList<>();

        db.collection("Slider").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    HashMap<Integer, String> myHashMap = new HashMap<>();
                    int k =0;

                    for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                        String imgUrl = queryDocumentSnapshot.getString("img_url");
                        String tittle = queryDocumentSnapshot.getString("sliderHeading");
                        String productid = queryDocumentSnapshot.getString("productid");
                        myHashMap.put(k++,productid);
                        slideModels.add(new SlideModel(imgUrl, tittle, ScaleTypes.FIT));
                    }

                    imageSlider.setImageList(slideModels, ScaleTypes.FIT);

                    // Set the click listener after images are loaded
                    imageSlider.setItemClickListener(new ItemClickListener() {
                        @Override
                        public void onItemSelected(int i) {
                            Intent intent =new Intent(getContext(),DetailedActivity.class);
                            intent.putExtra("productid",myHashMap.get(i));
                            startActivity(intent);
//                            Toast.makeText(getContext(), "Clicked on image " + (i + 1)+myHashMap.get(i)+"" , Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void doubleClick(int i) {
                            switch (i) {
                                case 0:
                                case 1:
                                case 2:

                                    break;
                                default:
                                    break;
                            }
                        }
                    });
                } else {
                    Toast.makeText(getActivity(), "Restart or Please Wait...", Toast.LENGTH_SHORT).show();
                }
            }
        }) .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Restart or Please Wait....", Toast.LENGTH_SHORT).show();
            }
        });






        progressDialog.setTitle("Welcome To Zee Shopping");
        progressDialog.setMessage("please wait...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();


        // Category
        catRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        categoryModelList = new ArrayList<>();
        categoryAdapter = new CategoryAdapter(getContext(), categoryModelList);
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
                        Toast.makeText(getActivity(), "Restart or Please Wait...", Toast.LENGTH_SHORT).show();
                    }



                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), "Restart or Please Wait....", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(getActivity(), "Restart or Please Wait...", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), "Restart or Please Wait....", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(getActivity(), "Restart or Please Wait...", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), "Restart or Please Wait....", Toast.LENGTH_SHORT).show();
                    }
                });



        mostDemoandRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        showAllModelList =new ArrayList<>();
        showAllAdapter =new ShowAllAdapter(getContext(),showAllModelList);
        mostDemoandRecyclerView.setAdapter(showAllAdapter);


        db.collection("ShowAll")
                .whereGreaterThan("rating", 4) // Add your where condition here
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
                        Toast.makeText(getActivity(), "Restart or Please Wait...", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), "Restart or Please Wait....", Toast.LENGTH_SHORT).show();
                    }
                });


        offerRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        discountModelList =new ArrayList<>();
        discountAdapter =new DiscountAdapter(getContext(),discountModelList);
        offerRecyclerView.setAdapter(discountAdapter);


        db.collection("ShowAll")
                .whereGreaterThan("offer", 40) // Add your where condition here
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
                            DiscountModel discountModel = document.toObject(DiscountModel.class);
                            discountModelList.add(discountModel);
                            discountAdapter.notifyDataSetChanged();
                            count++;
                        }
                    } else {
                        // Handle the task failure
                        Toast.makeText(getActivity(), "Restart or Please Wait...", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), "Restart or Please Wait....", Toast.LENGTH_SHORT).show();
                    }
                });


        recommendRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        recshowAllModelList =new ArrayList<>();
        recshowAllAdapter =new ShowAllAdapter(getContext(),recshowAllModelList);
        recommendRecyclerView.setAdapter(recshowAllAdapter);


        db.collection("ShowAll")
                .limit(10)  // Set the maximum limit to 6
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        int count = 0;
                        for (QueryDocumentSnapshot document : querySnapshot) {
                            if (count >= 10) {
                                break;  // Exit the loop if 6 items have been added
                            }
                            ShowAllModel recshowAllModel = document.toObject(ShowAllModel.class);
                            recshowAllModelList.add(recshowAllModel);
                            recshowAllAdapter.notifyDataSetChanged();
                            count++;
                        }
                    } else {
                        // Handle the task failure
                        Toast.makeText(getActivity(), "Restart or Please Wait...", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), "Restart or Please Wait....", Toast.LENGTH_SHORT).show();
                    }
                });

        return root;
    }

}