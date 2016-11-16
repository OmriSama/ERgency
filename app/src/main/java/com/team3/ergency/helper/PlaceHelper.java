package com.team3.ergency.helper;

import android.content.Context;
import android.util.Log;
import android.widget.Filter;
import android.os.Handler;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.AutocompletePredictionBuffer;
import com.google.android.gms.location.places.GeoDataApi;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by howard on 11/13/16.
 */

public class PlaceHelper {
    /**
     * Log tag for LocationActivity
     */
    public static final String TAG = "PlaceHelper";

    /**
     * Temporary static list for storing location predictions
     */
    private static List<PlaceSuggestion> tempSuggestionList = new ArrayList<>();

    /**
     * GoogleApiClient for getting location predictions
     */
    private static GoogleApiClient mGoogleApiClient;

    private static boolean predictionMutex = false;

    public PlaceHelper(GoogleApiClient googleApiClient) {
        mGoogleApiClient = googleApiClient;
    }

    public interface OnFindPlacesListener {
        void onResults(PlaceWrapper results);
    }

    public interface OnFindSuggestionsListener {
        void onResults(List<PlaceSuggestion> results);
    }

    /**
     * Find location prediction when user types a new text. If connected, return the results with
     * prediction list to the listener. If not, return results with empty list to the listener.
     */
    public static void getPrediction(Context context, String query,
                                       final int limit, final OnFindSuggestionsListener listener) {
        new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                tempSuggestionList.clear();

                // Check if user typed new text and if GoogleApiClient is connected
                if (constraint != null && constraint.length() != 0 &&
                        mGoogleApiClient != null && mGoogleApiClient.isConnected()) {

                    // Create and set LatLng bounds to the entire United States
                    LatLngBounds bounds = new LatLngBounds(new LatLng(28.70, -127.50),
                            new LatLng(48.85, -55.90));

                    // Create AutocompleteFilter with no filter
                    AutocompleteFilter filter = new AutocompleteFilter
                            .Builder()
                            .setTypeFilter(AutocompleteFilter.TYPE_FILTER_NONE)
                            .build();

                    // Set mutex for getting predictions
                    predictionMutex = true;
                    Places.GeoDataApi.getAutocompletePredictions(mGoogleApiClient,
                         constraint.toString(), bounds, filter)
                            .setResultCallback(
                                    new ResultCallback<AutocompletePredictionBuffer>() {
                                        @Override
                                        public void onResult(AutocompletePredictionBuffer buffer) {
                                            if (buffer == null) { return; }

                                            if (buffer.getStatus().isSuccess()) {
                                                for (AutocompletePrediction p : buffer) {
                                                    // Limit number of predictions to var limit
                                                    if (tempSuggestionList.size() > limit) {
                                                        break;
                                                    }
                                                    // Add new PlaceSuggestion to temp list
                                                    tempSuggestionList.add(new PlaceSuggestion(new PlaceWrapper(
                                                            p.getFullText(null), p.getPlaceId())));
                                                }
                                            }
                                            // Release buffer to prevent memory leak
                                            buffer.release();
                                            predictionMutex = false;
                                        }
                                    }
                            );

                    // Wait until predictionMutex is returned
                    while (predictionMutex) { /* Wait */  }
                }

                // Create new FilterResults and return results for processing
                FilterResults results = new FilterResults();
                results.values = tempSuggestionList;
                results.count = tempSuggestionList.size();
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (listener != null) {
                    listener.onResults((List<PlaceSuggestion>) results.values);
                }
            }
        }.filter(query);
    }
}
