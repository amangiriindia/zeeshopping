package com.example.amzoodmart.adapters;

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
import com.example.amzoodmart.R;
import com.example.amzoodmart.models.MyCartModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class MyCartAdapter extends RecyclerView.Adapter<MyCartAdapter.ViewHolder> {
    Context context;
    List<MyCartModel> list;
    FirebaseFirestore firestore;
    FirebaseAuth auth;

    int totalAmount =0;
    String pname ="";

    public MyCartAdapter(Context context, List<MyCartModel> list) {
        this.context = context;
        this.list = list;
        auth =FirebaseAuth.getInstance();
        firestore =FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public MyCartAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.my_cart_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyCartAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {

       Glide.with(context).load(list.get(position).getProductImgurl()).into(holder.img);
        holder.price.setText(list.get(position).getProductPrice());
        holder.name.setText(list.get(position).getProductName());
        holder.totalQuantity.setText(list.get(position).getTotalQuantity());

        holder.removeCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create an instance of the AlertDialog.Builder using the custom style
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
                                        // Document deleted successfully

                                        // Remove the item from the list
                                        list.remove(position);
                                        notifyItemRemoved(position);
                                        notifyItemRangeChanged(position, list.size());
                                        Toast.makeText(context, "Cart item deleted successfully", Toast.LENGTH_SHORT).show();
                                    }
                                });

                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // User canceled the delete operation
                        Toast.makeText(context, "Delete operation canceled", Toast.LENGTH_SHORT).show();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });
        // Total Amount pass to cart Activity
     totalAmount =totalAmount +list.get(position).getTotalPrice();
     pname =pname +(list.get(position).getProductName()+", ");

       Intent intent =new Intent("MyTotalAmount");
       intent.putExtra("totalAmount",totalAmount);
       intent.putExtra("pname",pname);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    public static class  ViewHolder extends RecyclerView.ViewHolder {
        TextView name,price,totalQuantity;
        ImageView img;
        Button removeCart;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name =itemView.findViewById(R.id.product_name);
            price =itemView.findViewById(R.id.product_price);
            img =itemView.findViewById(R.id.product_image);
            removeCart =itemView.findViewById(R.id.btn_remove);
            totalQuantity =itemView.findViewById(R.id.product_quantity);
        }
    }

}
