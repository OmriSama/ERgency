package com.team3.ergency;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import static com.team3.ergency.R.id.map;


public class LocationActivity extends AppCompatActivity
        implements ActivityCompat.OnRequestPermissionsResultCallback, OnMapReadyCallback {

    /**
     * Log tag for LocationActivity
     */
    public final String TAG = "LocationActivity";

    /**
     * Id to identify a location permission request
     */
    public final int REQUEST_LOCATION_ID = 0;

    /**
     * Location to use for HospitalSearchActivity
     */
    private Location mLocation;

    /**
     * Map to use for displaying location
     */
    GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;
    }

    public void requestLocation(View view) {
        // Check if location permissions are granted
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            requestLocationPermission();
        }
        else {
            Log.d(TAG, "Location permission has already been granted.");
            getLocation();
            setMap(new LatLng(mLocation.getLatitude(), mLocation.getLongitude()));
        }
    }

    /**
     * Requests Location permissions
     */
    private void requestLocationPermission() {
        Log.d(TAG, "Location permission NOT granted. Requesting permission.");

        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Display toast explanation for location permission
            Log.d(TAG, "Displaying location permission rationale");
            Toast.makeText(this, R.string.permissions_needed_location, Toast.LENGTH_LONG).show();
        }
        else {
            // Request location permissions
            ActivityCompat.requestPermissions(this, new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION},
                    REQUEST_LOCATION_ID);
        }
    }

    /**
     * Request a single location update and store that location.
     */
    private void getLocation() {
        // Acquire reference to system Location Manager
        LocationManager mLocationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        final LocationListener mLocationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                mLocation = location;
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
        mLocation = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        mLocationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, mLocationListener, null);
        Log.d(TAG, "Current location: " + mLocation.toString());
    }

    private void setMap(LatLng coordinate) {
        mMap.addMarker(new MarkerOptions()
                .position(coordinate)
                .title("Marker"));
        CameraUpdate cameraLocation = CameraUpdateFactory.newLatLngZoom(coordinate, 15);
        mMap.animateCamera(cameraLocation);
    }

    /**
     * Override callback received when permission response is received.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                          String permissions[], int[] grantResults) {
        if (requestCode == REQUEST_LOCATION_ID) {
            Log.d(TAG, "Location permission response received.");

            //  Check if location permission was granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission was granted. Do nothing.
            }
            else {
                // Permission not granted. Disable location button
                Button requestLocationButton = (Button) findViewById(R.id.request_location_button);
                requestLocationButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        Toast.makeText(getApplicationContext(),
                                R.string.permissions_needed_location, Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
    }

    public void submitLocation(View view) {
        if (mLocation == null) {
            EditText location_input = (EditText) findViewById(R.id.location_edittext);
            // Go to next Activity
        }
        else {
            // Go to next Activity
        }
    }

}
