package com.example.moodvie;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.example.objects.Person;
import com.google.gson.Gson;

public class SplashScreen extends AppCompatActivity
{
    protected <T extends View> T getView(int id) { return super.findViewById(id); }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(SplashScreen.this, new String[]{Manifest.permission.CAMERA}, 1);
        }
        else {
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
                    Gson gson = new Gson();
                    SharedPreferences sp = getSharedPreferences("autoLogin", MODE_PRIVATE);
                    String json = sp.getString("USER_LOGGED_IN", "");


                    // If the key contains information then log the user in automatically
                    if (!json.equals(""))
                    {
                        Person person = gson.fromJson(json, Person.class);
                        Intent i = new Intent(SplashScreen.this, HomeScreen.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        i.putExtra("personClass", person);
                        startActivity(i);
                        finish();
                    }

                    // Otherwise redirect the user to the login page so they can log in/create an account
                    else
                    {
                        startActivity(new Intent(getApplicationContext(), LoginScreen.class));
                        finish();
                    }
                }
            }, 2550);
        }
    }
}
