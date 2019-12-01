package com.example.moodvie;

import android.content.Context;
import android.content.pm.PackageManager;
import android.widget.Toast;

// The functions class stores methods that are accessed across multiple activities to avoid recreation
public class functions
{
    /**
     * Display a small message on the screen of the app
     *
     * @param context The interface of the app
     * @param message The text to be shown in the Toast message
     */
    public void createMessage(Context context, String message)
    {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * Check if the device that the app is being run on has a camera installed
     *
     * @param context The interface of the app
     * @return True if the device has a camera otherwise False
     */
    public boolean checkCameraHardware(Context context)
    {
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }

    /**
     * Check if a piece of text is blank or not
     *
     * @param text The piece of text to check
     * @return     True if the text is blank otherwise False
     */
    public Boolean isBlank(String text)
    {
        return text.equals("");
    }
}
