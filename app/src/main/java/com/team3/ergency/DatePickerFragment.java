package com.team3.ergency;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Calendar;

/**
 * Created by knnguy on 11/13/16.
 */

public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        Dialog date_picker = new DatePickerDialog(getActivity(), this, year, month, day);
        return date_picker;
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        EditText editTextView = (EditText) getActivity().findViewById(R.id.date_of_birth);
        editTextView.setText(month + "/" + day + "/" + year);
    }
}
