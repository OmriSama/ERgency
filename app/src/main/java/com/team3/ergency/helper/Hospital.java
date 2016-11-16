package com.team3.ergency.helper;

import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;

import java.util.List;

import static android.text.TextUtils.split;

/**
 * Created by howard on 11/14/16.
 */

public class Hospital {
    private String mPlaceId;
    private String mName;
    private String mAddress = "";
    private String mArea = "";
    private String mPhone = "";
    private double mLat;
    private double mLng;
    private String mDistance;
    private String mDuration;
    private boolean mFinishedProcessing = false;

    public Hospital(String placeId, String distance, String duration, GoogleApiClient googleApiClient) {
        mPlaceId = placeId;
        mDistance = distance;
        mDuration = duration;

        Places.GeoDataApi.getPlaceById(googleApiClient, mPlaceId)
                .setResultCallback(
                        new ResultCallback<PlaceBuffer>() {
                            @Override
                            public void onResult(@NonNull PlaceBuffer buffer) {
                                if (buffer.getStatus().isSuccess()) {
                                    mName = buffer.get(0).getName().toString();
                                    parseAddress(buffer.get(0).getAddress().toString());
                                    parsePhone(buffer.get(0).getPhoneNumber().toString());
                                    mLat = buffer.get(0).getLatLng().latitude;
                                    mLng = buffer.get(0).getLatLng().longitude;
                                    mFinishedProcessing = true;
                                }
                                // Release buffer to prevent memory leak
                                buffer.release();
                            }
                        }
                );
    }

    public String getPlaceId() { return mPlaceId; }
    public String getName(){
        return mName;
    }
    public String getAddress(){
        return mAddress;
    }
    public String getArea() {
        return mArea;
    }
    public String getPhone(){
        return mPhone;
    }
    public double getLat() {
        return mLat;
    }
    public double getLng() { return mLng; }
    public String getDistance() {
        return mDistance;
    }
    public String getDuration() {
        return mDuration;
    }

    public boolean waitForProcessing() {
        while (!mFinishedProcessing) {
            for (int i = 0; i < 25; ++i) {
                SystemClock.sleep(50);
            }
            return mFinishedProcessing;
        }
        return mFinishedProcessing;
    }

    public String toString() {
        return "Name: " + mName +
               "\nAddress: " + mAddress + ", " + mArea +
               "\nPhone: " + mPhone;
    }

    private void parsePhone(String phone) {
        mPhone = "(" + phone.substring(3,6) + ") " + phone.substring(7);
    }

    private void parseAddress(String address) {
        String[] addressComponents = TextUtils.split(address, ", ");
        int length = addressComponents.length;
        mArea = addressComponents[length-3] + ", " + addressComponents[length-2];
        for (int i = addressComponents.length-4; i >= 0; --i) {
            mAddress = addressComponents[i] + mAddress;
        }
    }
}
