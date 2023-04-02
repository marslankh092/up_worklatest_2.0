package com.sortscript.serfix.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sortscript.serfix.ModelForFirebase;
import com.sortscript.serfix.R;
import com.sortscript.serfix.databinding.ActivityMainBinding;
import com.sortscript.serfix.databinding.ActivityUserProfileBinding;

import java.util.ArrayList;

public class UserProfileActivity extends AppCompatActivity {
    ActivityUserProfileBinding binding;
    ArrayList<ModelForFirebase> user= new ArrayList<>();
    ArrayList<ModelForFirebase> arrayList= new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        firebaseData();
        binding.name.setEnabled(false);
        binding.address.setEnabled(false);
        binding.city.setEnabled(false);
        binding.phoneNumber.setEnabled(false);
        binding.save.setVisibility(View.GONE);
        RelativeLayout layout=findViewById(R.id.Layout);
        binding.editProfile.setOnClickListener(view -> {

            binding.name.setEnabled(true);
            binding.address.setEnabled(true);
            binding.city.setEnabled(true);
            binding.phoneNumber.setEnabled(true);
            binding.save.setVisibility(View.VISIBLE);
            Snackbar snackbar = Snackbar.make(layout, "Now You Can Edit Your Profile", Snackbar.LENGTH_LONG);
            snackbar.show();
        });
        binding.save.setOnClickListener(view -> {
            if(binding.name.equals("")){
                binding.name.setError("Enter Proper Name");
            }else if(binding.address.equals("")){
                binding.address.setError("Enter Proper address");
            }else if(binding.phoneNumber.equals("")){
                binding.phoneNumber.setError("Enter Proper phone Number");
            }else if(binding.phoneNumber.length()!=11&&binding.phoneNumber.length()!=13){
                binding.phoneNumber.setError("Enter Valid phone Number");
            }else if(binding.city.equals("")){
                binding.city.setError("Enter Valid city");
            }else{

                save_to_firebase();
            }
        });
    }

    private void save_to_firebase() {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please Wait");
        progressDialog.setMessage("Saving Information....");
        progressDialog.show();
        String UID= FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("UpWork").child("UserDetail").child(UID);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ref.child("Address").setValue(binding.address.getText().toString());
                ref.child("City").setValue(binding.city.getText().toString());
                ref.child("UserName").setValue(binding.name.getText().toString());
                ref.child("PhoneNumber").setValue(binding.phoneNumber.getText().toString());
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void firebaseData() {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("UpWork").child("UserDetail");
         reference.orderByChild("UID").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        ModelForFirebase model = dataSnapshot.getValue(ModelForFirebase.class);
                        binding.name.setText(model.getUserName());
                        binding.address.setText(model.getAddress());
                        binding.phoneNumber.setText(model.getPhoneNumber());
                        binding.city.setText(model.getCity());

                    }}


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}