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
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.team3.ergency.helper.PlaceHelper;
import com.team3.ergency.helper.PlaceSuggestion;

import java.util.List;

public class LocationActivity extends AppCompatActivity
        implements ActivityCompat.OnRequestPermissionsResultCallback, OnMapReadyCallback {
    /**
     * Log tag for LocationActivity
     */
    public static final String TAG = "LocationActivity";

    /**
     * Id to identify a location permission request
     */
    public final int REQUEST_LOCATION_ID = 0;

    /**
     * GoogleAPIClient to use for predicting addresses
     */
    private GoogleApiClient mGoogleApiClient;

    /**
     * Location to use for HospitalSearchActivity
     */
    private Location mLocation;

    /**
     * Map to use for displaying location
     */
    private GoogleMap mMap;

    /**
     * SearchView, SearchResultsList, SearchResultsAdapters for the search bar
     */
    private FloatingSearchView mSearchView;
    private RecyclerView mSearchResultsList;
//    private SearchResultsListAdapter mSearchResultsAdapter;

    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onStop() {
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
        super.onStop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        // Create GoogleApiClient
        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .build();

        // Create a map fragment
        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(
                        R.id.map_fragment);
        mapFragment.getMapAsync(this);

        // Setup floating search bar
        mSearchView = (FloatingSearchView) findViewById(R.id.search_bar_floatingsearchview);
        mSearchResultsList = (RecyclerView) findViewById(R.id.search_results_list);

        setupSearchView();
//        setupResultsList();
//        setupDrawer();
    }

    /**
     * Setup the search view:
     * (1) Listen to user typing text into the search and display suggestions
     */
    private void setupSearchView() {
        mSearchView.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {
            @Override
            public void onSearchTextChanged(String oldQuery, final String newQuery) {

                if (!oldQuery.equals("") && newQuery.equals("")) {
                    // If text is erased, clear search suggestions
                    mSearchView.clearSuggestions();
                }
                else {
                    // Create new placeHelper class to pass GoogleApiClient
                    PlaceHelper placeHelper = new PlaceHelper(mGoogleApiClient);

                    // Show loading animation on the left side of the search view
                    mSearchView.showProgress();

                    // Get predictions list and display the results. Remove the loading animation.
                    placeHelper.getPrediction(LocationActivity.this, newQuery, 5,
                            new PlaceHelper.OnFindSuggestionsListener() {
                                @Override
                                public void onResults(List<PlaceSuggestion> results) {
                                    mSearchView.swapSuggestions(results);
                                    mSearchView.hideProgress();
                                }
                            });
                }
            }
        });

//        mSearchView.setOnSearchListener(new FloatingSearchView.OnSearchListener() {
//            @Override
//            public void onSuggestionClicked(final SearchSuggestion searchSuggestion) {
//                PlaceSuggestion placeSuggestion = (PlaceSuggestion) placeSuggestion;
//                PlaceHelper.findPlace(getActivity(), colorSuggestion.getBody(),
//                        new PlaceHelper.OnFindColorsListener() {
//                            @Override
//                            public void onResults(Lost<PlaceWrapper> results) {
//                                mSearchResultsAdapter.swapData(results);
//                            }
//                        });
//                mLastQuery = searchSuggestion.getBody();
//            }
//
//            @Override
//            public void onSearchAction(String query) {
//                mLastQuery = query;
//                PlaceHelper.findPlace(getActivity(), query, new PlaceHelper.OnFindPlaceListener() {
//                    @Override
//                    public void onResults(List<PlaceWrapper> results) {
//                        mSearchResultsAdapter.swapData(results);
//                    }
//                });
//            }
//        });
    }

    /**
     * Override callback when map is created
     */
    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;
    }

    /**
     * OnClick method for location request button
     */
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
     * Override callback received when permission response is received.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                          String permissions[], int[] grantResults) {
        if (requestCode == REQUEST_LOCATION_ID) {
            Log.d(TAG, "Location permission response received.");

            //  Check if location permission was granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "Location permission has already been granted.");
                getLocation();
                setMap(new LatLng(mLocation.getLatitude(), mLocation.getLongitude()));
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

    /**
     * Request a single location update and store that location
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
            public void onStatusChanged(String provider, int status, Bundle extras) {}

            @Override
            public void onProviderEnabled(String provider) {}

            @Override
            public void onProviderDisabled(String provider) {}
        };
        mLocation = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        mLocationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, mLocationListener, null);
        if (mLocation != null) {
            Log.d(TAG, "Current location: " + mLocation.toString());
        }
    }

    /**
     * Set the map the passed LatLng coordinate and adjust camera to its location
     */
    private void setMap(LatLng coordinate) {
        // Add a marker to coordinate
        mMap.addMarker(new MarkerOptions()
                .position(coordinate)
                .title("Marker"));

        // Adjust camera to new coordinate
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(coordinate, 15));
    }
}
