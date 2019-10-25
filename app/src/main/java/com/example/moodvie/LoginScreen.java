package com.example.moodvie;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.example.databases.users;

public class LoginScreen extends AppCompatActivity
{
    protected <T extends View> T getView(int id) { return super.findViewById(id); }
    private final users userDatabase = new users(this);
    private final functions _functions = new functions();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Click listener for when the login button is clicked
        getView(R.id.LoginScreen_loginButton).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                TextView username = getView(R.id.LoginScreen_username);
                TextView password = getView(R.id.LoginScreen_password);
                Switch remember = getView(R.id.LoginScreen_rememberSwitch);

                if(_functions.isBlank(username.getText().toString(), password.getText().toString(), null, null))
                    _functions.createMessage(getApplicationContext(), "Fill in all fields to continue.");
                else
                {
                    if(userDatabase.exists(username.getText().toString()))
                    {
                        if(checkInfo(username.getText().toString(), password.getText().toString()))
                        {
                            if(remember.isChecked())
                            {
                                SharedPreferences.Editor editor = getApplicationContext().getSharedPreferences("autoLogin", MODE_PRIVATE).edit();
                                editor.putString("LOGIN_USERNAME", username.getText().toString()).apply();
                            }
                            else
                            {
                                SharedPreferences.Editor editor = getApplicationContext().getSharedPreferences("autoLogin", MODE_PRIVATE).edit();
                                editor.putString("LOGIN_USERNAME", "").apply();
                            }
                            _functions.createMessage(getApplicationContext(), "Authentication Successful");
                            startActivity(new Intent(getBaseContext(), HomeScreen.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK));
                        }
                        else
                            _functions.createMessage(getApplicationContext(), "Authentication Failed");
                    }
                    else
                    {
                        _functions.createMessage(getApplicationContext(), "This Account Doesn't Exist");
                    }
                }
            }
        });

        // Click listener for when the 'Create one here' text is clicked
        getView(R.id.LoginScreen_createAnAccount).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(getBaseContext(), CreateAccount.class));
            }
        });
    }

    private Boolean checkInfo(String username, String password)
    {
        Cursor info = userDatabase.getRow(username);

        while(info.moveToNext())
        {
            if(info.getString(0).equals(username) && info.getString(1).equals(password))
                return true;
        }
        return false;
    }
}
