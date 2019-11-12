package com.example.moodvie;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.databases.movies;

import java.util.Objects;

public class HomeScreen extends AppCompatActivity
{
    protected <T extends View> T getView(int id) { return super.findViewById(id);}
    functions _functions = new functions();
    private movies mdb = new movies(this);

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        Bundle b = getIntent().getExtras();
        final String username = Objects.requireNonNull(b).getString("id");

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
                    startActivity(new Intent(getApplicationContext(), BarcodeScanner.class).putExtra("id", username));
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

        //_functions.createMessage(getApplicationContext(), username);
    }

    @Override
    protected void onResume()
    {
        super.onResume();

    }
}