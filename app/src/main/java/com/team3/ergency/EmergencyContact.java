package com.team3.ergency;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;


public class EmergencyContact extends AppCompatActivity {


    //variable declaration
    String fileName = "PatientInformation.txt";
    FileOutputStream fileOut;
    private ArrayList<String> unfilledForms;
    private EditText ecFullName;
    private EditText ecAddress;
    private EditText ecCity;
    private AutoCompleteTextView ecState;
    private EditText ecZipCode;
    private EditText ecPhoneNumber;
    private String userInput;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_contact);

        // Set the state text view so that when the user types in one word, a list of
        // suggestions appear
        ecState = (AutoCompleteTextView) findViewById(R.id.ec_state);//**********//
        String[] statesAbbr = getResources().getStringArray(R.array.states_abbr);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, android.R.layout.select_dialog_item, statesAbbr);

        //User must enter in at least one letter for the suggestions to appear
        ecState.setThreshold(1);
        ecState.setAdapter(adapter2);


        // This puts the numbers into a phone number format
        EditText editText = (EditText) findViewById(R.id.ec_phone_number);
        editText.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
    }

    // Writes the patient's information to the Patient Information file
    public void saveInfo(View view) {

        unfilledForms = new ArrayList<>();

        // Open PatientInformation.txt in internal storage to store the patient information
        try {
            fileOut = openFileOutput(fileName, MODE_APPEND);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        writeToFile("\n\nEMERGENCY CONTACT\n");

        //Get the text from each TextEdit, check if valid, and write to file
        ecFullName = (EditText) findViewById(R.id.ec_full_name);
        userInput = ecFullName.getText().toString();
        if (userInput.length() > 0) {
            writeToFile("Contact Full Name: " + userInput + "\n");
        } else {
            unfilledForms.add("Contact Full Name");
        }

        ecAddress = (EditText) findViewById(R.id.ec_address);
        userInput = ecAddress.getText().toString();
        if (userInput.length() > 0) {
            writeToFile("Address: " + userInput + "\n");
        } else {
            unfilledForms.add("Address");
        }

        ecCity = (EditText) findViewById(R.id.ec_city);
        userInput = ecCity.getText().toString();
        if (userInput.length() > 0) {
            writeToFile("City: " + userInput + "\n");
        } else {
            unfilledForms.add("City");
        }

        userInput = ecState.getText().toString();
        if (userInput.length() > 0) {
            writeToFile("State: " + userInput + "\n");
        } else {
            unfilledForms.add("State");
        }

        ecZipCode = (EditText) findViewById(R.id.ec_zip_code);
        userInput = ecZipCode.getText().toString();
        if (userInput.length() > 0) {
            writeToFile("Zip Code: " + userInput + "\n");
        } else {
            unfilledForms.add("Zip Code");
        }

        ecPhoneNumber = (EditText) findViewById(R.id.ec_phone_number);
        userInput = ecPhoneNumber.getText().toString();
        if (userInput.length() > 0) {
            writeToFile("Phone Number: " + userInput + "\n");
        } else {
            unfilledForms.add("Phone Number");
        }

        //Close File
        closeFile(fileOut);

        //Check if any forms are unfilled. If none, then move to next screen
        if (unfilledForms.size() == 0) {
            Intent i = new Intent(this, Insurance.class);
            startActivity(i);
            finish();
        } else {
            //Show alert dialog telling user that some forms still need to be filled out
            generate_error_popup(unfilledForms);
        }

    }


    // Close file
    public void closeFile(FileOutputStream fileOut) {
        try {
            fileOut.close();
        } catch (IOException e) {
            e.printStackTrace();
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

    // Write information to the file
    public void writeToFile(String string) {
        try {
            fileOut.write(string.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
