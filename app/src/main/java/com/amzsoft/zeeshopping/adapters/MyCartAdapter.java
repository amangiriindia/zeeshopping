package com.amzsoft.zeeshopping.adapters;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.amzsoft.zeeshopping.R;
import com.amzsoft.zeeshopping.models.MyCartModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;






public class MyCartAdapter extends RecyclerView.Adapter<MyCartAdapter.ViewHolder> {
    Context context;
    List<MyCartModel> list;
    FirebaseFirestore firestore;
    FirebaseAuth auth;

    int totalAmount = 0;
    int totalQty = 0;
    String pname = "";

    public MyCartAdapter(Context context, List<MyCartModel> list) {
        this.context = context;
        this.list = list;
        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        calculateTotal();
    }

    @NonNull
    @Override
    public MyCartAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.my_cart_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyCartAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Glide.with(context).load(list.get(position).getProductImgurl()).into(holder.img);
        holder.price.setText("₹ "+list.get(position).getProductPrice());
        holder.name.setText(list.get(position).getProductName());
        holder.totalQuantity.setText(String.valueOf(list.get(position).getTotalQuantity()));

        holder.removeCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(context, R.style.AlertDialogCustom));
                builder.setTitle("Delete Cart Item");
                builder.setMessage("Are you sure you want to delete?");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        firestore.collection("AddToCart")
                                .document(auth.getCurrentUser().getUid())
                                .collection("User")
                                .document(list.get(position).getDocumentId())
                                .delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        list.remove(position);
                                        notifyItemRemoved(position);
                                        notifyItemRangeChanged(position, list.size());
                                        Toast.makeText(context, "Cart item deleted successfully", Toast.LENGTH_SHORT).show();

                                        calculateTotal();
                                    }
                                });
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(context, "Delete operation canceled", Toast.LENGTH_SHORT).show();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        calculateTotal();

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private void calculateTotal() {
        totalAmount = 0;
        totalQty = 0;
        pname = "";

        for (MyCartModel cartModel : list) {
            totalAmount += cartModel.getTotalPrice();
            totalQty += cartModel.getTotalQuantity();
            pname += cartModel.getProductName() + " - "+cartModel.getTotalQuantity()+",  ";
        }

        if (list.size() > 0) {
            pname = pname.substring(0, pname.length() - 2);
        }

        Intent intent = new Intent("MyTotalAmount");
        intent.putExtra("totalAmount", totalAmount);
        intent.putExtra("pname", pname);
        intent.putExtra("Qty", totalQty);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, price, totalQuantity;
        ImageView img;
        Button removeCart;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.product_name);
            price = itemView.findViewById(R.id.product_price);
            img = itemView.findViewById(R.id.product_image);
            removeCart = itemView.findViewById(R.id.btn_remove);
            totalQuantity =itemView.findViewById(R.id.product_quantity);
        }
    }


}
