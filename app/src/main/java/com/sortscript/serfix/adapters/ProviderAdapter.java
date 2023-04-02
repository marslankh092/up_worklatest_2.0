package com.sortscript.serfix.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.sortscript.serfix.activities.MainActivity;
import com.sortscript.serfix.ModelForFirebase;
import com.sortscript.serfix.R;
import com.sortscript.serfix.fragments.ChatFragment;

import java.util.ArrayList;

public class ProviderAdapter extends RecyclerView.Adapter<ProviderAdapter.Holder> {
    ArrayList<ModelForFirebase> arrayList;
    Context context;

    public ProviderAdapter(ArrayList<ModelForFirebase> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ProviderAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.user_list_item, parent, false);
        return new ProviderAdapter.Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProviderAdapter.Holder holder, int position) {
        Log.d("checkUid", "onBindViewHolder: " + arrayList.get(position).getUID());
        holder.name.setText(arrayList.get(position).getUserName());
        holder.ph_number.setText(arrayList.get(position).getPhoneNumber());
        holder.service.setText(arrayList.get(position).getServiceType());
        holder.itemView.setOnClickListener(view -> {
            FragmentTransaction ft1 = ((MainActivity) context).getSupportFragmentManager().beginTransaction();
            ft1.replace(R.id.frame_layout, new ChatFragment(arrayList.get(position)));
            ft1.commit();

        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class Holder extends RecyclerView.ViewHolder {
        TextView name, email, ph_number, address, cnic, city, service;

        public Holder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.user_name);
            ph_number = itemView.findViewById(R.id.user_phone);
            service = itemView.findViewById(R.id.user_service_type);

        }
    }

    interface ItemClickListener {
        public void request_show(ModelForFirebase model);

    }


}
