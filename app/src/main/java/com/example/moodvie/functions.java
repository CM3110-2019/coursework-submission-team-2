package com.example.moodvie;

import android.content.Context;
import android.content.pm.PackageManager;
import android.widget.Toast;

import com.example.databases.users;

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

    public Boolean checkIfExists(users db, String username)
    {
        return db.exists(username);
    }

    public Boolean isBlank(String username, String password, String email)
    {
        if(email != null)
            return username.equals("") || password.equals("") || email.equals("");
        return username.equals("") || password.equals("");
    }
}
