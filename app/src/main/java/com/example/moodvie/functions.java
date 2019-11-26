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

    public Boolean isBlank(String username, String password, String name, String search)
    {
        if(name != null && search == null)
            return username.equals("") || password.equals("") || name.equals("");
        else if(search != null)
            return search.equals("");
        return username.equals("") || password.equals("");
    }
}
