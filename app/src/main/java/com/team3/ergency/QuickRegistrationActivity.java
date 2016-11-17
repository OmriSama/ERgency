package com.team3.ergency;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.team3.ergency.helper.FileHelper;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class QuickRegistrationActivity extends AppCompatActivity {
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
                painScaleTextView.setText(""+(progress+1));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    public void saveInfo(View view) {
        unfilledForms = new ArrayList<>();

        // Create a new file in internal storage to store the patient information
        fileOut = FileHelper.createFileOutputStream(this,
                getResources().getString(R.string.output_file_quick_registration),
                MODE_PRIVATE);

        //Get the text from each TextEdit, check if valid, and write to file
        primaryConcern = (EditText) findViewById(R.id.qr_primary_concern_edittext);
        checkAndSaveField(primaryConcern, "Primary Concern");

        startTime = (EditText) findViewById(R.id.qr_start_time_edittext);
        checkAndSaveField(startTime, "Start Time");

        painRadiate = (EditText) findViewById(R.id.qr_pain_radiate_edittext);
        saveField(painRadiate, "Pain Radiate");

        associatedSymptoms = (EditText) findViewById(R.id.qr_associated_symptoms_edittext);
        saveField(associatedSymptoms, "Associated Symptoms");

        FileHelper.closeFileOutputStream(fileOut);

        //Check if any forms are unfilled. If none, then move to next screen
        if (unfilledForms.size() == 0) {
            Intent i = new Intent(this, Confirmation.class);
            startActivity(i);
            finish();
        } else {
            //Show alert dialog telling user that some forms still need to be filled out
            generate_error_popup(unfilledForms);
        }
    }

    //Write edit text field to file or display error if not filled
    public void checkAndSaveField(EditText editText, String label) {
        String userInput = editText.getText().toString();
        if (userInput.length() > 0) {
            FileHelper.writeToFile(fileOut, label + ": " + userInput + "\n");
        }
        else {
            unfilledForms.add(label);
        }
    }

    // Write edit text field to file
    public void saveField(EditText editText, String label) {
        String userInput = editText.getText().toString();
        if (userInput.length() > 0) {
            FileHelper.writeToFile(fileOut, label + ": " + userInput + "\n");
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
