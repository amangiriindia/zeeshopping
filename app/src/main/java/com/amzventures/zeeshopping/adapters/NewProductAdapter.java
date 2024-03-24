package com.amzventures.zeeshopping.adapters;

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
import com.amzventures.zeeshopping.R;
import com.amzventures.zeeshopping.activities.DetailedActivity;
import com.amzventures.zeeshopping.models.NewProductsModel;

import java.util.List;

public class NewProductAdapter extends RecyclerView.Adapter<NewProductAdapter.ViewHolder> {

    private Context context;
    private List<NewProductsModel> list ;

    public NewProductAdapter(Context context, List<NewProductsModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.new_products,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Glide.with(context).load(list.get(position).getImg_url()).into(holder.newImg);
        holder.newName.setText(list.get(position).getName());
      //  holder.newPrice.setText("₹"+String.valueOf(list.get(position).getPrice()));
        int  productPrice =list.get(position).getPrice();
        int offerPercent =list.get(position).getOffer();
        int afterofferPrice =(productPrice * (100 - offerPercent)) / 100;

        if(offerPercent>0){
            holder.newPrice.setVisibility(View.VISIBLE);
            holder.newPrice.setPaintFlags(holder.newPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.newPrice.setText(""+productPrice);
            holder.newPriceOffer.setText("₹"+afterofferPrice);
        }else {
            holder.newPriceOffer.setText("₹ " +afterofferPrice);
        }



        //for move to detail page
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(context, DetailedActivity.class);
//                intent.putExtra("detailed",list.get(position));
                intent.putExtra("productid",list.get(position).getProductId());
                context.startActivity(intent);
            }

        });

    }



    @Override
    public int getItemCount() {
        return list.size();
    }

    public  class ViewHolder extends RecyclerView.ViewHolder {
        ImageView newImg;
        TextView  newName,newPrice,newPriceOffer;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            newImg =itemView.findViewById(R.id.new_img);
            newName =itemView.findViewById(R.id.new_product_name);
            newPrice =itemView.findViewById(R.id.new_price);
            newPriceOffer =itemView.findViewById(R.id.new_off_price);

        }
    }

}
