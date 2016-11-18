package com.team3.ergency.helper;

import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import static android.R.attr.apiKey;

/**
 * Created by howard on 11/13/16.
 */


public class HospitalSearchHelper {
    private static final String TAG = "HospitalSearchHelper";

    public static String generateNearbySearchUrl(String apiKey, String query, double lat, double lng) {
        final int RADIUS = 5 * 1609;
        final String RANK_BY = "distance";
        final String SEARCH_TYPE = "hospital";

        return  "https://maps.googleapis.com/maps/api/place/nearbysearch/json" +
                "?location=" + lat + "," + lng +
//                "&radius=" + RADIUS +
                "&rankby=" + RANK_BY +
                "&type=" + SEARCH_TYPE +
                "&keyword=" + query +
                "&key=" + apiKey;
    }

    public static String generateDistanceMatrixUrl(String apiKey, String origin, ArrayList<String> placeIds) {
        final String units = "imperial";

        String destinationsStr = "place_id:" + TextUtils.join("|place_id:", placeIds);

        return  "https://maps.googleapis.com/maps/api/distancematrix/json" +
                "?origins=" + origin +
                "&destinations=" + destinationsStr +
                "&units=" + units +
                "&key=" + apiKey;
    }

    public static String readUrl(String strUrl) {
        URLConnection urlConn = null;
        StringBuilder returnSB = new StringBuilder();

        try {
            URL url = new URL(strUrl);
            urlConn = url.openConnection();
            if (urlConn != null) {
                urlConn.setReadTimeout(10000);
            }
            if (urlConn != null && urlConn.getInputStream() != null) {
                BufferedReader bufferedReader = new BufferedReader(
                        new InputStreamReader(urlConn.getInputStream()));

                if (bufferedReader != null) {
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        returnSB.append(line + "\n");
                    }
                    bufferedReader.close();
                }
            }
        }
        catch (Exception e) {
            Log.d(TAG, e.toString());
        }
        return returnSB.toString();
    }
}
