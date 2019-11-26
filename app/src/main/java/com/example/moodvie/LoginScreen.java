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
    protected <T extends View> T getView(int id) { return super.findViewById(id); }

    // Include the users database and the functions class
    private final users userDatabase = new users(this);
    private final functions _functions = new functions();

    // Create a Person object
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
                // Get the required Views username and password TextViews
                TextView usernameTV = (TextView) getView(R.id.LoginScreen_username);
                TextView passwordTV = getView(R.id.LoginScreen_password);
                Switch remember = getView(R.id.LoginScreen_rememberSwitch);

                // Convert the username and password TextViews into strings
                String username = usernameTV.getText().toString();
                String password = passwordTV.getText().toString();

                if(_functions.isBlank(username, password, null, null))
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
                            // Create a new Gson object
                            Gson gson = new Gson();

                            // Open a Shared Preferences editor to edit the "autoLogin" preference
                            SharedPreferences.Editor editor = getApplicationContext().getSharedPreferences("autoLogin", MODE_PRIVATE).edit();

                            // Set the name, username and password of the person object
                            person.setName("");
                            person.setUsername(username);
                            person.setPassword(password);

                            // Serialize the person object into a JSON string
                            String serializedPerson = gson.toJson(person);

                            /*
                             * If the remember me switch is enabled then the "USER_LOGGED_IN"
                             * key in the "autoLogin" preference will hold the serialized person
                             * object as a JSON string that can be de-serialized back into a Person()
                             * object for automatic logins otherwise the value of the "autoLogin"
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
     * @return true if the details match otherwise false
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
