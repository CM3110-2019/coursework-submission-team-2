package com.example.moodvie;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class HomeScreen extends AppCompatActivity
{
    protected <T extends View> T getView(int id) { return super.findViewById(id);}
    functions _functions = new functions();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        // Click listener for settings button
        getView(R.id.HomeScreen_settingButton).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(getApplicationContext(), SettingsPage.class));
            }
        });

        // Click listener for scan movie button
        getView(R.id.scanButton).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(_functions.checkCameraHardware(getApplicationContext()))
                    startActivity(new Intent(getApplicationContext(), BarcodeScanner.class));
                else
                    _functions.createMessage(getApplicationContext(), "No Camera Available");
            }
        });

        // Click listener for face scan button
        getView(R.id.faceButton).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(_functions.checkCameraHardware(getApplicationContext()))
                    startActivity(new Intent(getApplicationContext(), FaceScan.class));
                else
                    _functions.createMessage(getApplicationContext(), "No Camera Available");
            }
        });

        getView(R.id.searchButton).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                TextView search = getView(R.id.etSearchMovie);
                if(_functions.isBlank(null,null,null,search.getText().toString()))
                    _functions.createMessage(getApplicationContext(), "Enter a search query");
                else
                    _functions.createMessage(getApplicationContext(), "Do something");
            }
        });
    }
}