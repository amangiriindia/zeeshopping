package com.amzsoft.zeeshopping.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.amzsoft.zeeshopping.R;
import com.amzsoft.zeeshopping.activities.DetailedActivity;
import com.amzsoft.zeeshopping.models.PopularProductsModel;

import java.util.List;

public class PopularProductAdapter extends RecyclerView.Adapter<PopularProductAdapter.ViewHolder> {

    private Context context;
    private List<PopularProductsModel> popularProductsModelList;

    public PopularProductAdapter(Context context, List<PopularProductsModel> popularProductsModelList) {
        this.context = context;
        this.popularProductsModelList = popularProductsModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.popular_items,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Glide.with(context).load(popularProductsModelList.get(position).getImg_url()).into(holder.imageView);
        holder.name.setText(popularProductsModelList.get(position).getName());

        int  productPrice =popularProductsModelList.get(position).getPrice();
        int offerPercent =popularProductsModelList.get(position).getOffer();
        int afterofferPrice =(productPrice * (100 - offerPercent)) / 100;

        if(offerPercent>0){
            holder.pricet.setVisibility(View.VISIBLE);
            holder.pricet.setPaintFlags(holder.pricet.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.pricet.setText(""+productPrice);
            holder.offerPriceT.setText("₹"+afterofferPrice);
        }else {
            holder.offerPriceT.setText("₹ " +afterofferPrice);
        }

        //FOR DETAILED PRODUCT
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailedActivity.class);
                intent.putExtra("detailed",popularProductsModelList.get(position));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return popularProductsModelList.size();
    }

    public class  ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView name,pricet,offerPriceT;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.all_img);
            name = itemView.findViewById(R.id.all_product_name);
            pricet = itemView.findViewById(R.id.all_price);
            offerPriceT =itemView.findViewById(R.id.all_off_price);
        }
    }


}
