package com.sortscript.serfix.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sortscript.serfix.ModelForFirebase;
import com.sortscript.serfix.R;

import java.util.ArrayList;

public class RequestAdapter  extends RecyclerView.Adapter<RequestAdapter.Holder>{
    ArrayList<ModelForFirebase> arrayList;
    Context context;
    RequestAdapter.ItemClickListener itemClickListener;

    public RequestAdapter(ArrayList<ModelForFirebase> arrayList, Context context, RequestAdapter.ItemClickListener itemClickListener) {
        this.arrayList = arrayList;
        this.context = context;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public RequestAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater =LayoutInflater.from(parent.getContext());
        View view =inflater.inflate(R.layout.request_item,parent,false);
        return new RequestAdapter.Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestAdapter.Holder holder, int position) {


//        Picasso.get().load(arrayList.get(position).getProductImage()).into(holder.product_image);
        holder.name.setText(arrayList.get(position).getUserName());
        holder.ph_number.setText(arrayList.get(position).getPhoneNumber());
        holder.service.setText(arrayList.get(position).getServiceType());



        holder.itemView.setOnClickListener(view -> {
            itemClickListener.request_show(arrayList.get(position));
        });

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class Holder extends RecyclerView.ViewHolder
    {
//        ImageView product_image;
        TextView name,ph_number,service;

        public Holder(@NonNull View itemView) {
            super(itemView);
//            product_image=itemView.findViewById(R.id.product_image);
            name=itemView.findViewById(R.id.request_name);
            ph_number=itemView.findViewById(R.id.request_ph_number);
            service=itemView.findViewById(R.id.request_service);

        }
    }

    public interface ItemClickListener{
        public void request_show(ModelForFirebase model);

    }
}
