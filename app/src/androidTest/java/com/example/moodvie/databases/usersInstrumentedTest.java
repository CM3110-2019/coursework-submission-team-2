package com.example.moodvie.databases;

import android.content.ContentResolver;
import android.content.Context;
import android.database.CharArrayBuffer;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.database.MatrixCursor;
import android.net.Uri;
import android.os.Bundle;
import android.widget.SimpleCursorAdapter;

import androidx.test.platform.app.InstrumentationRegistry;

import com.example.databases.movies;
import com.example.databases.users;
import com.example.moodvie.functions;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class usersInstrumentedTest
{
    private final functions _functions = new functions();
    private users userDatabase;
    private final Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();

    @Before
    public void setUp()
    {
        userDatabase = new users(context);
    }

    @After
    public void tearDown()
    {
        userDatabase.close();
    }

    @Test
    public void $addUser()
    {
        String username = "test";
        String password = "test";

        boolean output;


        output = userDatabase.addData(username, password);

        assertTrue(output);
    }

    @Test
    public void $movieExists()
    {
        String username = "test";
        boolean output = userDatabase.exists(username);
        assertTrue(output);
    }
}
