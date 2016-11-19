package com.team3.ergency;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.team3.ergency.utils.FileUtils;

public class Confirmation extends AppCompatActivity {
    private TextView hospitalInformationTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);

        hospitalInformationTextView = (TextView) findViewById(R.id.conf_hospital_information);
        displayHospital(hospitalInformationTextView);
    }

    private void displayHospital(TextView view) {
        SharedPreferences pref = this.getSharedPreferences(this.getPackageName(), Activity.MODE_PRIVATE);

        String hospitalInfo = pref.getString("HOSPITAL_NAME", "") + "\n" +
                pref.getString("HOSPITAL_ADDRESS", "") + "\n" +
                pref.getString("HOSPITAL_PHONE", "");
        view.setText(hospitalInfo);
    }

    public void goBack(View view) {
        Intent i = new Intent(this, HomepageActivity.class);
        startActivity(i);
        finish();
    }
}
