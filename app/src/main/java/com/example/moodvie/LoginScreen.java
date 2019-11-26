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
    private final users userDatabase = new users(this);
    private final functions _functions = new functions();
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
                            person.setName("");
                            person.setUsername(username.getText().toString());
                            person.setPassword(password.getText().toString());

                            Gson gson = new Gson();
                            String json = gson.toJson(person);

                            if(remember.isChecked())
                            {
                                SharedPreferences.Editor editor = getApplicationContext().getSharedPreferences("autoLogin", MODE_PRIVATE).edit();
                                editor.putString("USER_LOGGED_IN", json).apply();
                            }
                            else
                            {
                                SharedPreferences.Editor editor = getApplicationContext().getSharedPreferences("autoLogin", MODE_PRIVATE).edit();
                                editor.putString("USER_LOGGED_IN", "").apply();
                            }
                            _functions.createMessage(getApplicationContext(), "Authentication Successful");



                            Intent i = new Intent(LoginScreen.this, HomeScreen.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            i.putExtra("personClass", person);
                            startActivity(i);
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
