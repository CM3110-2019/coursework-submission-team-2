package com.example.moodvie;


import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.databases.users;


public class CreateAccount extends AppCompatActivity
{
    protected <T extends View> T getView(int id) { return super.findViewById(id); }
    private final users userDatabase = new users(this);
    private final functions _functions = new functions();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        getView(R.id.createAccount_createButton).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                TextView name = getView(R.id.createAccount_name);
                TextView username = getView(R.id.createAccount_username);
                TextView password = getView(R.id.createAccount_password);

                // If inputs blank
                if(_functions.isBlank(username.getText().toString(), password.getText().toString(), name.getText().toString(), null))
                    _functions.createMessage(getApplicationContext(), "Fill in all fields to continue.");
                else
                {
                    // If username exists
                    if(userDatabase.exists(username.getText().toString())) {
                        _functions.createMessage(getApplicationContext(), "This username has already been created.");
                    }
                    else
                    {
                        // If insertion is successful
                        if(userDatabase.addData(username.getText().toString(), password.getText().toString(), name.getText().toString()))
                        {
                            _functions.createMessage(getApplicationContext(), "Successfully created the account.");
                            startActivity(new Intent(getBaseContext(), LoginScreen.class));
                        }
                        else
                        {
                            _functions.createMessage(getApplicationContext(), userDatabase.exists(username.getText().toString()).toString());
                            _functions.createMessage(getApplicationContext(), "Failed to create your account.");
                        }
                    }
                }
            }
        });
    }
}
