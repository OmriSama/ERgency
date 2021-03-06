package com.team3.ergency;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import static com.team3.ergency.R.id.address;

public class Insurance extends AppCompatActivity {

    ArrayList<String> unfilledForms;

    FileOutputStream fileOut;
    String fileName = "PatientInformation.txt";

    EditText policyHolderName;
    EditText insuranceCompany;
    EditText memberID;
    EditText groupID;
    EditText providerAddress;
    EditText city;
    AutoCompleteTextView state;
    EditText zipCode;
    EditText phoneNumber;

    String userInput;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insurance);

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

    // Writes the patient's information to the Patient Information file
    public void saveInfo(View view) {
        // Open PatientInformation.txt in internal storage to store the patient information
        try {
            fileOut = openFileOutput(fileName, MODE_APPEND);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        writeToFile("\n\nINSURANCE\n");

        //Get the text from each TextEdit, check if valid, and write to file
        policyHolderName = (EditText) findViewById(R.id.policy_holder_name);
        userInput = policyHolderName.getText().toString();
        if (userInput.length() > 0) {
            writeToFile("Policy Holder Name: " + userInput + "\n");
        }

        insuranceCompany = (EditText) findViewById(R.id.insurance_company);
        userInput = insuranceCompany.getText().toString();
        if (userInput.length() > 0) {
            writeToFile("Insurance Company: " + userInput + "\n");
        }

        memberID = (EditText) findViewById(R.id.member_id);
        userInput = memberID.getText().toString();
        if (userInput.length() > 0) {
            writeToFile("Member ID: " + userInput + "\n");
        }

        groupID = (EditText) findViewById(R.id.group_id);
        userInput = groupID.getText().toString();
        if (userInput.length() > 0) {
            writeToFile("Group ID: " + userInput + "\n");
        }

        providerAddress = (EditText) findViewById(address);
        userInput = providerAddress.getText().toString();
        if (userInput.length() > 0) {
            writeToFile("Provider Address: " + userInput + "\n");
        }

        city = (EditText) findViewById(R.id.city);
        userInput = city.getText().toString();
        if (userInput.length() > 0) {
            writeToFile("City: " + userInput + "\n");
        }

        userInput = state.getText().toString();
        if (userInput.length() > 0) {
            writeToFile("State: " + userInput + "\n");
        }

        zipCode = (EditText) findViewById(R.id.zip_code);
        userInput = zipCode.getText().toString();
        if (userInput.length() > 0) {
            writeToFile("Zip Code: " + userInput + "\n");
        }

        phoneNumber = (EditText) findViewById(R.id.phone_number);
        userInput = phoneNumber.getText().toString();
        if (userInput.length() > 0) {
            writeToFile("Phone Number: " + userInput + "\n");
        }

        //Close File
        closeFile(fileOut);

        //Go to next screen
        Intent i = new Intent(this, Allergies.class);
        startActivity(i);
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
