package com.amzventures.zeeshopping.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.amzventures.zeeshopping.R;
import com.amzventures.zeeshopping.activities.DetailedActivity;
import com.amzventures.zeeshopping.models.DiscountModel;

import java.util.List;

public class DiscountAdapter extends RecyclerView.Adapter<DiscountAdapter.ViewHolder>  {

    private Context context;
    private List<DiscountModel> discountModelList;

    public DiscountAdapter(Context context, List<DiscountModel> discountModelList) {
        this.context = context;
        this.discountModelList = discountModelList;
    }

    @NonNull
    @Override
    public DiscountAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DiscountAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.discount_items,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull DiscountAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        Glide.with(context).load(discountModelList.get(position).getImg_url()).into(holder.imageView);
        holder.name.setText(discountModelList.get(position).getName());
        holder.offer.setText((discountModelList.get(position).getOffer()+"% OFF"));

        //FOR DETAILED PRODUCT
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailedActivity.class);
                intent.putExtra("detailed",discountModelList.get(position));
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return discountModelList.size();
    }
    public class  ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView name,offer;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.discount_img);
            name = itemView.findViewById(R.id.discount_product_name);
            offer = itemView.findViewById(R.id.discount_offer);
        }
    }
}
