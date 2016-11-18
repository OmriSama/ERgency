package com.team3.ergency.utils;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by howard on 11/17/16.
 */

public class FileUtils {
    private static final String TAG = "FileUtils";

    // Create a new text file in internal storage
    public static FileOutputStream createFileOutputStream(Context context, String fileName, int mode) {
        FileOutputStream fos = null;
        try {
            fos = context.openFileOutput(fileName, mode);
        }
        catch (FileNotFoundException e) {
            Log.e(TAG, e.toString());
        }
        return fos;
    }

    // Write information to a file
    public static void writeToFile(FileOutputStream fos, String string) {
        try {
            fos.write(string.getBytes());
        } catch (IOException e) {
            Log.e(TAG, e.toString());
        }
    }

    // Read information from a file
    public static String readFromFile(Context context, String fileName) {
        String ret = "";

        try {
            InputStream inputStream = context.openFileInput(fileName);

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    receiveString += "\n";
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }

        }
        catch (Exception e) {
            Log.e(TAG, e.toString());
        }

        return ret;
    }

    // Close file
    public static void closeFileOutputStream(FileOutputStream fos) {
        try {
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
