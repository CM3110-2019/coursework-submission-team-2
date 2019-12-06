package com.example.moodvie.moodvie;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;

import com.example.moodvie.functions;

import org.junit.Test;

import static android.app.PendingIntent.getActivity;
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