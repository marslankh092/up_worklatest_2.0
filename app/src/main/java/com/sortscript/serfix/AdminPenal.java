package com.sortscript.serfix;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.sortscript.serfix.activities.CreateJobActivity;
import com.sortscript.serfix.activities.RequestsActivity;
import com.sortscript.serfix.activities.Users_listActivity;
import com.sortscript.serfix.activities.ViewProductsActivity;
import com.sortscript.serfix.databinding.ActivityAdminPenalBinding;

public class AdminPenal extends AppCompatActivity {

    ActivityAdminPenalBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminPenalBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.addProducts.setOnClickListener(view -> {
            startActivity(new Intent(AdminPenal.this, CreateJobActivity.class));
        });

        binding.viewProducts.setOnClickListener(view -> {
            startActivity(new Intent(AdminPenal.this, ViewProductsActivity.class));
        });
        binding.userList.setOnClickListener(view -> {
            startActivity(new Intent(AdminPenal.this, Users_listActivity.class));
        });
        binding.request.setOnClickListener(view -> {
            startActivity(new Intent(AdminPenal.this, RequestsActivity.class));
        });


    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, SignIn.class));
        this.finish();
    }
}