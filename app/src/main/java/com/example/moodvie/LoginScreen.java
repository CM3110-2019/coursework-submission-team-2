package com.example.moodvie;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LoginScreen extends AppCompatActivity implements View.OnClickListener
{
    protected <T extends View> T getView(int id) { return super.findViewById(id); }
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getView(R.id.createAnAccount).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(getBaseContext(), FaceScan.class));
            }
        });

        Button logInButton = findViewById(R.id.loginButton);
        logInButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.loginButton){
            Intent intent = new Intent(getApplicationContext(), HomeScreen.class);
            startActivity(intent);
        }
    }
}
