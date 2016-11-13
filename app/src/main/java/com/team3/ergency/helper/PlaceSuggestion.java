package com.team3.ergency.helper;

import android.os.Parcel;

import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;


/**
 * Created by howard on 11/13/16.
 */

public class PlaceSuggestion implements SearchSuggestion {
    private Place mPlace;
    private String mPlaceName;
    private boolean mIsHistory = false;

    public PlaceSuggestion(Place suggestion) {
        mPlace = suggestion;
        mPlaceName = suggestion.getName();
    }

    public PlaceSuggestion(Parcel source) {
        mPlace = source.readParcelable(Place.class.getClassLoader());
        mIsHistory = source.readInt() != 0;
    }

    public void setIsHistory(boolean isHistory) {
        isHistory = isHistory;
    }

    public boolean getIsHistory() {
        return mIsHistory;
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
        dest.writeInt(mIsHistory ? 1 : 0);
    }

}
