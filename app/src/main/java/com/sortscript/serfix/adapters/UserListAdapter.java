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

public class UserListAdapter  extends RecyclerView.Adapter<UserListAdapter.Holder>{
    ArrayList<ModelForFirebase> arrayList;
    Context context;
    UserListAdapter.ItemClickListener itemClickListener;

    public UserListAdapter(ArrayList<ModelForFirebase> arrayList, Context context, UserListAdapter.ItemClickListener itemClickListener) {
        this.arrayList = arrayList;
        this.context = context;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public UserListAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater =LayoutInflater.from(parent.getContext());
        View view =inflater.inflate(R.layout.user_list_item,parent,false);
        return new UserListAdapter.Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserListAdapter.Holder holder, int position) {

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
        TextView name,email,ph_number,address,cnic,city,service;

        public Holder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.user_name);
            ph_number=itemView.findViewById(R.id.user_phone);
            service=itemView.findViewById(R.id.user_service_type);

        }
    }

    public interface ItemClickListener{
        public void request_show(ModelForFirebase model);

    }


}
