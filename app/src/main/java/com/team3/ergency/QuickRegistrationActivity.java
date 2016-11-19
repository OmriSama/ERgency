package com.team3.ergency;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.team3.ergency.utils.FileUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

import static android.R.attr.label;

public class QuickRegistrationActivity extends AppCompatActivity {
    private static final int EMAIL_INTENT_REQUEST_CODE = 0;

    String piFileName;
    String qrFileName;
    FileOutputStream fileOut;

    private ArrayList<String> unfilledForms;

    private EditText primaryConcern;
    private EditText startTime;
    private EditText painRadiate;
    private EditText associatedSymptoms;

    private TextView painScaleTextView;
    private SeekBar painScaleSeekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick_registration);

        // Setup file names
        piFileName = getResources().getString(R.string.output_file_patient_information);
        qrFileName = getResources().getString(R.string.output_file_quick_registration);

        // Setup Views
        painScaleTextView = (TextView) findViewById(R.id.qr_pain_scale_textview);
        painScaleSeekBar = (SeekBar) findViewById(R.id.qr_pain_scale_seekbar);
        setupPainScaleSeekBar();
    }

    private void setupPainScaleSeekBar() {
        painScaleSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progress = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progressValue, boolean fromUser) {
                progress = progressValue;
                String progressStr = "" + (progress+1);
                painScaleTextView.setText(progressStr);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    public void sendInfo(View view) {
        unfilledForms = new ArrayList<>();

        // Create a new file in internal storage to store the patient information
        fileOut = FileUtils.createFileOutputStream(this, qrFileName, MODE_PRIVATE);

        //Get the text from each TextEdit, check if valid, and write to file
        primaryConcern = (EditText) findViewById(R.id.qr_primary_concern_edittext);
        checkAndSaveField(primaryConcern, "Primary Concern");

        FileUtils.writeToFile(fileOut, "Pain Scale: " + painScaleTextView.getText().toString() + "/10\n");

        startTime = (EditText) findViewById(R.id.qr_start_time_edittext);
        checkAndSaveField(startTime, "Start Date/Time");

        painRadiate = (EditText) findViewById(R.id.qr_pain_radiate_edittext);
        saveField(painRadiate, "Pain Radiate");

        associatedSymptoms = (EditText) findViewById(R.id.qr_associated_symptoms_edittext);
        saveField(associatedSymptoms, "Associated Symptoms");

        FileUtils.closeFileOutputStream(fileOut);

        //Check if any forms are unfilled. If none, then move to next screen
        if (unfilledForms.size() == 0) {
            sendInfoToHospital();
        } else {
            //Show alert dialog telling user that some forms still need to be filled out
            generate_error_popup(unfilledForms);
        }
    }

    //Write edit text field to file or display error if not filled
    private void checkAndSaveField(EditText editText, String label) {
        String userInput = editText.getText().toString();
        if (userInput.length() > 0) {
            FileUtils.writeToFile(fileOut, label + ": " + userInput + "\n");
        }
        else {
            unfilledForms.add(label);
        }
    }

    // Write edit text field to file
    private void saveField(EditText editText, String label) {
        String userInput = editText.getText().toString();
        if (userInput.length() > 0) {
            FileUtils.writeToFile(fileOut, label + ": " + userInput + "\n");
        }
    }

    //Generate error popup message
    private void generate_error_popup(ArrayList<String> array) {
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

    private void sendInfoToHospital() {
        // Configure Shared Preferences
        SharedPreferences pref = getSharedPreferences(getPackageName(), Activity.MODE_PRIVATE);

        // Configure files
        File piFile = new File(this.getFilesDir(), piFileName);
        File qrFile = new File(this.getFilesDir(), qrFileName);

        // Get Content URI for files
        final Uri piContentUri = FileProvider.getUriForFile(this, "com.team3.ergency.fileprovider", piFile);
        final Uri qrContentUri = FileProvider.getUriForFile(this, "com.team3.ergency.fileprovider", qrFile);

        // Put all attachment file URI in ArrayList
        ArrayList<Uri> uriList = new ArrayList<Uri>() {{
            add(piContentUri);
            add(qrContentUri);
        }};

        // Grant permission for reading and writing
        this.grantUriPermission("com.team3.ergency", piContentUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        this.grantUriPermission("com.team3.ergency", qrContentUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);

        // Create new mail Intent to send multiple attachments
        Intent emailIntent = new Intent(Intent.ACTION_SEND_MULTIPLE);
        emailIntent.setType("message/rfc822");

        // Generate source email address
        String sourceAddress = pref.getString("PATIENT_EMAIL", "");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, sourceAddress);

        // Generate subject line
        String subjectLine = "New ER Patient: " +
                             pref.getString("PATIENT_FIRST_NAME", "") + " " +
                             pref.getString("PATIENT_LAST_NAME", "");
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subjectLine);

        // Generate extra text
        String extraText = "A new patient has registered for emergency care:\n\n" +
                            "Name: " + pref.getString("PATIENT_FIRST_NAME", "") + " " + pref.getString("PATIENT_LAST_NAME", "") + "\n" +
                            "Phone: " + pref.getString("PATIENT_PHONE", "") + "\n\n" +
                            "The patient's medical profile and quick registration can be found in the attachments below.\n\n\n" +
                            "Message generated by ERgency App";
        emailIntent.putExtra(Intent.EXTRA_TEXT, extraText);

        // Attach file attachments
        emailIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uriList);

        try {
            startActivityForResult(Intent.createChooser(emailIntent, "Sending email..."), 0);
        }
        catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "Sending failed", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == EMAIL_INTENT_REQUEST_CODE) {
            Intent i = new Intent(this, Confirmation.class);
            startActivity(i);
            finish();
        }
    }
}
