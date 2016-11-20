package com.team3.ergency;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.team3.ergency.fragment.DatePickerFragment;
import com.team3.ergency.utils.FileUtils;

import java.io.FileOutputStream;
import java.util.ArrayList;

public class Immunizations extends AppCompatActivity {
    FileOutputStream fileOut;
    String fileName;

    private EditText dateOfFluVac;
    private EditText dateOfTdapVac;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_immunizations);

        fileName = getResources().getString(R.string.output_file_patient_information);
    }

    // Date of Birth selection
    public void launch_date_picker(View view) {
        DatePickerFragment date_picker = new DatePickerFragment();
        date_picker.show(getSupportFragmentManager(), "datePicker");
    }

    public void saveInfo(View view) {

        // Create a new file in internal storage to store the patient information
        fileOut = FileUtils.createFileOutputStream(this, fileName, MODE_PRIVATE);

        //Get the text from each TextEdit and write to file
        dateOfFluVac = (EditText) findViewById(R.id.flu_date_immunization);
        saveField(dateOfFluVac, "Date of Flu Vaccination");

        dateOfTdapVac = (EditText) findViewById(R.id.tdap_date_immunization);
        saveField(dateOfTdapVac, "Date of Tdap Vaccination");

        Intent i = new Intent(this, CongratsActivity.class);
        startActivity(i);
        finish();
    }

    //Write edit text field to file
    public void saveField(EditText editText, String label) {
        String userInput = editText.getText().toString();
        if (userInput != null && userInput.length() > 0) {
            FileUtils.writeToFile(fileOut, label + ": " + userInput + "\n");
        }
    }

    //Generate error popup message
    public void generate_error_popup(ArrayList<String> array) {
        String errorMessage = "Please fill in the following: \n";
        for (int i = 0; i < array.size(); i++) {
            errorMessage += "     " + array.get(i);
            //Add a new line to every message except the last message
            if (i != array.size() - 1) {
                errorMessage += "\n";
            }
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Alert");
        builder.setMessage(errorMessage);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });

        AlertDialog errorDialog = builder.create();
        errorDialog.show();
    }
}
