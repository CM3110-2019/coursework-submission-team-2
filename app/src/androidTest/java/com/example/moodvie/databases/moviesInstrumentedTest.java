package com.example.moodvie.databases;

import android.content.Context;
import android.database.Cursor;

import androidx.test.platform.app.InstrumentationRegistry;

import com.example.databases.movies;
import com.example.databases.users;
import com.example.moodvie.functions;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class moviesInstrumentedTest
{
    private final functions _functions = new functions();
    private movies movieDatabase;
    private final Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();

    @Before
    public void setUp()
    {
        movieDatabase = new movies(context);
    }

    @After
    public void tearDown()
    {
        movieDatabase.close();
    }

    @Test
    public void $addMovieToDatabase()
    {
        String movieName = "timmy";
        String movieDesc = "timmy";
        String movieCast = "timmy, timmy";
        String movieGenre = "timmy";
        String movieRating = "5";
        String moviePoster = "timmy";
        String username = "timmy";

        boolean output;


        output = movieDatabase.addData(movieName, movieDesc, movieCast, movieGenre, movieRating, moviePoster, username);

        assertTrue(output);
    }

    @Test
    public void $getNumberOfRows()
    {
        assertEquals(1, movieDatabase.getNumberOfRows("timmy"));
    }

    @Test
    public void $changeMovieOwner()
    {
        String username = "test";
        String newUsername = "timmy";

        movieDatabase.changeMovieOwner(username, newUsername);
    }

    @Test
    public void $getAllData()
    {
        Cursor cursor = movieDatabase.getAllData("timmy");
    }

    @Test
    public void $movieExists()
    {
        String username = "timmy";
        String movie = "timmy";
        boolean output = movieDatabase.movieExists(username, movie);
        assertTrue(output);
    }

    @Test
    public void $deleteMovie()
    {
        String username = "timmy";
        String movie = "timmy";
        boolean output = movieDatabase.deleteMovie(username, movie);
        assertTrue(output);
    }
}
