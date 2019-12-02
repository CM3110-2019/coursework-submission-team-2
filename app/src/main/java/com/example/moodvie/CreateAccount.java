package com.example.moodvie;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.databases.users;


public class CreateAccount extends AppCompatActivity
{
    // Return the super class of a views ID
    private <T extends View> T getView(int id) { return super.findViewById(id); }

    // Instantiate the users database and functions class
    private final users userDatabase = new users(this);
    private final functions _functions = new functions();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        // OnClickListener for the Create Account Button
        getView(R.id.createAccount_createButton).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // Get the required TextViews
                TextView username = getView(R.id.createAccount_username);
                TextView password = getView(R.id.createAccount_password);

                // Check if any of the TextViews are blank, if they're not try create the account
                if(_functions.isBlank(username.getText().toString()) || _functions.isBlank(password.getText().toString()))
                    _functions.createMessage(getApplicationContext(), getString(R.string.fill_in_all_fields));

                else
                {
                    // If the username exists in the database
                    if(userDatabase.exists(username.getText().toString())) {
                        _functions.createMessage(getApplicationContext(), getString(R.string.username_exists));
                    }
                    else
                    {
                        /*
                         * Try add the user to the user database and launch the login page otherwise
                         * notify them that creating the account failed
                         */
                        if(userDatabase.addData(username.getText().toString(), password.getText().toString()))
                        {
                            _functions.createMessage(getApplicationContext(), getString(R.string.successfully_created_account));
                            startActivity(new Intent(getBaseContext(), LoginScreen.class));
                        }
                        else
                            _functions.createMessage(getApplicationContext(), getString(R.string.failed_to_create_account));
                    }
                }
            }
        });
    }
}
