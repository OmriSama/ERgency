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

public class ChronicConditions extends AppCompatActivity {

    ArrayList<EditText> conditionsList;
    ArrayList<EditText> diagnosisDateList;

    FileOutputStream fileOut;
    String fileName = "PatientInformation.txt";

    private EditText conditionEditText;
    private EditText diagnosisDateEditText;
    private LinearLayout ccInfoLinearLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chronic_conditions);

        // Setup View and Layout variables
        conditionEditText = (EditText) findViewById(R.id.cc_condition);
        diagnosisDateEditText = (EditText) findViewById(R.id.cc_date_of_diagnosis);
        ccInfoLinearLayout = (LinearLayout) findViewById(R.id.cc_chronic_info);

        // Initialize surgery and surgery date lists with first EditText
        conditionsList = new ArrayList<EditText>() {{
            add(conditionEditText);
        }};
        diagnosisDateList = new ArrayList<EditText>() {{
            add(diagnosisDateEditText);
        }};
    }

    public void addMore(View view) {
        final int margin_dp = (int) getResources().getDimension(R.dimen.primary_view_margin);
        final int top_margin_dp = (int) getResources().getDimension(R.dimen.extra_form_vertical_margin);

        // Create the layout for the surgery EditText
        LinearLayout.LayoutParams surgeryLayout = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        surgeryLayout.setMargins(margin_dp, top_margin_dp, margin_dp, margin_dp);

        // Create a new EditText for the surgery name
        EditText newSurgery = new EditText(this);
        newSurgery.setHint("Condition");
        newSurgery.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        newSurgery.setLayoutParams(surgeryLayout);

        conditionsList.add(newSurgery);
        ccInfoLinearLayout.addView(newSurgery);

        // Create the layout for the surgery date
        LinearLayout.LayoutParams dateLayout = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dateLayout.setMargins(margin_dp, margin_dp, margin_dp, margin_dp);

        // Create a new EditText for the surgery name
        EditText newSurgeryDate = new EditText(this);
        newSurgeryDate.setHint("Date of Diagnosis");
        newSurgeryDate.setInputType(InputType.TYPE_CLASS_DATETIME);
        newSurgeryDate.setLayoutParams(dateLayout);


        diagnosisDateList.add(newSurgeryDate);
        ccInfoLinearLayout.addView(newSurgeryDate);
    }

    public void saveConditionInfo(View view) {

        // Open PatientInformation.txt in internal storage to store the patient information
        try {
            fileOut = openFileOutput(fileName, MODE_APPEND);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        writeToFile("\n\nCHRONIC CONDITIONS\n");

        // If the condition field is filled out, then a date must be required. Initially set to false
        boolean requireDate = false;

        for (int i = 0; i < conditionsList.size(); i++) {
            String surgeryInput = conditionsList.get(i).getText().toString();
            if (surgeryInput.length() <= 0) {
                break;
            } else {
                writeToFile("Chronic Condition: " + surgeryInput + "\n");
                // Condition name was entered. Date of diagnosis is required
                requireDate = true;
            }

            surgeryInput = diagnosisDateList.get(i).getText().toString();
            if (surgeryInput.length() <= 0) {
                Toast.makeText(getApplicationContext(), "Please fill in the date of diagnosis", Toast.LENGTH_LONG).show();
            } else {
                writeToFile("Date of Diagnosis: " + surgeryInput + "\n");
                // Reset the variable
                requireDate = false;
            }
        }

        if (!requireDate) {
            Intent intent = new Intent(this, SurgicalHistory.class);
            startActivity(intent);
            finish();
        }

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
