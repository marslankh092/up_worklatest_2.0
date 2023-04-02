package com.sortscript.serfix.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sortscript.serfix.GpsTracker;
import com.sortscript.serfix.R;
import com.sortscript.serfix.SignIn;
import com.sortscript.serfix.databinding.ActivitySignUpBinding;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class SignUp extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    String[] service = {"Multiple","Designer", "Developer"};
    String user_type = "User", service_type = "Multiple", city = "", address = "";
    FirebaseAuth firebaseAuth;
    Double latitude, longitude;
    String active = "";

    protected LocationManager locationManager;
    private ActivitySignUpBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.address.setEnabled(false);
        firebaseAuth = FirebaseAuth.getInstance();

        RequestingLocPermission();


        binding.search.setOnClickListener(view -> {

            binding.address.setEnabled(true);
            getLocation();
        });


        binding.MoveOnSignIn.setOnClickListener(view -> {
            startActivity(new Intent(SignUp.this, SignIn.class));
            finish();
        });


        binding.spinner.setOnItemSelectedListener(this);

        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, service);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        binding.spinner.setAdapter(aa);

        binding.RegisterNow.setOnClickListener(view -> {

            if (binding.Name.getText().toString().isEmpty()) {
                binding.Name.setError("Detail in Empty*");
            } else if (binding.Email.getText().toString().isEmpty()) {
                binding.Email.setError("Detail in Empty*");
            } else if (binding.Password.getText().toString().isEmpty()) {
                binding.Password.setError("Detail in Empty*");
            } else if (binding.ConfirmPassword.getText().toString().isEmpty()) {
                binding.ConfirmPassword.setError("Detail in Empty*");
            } else if (binding.phoneNumber.getText().toString().isEmpty()) {
                binding.phoneNumber.setError("Detail in Empty*");
            } else if (binding.phoneNumber.getText().toString().length() != 11 && binding.phoneNumber.getText().toString().length() != 13) {
                binding.phoneNumber.setError("Invalid Number");
            } else if (binding.city.getText().toString().isEmpty()) {
                binding.city.setError("Detail in Empty*");
            } else if (binding.address.getText().toString().isEmpty()) {
                binding.address.setError("Detail in Empty*");
            }
            city = binding.city.getText().toString();
            address = binding.address.getText().toString();
            active = "Yes";
            SendRequestForRegistration();

        });


    }


    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();

        switch (view.getId()) {
            case R.id.User:
                if (checked)
                    user_type = "User";
                binding.extraFields.setVisibility(View.VISIBLE);
                break;
            case R.id.Service_Provider:
                if (checked)
                    user_type = "Admin";
                binding.extraFields.setVisibility(View.VISIBLE);
                break;
            default:
                user_type = "";
                binding.extraFields.setVisibility(View.GONE);
                break;

        }
    }

    private void SendRequestForRegistration() {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please Wait");
        progressDialog.setMessage("Creating Account....");
        progressDialog.show();


        RelativeLayout LayoutShow;
        LayoutShow = findViewById(R.id.LayoutShow);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("UpWork").child("UserDetail");


        firebaseAuth.createUserWithEmailAndPassword(binding.Email.getText().toString(), binding.Password.getText().toString()).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {

                String UID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                String Private = reference.push().getKey();

                HashMap<String, Object> map = new HashMap<>();
                map.put("UserName", binding.Name.getText().toString());
                map.put("Email", binding.Email.getText().toString());
                map.put("Password", binding.Password.getText().toString());
                map.put("PhoneNumber", binding.phoneNumber.getText().toString());
                map.put("City", city);
                map.put("Address", address);
                map.put("ServiceType", service_type);
                map.put("UserType", user_type);
                map.put("UID", UID);
                map.put("Longitude", longitude);
                map.put("Latitude", latitude);
                map.put("Active_user", active);
                map.put("Private", Private);


                reference.child(UID).setValue(map).addOnCompleteListener(task1 -> {
                    if (task1.isSuccessful()) {
                        Toast.makeText(this, "Registration Request Send", Toast.LENGTH_SHORT).show();
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(SignUp.this, SignIn.class));
                        finish();
                        progressDialog.dismiss();
                    } else {
                        Snackbar.make(LayoutShow, "Check Your Connection", Snackbar.LENGTH_LONG).show();
                        progressDialog.dismiss();
                    }

                });

            } else {
                Snackbar.make(LayoutShow, "Registration Request Sending Fail", Snackbar.LENGTH_LONG).show();
                progressDialog.dismiss();
            }
        });


    }

    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {

        service_type = service[position];
        Toast.makeText(getApplicationContext(), service[position], Toast.LENGTH_LONG).show();

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }

    public void getLocation() {
        GpsTracker gpsTracker = new GpsTracker(SignUp.this);
        if (gpsTracker.canGetLocation()) {
            latitude = gpsTracker.getLatitude();
            longitude = gpsTracker.getLongitude();
            String address = getCompleteAddressString(latitude, longitude);
            binding.address.setText(address);
        } else {
            gpsTracker.showSettingsAlert();
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


    private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();
                //binding.city.startActionMode(returnedAddress.getLocality().);
                //                currentLocEtId.setText("" + strAdd);
                Toast.makeText(this, "" + strAdd, Toast.LENGTH_SHORT).show();
            } else {
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strAdd;
    }

}