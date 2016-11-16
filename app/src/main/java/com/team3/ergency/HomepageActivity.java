package com.team3.ergency;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class HomepageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
    }

    public void findHospitals(View view) {
        Intent locationIntent = new Intent(this, LocationActivity.class);
        startActivity(locationIntent);
        finish();
    }
}
