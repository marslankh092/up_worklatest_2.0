package com.sortscript.serfix.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sortscript.serfix.activities.MainActivity;
import com.sortscript.serfix.ModelForFirebase;
import com.sortscript.serfix.SignIn;
import com.sortscript.serfix.activities.CartActivity;
import com.sortscript.serfix.activities.UserProfileActivity;
import com.sortscript.serfix.databinding.FragmentAccountBinding;

import java.util.ArrayList;

public class AccountFragment extends Fragment {

    FragmentAccountBinding binding;

    ArrayList<ModelForFirebase> user= new ArrayList<>();
    ArrayList<ModelForFirebase> arrayList= new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAccountBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        firebaseData();
        binding.myAcount.setOnClickListener(view1 -> {
            Intent intent = new Intent(getContext(), UserProfileActivity.class);
            startActivity(intent);
        });
        binding.Logout.setOnClickListener(view1 -> {

            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(getContext(), SignIn.class));
            getActivity().finish();
        });
        binding.ShareApp.setOnClickListener(view1 -> {
            try {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "My application name");
                String shareMessage= "\nLet me recommend you this application\n\n";
                shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + getActivity().getPackageName() +"\n\n";
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                startActivity(Intent.createChooser(shareIntent, "choose one"));
            } catch(Exception e) {
                //e.toString();
            }
        });
        binding.RateUs.setOnClickListener(view1 -> {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getActivity().getPackageName())));
        });
        binding.myCart.setOnClickListener(view1 -> {
            Intent intent = new Intent(getContext(), CartActivity.class);
            startActivity(intent);
        });
        return view;
    }


    public void firebaseData() {

        String UID= FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("UpWork").child("UserDetail");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                arrayList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    ModelForFirebase Model = snapshot.getValue(ModelForFirebase.class);
                    arrayList.add(Model);

                }
                for(int i =0 ; i<arrayList.size();i++){
                    if(arrayList.get(i).getUID().equals(UID)){
                        user.add(arrayList.get(i));
                        binding.name.setText(user.get(0).getUserName());
                        return;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });


    }
    public void onBackPressed() {
        if (getParentFragmentManager().getBackStackEntryCount() > 0) {
            getParentFragmentManager().popBackStack();
        } else {
            startActivity(new Intent(getContext(), MainActivity.class));
        }
    }
}