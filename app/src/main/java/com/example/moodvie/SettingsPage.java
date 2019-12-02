package com.example.moodvie;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;

import com.example.databases.movies;
import com.example.databases.users;
import com.example.objects.Person;

public class SettingsPage extends AppCompatActivity
{
    // Return the super class of a views ID
    private <T extends View> T getView(int id) { return super.findViewById(id);}

    // Include the functions class, users database and movies database
    private final functions _functions = new functions();
    private final users user = new users(this);
    private final movies movie = new movies(this);

    // Reference variable for a Person object
    private Person person;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_page);

        // De-serialise the person object stored in the Intent extras
        person = (Person) getIntent().getSerializableExtra("personClass");

        // Click Listener for the Logout Button
        getView(R.id.SettingsPage_logout).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                logout();
            }
        });

        // Click Listener for the Change Username Button
        getView(R.id.SettingsPage_changeUsername).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                createInputPrompt("Change Username", InputType.TYPE_CLASS_TEXT, "username");
            }
        });

        // Click Listener for the Change Password Button
        getView(R.id.SettingsPage_changePassword).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                createInputPrompt("Change Password", InputType.TYPE_TEXT_VARIATION_PASSWORD, "password");
            }
        });
    }

    /**
     * Used to log the user out of their account - if the user has selected to be remembered by the
     * app then this will also be cleared on the logout
     */
    private void logout()
    {
        /*
         * Access Shared Preferences and get the 'autoLogin' key to edit the
         * 'USER_LOGGED_IN' string to be blank
         */
        SharedPreferences sp = getSharedPreferences("autoLogin", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("USER_LOGGED_IN", "").apply();

        // Kill the activity and re-direct the user back to the login page
        finish();
        _functions.createMessage(getApplicationContext(), "Logged out");
        startActivity(new Intent(getApplicationContext(), LoginScreen.class));
    }

    /**
     * Create an AlertDialog prompt to allow the user to input some text
     *
     * @param title The title of the prompt
     * @param type  Specify the type of input expected;
     *              - InputType.TYPE_CLASS_TEXT sets the input as text
     *              - InputType.TYPE_TEXT_VARIATION_PASSWORD sets the input as a password,
     *                and will mask the text
     *
     * @param flag  What the prompts being used for
     */
    private void createInputPrompt(String title, int type, final String flag)
    {
        // Instantiate an AlertDialog builder
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(title);

        // Setup the input and display it in the AlertDialog prompt
        final EditText input = new EditText(this);
        input.setInputType(type);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton(getString(R.string.change_flag, flag), new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                // Get the input text
                String inputText = input.getText().toString();

                /*
                 * If the input of the text is not blank then switch the flag to carry out the
                 * correct action otherwise notify the user that their input was blank
                 */
                if(!_functions.isBlank(inputText))
                {
                    switch(flag)
                    {
                        case "username":
                            /*
                             *  If the username doesn't exist in the user database then update the
                             *  username to the new one and change the movie ownership to be owned
                             *  by the new username. If the username is taken then notify the user
                             */
                            if(!user.exists(inputText))
                            {
                                user.changeUsername(person.getUsername(), inputText);
                                movie.changeMovieOwner(person.getUsername(), inputText);
                            }
                            else
                                _functions.createMessage(getApplicationContext(), getString(R.string.username_exists));
                            break;

                        case "password":
                            // Change the password of the user
                            user.changePassword(person.getUsername(), inputText);
                            break;
                    }

                    // Log the user out
                    _functions.createMessage(getApplicationContext(), getString(R.string.logging_out));
                    logout();
                }
                else
                    // If the input is blank notify the user
                    _functions.createMessage(getApplicationContext(), getString(R.string.fill_in_field));
            }
        });

        builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.cancel();
            }
        });

        // Show the dialog
        builder.show();
    }
}
