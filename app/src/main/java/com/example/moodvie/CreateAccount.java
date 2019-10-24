package com.example.moodvie;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.databases.users;

import org.w3c.dom.Text;

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

                // If the inputs are blank then notify the user that their input is blank
                if(_functions.isBlank(username.getText().toString(), password.getText().toString(), name.getText().toString()))
                    _functions.createMessage(getApplicationContext(), "Fill in all fields to continue.");
                else
                    _functions.createMessage(getApplicationContext(), "Not Blank");

            }
        });
    }
}
