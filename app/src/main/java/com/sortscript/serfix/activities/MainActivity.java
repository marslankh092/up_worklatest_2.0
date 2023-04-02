package com.sortscript.serfix.activities;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sortscript.serfix.GpsTracker;
import com.sortscript.serfix.ModelForFirebase;
import com.sortscript.serfix.R;
import com.sortscript.serfix.SignIn;
import com.sortscript.serfix.databinding.ActivityMainBinding;
import com.sortscript.serfix.fragments.AccountFragment;
import com.sortscript.serfix.fragments.HomeFragment;
import com.sortscript.serfix.fragments.ProvidersListFragment;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    double latitude, longitude;
    ActivityMainBinding binding;
    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    ArrayList<ModelForFirebase> arrayList = new ArrayList<>();
    String search_str = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getLocation();
        RequestingLocPermission();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        FragmentTransaction ft2 = getSupportFragmentManager().beginTransaction();
        ft2.replace(R.id.frame_layout, new HomeFragment());
        ft2.commit();
        binding.textHome.setVisibility(View.VISIBLE);
        binding.textMap.setVisibility(View.GONE);
        binding.textAccount.setVisibility(View.GONE);
        binding.frameLayout.setVisibility(View.VISIBLE);
        binding.textMessage.setVisibility(View.GONE);
        binding.mapFragment.setVisibility(View.GONE);


        binding.all.setOnClickListener(view -> {
            filter_makers("All");

            binding.all.setEnabled(false);
            binding.mechanic.setEnabled(true);
            binding.carTower.setEnabled(true);
            binding.fuelProvider.setEnabled(true);
            binding.driver.setEnabled(true);
            binding.all.setBackground(ContextCompat.getDrawable(this, R.drawable.selected_back));
            binding.mechanic.setBackground(ContextCompat.getDrawable(this, R.drawable.option_back));
            binding.carTower.setBackground(ContextCompat.getDrawable(this, R.drawable.option_back));
            binding.fuelProvider.setBackground(ContextCompat.getDrawable(this, R.drawable.option_back));
            binding.driver.setBackground(ContextCompat.getDrawable(this, R.drawable.option_back));

        });
        binding.mechanic.setOnClickListener(view -> {
            filter_makers("Mechanic");

            binding.all.setEnabled(true);
            binding.mechanic.setEnabled(false);
            binding.carTower.setEnabled(true);
            binding.fuelProvider.setEnabled(true);
            binding.driver.setEnabled(true);
            binding.all.setBackground(ContextCompat.getDrawable(this, R.drawable.option_back));
            binding.mechanic.setBackground(ContextCompat.getDrawable(this, R.drawable.selected_back));
            binding.carTower.setBackground(ContextCompat.getDrawable(this, R.drawable.option_back));
            binding.fuelProvider.setBackground(ContextCompat.getDrawable(this, R.drawable.option_back));
            binding.driver.setBackground(ContextCompat.getDrawable(this, R.drawable.option_back));
        });
        binding.carTower.setOnClickListener(view -> {
            filter_makers("Car Tower");

            binding.all.setEnabled(true);
            binding.mechanic.setEnabled(true);
            binding.carTower.setEnabled(false);
            binding.fuelProvider.setEnabled(true);
            binding.driver.setEnabled(true);
            binding.all.setBackground(ContextCompat.getDrawable(this, R.drawable.option_back));
            binding.mechanic.setBackground(ContextCompat.getDrawable(this, R.drawable.option_back));
            binding.carTower.setBackground(ContextCompat.getDrawable(this, R.drawable.selected_back));
            binding.fuelProvider.setBackground(ContextCompat.getDrawable(this, R.drawable.option_back));
            binding.driver.setBackground(ContextCompat.getDrawable(this, R.drawable.option_back));
        });
        binding.fuelProvider.setOnClickListener(view -> {
            filter_makers("Fuel Provider");

            binding.all.setEnabled(true);
            binding.mechanic.setEnabled(true);
            binding.carTower.setEnabled(true);
            binding.fuelProvider.setEnabled(false);
            binding.driver.setEnabled(true);
            binding.all.setBackground(ContextCompat.getDrawable(this, R.drawable.option_back));
            binding.mechanic.setBackground(ContextCompat.getDrawable(this, R.drawable.option_back));
            binding.carTower.setBackground(ContextCompat.getDrawable(this, R.drawable.option_back));
            binding.fuelProvider.setBackground(ContextCompat.getDrawable(this, R.drawable.selected_back));
            binding.driver.setBackground(ContextCompat.getDrawable(this, R.drawable.option_back));
        });
        binding.driver.setOnClickListener(view -> {
            filter_makers("Driver");

            binding.all.setEnabled(true);
            binding.mechanic.setEnabled(true);
            binding.carTower.setEnabled(true);
            binding.fuelProvider.setEnabled(true);
            binding.driver.setEnabled(false);
            binding.all.setBackground(ContextCompat.getDrawable(this, R.drawable.option_back));
            binding.mechanic.setBackground(ContextCompat.getDrawable(this, R.drawable.option_back));
            binding.carTower.setBackground(ContextCompat.getDrawable(this, R.drawable.option_back));
            binding.fuelProvider.setBackground(ContextCompat.getDrawable(this, R.drawable.option_back));
            binding.driver.setBackground(ContextCompat.getDrawable(this, R.drawable.selected_back));
        });

        binding.home.setOnClickListener(view -> {
            FragmentTransaction ft1 = getSupportFragmentManager().beginTransaction();
            ft1.replace(R.id.frame_layout, new HomeFragment());
            ft1.commit();
            binding.textHome.setVisibility(View.VISIBLE);
            binding.textMap.setVisibility(View.GONE);
            binding.textAccount.setVisibility(View.GONE);
            binding.frameLayout.setVisibility(View.VISIBLE);
            binding.textMessage.setVisibility(View.GONE);
            binding.mapFragment.setVisibility(View.GONE);
        });

        binding.messageBottom.setOnClickListener(view -> {
            FragmentTransaction ft1 = getSupportFragmentManager().beginTransaction();
            ft1.replace(R.id.frame_layout, new ProvidersListFragment());
            ft1.commit();
            binding.textHome.setVisibility(View.GONE);
            binding.textMap.setVisibility(View.GONE);
            binding.textAccount.setVisibility(View.GONE);
            binding.frameLayout.setVisibility(View.VISIBLE);
            binding.textMessage.setVisibility(View.VISIBLE);
            binding.mapFragment.setVisibility(View.GONE);
        });
        binding.mapBottom.setOnClickListener(view -> {
            binding.textHome.setVisibility(View.GONE);
            binding.textMap.setVisibility(View.VISIBLE);
            binding.textAccount.setVisibility(View.GONE);
            binding.textMessage.setVisibility(View.GONE);
            binding.frameLayout.setVisibility(View.GONE);
            binding.mapFragment.setVisibility(View.VISIBLE);
        });
        binding.account.setOnClickListener(view -> {
            FragmentTransaction ft1 = getSupportFragmentManager().beginTransaction();
            ft1.replace(R.id.frame_layout, new AccountFragment());
            ft1.commit();
            binding.textHome.setVisibility(View.GONE);
            binding.textMap.setVisibility(View.GONE);
            binding.textAccount.setVisibility(View.VISIBLE);
            binding.frameLayout.setVisibility(View.VISIBLE);
            binding.textMessage.setVisibility(View.GONE);
            binding.mapFragment.setVisibility(View.GONE);
        });


    }

    private void filter_makers(String servive) {

        mMap.clear();
        CircleOptions circleOptions = new CircleOptions();
        circleOptions.center(new LatLng(latitude, longitude));
        circleOptions.radius(3000);
        circleOptions.fillColor(Color.TRANSPARENT);
        circleOptions.strokeWidth(3);
        circleOptions.strokeColor(Color.RED);
        mMap.addCircle(circleOptions);
        if (servive.equals("All")) {

            for (int i = 0; i < arrayList.size(); i++) {


                LatLng maker = new LatLng(arrayList.get(i).getLatitude(), arrayList.get(i).getLongitude());
                mMap.addMarker(new MarkerOptions().position(maker).title("" + arrayList.get(i).getUserName() + "\n" + arrayList.get(i).getPhoneNumber())
                        .icon(BitmapFromVector(getApplicationContext(), R.drawable.location_pin)));


            }
        } else {

            for (int i = 0; i < arrayList.size(); i++) {

                if (arrayList.get(i).getServiceType().equals(servive)) {

                    LatLng maker = new LatLng(arrayList.get(i).getLatitude(), arrayList.get(i).getLongitude());
                    mMap.addMarker(new MarkerOptions().position(maker).title("" + arrayList.get(i).getUserName() + "\n" + arrayList.get(i).getPhoneNumber())
                            .icon(BitmapFromVector(getApplicationContext(), R.drawable.location_pin)));


                }
            }
        }


    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);

        firebaseData();

        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
        } else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private BitmapDescriptor BitmapFromVector(Context context, int vectorResId) {
        // below line is use to generate a drawable.
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);

        // below line is use to set bounds to our vector drawable.
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());

        // below line is use to create a bitmap for our
        // drawable which we have added.
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);

        // below line is use to add bitmap in our canvas.
        Canvas canvas = new Canvas(bitmap);

        // below line is use to draw our
        // vector drawable in canvas.
        vectorDrawable.draw(canvas);

        // after generating our bitmap we are returning our bitmap.
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }


    public void firebaseData() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("UpWork").child("UserDetail");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                arrayList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ModelForFirebase Model = snapshot.getValue(ModelForFirebase.class);
                    arrayList.add(Model);

                }
                makers_map();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });


    }

    public void makers_map() {

        getLocation();
        LatLng maker1 = new LatLng(latitude, longitude);
        float zoomLevel = 12.7f; //This goes up to 21
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(maker1, zoomLevel));


        for (int i = 0; i < arrayList.size(); i++) {

            LatLng maker = new LatLng(arrayList.get(i).getLatitude(), arrayList.get(i).getLongitude());
            mMap.addMarker(new MarkerOptions().position(maker).title("" + arrayList.get(i).getUserName() + "\n" + arrayList.get(i).getPhoneNumber())
                    .icon(BitmapFromVector(getApplicationContext(), R.drawable.location_pin)));

        }

        CircleOptions circleOptions = new CircleOptions();
        circleOptions.center(new LatLng(latitude, longitude));
        circleOptions.radius(3000);
        circleOptions.fillColor(Color.TRANSPARENT);
        circleOptions.strokeWidth(3);
        circleOptions.strokeColor(Color.RED);
        mMap.addCircle(circleOptions);

        mMap.setOnMarkerClickListener(marker -> {
            RequestingwritePermission();
            for (int i = 0; i < arrayList.size(); i++) {
                search_str = arrayList.get(i).getUserName() + "\n" + arrayList.get(i).getPhoneNumber();
                if (search_str.equals(marker.getTitle())) {
                    detail_alert(arrayList.get(i).getUserName(), arrayList.get(i).getPhoneNumber(), arrayList.get(i).getServiceType());
                    break;
                }
            }

            return false;
        });
    }

    public void getLocation() {
        GpsTracker gpsTracker = new GpsTracker(MainActivity.this);
        if (gpsTracker.canGetLocation()) {
            latitude = gpsTracker.getLatitude();
            longitude = gpsTracker.getLongitude();
            return;
        } else {
            gpsTracker.showSettingsAlert();
        }
    }


    public void detail_alert(String name_st, String phone_st, String service_st) {

        RequestingreadPermission();
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.marker_on_clicked);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        TextView name = dialog.findViewById(R.id.name);
        TextView service = dialog.findViewById(R.id.service);
        TextView phone = dialog.findViewById(R.id.phone);

        name.setText("" + name_st);
        service.setText("" + service_st);
        phone.setText("" + phone_st);




        dialog.show();

    }

    private void RequestingLocPermission() {
        try {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CALL_PHONE}, 101);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestingwritePermission();
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

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0)
            getSupportFragmentManager().popBackStack();
        else{
            startActivity(new Intent(this, SignIn.class));
            Toast.makeText(this, "Logout", Toast.LENGTH_SHORT).show();
            this.finish();
        }
    }
}