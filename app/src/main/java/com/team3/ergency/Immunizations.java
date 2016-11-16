package com.team3.ergency;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

public class Immunizations extends AppCompatActivity {

    Button continueBt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Immunizations");
        setContentView(R.layout.activity_immunizations);

        //Changes CONTINUE button color
        continueBt = (Button) findViewById(R.id.immunizations_complete_button);
        continueBt.setBackgroundColor(Color.RED);
        continueBt.setTextColor(Color.WHITE);
    }
}
