package com.example.moodvie;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HomeScreen extends AppCompatActivity implements View.OnClickListener
{
    protected <T extends View> T getView(int id) { return super.findViewById(id);}
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        Button faceButton = findViewById(R.id.faceButton);
        faceButton.setOnClickListener(this);

        getView(R.id.scanButton).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(getBaseContext(), BarcodeScanner.class));
            }
        });
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.faceButton){
            Intent intent = new Intent(getApplicationContext(), FaceScan.class);
            startActivity(intent);
        }
    }
}