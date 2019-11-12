package com.example.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class movies extends SQLiteOpenHelper
{

    // Create a series of table and column names
    private static final String USER_TABLE = "movies";
    private static final String MOVIE_NAME = "movie_name";
    private static final String MOVIE_SYNOPSIS = "movie_synopsis";
    private static final String MOVIE_CAST = "movie_cast";
    private static final String MOVIE_GENRES = "movie_genres";
    private static final String MOVIE_RATING = "movie_rating";
    private static final String MOVIE_POSTER = "movie_poster";
    private static final String USERNAME = "username";

    public movies(Context context)
    {
        super(context, USER_TABLE, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        // Create the table
        String createTable = "CREATE TABLE " + USER_TABLE + " (" + MOVIE_NAME + " VARCHAR(50), " + MOVIE_SYNOPSIS + " VARCHAR(200), " + MOVIE_CAST + " VARCHAR(200), " + MOVIE_GENRES + " VARCHAR(200), " + MOVIE_RATING + " INTEGAR(5), " + MOVIE_POSTER + " VARCHAR(25), " + USERNAME + " VARCHAR(20))";
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

     */
    public boolean addData(String name, String synopsis, String cast, String genres, String rating, String poster, String username)
    {
        // Access the database so we can access SQL commands
        SQLiteDatabase db = this.getWritableDatabase();

        // Create an empty set of values that we can add information to
        ContentValues content = new ContentValues();

        // Add the information to the set
        content.put(MOVIE_NAME, name);
        content.put(MOVIE_SYNOPSIS, synopsis);
        content.put(MOVIE_CAST, cast);
        content.put(MOVIE_GENRES, genres);
        content.put(MOVIE_RATING, rating);
        content.put(MOVIE_POSTER, poster);
        content.put(USERNAME, username);

        // Try insert the contents of the content set into the database as a new row
        long result = db.insert(USER_TABLE, null, content);

        // return the result of the insertion
        return result != 1;
    }

    /**
     * Get all of the information from USER_TABLE
     * @return The information from USER_TABLE
     */
    public Cursor getAllData(String username)
    {
        // Access the database so we can access SQL commands
        SQLiteDatabase db = this.getWritableDatabase();

        // Create a query to get all of the data from USER_TABLE
        String query = "SELECT * FROM " + USER_TABLE + " WHERE " + USERNAME + " = " + username;

        // Return the result of the query as a Cursor object
        return db.rawQuery(query, null);
    }

    /**
     * Gets a row from the table
     * @param username The user that's being looked for
     */
    public void getRow(String username)
    {
        // Access the database so we can access SQL commands
        SQLiteDatabase db = this.getWritableDatabase();

        // Create a query to get a row from the table for a specific user
        //String query = "SELECT * FROM " + USER_TABLE + " WHERE " + USER_USERNAME + " = '" + username + "'";

        // Return the result of the query as a Cursor object
        //return db.rawQuery(query, null);
    }
}
