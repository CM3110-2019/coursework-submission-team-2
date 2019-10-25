package com.example.moodvie;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class SettingsPage extends AppCompatActivity
{
    protected <T extends View> T getView(int id) { return super.findViewById(id);}
    functions _functions = new functions();
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_page);

        getView(R.id.SettingsPage_logout).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                SharedPreferences sp = getSharedPreferences("autoLogin", MODE_PRIVATE);

                SharedPreferences.Editor editor = sp.edit();

                editor.putString("LOGIN_USERNAME", "").apply();

                // Kill the activity and re-direct the user back to the option page
                finish();
                _functions.createMessage(getApplicationContext(), "Logged out");
                startActivity(new Intent(getApplicationContext(), LoginScreen.class));
            }
        });
    }
}
