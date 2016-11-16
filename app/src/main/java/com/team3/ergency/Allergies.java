package com.team3.ergency;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Allergies extends AppCompatActivity {

    Button continueBt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Allergies");
        setContentView(R.layout.activity_allergies);

        //Changes CONTINUE button color
        continueBt = (Button) findViewById(R.id.allergies_continue);
        continueBt.setBackgroundColor(Color.RED);
        continueBt.setTextColor(Color.WHITE);
    }

    public void saveInsuranceInfo(View view) {
        Intent i = new Intent(this, SkipScreen.class);
        startActivity(i);
        finish();
    }
}
