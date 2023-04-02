package com.sortscript.serfix;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sortscript.serfix.activities.MainActivity;
import com.sortscript.serfix.activities.SignUp;
import com.sortscript.serfix.databinding.ActivitySignInBinding;
import com.sortscript.serfix.modela.CartModel;

import java.util.ArrayList;

public class SignIn extends AppCompatActivity {
    ActivitySignInBinding binding;
    FirebaseAuth firebaseAuth;
    ArrayList<ModelForFirebase> arrayList = new ArrayList<>();
    String check = "Yes";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();
        RequestingwritePermission();


        binding.MoveOnSignUp.setOnClickListener(view -> {

            startActivity(new Intent(SignIn.this, SignUp.class));
            finish();

        });

        binding.LoginNow.setOnClickListener(view -> {
            if (binding.Email.getText().toString().isEmpty()) {
                binding.Email.setError("Detail in Empty*");
            } else if (binding.Password.getText().toString().isEmpty()) {
                binding.Password.setError("Detail in Empty*");
            }
//            else if (binding.Email.getText().toString().equals("jani@gmail.com") && binding.Password.getText().toString().equals("782600")) {
//                startActivity(new Intent(SignIn.this, AdminPenal.class));
//                finish();
//            }
            else {
                CheckAccountIsActive();
            }
        });

        binding.ForgetPassword.setOnClickListener(view -> {
            startActivity(new Intent(SignIn.this, ForgetPassword.class));

        });
    }

    private void CheckAccountIsActive() {

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please Wait");
        progressDialog.setMessage("Login....");
        progressDialog.show();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("UpWork").child("UserDetail");

        reference.orderByChild("Email").equalTo(binding.Email.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() != null) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                        String Email = binding.Email.getText().toString();
                        String Pass = binding.Password.getText().toString();
                        firebaseAuth.signInWithEmailAndPassword(Email, Pass).addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("UpWork").child("UserDetail");
                                databaseReference.orderByChild("UID").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        Log.d("checkSignIn", "onDataChange: "+snapshot);
                                        if (snapshot.exists()) {
                                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                ModelForFirebase model = dataSnapshot.getValue(ModelForFirebase.class);
                                                if (model.getActive_user().equals("Yes")) {
                                                    if(model.getUserType().equals("User")){
                                                        startActivity(new Intent(SignIn.this, MainActivity.class));
                                                    }else{
                                                        startActivity(new Intent(SignIn.this, AdminPenal.class));
                                                    }
                                                    Helper.setUser(SignIn.this,model);
                                                    finish();
                                                } else {
                                                    Toast.makeText(SignIn.this, "Waiting For Your Account Activation", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        } else {
                                            Toast.makeText(SignIn.this, "Not Found", Toast.LENGTH_SHORT).show();
                                        }

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });


                            } else {
                                Toast.makeText(SignIn.this, "" + task.getException(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    progressDialog.dismiss();

                } else {
                    progressDialog.dismiss();
                    Snackbar.make(binding.LoginPage, "First Register Your Self", Snackbar.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void RequestingwritePermission() {
        try {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_CONTACTS}, 101);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestingreadPermission();
    }
    private void RequestingreadPermission() {
        try {
//            RequestingwritePermission();
            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_CONTACTS}, 101);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}