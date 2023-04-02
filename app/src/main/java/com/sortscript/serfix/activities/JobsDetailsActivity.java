package com.sortscript.serfix.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sortscript.serfix.Helper;
import com.sortscript.serfix.databinding.ActivityProductDetailAndSaleBinding;
import com.sortscript.serfix.modela.JobModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

public class JobsDetailsActivity extends AppCompatActivity {
    ActivityProductDetailAndSaleBinding binding;
    JobModel jobModel;
    DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProductDetailAndSaleBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        jobModel = Helper.getJob(this);
        if (jobModel == null) {
            Toast.makeText(this, "Job descriptions is not available",
                    Toast.LENGTH_SHORT).show();
            finish();
            return;
        } else {
            Helper.setJob(this, null);
        }

        Log.e("TAG", jobModel.toString());

        if (Helper.getUser(this).getUserType().equals("User")) {
            binding.btnApplyRemove.setText("Apply");
            binding.btnApplyRemove.setTag("Apply");
        } else {
            binding.btnApplyRemove.setText("Remove");
            binding.btnApplyRemove.setTag("Remove");
        }

        binding.btnApplyRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getTag().equals("Remove")) {

                } else { //apply
                    applyForJobByUser();
                }
            }
        });

    }

    public void applyForJobByUser() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("UpWork").child("AppliedJobs");
        String key = reference.push().getKey();
        HashMap<String, Object> map = new HashMap<>();
        map.put("AdminPhone", jobModel.getContactNo());
        map.put("UserKey", Helper.getUser(this).getUID());
        map.put("JobKey", jobModel.getKey());
        map.put("Key", key);

        reference.child(key).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    binding.btnApplyRemove.setVisibility(View.GONE);
                    Toast.makeText(JobsDetailsActivity.this, "Applied on this job", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(JobsDetailsActivity.this, "Job application failed", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    public void setJobUnAvailable() {

    }
}