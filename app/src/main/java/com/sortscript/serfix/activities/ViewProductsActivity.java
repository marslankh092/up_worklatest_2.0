package com.sortscript.serfix.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sortscript.serfix.Helper;
import com.sortscript.serfix.databinding.ActivityViewProductsBinding;
import com.sortscript.serfix.modela.JobModel;
import com.sortscript.serfix.adapters.ViewProductsAdapter;

import java.util.ArrayList;

public class ViewProductsActivity extends AppCompatActivity implements ViewProductsAdapter.ItemClickListener {
    ActivityViewProductsBinding binding;
    String UID = "";
    ArrayList<JobModel> arraylist = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityViewProductsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        firebaseData();
        binding.recyclerview.setLayoutManager(new GridLayoutManager(ViewProductsActivity.this,2));


    }
    public void firebaseData() {

        ProgressDialog progressDialog = new ProgressDialog(ViewProductsActivity.this);
        progressDialog.setTitle("Please Wait");
        progressDialog.setMessage("Getting Data....");
        progressDialog.show();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("UpWork").child("Jobs");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                arraylist.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    JobModel Model = snapshot.getValue(JobModel.class);
                    arraylist.add(Model);

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
        binding.recyclerview.setAdapter(new ViewProductsAdapter(arraylist,ViewProductsActivity.this,this));
    }

    @Override
    public void delete(JobModel model) {
        delete_firebaseData(model.getKey());
        Toast.makeText(this, "Item Deleted", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void details(JobModel model) {
        Helper.setJob(this, model);
        startActivity(new Intent(this, JobsDetailsActivity.class));
    }


    public void delete_firebaseData(String key) {

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please Wait");
        progressDialog.setMessage("Removing Item....");
        progressDialog.show();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("UpWork").child("Jobs").child(key);
        ref.removeValue();
        setAdapter();
        progressDialog.dismiss();

    }
}