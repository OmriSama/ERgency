package com.team3.ergency;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class Allergies extends AppCompatActivity {

    FileOutputStream fileOut;
    String fileName = "PatientInformation.txt";
    Button continueBt;
    private EditText medicationAllergies;
    private EditText environmentalAllergies;
    private EditText foodAllergies;
    private EditText otherAllergies;
    private String input;

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

        // Open the existing PatientInfo.txt file
        try {
            fileOut = openFileOutput(fileName, MODE_APPEND);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        //Write the patient's information into the file
        medicationAllergies = (EditText) findViewById(R.id.medication_allergies);
        input = medicationAllergies.getText().toString();
        writeToFile("Medication Allergies: " + input + "\n");

        environmentalAllergies = (EditText) findViewById(R.id.envoronmental_allergies);
        input = environmentalAllergies.getText().toString();
        writeToFile("Environmental Allergies: " + input + "\n");

        foodAllergies = (EditText) findViewById(R.id.food_allergies);
        input = foodAllergies.getText().toString();
        writeToFile("Food Allergies: " + input + "\n");

        otherAllergies = (EditText) findViewById(R.id.other_allergies);
        input = otherAllergies.getText().toString();
        writeToFile("Other Allergies: " + input + "\n");

        Intent i = new Intent(this, SkipScreen.class);
        startActivity(i);
        finish();
    }

    // Writes the patient's information to the file
    public void writeToFile(String string) {
        try {
            fileOut.write(string.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
