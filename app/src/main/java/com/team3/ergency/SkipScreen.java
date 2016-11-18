package com.team3.ergency;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.team3.ergency.helper.LaunchManager;

public class SkipScreen extends AppCompatActivity {
    private LaunchManager launchManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skip_screen);

        launchManager = new LaunchManager(this);
        // Skip to homepage if this intro pages already displayed to user
        if (!launchManager.isProfileFilled()) {
            launchManager.setProfileFilled(true);
        }
        Log.d("Skip screen", launchManager.isProfileFilled()+"");
    }

    public void goToHomepage(View view) {
        Intent locationIntent = new Intent(this, HomepageActivity.class);
        startActivity(locationIntent);
        finish();
    }

    public void continueForms(View view) {
        Intent continueFilling = new Intent(this, PhysicianInfo.class);
        startActivity(continueFilling);
        finish();
    }
}
