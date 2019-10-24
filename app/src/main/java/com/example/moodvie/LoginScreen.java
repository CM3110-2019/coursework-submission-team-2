package com.example.moodvie;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.databases.users;

public class LoginScreen extends AppCompatActivity implements View.OnClickListener
{
    protected <T extends View> T getView(int id) { return super.findViewById(id); }
    private final users userDatabase = new users(this);
    private final functions _functions = new functions();
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getView(R.id.createAnAccount).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                TextView username = getView(R.id.usernameInput);
                TextView password = getView(R.id.passwordInput);

                if(_functions.isBlank(username.getText().toString(), password.getText().toString(), null))
                    _functions.createMessage(getApplicationContext(), "Blank");
                else
                    _functions.createMessage(getApplicationContext(), "Not blank");

                //startActivity(new Intent(getBaseContext(), CreateAccount.class));
            }
        });
    }
}
