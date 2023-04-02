package com.sortscript.serfix.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sortscript.serfix.ModelForFirebase;
import com.sortscript.serfix.R;
import com.sortscript.serfix.adapters.RequestAdapter;
import com.sortscript.serfix.databinding.ActivityRequestsBinding;

import java.util.ArrayList;

public class RequestsActivity extends AppCompatActivity implements RequestAdapter.ItemClickListener {
    ActivityRequestsBinding binding;
    ArrayList<ModelForFirebase> arrayList = new ArrayList<>();
    ArrayList<ModelForFirebase> request = new ArrayList<>();
    TextView name, phone_number, email, address, cnic, city, service_type;
    CardView reject, accept;
    ModelForFirebase modelForFirebase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRequestsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.recyclerview.setLayoutManager(new LinearLayoutManager(RequestsActivity.this));
        firebaseData();

    }

    public void firebaseData() {

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please Wait");
        progressDialog.setMessage("Getting Data....");
        progressDialog.show();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("UpWork").child("UserDetail");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                arrayList.clear();
                request.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ModelForFirebase Model = snapshot.getValue(ModelForFirebase.class);
                    arrayList.add(Model);
                }
                for(int i=0 ;i<arrayList.size();i++){

                 if(arrayList.get(i).getActive_user().equals("No")){
                     request.add(arrayList.get(i));
                 }
                }
                setAdapter();
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
    }

    private void setAdapter() {

        binding.recyclerview.setAdapter(new RequestAdapter(request, this, this));
    }

    @Override
    public void request_show(ModelForFirebase model) {
        modelForFirebase=model;
        open_alert_box(model.getUID());

    }

    private void open_alert_box(String uid) {
        final Dialog dialog = new Dialog(RequestsActivity.this);
        dialog.setContentView(R.layout.request_alert_box);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        name = dialog.findViewById(R.id.request_name);
        email = dialog.findViewById(R.id.request_email);
        phone_number = dialog.findViewById(R.id.request_phone_alert);
        address = dialog.findViewById(R.id.request_address);
        cnic = dialog.findViewById(R.id.request_cnic);
        city = dialog.findViewById(R.id.request_city);
        service_type = dialog.findViewById(R.id.request_service_type);
        reject = dialog.findViewById(R.id.request_reject);
        accept = dialog.findViewById(R.id.request_accept);

        name.setText(modelForFirebase.getUserName());
        email.setText(modelForFirebase.getEmail());
        phone_number.setText(modelForFirebase.getPhoneNumber());
        address.setText(modelForFirebase.getAddress());
        cnic.setText(modelForFirebase.getUserType());
        city.setText(modelForFirebase.getCity());
        service_type.setText(modelForFirebase.getServiceType());
        accept.setOnClickListener(view -> {
            save_to_firebase("Yes",uid);
            dialog.dismiss();

        });
        reject.setOnClickListener(view -> {
//            save_to_firebase("Reject",uid);
            dialog.dismiss();
        });


        dialog.setCancelable(true);
        dialog.show();
    }

    private void save_to_firebase(String option,String uid) {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please Wait");
        progressDialog.setMessage("Saving Information....");
        progressDialog.show();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("UpWork").child("UserDetail").child(uid);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ref.child("Active_user").setValue(option);
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}