package com.example.amzoodmart.adapters;

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
                String address ="";
                address +=myOrderModelList.get(position).getUserName() +", "
                        + myOrderModelList.get(position).getUserCity() +", "
                        +myOrderModelList.get(position).getUserCode()+", "
                        +myOrderModelList.get(position).getUserDistict() +", "
                        +myOrderModelList.get(position).getUserAddress_detailed() +", "
                        +myOrderModelList.get(position).getUserNumber() +". ";

                Intent intent =new Intent("MyTotalAmount");
                intent.putExtra("orderId",myOrderModelList.get(position).getOrderId());
                intent.putExtra("orderName",myOrderModelList.get(position).getProductName());
                intent.putExtra("orderPrice",myOrderModelList.get(position).getProductPrice());
                intent.putExtra("orderQty",myOrderModelList.get(position).getProductQuantity());
                intent.putExtra("orderPayment",myOrderModelList.get(position).getMethod());
                intent.putExtra("orderStatus",myOrderModelList.get(position).getOrderStatus());
                intent.putExtra("orderDate",myOrderModelList.get(position).getCurrentDate());
                intent.putExtra("orderImgUrl",myOrderModelList.get(position).getProductImgUrl());
                intent.putExtra("documentId",myOrderModelList.get(position).getDocumentId());
                intent.putExtra("orderAddress",address);



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
