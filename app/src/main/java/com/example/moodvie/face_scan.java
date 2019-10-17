package com.example.moodvie;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


public class face_scan extends AppCompatActivity
{
    functions _functions = new functions();

    protected <T extends View> T getView(int id) { return super.findViewById(id); }
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.face_scan);

        getView(R.id.takePicture).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(_functions.checkCameraHardware(getApplicationContext()))
                    _functions.createMessage(getApplicationContext(), "got a camera");
            }
        });
    }

}
