package com.sortscript.serfix.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sortscript.serfix.AdminPenal;
import com.sortscript.serfix.Helper;
import com.sortscript.serfix.ModelForFirebase;
import com.sortscript.serfix.SignIn;
import com.sortscript.serfix.databinding.ActivitySplashScreenBinding;

public class SplashScreenActivity extends AppCompatActivity {
    ActivitySplashScreenBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        RequestingLocPermission();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("UpWork").child("UserDetail");

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            reference.orderByChild("UID").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    if (snapshot.exists()) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            ModelForFirebase model = dataSnapshot.getValue(ModelForFirebase.class);
                            if (model.getActive_user().equals("Yes")) {
                                if(model.getUserType().equals("User")){
                                    startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
                                }else{
                                    startActivity(new Intent(SplashScreenActivity.this, AdminPenal.class));
                                }
                                Helper.setUser(SplashScreenActivity.this,model);
                                finish();
                            } else {
                                Toast.makeText(SplashScreenActivity.this, "Waiting For Your Account Activation", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } else {
                        Toast.makeText(SplashScreenActivity.this, "Not Found", Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    startActivity(new Intent(SplashScreenActivity.this, SignIn.class));
                    finish();
                }
            }, 43);

        }


    }

    private void RequestingLocPermission() {
        try {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, SignIn.class));
        this.finish();
    }
}
