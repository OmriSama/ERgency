package com.team3.ergency.helper;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;


/**
 * Created by howard on 11/12/16.
 */

public class PlaceWrapper implements Parcelable {
    private String mName;
    private String mPlaceId;

    public PlaceWrapper(CharSequence name, String placeId) {
        mName = name.toString();
        mPlaceId = placeId;
    }

    public String getName() {
        return mName;
    }

    public String getPlaceId() {
        return mPlaceId;
    }

    public PlaceWrapper(Parcel source) {
        String [] data = new String[2];

        source.readStringArray(data);
        mName = data[0];
        mPlaceId = data[1];
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[] {mName, mPlaceId});
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public PlaceWrapper createFromParcel(Parcel source) {
            return new PlaceWrapper(source);
        }

        public PlaceWrapper[] newArray(int size) {
            return new PlaceWrapper[size];
        }
    };
}
