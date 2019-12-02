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
        String createTable = "CREATE TABLE " + USER_TABLE + " (" + USER_USERNAME + " VARCHAR(10) PRIMARY KEY, " + USER_PASSWORD + " VARCHAR(10))";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        // If the table already exists then drop it
        db.execSQL("DROP TABLE IF EXISTS " + USER_TABLE);
    }

    /**
     * Insert a new user into the database
     *
     * @param username The username to be inserted
     * @param password The password to be inserted
     * @return         TRUE if the result is inserted else FALSE
     */
    public boolean addData(String username, String password)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues content = new ContentValues();

        content.put(USER_USERNAME, username);
        content.put(USER_PASSWORD, password);

        long result = db.insert(USER_TABLE, null, content);

        return result != 1;
    }

    /**
     * Check if a user exists in database by creating a query that tries to select 1 row if it exists
     *
     * @param name The username that is being checked
     * @return     TRUE if a user with that name is found, otherwise FALSE
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
     * Get all of the row from the database based on a specific username
     *
     * @param username The user that's being looked for
     * @return         All the rows that the user is present in as a Cursor object
     */
    public Cursor getRow(String username)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + USER_TABLE + " WHERE " + USER_USERNAME + " = '" + username + "'";
        return db.rawQuery(query, null);
    }

    /**
     * Change the username of an account
     *
     * @param username    The old username
     * @param newUsername The new username to be set
     */
    public void changeUsername(String username, String newUsername)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + USER_TABLE + " SET " + USER_USERNAME + " = '" + newUsername + "'" + " WHERE " + USER_USERNAME + " = '" + username + "'";
        db.execSQL(query);
    }

    /**
     * Change the password of an account for a specific username
     *
     * @param username The username of the account
     * @param password The new password to be set
     */
    public void changePassword(String username, String password)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + USER_TABLE + " SET " + USER_PASSWORD + " = '" + password + "'" + " WHERE " + USER_USERNAME + " = '" + username + "'";
        db.execSQL(query);
    }
}
