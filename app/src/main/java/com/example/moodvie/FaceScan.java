package com.example.moodvie;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;


public class FaceScan extends AppCompatActivity
{
    functions _functions = new functions();

    protected <T extends View> T getView(int id) { return super.findViewById(id); }
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_scan);

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
