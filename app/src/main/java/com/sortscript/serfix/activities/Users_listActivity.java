package com.sortscript.serfix.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.sortscript.serfix.Helper;
import com.sortscript.serfix.ModelForFirebase;
import com.sortscript.serfix.R;
import com.sortscript.serfix.adapters.UserListAdapter;
import com.sortscript.serfix.databinding.ActivityUsersListBinding;

import java.util.ArrayList;

public class Users_listActivity extends AppCompatActivity implements UserListAdapter.ItemClickListener {

    ActivityUsersListBinding binding;
    ArrayList<ModelForFirebase> arrayList= new ArrayList<>();
    ArrayList<ModelForFirebase> user= new ArrayList<>();
    ModelForFirebase modelForFirebase;
    TextView name, phone_number, email, address, cnic, city, service_type,changing_text;
    CardView reject, accept;
    LinearLayout button_layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUsersListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.recyclerview.setLayoutManager(new LinearLayoutManager(Users_listActivity.this));
        firebaseData();

    }

    public void firebaseData() {

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please Wait");
        progressDialog.setMessage("Getting Data....");
        progressDialog.show();

        String type = "User";
        if(Helper.getUser(this).getUserType().equals("User")){
            type = "Admin";
        }else{
            type = "User";
        }
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("UpWork")
                .child("UserDetail");
        Query query = ref.orderByChild("UserType").equalTo(type);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                arrayList.clear();
                user.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ModelForFirebase Model = snapshot.getValue(ModelForFirebase.class);
                    arrayList.add(Model);
                }
                for(int i=0;i<arrayList.size();i++){
                    if(arrayList.get(i).getActive_user().equals("Yes")){
                        if(arrayList.get(i).getUserType().equals("User")){
                            user.add(arrayList.get(i));
                        }

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
        binding.recyclerview.removeAllViews();
        binding.recyclerview.setAdapter(new UserListAdapter(user, this,this));
    }

    @Override
    public void request_show(ModelForFirebase model) {
        modelForFirebase=model;
        open_alert_box(model.getUID());
    }

    private void open_alert_box(String uid) {
        final Dialog dialog = new Dialog(Users_listActivity.this);
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
        button_layout = dialog.findViewById(R.id.button_layout);
        changing_text = dialog.findViewById(R.id.changing_text);

        name.setText(modelForFirebase.getUserName());
        changing_text.setText("Block");
        email.setText(modelForFirebase.getEmail());
        phone_number.setText(modelForFirebase.getPhoneNumber());
        address.setText(modelForFirebase.getAddress());
        cnic.setText(modelForFirebase.getUserType());
        city.setText(modelForFirebase.getCity());
        service_type.setText(modelForFirebase.getServiceType());
        accept.setOnClickListener(view -> {

            save_to_firebase("No",uid);
            dialog.dismiss();

        });
        reject.setOnClickListener(view -> {

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

        DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference("UpWork").child("UserDetail").child(uid);
        ref1.removeValue();

//        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("UpWork").child("UserDetail").child(uid);
//        ref.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                ref.child("Active_user").setValue(option);
//                progressDialog.dismiss();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });

    }
}