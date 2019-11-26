package com.example.moodvie;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.example.objects.Person;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("ALL")

public class SplashScreen extends AppCompatActivity
{
    protected <T extends View> T getView(int id) { return super.findViewById(id); }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        /*
         * If the camera permissions haven't been granted on the first launch then ask for them
         * otherwise run the splash screen
         */
        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(SplashScreen.this, new String[]{Manifest.permission.CAMERA}, 1);
        else
            runSplashScreen();
    }

    /**
     * Handle the grant results from the permissions asked on the first launch of the app.
     *
     * If the permission was granted then run the splash screen otherwise keep prompting for the
     * camera permissions to be granted until they are.
     *
     * @param requestCode  The request code passed to onRequestPermissionsResult()
     * @param permissions  The requested permissions - this will never be null.
     * @param grantResults The grant results for the corresponding permissions which is either
     *                     PERMISSION_GRANTED or PERMISSION_DENIED - this is also never null.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        /*
         * Create a HashMap that sets the key as the permission and the value as the grant result
         * of the permission
         *
         * Essentially its going to loop through all of the permissions that have been asked for
         * and create a new entry in the hash map on each iteration to store the permission as the key
         * and the grant result as the keys value
         */
        int index = 0;
        Map<String, Integer> PermissionsMap = new HashMap<String, Integer>();

        for (String permission : permissions)
        {
            PermissionsMap.put(permission, grantResults[index]);
            index++;
        }

        /*
         * If the user denies the camera permission then keep prompting for it otherwise run
         * the splash screen if they grant the permission
         */
        if(PermissionsMap.get(Manifest.permission.CAMERA) != 0)
            ActivityCompat.requestPermissions(SplashScreen.this, new String[]{Manifest.permission.CAMERA}, 1);
        else
            runSplashScreen();
    }


    private void runSplashScreen()
    {
        // Get the ImageView of the Moodvie logo
        ImageView moodvie = getView(R.id.SplashScreen_moodvie);

        // Animate the Moodvie logo by applying an animation from AnimationUtils on it
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.myanim);
        moodvie.startAnimation(animation);

        /*
         * While the logo is animating - create a new Handler() that will create a new Runnable()
         * to automatically log the user in if they have selected this option in the app, this
         * runnable will execute after a delay of 2.55s
         */
        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                /*
                 * Access Shared Preferences and get the 'autoLogin' key to access the
                 * 'USER_LOGGED_IN' string
                 */
                SharedPreferences sp = getSharedPreferences("autoLogin", MODE_PRIVATE);
                String loginKey = sp.getString("USER_LOGGED_IN", "");

                /*
                 * If the key contains information then this means that a user told the app to
                 * remember their log in details so they will be logged in automatically on each
                 * app launch.
                 */
                if (!loginKey.equals(""))
                {
                    // Create a Gson object
                    Gson gson = new Gson();

                    /*
                     * De-serialize the "USER_LOGGED_IN" JSON string in the Shared Preferences back
                     * into a Person() object
                     */
                    Person person = gson.fromJson(loginKey, Person.class);

                    /*
                     * Automatically log the user in, put them to the HomeScreen activity and
                     * pass the person object as an extra
                     */
                    startActivity(new Intent(SplashScreen.this, HomeScreen.class).putExtra("personClass", person));
                    finish();
                }

                /*
                 * If the key contains no information then redirect the user to the login page
                 * so they can log in/create an account
                 */
                else
                {
                    startActivity(new Intent(getApplicationContext(), LoginScreen.class));
                    finish();
                }
            }
        }, 2550);
    }
}
