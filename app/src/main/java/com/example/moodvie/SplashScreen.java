package com.example.moodvie;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class SplashScreen extends AppCompatActivity
{
    protected <T extends View> T getView(int id) { return super.findViewById(id); }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        ImageView moodvie = getView(R.id.SplashScreen_moodvie);

        // Get the animation from AnimationUtils and load it
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.myanim);
        moodvie.startAnimation(animation);

        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                /*
                    Access Shared Preferences and get the 'autoLogin' key to access the 'LOGIN_ID'
                    string
                */
                SharedPreferences sp = getSharedPreferences("autoLogin", MODE_PRIVATE);
                String userID = sp.getString("LOGIN_USERNAME", "");

                // If the key contains information then log the user in automatically
                if(!userID.equals(""))
                {
                    startActivity(new Intent(getBaseContext(), HomeScreen.class).putExtra("id", userID));
                    finish();
                }

                // Otherwise redirect the user to the option page so they can log in/create an account
                else {
                    startActivity(new Intent(getBaseContext(), LoginScreen.class));
                    finish();
                }
            }}, 2550);
    }
}
