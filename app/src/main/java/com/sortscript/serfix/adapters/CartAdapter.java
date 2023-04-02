package com.sortscript.serfix.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sortscript.serfix.modela.CartModel;
import com.sortscript.serfix.modela.JobModel;
import com.sortscript.serfix.R;

import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.Holder>{
    ArrayList<CartModel> arrayList;
    Context context;
    ArrayList<JobModel> arrayListnew=new ArrayList<>();
    CartAdapter.ItemClickListener itemClickListener;

    public CartAdapter(ArrayList<CartModel> arrayList, Context context, CartAdapter.ItemClickListener itemClickListener) {
        this.arrayList = arrayList;
        this.context = context;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public CartAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater =LayoutInflater.from(parent.getContext());
        View view =inflater.inflate(R.layout.cart_item,parent,false);
        return new CartAdapter.Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.Holder holder, int position) {

//        Picasso.get().load(arrayList.get(position).getImage()).into(holder.item_image);
        holder.item_name.setText(arrayList.get(position).getProduct());
        holder.item_price.setText(arrayList.get(position).getPrice());
        holder.item_quantity.setText(arrayList.get(position).getQuantity());
        holder.item_total_price.setText(arrayList.get(position).getTotalAmount());



        holder.delete_item.setOnClickListener(view -> {
            Toast.makeText(context, "Delete", Toast.LENGTH_SHORT).show();
            itemClickListener.delete(arrayList.get(position));
        });

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class Holder extends RecyclerView.ViewHolder
    {
        ImageView delete_item;
        TextView item_name,item_quantity,item_price,item_total_price;

        public Holder(@NonNull View itemView) {
            super(itemView);
            delete_item=itemView.findViewById(R.id.delete_item);
            item_name=itemView.findViewById(R.id.item_name);
            item_quantity=itemView.findViewById(R.id.item_quantity);
            item_price=itemView.findViewById(R.id.item_price);
            item_total_price=itemView.findViewById(R.id.item_total_price);

        }
    }

    public interface ItemClickListener{
        public void delete(CartModel model);

    }
}
