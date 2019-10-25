package com.example.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class users extends SQLiteOpenHelper
{

    // Create a series of table and column names
    private static final String USER_TABLE = "users";
    private static final String USER_USERNAME = "username";
    private static final String USER_PASSWORD = "password";
    private static final String USER_NAME = "user_name";

    public users(Context context)
    {
        super(context, USER_TABLE, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        // Create the table
        String createTable = "CREATE TABLE " + USER_TABLE + " (" + USER_USERNAME + " VARCHAR(10) PRIMARY KEY, " + USER_PASSWORD + " VARCHAR(10), " + USER_NAME + " VARCHAR(20))";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        // If the table already exists then drop it
        db.execSQL("DROP TABLE IF EXISTS " + USER_TABLE);
    }

    /**
     *
     * @param username The username to be inserted
     * @param password The password to be inserted
     * @return TRUE if the result is inserted else FALSE
     */
    public boolean addData(String username, String password, String name)
    {
        // Access the database so we can access SQL commands
        SQLiteDatabase db = this.getWritableDatabase();

        // Create an empty set of values that we can add information to
        ContentValues content = new ContentValues();

        // Add the information to the set
        content.put(USER_USERNAME, username);
        content.put(USER_PASSWORD, password);
        content.put(USER_NAME, name);

        // Try insert the contents of the content set into the database as a new row
        long result = db.insert(USER_TABLE, null, content);

        // return the result of the insertion
        return result != 1;
    }

    /**
     * Get all of the information from USER_TABLE
     * @return The information from USER_TABLE
     */
    public Cursor getAllData()
    {
        // Access the database so we can access SQL commands
        SQLiteDatabase db = this.getWritableDatabase();

        // Create a query to get all of the data from USER_TABLE
        String query = "SELECT * FROM " + USER_TABLE;

        // Return the result of the query as a Cursor object
        return db.rawQuery(query, null);
    }

    /**
     * Check if a user exists in database
     * @param name The username that is being checked
     * @return TRUE if a user with that name is found, otherwise FALSE
     */
    public Boolean exists(String name)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        String query = "SELECT " + 1 + " FROM " + USER_TABLE + " WHERE " + USER_USERNAME + " = '" + name + "' COLLATE NOCASE " + "";

        Cursor cursor = db.rawQuery(query, null);

        long rows = cursor.getCount();
        cursor.close();

        return rows > 0;
    }

    /**
     * Gets a row from the table
     * @param username The user that's being looked for
     * @return The row of the user being looked for
     */
    public Cursor getRow(String username)
    {
        // Access the database so we can access SQL commands
        SQLiteDatabase db = this.getWritableDatabase();

        // Create a query to get a row from the table for a specific user
        String query = "SELECT * FROM " + USER_TABLE + " WHERE " + USER_USERNAME + " = '" + username + "'";

        // Return the result of the query as a Cursor object
        return db.rawQuery(query, null);
    }
}
