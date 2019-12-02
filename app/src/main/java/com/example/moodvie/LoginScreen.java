package com.example.moodvie;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

import com.example.databases.users;
import com.example.objects.Person;
import com.google.gson.Gson;

public class LoginScreen extends AppCompatActivity
{
    // Return the super class of a views ID
    private <T extends View> T getView(int id) { return super.findViewById(id); }

    // Instantiate the movies database and functions class
    private final users userDatabase = new users(this);
    private final functions _functions = new functions();

    // Reference variable for a Person object
    private final Person person = new Person();

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
                // Get the required TextViews
                TextView usernameTV = getView(R.id.LoginScreen_username);
                TextView passwordTV = getView(R.id.LoginScreen_password);
                Switch remember = getView(R.id.LoginScreen_rememberSwitch);

                // Convert the username and password TextViews into strings
                String username = usernameTV.getText().toString();
                String password = passwordTV.getText().toString();

                /*
                 * Check if the username or password are blank, if either of them are then notify
                 * the user otherwise check if the username exists. If the username exists then
                 * try log into the account otherwise notify the user that the account doesn't exist
                 */
                if(_functions.isBlank(username) || _functions.isBlank(password))
                    _functions.createMessage(getApplicationContext(), getString(R.string.fill_in_all_fields));
                else
                {
                    // If the username exists in the user database
                    if(userDatabase.exists(username))
                    {
                        /*
                         * If the username and password entered by the user match the details
                         * stored in the database
                         */
                        if(checkInfo(username, password))
                        {
                            // Set the username and password of the person object
                            person.setUsername(username);
                            person.setPassword(password);

                            // Open a Shared Preferences editor to edit the "autoLogin" preference
                            SharedPreferences.Editor editor = getApplicationContext().getSharedPreferences("autoLogin", MODE_PRIVATE).edit();

                            // Serialize the person object into a JSON string using Gson
                            Gson gson = new Gson();
                            String serializedPerson = gson.toJson(person);

                            /*
                             * If the remember me switch is enabled then the "USER_LOGGED_IN"
                             * key in the "autoLogin" preference will hold the serialized person
                             * object as a JSON string that can be de-serialized back into a Person()
                             * object for automatic logging in otherwise the value of the "autoLogin"
                             * key will store an empty string.
                             */
                            if(remember.isChecked())
                                editor.putString("USER_LOGGED_IN", serializedPerson).apply();
                            else
                                editor.putString("USER_LOGGED_IN", "").apply();

                            _functions.createMessage(getApplicationContext(), getString(R.string.auth_success));

                            // Start the HomeScreen activity and pass the person object an extra
                            startActivity(new Intent(LoginScreen.this, HomeScreen.class).putExtra("personClass", person));
                            finish();
                        }

                        // If the details don't match then notify the user
                        else
                            _functions.createMessage(getApplicationContext(), getString(R.string.auth_failed));
                    }

                    // If the username isn't in the database then the user doesn't exist
                    else
                        _functions.createMessage(getApplicationContext(), getString(R.string.account_doesnt_exist));
                }
            }
        });

        // Click listener for when the 'Create one here' text is clicked
        getView(R.id.LoginScreen_createAnAccount).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                // Open the CreateAccount activity
                startActivity(new Intent(getApplicationContext(), CreateAccount.class));
            }
        });
    }

    /**
     * Do a comparison to check whether or not the username and password entered by the user
     * match the user details in the database
     *
     * @param username  The username entered in the TextView of the login page
     * @param password  The password entered in the TextView of the login page
     * @return          True if the details match otherwise False
     */
    private Boolean checkInfo(String username, String password)
    {
        // Query a row in the database based on a username
        Cursor info = userDatabase.getRow(username);

        // Iterate over the returned row
        while(info.moveToNext())
        {
            // Check if the username and password match the user details in the database
            if(info.getString(0).equals(username) && info.getString(1).equals(password))
                return true;
        }
        return false;
    }
}
