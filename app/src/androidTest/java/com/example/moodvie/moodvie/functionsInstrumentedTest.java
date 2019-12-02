package com.example.moodvie.moodvie;

import android.content.Context;
import android.os.IBinder;
import android.view.WindowManager;

import androidx.test.espresso.Root;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.databases.users;
import com.example.moodvie.HomeScreen;
import com.example.moodvie.functions;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static android.app.PendingIntent.getActivity;
import static android.service.autofill.Validators.not;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class functionsInstrumentedTest
{
    private final functions _functions = new functions();
    private final Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();

    @Test
    public void $checkForCamera()
    {
        boolean output = _functions.checkCameraHardware(context);
        assertTrue(output);
    }
}