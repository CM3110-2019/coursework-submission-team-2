package com.example.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class movies extends SQLiteOpenHelper
{

    // Create a table with a series of column names
    private static final String MOVIE_TABLE = "movies";
    private static final String MOVIE_NAME = "movie_name";
    private static final String MOVIE_SYNOPSIS = "movie_synopsis";
    private static final String MOVIE_CAST = "movie_cast";
    private static final String MOVIE_GENRES = "movie_genres";
    private static final String MOVIE_RATING = "movie_rating";
    private static final String MOVIE_POSTER = "movie_poster";
    private static final String USERNAME = "username";

    public movies(Context context)
    {
        super(context, MOVIE_TABLE, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        // Create the table
        String createTable = "CREATE TABLE " + MOVIE_TABLE + " (" + MOVIE_NAME + " VARCHAR(75), " + MOVIE_SYNOPSIS + " VARCHAR(200), " + MOVIE_CAST + " VARCHAR(200), " + MOVIE_GENRES + " VARCHAR(200), " + MOVIE_RATING + " INTEGAR(5), " + MOVIE_POSTER + " VARCHAR(25), " + USERNAME + " VARCHAR(20))";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        // If the table already exists then drop it
        db.execSQL("DROP TABLE IF EXISTS " + MOVIE_TABLE);
    }

    /**
     * Add a movie into the database as a new row by setting up a ContentValues() set to store the
     * movie information
     *
     * @param name      The name of the movie
     * @param synopsis  The overview of the movie
     * @param cast      The cast of the movie
     * @param genres    The genre of the movie
     * @param rating    The rating of the movie
     * @param poster    The location to the movies poster path
     * @param username  The user who owns the movie
     * @return          The status of the insert
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
        long result = db.insert(MOVIE_TABLE, null, content);

        // return the result of the insertion
        return result != 1;
    }

    /**
     * Get all of the information from the MOVIE_TABLE for a specific user
     *
     * @param username The user that owns the movie
     * @return         All the rows that user is found in
     */
    public Cursor getAllData(String username)
    {
        // Access the database so we can access SQL commands
        SQLiteDatabase db = this.getWritableDatabase();

        // Create a query to get all of the data from MOVIE_TABLE
        String query = "SELECT * FROM " + MOVIE_TABLE + " WHERE " + USERNAME + " = '" + username + "'";

        // Return the result of the query as a Cursor object
        return db.rawQuery(query, null);
    }

    /**
     * Gets a row from the table
     *
     * @param username The user that's being looked for
     */
    public Cursor getRow(String username)
    {
        // Access the database so we can access SQL commands
        SQLiteDatabase db = this.getWritableDatabase();

        // Create a query to get a row from the table for a specific user
        String query = "SELECT * FROM " + MOVIE_TABLE + " WHERE " + USERNAME + " = '" + username + "'";

        // Return the result of the query as a Cursor object
        return db.rawQuery(query, null);
    }

    /**
     * Check if a movie already exists in the table and is owned by the specified user
     *
     * @param username The username of who owns the movie
     * @param movie    The name of the movie that is being searched for
     * @return         True if the movie if they already own the movie else False
     */
    public Boolean movieExists(String username, String movie)
    {
        // Access the database so we can access SQL commands
        SQLiteDatabase db = this.getWritableDatabase();

        // Create a query to check if a specific user exists in USER_TABLE
        String query = "SELECT * FROM " + MOVIE_TABLE + " WHERE " + MOVIE_NAME + " = '" + movie + "'" + " and " + USERNAME + " = '" + username + "'";

        // Create a Cursor object from the query
        Cursor cursor = db.rawQuery(query, null);

        //
        return getCount(cursor) > 0;
    }

    /**
     * Delete a users movie out of the table
     *
     * @param username The username of who owns the movie
     * @param movie    The name of the movie that is being searched for
     * @return         True if the movie has been delete else False
     */
    public boolean deleteMovie(String username, String movie)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(MOVIE_TABLE, MOVIE_NAME +  "=? and " + USERNAME + "=?", new String[]{movie, username}) > 0;
    }

    /**
     * Get all the rows a specified user in the table and return how many rows they have
     *
     * @param username The username of who owns the movie
     * @return         The number of rows the user is found in
     */
    public int getNumberOfRows(String username)
    {
        // Access the database so we can access SQL commands
        SQLiteDatabase db = this.getReadableDatabase();

        // Create a query to get a row from the table for a specific user
        String query = "SELECT * FROM " + MOVIE_TABLE + " WHERE " + USERNAME + " = '" + username + "'";

        // Create a Cursor object from the query
        Cursor cursor = db.rawQuery(query, null);

        // Return the number of rows
        return getCount(cursor);
    }

    /**
     * Get the number of rows found in a Cursor object
     *
     * @param c A cursor object returned from a SQL query
     * @return  The number of rows found in the cursor
     */
    public int getCount(Cursor c)
    {
        return c.getCount();
    }

    public void changeMovieOwner(String username, String newUsername)
    {
        // Access the database so we can access SQL commands
        SQLiteDatabase db = this.getWritableDatabase();

        // Create a query to get a row from the table for a specific user
        String query = "UPDATE " + MOVIE_TABLE + " SET " + USERNAME + " = '" + newUsername + "'" + " WHERE " + USERNAME + " = '" + username + "'";
        // Return the result of the query as a Cursor object
        db.execSQL(query);
    }
}
