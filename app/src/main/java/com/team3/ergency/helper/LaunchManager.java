package com.team3.ergency.helper;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by howard on 11/18/16.
 */

public class LaunchManager {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context context;

    public LaunchManager(Context context) {
        this.context = context;
        pref = context.getSharedPreferences(context.getPackageName(), Activity.MODE_PRIVATE);
        editor = pref.edit();
    }

    public void setIntroDisplayed(boolean isFirstLaunch) {
        editor.putBoolean("IS_INTRO_DISPLAYED", isFirstLaunch);
        editor.commit();
    }

    public boolean isIntroDisplayed() {
        return pref.getBoolean("IS_INTRO_DISPLAYED", false);
    }

    public void setProfileFilled(boolean isProfileFilled) {
        editor.putBoolean("IS_PROFILE_FILLED", isProfileFilled);
        editor.commit();
    }

    public boolean isProfileFilled() {
        return pref.getBoolean("IS_PROFILE_FILLED", false);
    }
}
