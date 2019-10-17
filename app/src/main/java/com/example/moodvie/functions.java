package com.example.moodvie;

import android.content.Context;
import android.widget.Toast;

public class functions
{
    public void createMessage(Context context, String message)
    {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
