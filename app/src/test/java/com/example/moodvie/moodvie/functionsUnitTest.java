package com.example.moodvie.moodvie;

import com.example.moodvie.functions;

import org.junit.Test;

import static org.junit.Assert.*;

public class functionsUnitTest
{
    private functions _functions = new functions();
    @Test
    public void isBlank()
    {
        Boolean output;
        String message = "";
        output= _functions.isBlank(message);

        assertTrue(output);
    }
}
