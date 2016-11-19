package com.team3.ergency;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.AutocompletePredictionBuffer;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.team3.ergency.adapter.HospitalListAdapter;
import com.team3.ergency.gson.DistanceMatrixResponse;
import com.team3.ergency.helper.Hospital;
import com.team3.ergency.helper.HospitalSearchHelper;
import com.team3.ergency.gson.NearbySearchResponse;
import com.team3.ergency.helper.PlaceWrapper;
import com.team3.ergency.helper.PlaceSuggestion;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import static com.team3.ergency.R.id.hospital_list_view;

public class LocationActivity extends AppCompatActivity
        implements ActivityCompat.OnRequestPermissionsResultCallback, OnMapReadyCallback {

    /**
     * Log tag for LocationActivity
     */
    public static final String TAG = "LocationActivity";

    /**
     * GoogleAPIClient to use for predicting addresses
     */
    private GoogleApiClient mGoogleApiClient;

    /**
     * Id for the request location id
     */
    private final int REQUEST_LOCATION_ID = 0;

    /**
     * Location to use for getting current location
     */
    private Location mLocation;

    /**
     * Coordinates to use for getting surrounding hospitals
     */
    private LatLng mCoordinates;

    /**
     * Save a list of nearby Hospitals
     */
    private ArrayList<Hospital> mHospitals = new ArrayList<>();

    /**
     * Map to use for displaying location
     */
    private GoogleMap mMap;

    /**
     * SearchView for the location search bar
     */
    private FloatingSearchView mSearchView;

    /**
     * ProgressBar for loading up hospital search information
     */
    ProgressBar mProgressBar;

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

        // Set progress bar
        mProgressBar = (ProgressBar) findViewById(R.id.location_progress_bar);

        // Create GoogleApiClient
        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(AppIndex.API).build();

        // Create a map fragment
        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(
                        R.id.map_fragment);
        mapFragment.getMapAsync(this);

        // Setup floating search bar
        mSearchView = (FloatingSearchView) findViewById(R.id.search_bar_floatingsearchview);
        setupSearchView();

        // Set ListView off screen
        Point size = new Point();
        getWindowManager().getDefaultDisplay().getSize(size);

        ListView hospitalListView = (ListView) findViewById(R.id.hospital_list_view);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)
                hospitalListView.getLayoutParams();
        int height = params.height = (int) (size.y * 0.45);
        params.setMargins(0, 0, 0, -1 * height);
        hospitalListView.setLayoutParams(params);
    }

    /**
     * Override callback when map is created
     */
    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;
    }

    /**
     * Setup the search view:
     * (1) Listen to user typing text into the search and display suggestions
     * (2) Change camera view and place marker on map when user selects a location
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
                    // Show loading animation on the left side of the search view
                    mSearchView.showProgress();

                    getPredictions(newQuery, new ResultCallback<AutocompletePredictionBuffer>() {
                        @Override
                        public void onResult(@NonNull AutocompletePredictionBuffer buffer) {
                            List<PlaceSuggestion> suggestionList = new ArrayList<>();

                            if (buffer.getStatus().isSuccess()) {
                                for (AutocompletePrediction p : buffer) {
                                    // Limit number of predictions to 5
                                    if (suggestionList.size() > 5) {
                                        break;
                                    }
                                    // Add new PlaceSuggestion to suggestion list
                                    suggestionList.add(new PlaceSuggestion(new PlaceWrapper(
                                            p.getFullText(null), p.getPlaceId())));
                                }
                            }
                            // Release buffer to prevent memory leak
                            buffer.release();
                            mSearchView.swapSuggestions(suggestionList);
                            mSearchView.hideProgress();
                        }
                    });
                }
            }
        });

        mSearchView.setOnSearchListener(new FloatingSearchView.OnSearchListener() {
            @Override
            public void onSuggestionClicked(final SearchSuggestion searchSuggestion) {
                PlaceWrapper place = ((PlaceSuggestion) searchSuggestion).getPlaceWrapper();
                if (mGoogleApiClient == null) {
                    return;
                }
                setMapFromSearch(place);
            }

            @Override
            public void onSearchAction(String query) {
                getPredictions(query, new ResultCallback<AutocompletePredictionBuffer>() {
                    @Override
                    public void onResult(@NonNull AutocompletePredictionBuffer buffer) {
                        if (buffer.getStatus().isSuccess()) {
                            AutocompletePrediction prediction = buffer.get(0);
                            PlaceWrapper place = new PlaceWrapper(
                                    prediction.getFullText(null), prediction.getPlaceId());
                            setMapFromSearch(place);
                        }
                        // Release buffer to prevent memory leak
                        buffer.release();
                    }
                });
            }
        });
    }

    private void getPredictions(String query, ResultCallback<AutocompletePredictionBuffer> callback) {
        if (query != null && query.length() != 0 &&
                mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            // Create and set LatLng bounds to the entire United States
            LatLngBounds bounds = new LatLngBounds(new LatLng(28.70, -127.50),
                    new LatLng(48.85, -55.90));

            Places.GeoDataApi.getAutocompletePredictions(mGoogleApiClient, query, bounds, null)
                    .setResultCallback(callback);
        }
    }

    private void setMapFromSearch(PlaceWrapper place) {
        Places.GeoDataApi.getPlaceById(mGoogleApiClient, place.getPlaceId())
                .setResultCallback(
                        new ResultCallback<PlaceBuffer>() {
                            @Override
                            public void onResult(@NonNull PlaceBuffer buffer) {
                                if (buffer.getStatus().isSuccess()) {
                                    mCoordinates = buffer.get(0).getLatLng();
                                    findNearbyHospitals("hospital+urgent+care+emergency");
                                }
                                // Release buffer to prevent memory leak
                                buffer.release();
                            }
                        }
                );
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
            findNearbyHospitals("hospital+urgent+care+emergency");
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
                Log.d(TAG, "Location permission granted.");
                getLocation();
                if (mCoordinates != null) {
                    findNearbyHospitals("hospital+urgent+care+emergency");
                }
            }
            else {
                // Permission not granted. Disable location button
                ImageButton requestLocationButton = (ImageButton) findViewById(R.id.request_location_button);
                requestLocationButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        Toast.makeText(getApplicationContext(), R.string.permissions_needed_location,
                                Toast.LENGTH_LONG).show();
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
            mCoordinates = new LatLng(mLocation.getLatitude(), mLocation.getLongitude());
        }
    }

    private void findNearbyHospitals(String query) {
        mProgressBar.setVisibility(View.VISIBLE);

        FloatingActionButton locationButton = (FloatingActionButton) findViewById(
                R.id.request_location_button);
        animateY(locationButton, 2, 200);

        int parseLimit = 5;

        String nearbySearchUrl = HospitalSearchHelper.generateNearbySearchUrl(
                getResources().getString(R.string.key_google_maps_web_services),
                query, mCoordinates.latitude, mCoordinates.longitude);
        NearbySearchTask nearbySearchTask = new NearbySearchTask(parseLimit);
        nearbySearchTask.execute(nearbySearchUrl);
    }

    private class NearbySearchTask extends AsyncTask<Object, Integer, ArrayList<String>> {
        private int mParseLimit = 0;

        public NearbySearchTask(int parseLimit) {
            mParseLimit = parseLimit;
        }

        @Override
        protected ArrayList<String> doInBackground(Object... inputObj ) {
            String nearbySearchData = "";
            try {
                nearbySearchData = HospitalSearchHelper.readUrl((String) inputObj[0]);
            }
            catch (Exception e) {
                Log.d(TAG, e.toString());
            }

            NearbySearchResponse response;
            ArrayList<String> placeIds = new ArrayList<>();
            try {
                Gson gson = new Gson();
                response = gson.fromJson(nearbySearchData, NearbySearchResponse.class);
                for (int i = 0; i < mParseLimit; ++i) {
                    placeIds.add(response.getResults().get(i).getPlace_id());
                }
            }
            catch (Exception e) {
                Log.d(TAG, e.toString());
            }
            return placeIds;
        }

        @Override
        protected void onPostExecute(ArrayList<String>  result) {
            DistanceMatrixTask distanceMatrixTask = new DistanceMatrixTask();
            distanceMatrixTask.execute(result);

        }
    }

    private class DistanceMatrixTask extends AsyncTask<Object, Integer, ArrayList<String>> {
        @Override
        protected ArrayList<String> doInBackground(Object... inputObj) {
            String distanceMatrixUrl = HospitalSearchHelper.generateDistanceMatrixUrl(
                    getResources().getString(R.string.key_google_maps_web_services),
                    mCoordinates.latitude+","+mCoordinates.longitude,
                    (ArrayList<String>) inputObj[0]);

            String distanceMatrixData = "";

            try {
                distanceMatrixData = HospitalSearchHelper.readUrl(distanceMatrixUrl);
            } catch (Exception e) {
                Log.d(TAG, e.toString());
            }

            DistanceMatrixResponse response;
            ArrayList<String> hospitalLocators = new ArrayList<>();

            try {
                Gson gson = new Gson();
                response = gson.fromJson(distanceMatrixData, DistanceMatrixResponse.class);
                int i = 0;
                for (DistanceMatrixResponse.RowsBean.ElementsBean elements : response.getRows().get(0).getElements()) {
                    hospitalLocators.add(((ArrayList<String>) inputObj[0]).get(i++) + "," +
                            elements.getDistance().getText() + "," +
                            elements.getDuration().getText());
                }
            }
            catch (Exception e) {
                Log.d(TAG, e.toString());
            }

            return hospitalLocators;
        }

        @Override
        protected void onPostExecute(ArrayList<String> result) {
            compileHospitals(result);
        }
    }

    public void compileHospitals(ArrayList<String> hospitalList) {
        mHospitals.clear();
        for (String hospitalLocator : hospitalList) {
            String[] items = TextUtils.split(hospitalLocator, ",");
            mHospitals.add(new Hospital(items[0], items[1], items[2], mGoogleApiClient));
        }

        WaitForHospitalsTask waitForHospitalsTask = new WaitForHospitalsTask();
        waitForHospitalsTask.execute(mHospitals);
    }

    private class WaitForHospitalsTask extends AsyncTask<Object, Integer, Boolean> {
        @Override
        protected Boolean doInBackground(Object... inputObj) {
            boolean processingSuccess = true;
            for (Hospital hospital : (ArrayList<Hospital>) inputObj[0]) {
                if (hospital.waitForProcessing() == false) {
                    processingSuccess = false;
                }
            }
            return processingSuccess;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            displayHospitals(result);
        }
    }

    public void displayHospitals(boolean processingSuccess) {
        HospitalListAdapter adapter = new HospitalListAdapter(this, mHospitals);
        final ListView hospitalListView = (ListView) findViewById(hospital_list_view);
        hospitalListView.setAdapter(adapter);

        mProgressBar.setVisibility(View.GONE);

        setMap(true);

        animateY(hospitalListView, -1, 500);

        hospitalListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Create a file stream to store hospital information
                FileOutputStream fos = null;
                try {
                    fos = openFileOutput(getResources().getString(
                            R.string.output_file_hospital_information), MODE_PRIVATE);
                }
                catch (Exception e) {
                    Log.d(TAG, e.toString());
                }

                // Write hospital information to file stream
                try {
                    if (fos != null) {
                        fos.write(hospitalListView.getItemAtPosition(position)
                                .toString()
                                .getBytes());
                    }
                }
                catch (Exception e) {
                    Log.d(TAG, e.toString());
                }

                goToNextActivity();
            }
        });
    }

    /**
     * Set the map the passed LatLng coordinates and adjust camera to its location
     */
    private void setMap(boolean isClear) {
        // Clear map of markers if needed
        if (isClear) {
            mMap.clear();
        }

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (Hospital hospital : mHospitals) {
            builder.include(mMap.addMarker(new MarkerOptions()
                .position(new LatLng(hospital.getLat(), hospital.getLng()))
                .title(hospital.getName())
                .snippet(hospital.getDistance()))
                    .getPosition());
        }

        // Adjust camera to new coordinates
        Point size = new Point();
        getWindowManager().getDefaultDisplay().getSize(size);
        mMap.setPadding(0, 100, 0, (int) (size.y/2.25));
        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 200));
    }

    /**
     * Animations functions for ListView and Button
     */
    private void animateY(View view, int distance, int duration) {
        ObjectAnimator anim = ObjectAnimator.ofFloat(view,
                View.TRANSLATION_Y, 0, distance * view.getHeight());
        anim.setDuration(duration);
        anim.start();
    }

    /**
     * Go to next activity
     */
    private void goToNextActivity() {
        Intent locationIntent = new Intent(this, QuickRegistrationActivity.class);
        startActivity(locationIntent);
        finish();
    }
}
