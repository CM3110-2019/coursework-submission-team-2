package com.example.moodvie;

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

        getView(R.id.createAccountButton).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                TextView username = getView(R.id.userNameInput);
                TextView password = getView(R.id.password);
                TextView email = getView(R.id.emailInput2);

                // If the inputs are blank then notify the user that their input is blank
                if(_functions.isBlank(username.getText().toString(), password.getText().toString(), email.getText().toString()))
                    _functions.createMessage(getApplicationContext(), "Blank");
                else
                    _functions.createMessage(getApplicationContext(), "Not Blank");


            }
        });
    }
}
