package com.example.objects;
import java.io.Serializable;

public class Person implements Serializable
{
    // Instance variables for the name, username and password
    private String name, username, password;

    // Construct the class and set up the instance variables
    public Person()
    {
        this.name = name;
        this.username = username;
        this.password = password;
    }

    public String getName()
    {
        return this.name;
    }

    public String getUsername()
    {
        return username;
    }

    public String getPassword()
    {
        return password;
    }

    public void setName(String newName)
    {
        this.name = newName;
    }

    public void setUsername(String newUsername)
    {
        this.username = newUsername;
    }

    public void setPassword(String newPassword)
    {
        this.password = newPassword;
    }
}
