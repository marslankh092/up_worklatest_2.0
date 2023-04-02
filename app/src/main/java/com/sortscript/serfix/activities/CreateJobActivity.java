package com.sortscript.serfix.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.sortscript.serfix.Helper;
import com.sortscript.serfix.ModelForFirebase;
import com.sortscript.serfix.SignIn;
import com.sortscript.serfix.databinding.ActivityCreateProductBinding;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class CreateJobActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    ActivityCreateProductBinding binding;
    
    String title, description, postedDateTime, lastDate, salary,
    status, jobType = "Developer", location, jobPostedBy, contactNo;

    String[] service = {"Designer", "Developer"};
    public static final int PICK_IMAGE = 1;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    Uri uri = null;
    DatabaseReference reference;
    StorageReference storage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateProductBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        storage = FirebaseStorage.getInstance().getReference();
        binding.productImage.setOnClickListener(view -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
        });

        binding.save.setOnClickListener(view -> {
            if (uri==null){
                Toast.makeText(this, "Enter Your Job Image", Toast.LENGTH_SHORT).show();
            }else if(binding.productName.getText().toString().equals("")){
                binding.productName.setError("Enter Job Title");
            }else if(binding.productDescription.getText().toString().equals("")){
                binding.productDescription.setError("Enter Description");
            }else if(binding.Salary.getText().toString().equals("")){
                binding.Salary.setError("Enter Salary");
            }else if(binding.lastDate.getText().toString().equals("")){
                binding.lastDate.setError("Enter Last Date");
            }else {
                uploading(uri);
            }

        });

        binding.spinner.setOnItemSelectedListener(this);

        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, service);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        binding.spinner.setAdapter(aa);

    }

    public void put_items_in_firebase(Uri urii) {
        Date todaysdate = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HHmmss");
        postedDateTime = format.format(todaysdate);

        reference = FirebaseDatabase.getInstance().getReference().child("UpWork").child("Jobs");
        String key = reference.push().getKey();
        HashMap<String, Object> map = new HashMap<>();
        map.put("Title", binding.productName.getText().toString().trim());
        map.put("Description", binding.productDescription.getText().toString().trim());
        map.put("Salary", binding.Salary.getText().toString().trim());
        map.put("LastDate", binding.lastDate.getText().toString().trim());
        map.put("DatePosted",postedDateTime);
        map.put("Status", "Active");
        ModelForFirebase model = Helper.getUser(CreateJobActivity.this);
        map.put("Location", model.getAddress());
        map.put("PostedBy", model.getUserName());
        map.put("ContactNo", model.getPhoneNumber());
        map.put("ImageUrl", urii.toString());
        map.put("Key", key);

        assert key != null;
        reference.child(key).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(CreateJobActivity.this, "set", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(CreateJobActivity.this,ViewProductsActivity.class));
                finish();
            }
        });



    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        uri = data.getData();
        if(uri!=null){
            if (requestCode == PICK_IMAGE) {
                binding.productImage.setImageURI(uri);
                binding.productText.setVisibility(View.GONE);
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {


            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void uploading(Uri uri) {

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please Wait");
        progressDialog.setMessage("Creating Job....");
        progressDialog.show();

        StorageReference file = storage.child(System.currentTimeMillis() + "," + getfileextension(uri));
        file.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                file.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri urii) {

                        put_items_in_firebase(urii);
                        progressDialog.dismiss();

                    }
                });
            }

        });
    }

    private String getfileextension(Uri uri) {
        ContentResolver resolver = CreateJobActivity.this.getApplicationContext().getContentResolver();
        MimeTypeMap map = MimeTypeMap.getSingleton();
        return map.getExtensionFromMimeType(resolver.getType(uri));
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        jobType = service[position];
        Toast.makeText(getApplicationContext(), service[position], Toast.LENGTH_LONG).show();

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}

