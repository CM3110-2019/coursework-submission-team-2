package com.example.moodvie;

import android.content.Context;
import android.content.pm.PackageManager;
import android.widget.Toast;

public class functions
{
    public void createMessage(Context context, String message)
    {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
    /** Check if this device has a camera */
    public boolean checkCameraHardware(Context context)
    {
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }
}
