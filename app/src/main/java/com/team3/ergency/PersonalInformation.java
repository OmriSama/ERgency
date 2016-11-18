package com.team3.ergency;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Spinner;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import android.app.AlertDialog;

import com.team3.ergency.fragment.DatePickerFragment;

public class PersonalInformation extends AppCompatActivity {

    String filename = "PatientInformation.txt";
    FileOutputStream fileOut;

    private ArrayList<String> unfilledForms;

    private EditText firstName;
    private EditText middleName;
    private EditText lastName;
    private EditText dateOfBirth;
    private Spinner sex;
    private EditText address;
    private EditText city;
    private AutoCompleteTextView state;
    private EditText zipCode;
    private EditText phoneNumber;
    private EditText emailAddress;

    private String userInput;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_information);


        // This creates the sex drop down menu
        Spinner spinner = (Spinner) findViewById(R.id.sex);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.sex, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        // Set the state text view so that when the user types in one word, a list of
        // suggestions appear
        state = (AutoCompleteTextView) findViewById(R.id.state);
        String[] statesAbbr = getResources().getStringArray(R.array.states_abbr);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, android.R.layout.select_dialog_item, statesAbbr);
        //User must enter in at least one letter for the suggestions to appear
        state.setThreshold(1);
        state.setAdapter(adapter2);

        // This puts the numbers into a phone number format
        EditText editText = (EditText) findViewById(R.id.phone_number);
        editText.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
    }


    // Date of Birth selection
    public void launch_date_picker(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager)  this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);

        DatePickerFragment date_picker = new DatePickerFragment();
        date_picker.show(getSupportFragmentManager(), "datePicker");
    }


    // Writes the patient's information to the Patient Information file
    public void saveInfo(View view) {

        unfilledForms = new ArrayList<>();

        // Create a new file in internal storage to store the patient information
        fileOut = create_file(fileOut);

        //Get the text from each TextEdit, check if valid, and write to file
        firstName = (EditText) findViewById(R.id.first_name);
        userInput = firstName.getText().toString();
        if (userInput.length() > 0) {
            writeToFile("First Name: " + userInput + "\n");
        } else {
            unfilledForms.add("First Name");
        }


        middleName = (EditText) findViewById(R.id.middle_name);
        userInput = middleName.getText().toString();
        if (userInput.length() > 0) {
            writeToFile("Middle Name: " + userInput + "\n");
        } else {
            writeToFile("Middle Name: " + "\n");
        }

        lastName = (EditText) findViewById(R.id.last_name);
        userInput = lastName.getText().toString();
        if (userInput.length() > 0) {
            writeToFile("Last Name: " + userInput + "\n");
        } else {
            unfilledForms.add("Last Name");
        }

        dateOfBirth = (EditText) findViewById(R.id.date_of_birth);
        userInput = dateOfBirth.getText().toString();
        if (userInput.length() > 0) {
            writeToFile("Date of Birth: " + userInput + "\n");
        } else {
            unfilledForms.add("Date of Birth");
        }

        sex = (Spinner) findViewById(R.id.sex);
        writeToFile(sex.getSelectedItem().toString() + "\n");

        address = (EditText) findViewById(R.id.address);
        userInput = address.getText().toString();
        if (userInput.length() > 0) {
            writeToFile("Address: " + userInput + "\n");
        } else {
            unfilledForms.add("Address");
        }

        city = (EditText) findViewById(R.id.city);
        userInput = city.getText().toString();
        if (userInput.length() > 0) {
            writeToFile("City: " + userInput + "\n");
        } else {
            unfilledForms.add("City");
        }

        userInput = state.getText().toString();
        if (userInput.length() > 0) {
            writeToFile("State: " + userInput + "\n");
        } else {
            unfilledForms.add("State");
        }

        zipCode = (EditText) findViewById(R.id.zip_code);
        userInput = zipCode.getText().toString();
        if (userInput.length() > 0) {
            writeToFile("Zip Code: " + userInput + "\n");
        } else {
            unfilledForms.add("Zip Code");
        }

        phoneNumber = (EditText) findViewById(R.id.phone_number);
        userInput = phoneNumber.getText().toString();
        if (userInput.length() > 0) {
            writeToFile("Phone Number: " + userInput + "\n");
        } else {
            unfilledForms.add("Phone Number");
        }

        emailAddress = (EditText) findViewById(R.id.email_address);
        userInput = emailAddress.getText().toString();
        if (userInput.length() > 0) {
            writeToFile("Email Address: " + userInput + "\n");
        } else {
            unfilledForms.add("Email Address");
        }

        //Close File
        closeFile(fileOut);

        //Check if any forms are unfilled. If none, then move to next screen
        if (unfilledForms.size() == 0) {
            Intent i = new Intent(this, EmergencyContact.class);
            startActivity(i);
            finish();
        } else {
            //Show alert dialog telling user that some forms still need to be filled out
            generate_error_popup(unfilledForms);
        }

    }


    // Create a new text file in internal storage
    public FileOutputStream create_file(FileOutputStream fos) {
        try {
            fos = openFileOutput(filename, MODE_PRIVATE);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return fos;
    }


    // Write information to the file
    public void writeToFile(String string) {
        try {
            fileOut.write(string.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
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
}



