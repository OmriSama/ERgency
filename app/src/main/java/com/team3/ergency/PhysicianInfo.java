package com.team3.ergency;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.io.FileOutputStream;
import java.util.ArrayList;

public class PhysicianInfo extends AppCompatActivity {

    ArrayList<EditText> specialistNameList;
    ArrayList<EditText> specialistNumberList;

    FileOutputStream fileOut;
    String fileName = "PatientInformation.txt";

    private EditText conditionEditText;
    private EditText diagnosisDateEditText;
    private LinearLayout ccInfoLinearLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_physician_info);


    }

    // TODO: Insert addMore(View view) function

    // TODO: Insert saveSpecialistInfo(View view) function
}
