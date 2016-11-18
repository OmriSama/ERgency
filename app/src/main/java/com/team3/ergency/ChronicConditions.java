package com.team3.ergency;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.team3.ergency.fragment.DatePickerFragment;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class ChronicConditions extends AppCompatActivity {

    ArrayList<EditText> conditions_list;
    ArrayList<EditText> dateofDiagnosis_list;

    FileOutputStream fileOut;
    String fileName = "PatientInformation.txt";

    private EditText condition;
    private EditText dateOfDiagnosis;
    private LinearLayout ccInfoLinearLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Chronic Conditions");
        setContentView(R.layout.activity_chronic_conditions);

        condition = (EditText) findViewById(R.id.cc_condition);
        dateOfDiagnosis = (EditText) findViewById(R.id.cc_date_of_diagnosis);
        ccInfoLinearLayout = (LinearLayout) findViewById(R.id.cc_chronic_info);

        conditions_list = new ArrayList<>();
        dateofDiagnosis_list = new ArrayList<>();

        conditions_list.add(condition);
        dateofDiagnosis_list.add(dateOfDiagnosis);
    }

    public void addMore(View view) {
        final int margin_dp = (int) getResources().getDimension(R.dimen.primary_view_margin);

        // Create the layout for the surgery EditText
        LinearLayout.LayoutParams surgeryLayout = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        surgeryLayout.setMargins(margin_dp, margin_dp, margin_dp, margin_dp);

        // Create a new EditText for the surgery name
        EditText newSurgery = new EditText(this);
        newSurgery.setHint("Condition");
        newSurgery.setInputType(InputType.TYPE_CLASS_TEXT);
        newSurgery.setLayoutParams(surgeryLayout);

        conditions_list.add(newSurgery);
        ccInfoLinearLayout.addView(newSurgery);

        // Create the layout for the surgery date
        LinearLayout.LayoutParams dateLayout = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dateLayout.setMargins(margin_dp, margin_dp, margin_dp, margin_dp);

        // Create a new EditText for the surgery name
        EditText newSurgeryDate = new EditText(this);
        newSurgeryDate.setHint("Date of Diagnosis");
        newSurgeryDate.setFocusable(false);
        newSurgeryDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerFragment date_picker = new DatePickerFragment();
                date_picker.show(getSupportFragmentManager(), "datePicker");
            }
        });
        newSurgeryDate.setLayoutParams(dateLayout);


        dateofDiagnosis_list.add(newSurgeryDate);
        ccInfoLinearLayout.addView(newSurgeryDate);
    }

    public void saveSurgeryInfo() {

        // Open PatientInformation.txt in internal storage to store the patient information
        try {
            fileOut = openFileOutput(fileName, MODE_APPEND);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // If the condition field is filled out, then a date must be required. Initially set to false
        boolean requireDate = false;

        for (int i = 0; i < conditions_list.size(); i++) {
            String surgeryInput = conditions_list.get(i).getText().toString();
            if (surgeryInput.length() < 0) {
                break;
            } else {
                writeToFile("Chronic Condition: " + surgeryInput + "\n");
                // Condition name was entered. Date of diagnosis is required
                requireDate = true;
            }

            surgeryInput = dateofDiagnosis_list.get(i).getText().toString();
            if (surgeryInput.length() < 0) {
                Toast.makeText(getApplicationContext(), "Please fill in the date of diagnosis", Toast.LENGTH_LONG).show();
            } else {
                writeToFile("Date of Diagnosis: " + surgeryInput + "\n");
                // Reset the variable
                requireDate = false;
            }
        }

        if (requireDate == false) {
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
