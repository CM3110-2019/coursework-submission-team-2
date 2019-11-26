package com.example.objects;
import java.io.Serializable;

/**
 * This class holds the information for an account, it implements Serializable which allows an
 * object once its created to be converted into a byte stream, this allows the object to be accessed
 * over multiple activities without making a new instance of it. When the object gets de-serialized
 * it will recreate the actual object in memory.
 */
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

    /**
     * Get the name of the account
     * @return the name of the account
     */
    public String getName()
    {
        return this.name;
    }

    /**
     * Get the username of the account
     * @return the username of the account
     */
    public String getUsername()
    {
        return username;
    }

    /**
     * Get the password of the account
     * @return the password of the account
     */
    public String getPassword()
    {
        return password;
    }

    /**
     * Changes the name of the account
     * @param newName The new name
     */
    public void setName(String newName)
    {
        this.name = newName;
    }

    /**
     * Changes the username of the account
     * @param newUsername The new username
     */
    public void setUsername(String newUsername)
    {
        this.username = newUsername;
    }

    /**
     * Changes the password of the account
     * @param newPassword The new password
     */
    public void setPassword(String newPassword)
    {
        this.password = newPassword;
    }
}
