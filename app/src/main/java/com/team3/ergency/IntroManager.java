package com.team3.ergency;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by knnguy on 11/12/16.
 */

public class IntroManager {

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context context;

    public IntroManager(Context context) {

        this.context = context;
        pref = context.getSharedPreferences("first",0);
        editor = pref.edit();
    }

    public void setFirstLaunch(boolean isFirstLaunch) {
        editor.putBoolean("check", isFirstLaunch);
        editor.commit();
    }

    public boolean checkIfFirstLaunch() {
        return pref.getBoolean("check", true);
    }
}
