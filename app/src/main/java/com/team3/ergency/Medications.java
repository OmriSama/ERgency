package com.team3.ergency;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class Medications extends AppCompatActivity {

    ArrayList<EditText> medicationsList;
    int medicationNum = 3;
    private FileOutputStream fileOut;
    private String fileName = "PatientInformation.txt";
    private EditText medication1;
    private EditText medication2;
    private EditText medication3;
    private LinearLayout medicalInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medications);

        medication1 = (EditText) findViewById(R.id.m_medication1);
        medication2 = (EditText) findViewById(R.id.m_medication2);
        medication3 = (EditText) findViewById(R.id.m_medication3);
        medicalInfo = (LinearLayout) findViewById(R.id.m_medical_info);

        medicationsList = new ArrayList<EditText>() {{
            add(medication1);
            add(medication2);
            add(medication3);
        }};
    }

    public void addMore(View view) {

        final int margin_dp = (int) getResources().getDimension(R.dimen.primary_view_margin);

        // Create the layout for the surgery EditText
        LinearLayout.LayoutParams medicationLayout = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        medicationLayout.setMargins(margin_dp, margin_dp, margin_dp, margin_dp);

        // Create a new EditText for the surgery name
        EditText newMedication = new EditText(this);

        // Increase number of medications
        ++medicationNum;

        newMedication.setHint("Medication " + medicationNum);
        newMedication.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        newMedication.setLayoutParams(medicationLayout);

        medicationsList.add(newMedication);
        medicalInfo.addView(newMedication);

    }

    public void saveMedicationInfo(View view) {

        // Open PatientInformation.txt in internal storage to store the patient information
        try {
            fileOut = openFileOutput(fileName, MODE_APPEND);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < medicationsList.size(); i++) {
            String medicationInput = medicationsList.get(i).getText().toString();
            if (medicationInput.length() <= 0) {
                break;
            } else {
                writeToFile("Medication " + medicationNum + ": " + medicationInput + "\n");
            }

        }

        Intent intent = new Intent(this, ChronicConditions.class);
        startActivity(intent);
        finish();
    }

    // Write information to the file
    public void writeToFile(String string) {
        try {
            fileOut.write(string.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
