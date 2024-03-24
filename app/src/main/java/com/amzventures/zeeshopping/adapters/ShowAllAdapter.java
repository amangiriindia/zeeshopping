package com.amzventures.zeeshopping.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.amzventures.zeeshopping.R;
import com.amzventures.zeeshopping.activities.DetailedActivity;
import com.amzventures.zeeshopping.models.ShowAllModel;

import java.util.List;

public class ShowAllAdapter extends RecyclerView.Adapter<ShowAllAdapter.ViewHolder> {

    private Context context;
    private List<ShowAllModel> list;

    public ShowAllAdapter(Context context, List<ShowAllModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.show_all_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        Glide.with(context).load(list.get(position).getImg_url()).into(holder.mItemImage);
        holder.mName.setText(list.get(position).getName());
        //for rating bar
        RatingBar ratingBar = holder.ratingBar;
        ratingBar.setRating((float) list.get(position).getRating());
        Context context = ratingBar.getContext();
        int color = ContextCompat.getColor(context, R.color.green);
        ratingBar.setProgressTintList(ColorStateList.valueOf(color));

        int delevary =list.get(position).getDelivery();

        if(delevary==0){
            holder.deleveryText.setVisibility(View.VISIBLE);
            holder.deleveryText.setText("Free delivery");
        }

       int  productPrice =list.get(position).getPrice();
       int offerPercent =list.get(position).getOffer();
       int afterofferPrice =(productPrice * (100 - offerPercent)) / 100;
        if(offerPercent>0){
            holder.offerText.setVisibility(View.VISIBLE);
            holder.mCost.setVisibility(View.VISIBLE);
            holder.mCost.setPaintFlags(holder.mCost.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.offerText.setText(offerPercent+"% off");
            holder.mCost.setText(""+productPrice);
            holder.afterOfferCostText.setText("₹"+afterofferPrice);
        }else {
            holder.afterOfferCostText.setText("₹ " +afterofferPrice);
        }



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

    public class ViewHolder extends RecyclerView.ViewHolder{

        private ImageView mItemImage;
        private TextView mCost,offerText,afterOfferCostText,deleveryText;
        RatingBar ratingBar;
        private TextView mName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mItemImage =itemView.findViewById(R.id.item_image);
            mCost =itemView.findViewById(R.id.item_cost);
            mName = itemView.findViewById(R.id.item_nam);
            ratingBar =itemView.findViewById(R.id.my_rating);
            offerText =itemView.findViewById(R.id.item_offer);
            afterOfferCostText = itemView.findViewById(R.id.item_off_price);
            deleveryText =itemView.findViewById(R.id.item_delevary_status);
        }
    }

}
