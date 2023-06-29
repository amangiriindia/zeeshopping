package com.amzsoft.zeeshopping.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.amzsoft.zeeshopping.R;
import com.amzsoft.zeeshopping.models.MyOrderModel;

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
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        Glide.with(context).load(myOrderModelList.get(position).getProductImgUrl()).into(holder.productImg);
        holder.productName.setText(myOrderModelList.get(position).getProductName());
        holder.productQunatity.setText(myOrderModelList.get(position).getProductQuantity());
        holder.date.setText(myOrderModelList.get(position).getCurrentDate());
        holder.productPrice.setText("₹ "+myOrderModelList.get(position).getProductPrice());
        LinearLayout linearLayout = holder.linearLayout;
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent =new Intent("MyTotalAmount");
                intent.putExtra("orderId",myOrderModelList.get(position).getOrderId());
                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
            }
        });





    }

    @Override
    public int getItemCount() {
        return myOrderModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout linearLayout;
         ImageView productImg;
         TextView productName,productQunatity,productPrice,date;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productImg =itemView.findViewById(R.id.order_item_image);
            productName =itemView.findViewById(R.id.order_item_name);
            productPrice =itemView.findViewById(R.id.order_item_price);
            date =itemView.findViewById(R.id.order_item_date);
            productQunatity =itemView.findViewById(R.id.order_item_quantity);
            linearLayout = itemView.findViewById(R.id.layout_order_item);
        }
    }

}
