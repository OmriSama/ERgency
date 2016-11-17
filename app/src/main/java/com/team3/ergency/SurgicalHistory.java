package com.team3.ergency;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

public class SurgicalHistory extends AppCompatActivity {

    Button shNextBt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Surgical History");
        setContentView(R.layout.activity_surgical_history);

        //Changes CONTINUE button color
        shNextBt = (Button) findViewById(R.id.sh_next_bt);
        shNextBt.setBackgroundColor(Color.RED);
        shNextBt.setTextColor(Color.WHITE);
    }
}
