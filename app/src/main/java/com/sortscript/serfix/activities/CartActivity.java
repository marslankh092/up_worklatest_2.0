package com.sortscript.serfix.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sortscript.serfix.adapters.CartAdapter;
import com.sortscript.serfix.databinding.ActivityCartBinding;
import com.sortscript.serfix.modela.CartModel;
import com.sortscript.serfix.R;

import java.util.ArrayList;

public class CartActivity extends AppCompatActivity implements CartAdapter.ItemClickListener {

    ActivityCartBinding binding;
    ArrayList<CartModel> arraylist = new ArrayList<>();
    String UID = "";
    DatabaseReference ref=FirebaseDatabase.getInstance().getReference();
    int bill = 0, items = 0;
    TextInputEditText name, phone_number,address;
    String name_str = "",ph_str = "",address_str ="";
    CardView cancel_button,place_order_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.recyclerview.setLayoutManager(new LinearLayoutManager(CartActivity.this));
        firebaseData();
        binding.placeOrder.setOnClickListener(view -> {
            dialogBoxForDetails();
        });

    }

    private void dialogBoxForDetails() {
        final Dialog dialog = new Dialog(CartActivity.this);
        dialog.setContentView(R.layout.place_order_deatils);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        name= dialog.findViewById(R.id.name_for_placeOrder);
        phone_number= dialog.findViewById(R.id.phone_number_for_placeOrder);
        address= dialog.findViewById(R.id.address_for_placeOrder);
        place_order_button= dialog.findViewById(R.id.place_order_button_for_placeOrder);
        cancel_button= dialog.findViewById(R.id.cancel_button_for_placeOrder);

        place_order_button.setOnClickListener(view -> {
            if(name.getText().toString().equals("")){
                name.setError("Please Provide Name");
            }else if(phone_number.getText().toString().equals("")){
                phone_number.setError("Please Provide Phone Number");
            }else if(phone_number.getText().toString().length()!=13&&phone_number.getText().toString().length()!=11){
                phone_number.setError("Not Valid Phone Number");
            }else if(address.getText().toString().equals("")){
                address.setError("Please Provide Address");
            }else{
                if(arraylist.size()>0){
                    save_to_firebase();
                }else {
                    Toast.makeText(this, "No Order To deliver", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }
        });
        cancel_button.setOnClickListener(view -> {
            dialog.dismiss();
        });


        dialog.setCancelable(false);
        dialog.show();

    }


    private void save_to_firebase() {


        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please Wait");
        progressDialog.setMessage("Saving Information....");
        progressDialog.show();
        String UID= FirebaseAuth.getInstance().getCurrentUser().getUid();
        String Private = ref.push().getKey();
        ref = FirebaseDatabase.getInstance().getReference("UpWork").child("Orders").child(UID).child(Private);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ref.child("Name").setValue(name.getText().toString());
                ref.child("Phone").setValue(phone_number.getText().toString());
                ref.child("Address").setValue(address.getText().toString());
                ref.child("Product").setValue("Multiple");
                ref.child("Description").setValue("");
                ref.child("Price").setValue(String.valueOf(bill));
                ref.child("Quantity").setValue(String.valueOf(items));
                ref.child("Image").setValue(arraylist.get(0).getImage());
                ref.child("Private").setValue(Private);
                ref.child("TotalAmount").setValue(String.valueOf(bill));
                progressDialog.dismiss();
                Toast.makeText(CartActivity.this, "Order Created", Toast.LENGTH_SHORT).show();
                Empty_card();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    public void firebaseData() {

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please Wait");
        progressDialog.setMessage("Getting Data....");
        progressDialog.show();

        UID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("UpWork").child("Carts").child(UID);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                arraylist.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    CartModel Model = snapshot.getValue(CartModel.class);
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
    public void delete_firebaseData(String key) {

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please Wait");
        progressDialog.setMessage("Removing Item....");
        progressDialog.show();

        UID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("UpWork").child("Carts").child(UID).child(key);
        ref.removeValue();
        setAdapter();
        progressDialog.dismiss();

    }
    public void Empty_card() {

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please Wait");
        progressDialog.setMessage("Removing Item....");
        progressDialog.show();

        UID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("UpWork").child("Carts").child(UID);
        ref.removeValue();
        setAdapter();
        progressDialog.dismiss();
        finish();

    }


    private void setAdapter() {
        int quantity = 0, amount = 0;
        items = 0;
        bill = 0;
        for (int i = 0; i < arraylist.size(); i++) {
            try {
                quantity = Integer.parseInt(arraylist.get(i).getQuantity());
                amount = Integer.parseInt(arraylist.get(i).getTotalAmount());
                items = items + quantity;
                bill = bill + amount;
            } catch (Exception exception) {

            }

        }
        binding.totalItem.setText("" + items);
        binding.totalBill.setText("" + bill);

        binding.recyclerview.setAdapter(new CartAdapter(arraylist, this, this));
    }

    @Override
    public void delete(CartModel model) {
        delete_firebaseData(model.getPrivate());

    }
}