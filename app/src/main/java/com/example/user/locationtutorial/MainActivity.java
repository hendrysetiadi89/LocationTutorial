package com.example.user.locationtutorial;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;

public class MainActivity extends AppCompatActivity
        implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener{

    public static final int LOC_INTERVAL = 120000;
    private GoogleApiClient mClient;
    private View buttonPrintLocation;
    private Location location;
    private LocationRequest locationRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonPrintLocation = findViewById(R.id.but_print_location);
        buttonPrintLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                        !=
                        PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(MainActivity.this,"You need to add location permission first", Toast.LENGTH_LONG).show();
                    ActivityCompat.requestPermissions(MainActivity.this,new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 123);
                    return;
                }
                location = LocationServices.FusedLocationApi.getLastLocation(mClient);
                if (location == null) {
                    LocationServices.FusedLocationApi.requestLocationUpdates(mClient, locationRequest, MainActivity.this);
                } else {
                    onLocationChanged(location);
                }
            }
        });

        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(LOC_INTERVAL);
        locationRequest.setFastestInterval(LOC_INTERVAL);

        mClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .enableAutoManage(this, this)
                .build();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.i("Test", "API conn successful");
        Toast.makeText(MainActivity.this,"conn success", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i("Test", "API conn suspended");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i("Test", "API conn failed");
        Toast.makeText(MainActivity.this,"conn failed", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onLocationChanged(Location location) {
        Toast.makeText(MainActivity.this, location.getLatitude() + " " + location.getLongitude(), Toast.LENGTH_LONG).show();
    }
}
