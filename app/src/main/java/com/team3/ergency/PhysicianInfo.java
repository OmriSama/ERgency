package com.team3.ergency;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class PhysicianInfo extends AppCompatActivity {

    ArrayList<String> specializationList;
    ArrayList<EditText> specialistNameList;
    ArrayList<EditText> specialistPhoneList;

    FileOutputStream fileOut;
    String fileName = "PatientInformation.txt";

    private EditText primaryCareNameEditText;
    private EditText primaryCarePhoneEditText;
    private LinearLayout phyInfoLinearLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_physician_info);

        // Setup View and Layout variables
        primaryCareNameEditText = (EditText) findViewById(R.id.phyi_primary_care_name);
        primaryCarePhoneEditText = (EditText) findViewById(R.id.phyi_primary_care_phone);
        phyInfoLinearLayout = (LinearLayout) findViewById(R.id.phyi_physician_info);

        // Add text watcher for phone EditText
        primaryCarePhoneEditText.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

        // Initialize surgery and surgery date lists with first EditText
        specializationList = new ArrayList<String>() {{
            add(((TextView) findViewById(R.id.phyi_primary_care_type)).getText().toString());
        }};
        specialistNameList = new ArrayList<EditText>() {{
            add(primaryCareNameEditText);
        }};
        specialistPhoneList = new ArrayList<EditText>() {{
            add(primaryCarePhoneEditText);
        }};
    }

    public void addMore(View view) {
        // Inflate dialog box layout
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(this);
        View dialogView = layoutInflaterAndroid.inflate(R.layout.user_input_dialog, null);

        // Create new dialog box and set it inside the view
        AlertDialog.Builder alertDialogBuilderSpecialization = new AlertDialog.Builder(this);
        alertDialogBuilderSpecialization.setView(dialogView);

        final EditText specializationEditText = (EditText) dialogView.findViewById(R.id.user_input_dialog);
        alertDialogBuilderSpecialization
                .setCancelable(false)
                .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {
                        String specializationInput = specializationEditText.getText().toString();
                        if (specializationInput.length() <= 0) {
                            Toast.makeText(getApplicationContext(), "Please specify a specialization", Toast.LENGTH_LONG).show();
                        } else {
                            specializationList.add(specializationInput);
                            addSpecialist(specializationInput);
                        }

                    }
                })

                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                dialogBox.cancel();
                            }
                        });

        AlertDialog alertDialogAndroid = alertDialogBuilderSpecialization.create();
        alertDialogAndroid.show();
    }

    public void saveSpecialistInfo(View view) {

        // Open PatientInformation.txt in internal storage to store the patient information
        try {
            fileOut = openFileOutput(fileName, MODE_APPEND);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // If the condition field is filled out, then a date must be required. Initially set to false
        boolean requireDate = false;

        writeToFile("\n\nPHYSICIAN INFO");

        for (int i = 0; i < specializationList.size(); i++) {
            writeToFile(specializationList.get(i) + "\n");

            String specialistInput = specialistNameList.get(i).getText().toString();
            if (specialistInput.length() <= 0) {
                break;
            } else {
                writeToFile("Physician Name: " + specialistInput + "\n");
                // Condition name was entered. Date of diagnosis is required
                requireDate = true;
            }

            specialistInput = specialistPhoneList.get(i).getText().toString();
            if (specialistInput.length() <= 0) {
                Toast.makeText(getApplicationContext(), "Please fill in the specialist's phone number", Toast.LENGTH_LONG).show();
            } else {
                writeToFile("Physician Phone: " + specialistInput + "\n");
                // Reset the variable
                requireDate = false;
            }
        }

        if (!requireDate) {
            Intent intent = new Intent(this, Medications.class);
            startActivity(intent);
            finish();
        }
    }

    // Write information to the file
    private void writeToFile(String string) {
        try {
            fileOut.write(string.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addSpecialist(String specializationStr) {
        final int margin_dp = (int) getResources().getDimension(R.dimen.primary_view_margin);
        final int top_margin_dp = (int) getResources().getDimension(R.dimen.extra_form_vertical_margin);

        final int padding_left_dp = (int) getResources().getDimension(R.dimen.form_text_header_padding);

        // Create the layout for the specialization
        LinearLayout.LayoutParams specializationLayout = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        specializationLayout.setMargins(margin_dp+padding_left_dp, top_margin_dp, margin_dp, margin_dp);

        // Create a new TextView for the specialization
        TextView specializationTextView = new TextView(this);
        specializationTextView.setText(specializationStr);
        specializationTextView.setTextAppearance(this, android.R.style.TextAppearance_Large);
        specializationTextView.setTextAppearance(android.R.style.TextAppearance_Large);
        specializationTextView.setLayoutParams(specializationLayout);

        phyInfoLinearLayout.addView(specializationTextView);

        // Create the layout for the specialist name EditText
        LinearLayout.LayoutParams specialistLayout = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        specialistLayout.setMargins(margin_dp, margin_dp, margin_dp, margin_dp);

        // Create a new EditText for the specialist name
        EditText newSpecialistName = new EditText(this);
        newSpecialistName.setHint("Specialist Name");
        newSpecialistName.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
        newSpecialistName.setLayoutParams(specialistLayout);

        specialistNameList.add(newSpecialistName);
        phyInfoLinearLayout.addView(newSpecialistName);

        // Create the layout for the specialist phone
        LinearLayout.LayoutParams dateLayout = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dateLayout.setMargins(margin_dp, margin_dp, margin_dp, margin_dp);

        // Create a new EditText for the specialist phone
        EditText newSpecialistPhone = new EditText(this);
        newSpecialistPhone.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
        newSpecialistPhone.setHint("Phone Number");
        newSpecialistPhone.setInputType(InputType.TYPE_CLASS_PHONE);
        newSpecialistPhone.setLayoutParams(dateLayout);

        specialistPhoneList.add(newSpecialistPhone);
        phyInfoLinearLayout.addView(newSpecialistPhone);
    }
}
