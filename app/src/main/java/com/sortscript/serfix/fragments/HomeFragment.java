package com.sortscript.serfix.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sortscript.serfix.Helper;
import com.sortscript.serfix.adapters.MyAdapter;
import com.sortscript.serfix.databinding.FragmentHomeBinding;
import com.sortscript.serfix.modela.AppliedJob;
import com.sortscript.serfix.modela.JobModel;
import com.sortscript.serfix.activities.JobsDetailsActivity;

import java.util.ArrayList;

public class HomeFragment extends Fragment implements MyAdapter.ItemClickListener {

    FragmentHomeBinding binding;
    ArrayList<JobModel> arraylist = new ArrayList<>();
    ArrayList<AppliedJob> appliedJobs =  new ArrayList<>();

    @Override
    public View onCreateView (LayoutInflater inflater,
                              ViewGroup container,
                              Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        firebaseData();
        binding.recyclerview.setLayoutManager(new GridLayoutManager(getActivity(),2));


        return view;
    }

    ProgressDialog progressDialog;
    public void firebaseData() {
        progressDialog = new ProgressDialog(getActivity());
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
                checkForAppliedJobs();
                //setAdapter();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });


    }

    private void checkForAppliedJobs(){
        DatabaseReference ref = FirebaseDatabase.getInstance()
                .getReference("UpWork").child("AppliedJobs");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                appliedJobs.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    AppliedJob appliedJob = snapshot.getValue(AppliedJob.class);
                    appliedJobs.add(appliedJob);
                }
                for(JobModel jm: arraylist){
                    for(AppliedJob aj:appliedJobs){
                        if(jm.getKey().equals(aj.getJobKey()) &&
                                aj.getUserKey().equals(Helper.getUser(getContext()).getUID())) {
                            jm.setApplied(true);
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
        binding.recyclerview.setAdapter(new MyAdapter(arraylist,getActivity(),this));
    }

    @Override
    public void update(JobModel model) {
        Helper.setJob(getContext(), model);
        Intent intent = new Intent(getContext(), JobsDetailsActivity.class);
        //intent.putExtra("key",model.getKey());
        startActivity(intent);

    }
}