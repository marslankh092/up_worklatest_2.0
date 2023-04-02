package com.sortscript.serfix.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sortscript.serfix.modela.JobModel;
import com.sortscript.serfix.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.Holder>{
    ArrayList<JobModel> arrayList;
    Context context;
    ItemClickListener itemClickListener;

    public MyAdapter(ArrayList<JobModel> arrayList, Context context, ItemClickListener itemClickListener) {
        this.arrayList = arrayList;
        this.context = context;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public MyAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater =LayoutInflater.from(parent.getContext());
        View view =inflater.inflate(R.layout.item_firebase,parent,false);
        return new MyAdapter.Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter.Holder holder, int position) {


        Picasso.get().load(arrayList.get(position).getImageUrl()).into(holder.product_image);
        holder.name.setText(arrayList.get(position).getTitle());
        holder.price_product.setText("PKR "+arrayList.get(position).getSalary());
        if(arrayList.get(position).getApplied()!=null) {
            if (arrayList.get(position).getApplied()) {
                holder.name.setTextColor(Color.CYAN);
            } else {
                holder.name.setTextColor(Color.RED);
            }
        }


        holder.itemView.setOnClickListener(view -> {
            itemClickListener.update(arrayList.get(position));
        });

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class Holder extends RecyclerView.ViewHolder
    {
        ImageView product_image;
        TextView name,price_product;

        public Holder(@NonNull View itemView) {
            super(itemView);
            product_image=itemView.findViewById(R.id.product_image_my_adapter);
            name=itemView.findViewById(R.id.name);
            price_product=itemView.findViewById(R.id.price_product);


        }
    }

    public interface ItemClickListener{
        public void update(JobModel model);

    }
}
