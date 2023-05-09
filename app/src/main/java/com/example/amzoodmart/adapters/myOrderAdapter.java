package com.example.amzoodmart.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.amzoodmart.R;
import com.example.amzoodmart.models.MyOrderModel;

import java.util.List;

public class myOrderAdapter extends RecyclerView.Adapter<myOrderAdapter.ViewHolder> {

    Context context;
    List<MyOrderModel>myOrderModelList;

    public myOrderAdapter(Context context, List<MyOrderModel> myOrderModelList) {
        this.context = context;
        this.myOrderModelList = myOrderModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.order_items,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Glide.with(context).load(myOrderModelList.get(position).getProductImgUrl()).into(holder.productImg);
        holder.productName.setText(myOrderModelList.get(position).getProductName());
        holder.productQunatity.setText(myOrderModelList.get(position).getProductQuantity());
        holder.productDesc.setText(myOrderModelList.get(position).getProductDesc());
        holder.productPrice.setText(myOrderModelList.get(position).getProductPrice());

    }

    @Override
    public int getItemCount() {
        return myOrderModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
         ImageView productImg;
         TextView productName,productQunatity,productDesc,productPrice;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productImg =itemView.findViewById(R.id.order_item_image);
            productName =itemView.findViewById(R.id.order_item_name);
            productPrice =itemView.findViewById(R.id.order_item_price);
            productDesc =itemView.findViewById(R.id.order_item_description);
            productQunatity =itemView.findViewById(R.id.order_item_quantity);
        }
    }

}
