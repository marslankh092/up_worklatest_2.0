package com.sortscript.serfix.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.sortscript.serfix.databinding.ActivityPendingBinding;
import com.sortscript.serfix.databinding.ActivitySignInBinding;

public class PendingActivity extends AppCompatActivity {
    ActivityPendingBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPendingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}