package com.team3.ergency;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.team3.ergency.fragment.DatePickerFragment;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class SurgicalHistory extends AppCompatActivity {

    private ArrayList<EditText> surgeryList;
    private ArrayList<EditText> surgeryDateList;

    private FileOutputStream fileOut;
    private String fileName = "PatientInformation.txt";

    private EditText surgeryEditText;
    private EditText surgeryDateEditText;
    private LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_surgical_history);

        // Setup View and Layout variables
        surgeryEditText = (EditText) findViewById(R.id.sh_surgery);
        surgeryDateEditText = (EditText) findViewById(R.id.sh_date_of_surgery);
        linearLayout = (LinearLayout) findViewById(R.id.sh_surgery_info);

        // Initialize surgery and surgery date lists with first EditText
        surgeryList = new ArrayList<EditText>() {{
            add(surgeryEditText);
        }};
        surgeryDateList = new ArrayList<EditText>() {{
            add(surgeryDateEditText);
        }};
    }

    public void launch_date_picker(View view) {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

        DatePickerFragment datePicker = new DatePickerFragment();
        datePicker.show(getSupportFragmentManager(), "datePicker");
    }

    public void addMore(View view) {
        final int margin_dp = (int) getResources().getDimension(R.dimen.primary_view_margin);

        // Create the layout for the surgery EditText
        LinearLayout.LayoutParams surgeryLayout = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        surgeryLayout.setMargins(margin_dp, margin_dp, margin_dp, margin_dp);

        // Create a new EditText for the surgery name
        EditText newSurgery = new EditText(this);
        newSurgery.setHint("Surgery");
        newSurgery.setInputType(InputType.TYPE_CLASS_TEXT);
        newSurgery.setLayoutParams(surgeryLayout);

        surgeryList.add(newSurgery);
        linearLayout.addView(newSurgery);

        // Create the layout for the surgery date
        LinearLayout.LayoutParams dateLayout = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dateLayout.setMargins(margin_dp, margin_dp, margin_dp, margin_dp);

        // Create a new EditText for the surgery name
        EditText newSurgeryDate = new EditText(this);
        newSurgeryDate.setHint("Date of Surgery");
        newSurgeryDate.setFocusable(false);
        newSurgeryDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerFragment date_picker = new DatePickerFragment();
                date_picker.show(getSupportFragmentManager(), "datePicker");
            }
        });
        newSurgeryDate.setLayoutParams(dateLayout);

        // Add EditTexts to ArrayList
        surgeryDateList.add(newSurgeryDate);
        linearLayout.addView(newSurgeryDate);
    }

    public void saveSurgeryInfo() {

        // Open PatientInformation.txt in internal storage to store the patient information
        try {
            fileOut = openFileOutput(fileName, MODE_APPEND);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < surgeryList.size(); i++) {
            String surgeryInput = surgeryList.get(i).getText().toString();
            if (surgeryInput.length() < 0) {
                break;
            } else {
                writeToFile("Surgery: " + surgeryInput + "\n");
            }

            surgeryInput = surgeryDateList.get(i).getText().toString();
            if (surgeryInput.length() < 0) {
                Toast.makeText(getApplicationContext(), "Please fill in the surgery date", Toast.LENGTH_LONG).show();
            } else {
                writeToFile("Surgery Date: " + surgeryInput + "\n");
            }
        }

        Intent intent = new Intent(this, Immunizations.class);
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
