package com.team3.ergency.helper;

import android.os.Parcel;

import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;


/**
 * Created by howard on 11/13/16.
 */

public class PlaceSuggestion implements SearchSuggestion {
    private PlaceWrapper mPlaceWrapper;
    private String mPlaceName;

    public PlaceSuggestion(PlaceWrapper suggestion) {
        mPlaceWrapper = suggestion;
        mPlaceName = suggestion.getName();
    }

    public PlaceSuggestion(Parcel source) {
        mPlaceWrapper = source.readParcelable(PlaceWrapper.class.getClassLoader());
    }

    public PlaceWrapper getPlaceWrapper() {
        return mPlaceWrapper;
    }

    @Override
    public String getBody() {
        return mPlaceName;
    }

    public static final Creator<PlaceSuggestion> CREATOR = new Creator<PlaceSuggestion>() {
        @Override
        public PlaceSuggestion createFromParcel(Parcel source) {
            return new PlaceSuggestion(source);
        }

        @Override
        public PlaceSuggestion[] newArray(int size) {
            return new PlaceSuggestion[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mPlaceName);
    }
}
