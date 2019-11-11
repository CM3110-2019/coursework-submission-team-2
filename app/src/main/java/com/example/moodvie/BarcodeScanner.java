package com.example.moodvie;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.zxing.Result;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class BarcodeScanner extends Activity implements ZXingScannerView.ResultHandler
{
    private ZXingScannerView mScannerView;
    functions _functions = new functions();

    @Override
    public void onCreate(Bundle state)
    {
        super.onCreate(state);
        mScannerView = new ZXingScannerView(this);
        setContentView(mScannerView);
    }

    @Override
    public void onResume()
    {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }

    @Override
    public void onPause()
    {
        super.onPause();
        mScannerView.stopCamera();
    }

    @Override
    public void handleResult(Result result)
    {
        // Start the APIs
        upcitemdbAPI(getResources().getString(R.string.upcitemdbAPI, result.getText()));
    }

    /**
     * Get the TMDB movie ID based off the movie title
     * @param url the API url
     */
    public void tmdbAPIGetID(String url)
    {
        // Create a Volley RequestQueue object to contain HTTP requests
        RequestQueue mRequestQueue = Volley.newRequestQueue(this);
        mScannerView.resumeCameraPreview(this);


        // Prepare and parse the HTTP request into a string
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>()
        {
            /**
             * Get the response of the HTTP request and handle it
             * @param response The HTTP response
             */
            @Override
            public void onResponse(String response)
            {
                try
                {
                    // Set up an initial JSONObject to map the HTTP response since it returns JSON
                    JSONObject rootObject = new JSONObject(response);

                    // Map the results array and fetch the '0' JSON object
                    JSONArray results = rootObject.getJSONArray("results");
                    JSONObject zero = results.getJSONObject(0);

                    // Call the tmdbAPIMovieInformation method and pass the movie ID into the URL
                    tmdbAPIMovieInformation(getResources().getString(R.string.tmdbAPIMovieInformation, zero.getString("id")));
                }

                /*
                 * If the barcode isn't found then a JSONException error is thrown.
                 *
                 * Finish the activity and notify the user that the barcode wasn't found
                 */
                catch (JSONException e)
                {
                    finish();
                    _functions.createMessage(getApplicationContext(), "This Barcode Was Not Found In The Database");
                }
            }
        }, new Response.ErrorListener()
        {
            /*
             * If the HTTP request fails to establish a response then a VolleyError is thrown.
             *
             * Finish the activity and notify the user to check they have an active
             * Internet connection
             */
            @Override
            public void onErrorResponse(VolleyError error)
            {
                finish();
                _functions.createMessage(getApplicationContext(), "Can't Connect To API - Check You Are Connected To The Internet");
            }
        });

        // Add the HTTP request to the request queue
        mRequestQueue.add(stringRequest);
    }

    /**
     * Get the movie information based on the movie ID
     * @param url the API url passed in
     */
    public void tmdbAPIMovieInformation(String url)
    {
        // Create a Volley RequestQueue object to contain HTTP requests
        RequestQueue mRequestQueue = Volley.newRequestQueue(this);
        mScannerView.resumeCameraPreview(this);

        // Prepare and parse the HTTP request into a string using StringRequest
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>()
        {
            /**
             * Get the response of the HTTP request and handle it
             * @param response The HTTP response
             */
            @Override
            public void onResponse(String response)
            {
                try
                {
                    // Set up an initial JSONObject to map the HTTP response since it returns JSON
                    JSONObject rootObject = new JSONObject(response);

                    // Set up the credits JSON object and cast JSON array
                    JSONObject creditsObject = rootObject.getJSONObject("credits");
                    JSONArray cast = creditsObject.getJSONArray("cast");


                    /*
                     *  Get the first 10 cast members that star in the movie and store them in
                     *  an ArrayList. Only showing the first ten due to there potentially being
                     *  hundreds of cast and the first 10 typically tend to be the most "important".
                     *
                     *  To show all the cast members just change the for loop from;
                     *  for(int i=0; i<10;i++) -> for(int i=0; i<cast.length();i++)
                     */
                    ArrayList<String> castList = new ArrayList<>();
                    for(int i=0; i<10;i++)
                    {
                        JSONObject castMember = cast.getJSONObject(i);
                        castList.add(castMember.getString("name"));
                    }

                    /*
                     *  Create a StringBuilder object to combine all the cast names into one string.
                     *
                     *  StringBuilder is being used for performance improvements and because
                     *  StringBuilder is mutable (modify-able).
                     */
                    StringBuilder stringBuilder = new StringBuilder();
                    for (String name : castList)
                    {
                        stringBuilder.append(name).append(", ");
                    }

                    /*
                     *  Get basic information from the root object;
                     *  movie title, synopsis, poster, rating
                     */
                    String title = rootObject.getString("original_title");
                    String overview = rootObject.getString("overview");
                    String poster = rootObject.getString("poster_path");
                    String vote_average = rootObject.getString("vote_average");

                    /*
                     *  Start the Movie Page Activity and pass intent extras so that the activity
                     *  can receive the movie information
                     */
                    Intent intent = new Intent(BarcodeScanner.this, MoviePage.class);
                    intent.putExtra("movieTitle", title);
                    intent.putExtra("movieOverview", overview);
                    intent.putExtra("moviePoster", poster);
                    intent.putExtra("movieRating", vote_average);
                    intent.putExtra("movieCast", stringBuilder.toString());
                    startActivity(intent);
                }

                /*
                    If the barcode isn't found then a JSONException error is thrown.

                    Finish the activity and notify the user that the barcode wasn't found
                 */
                catch (JSONException e)
                {
                    finish();
                    _functions.createMessage(getApplicationContext(), "This Barcode Was Not Found In The Database");
                }
            }
        }, new Response.ErrorListener()
        {
            /*
             * If the HTTP request fails to establish a response then a VolleyError is thrown.
             *
             * Finish the activity and notify the user to check they have an active
             * Internet connection
             */
            @Override
            public void onErrorResponse(VolleyError error)
            {
                finish();
                _functions.createMessage(getApplicationContext(), "Can't Connect To API - Check You Are Connected To The Internet");
            }
        });

        // Add the HTTP request to the request queue
        mRequestQueue.add(stringRequest);
    }

    public void upcitemdbAPI(String url)
    {
        // Create a Volley RequestQueue object to contain HTTP requests
        RequestQueue mRequestQueue = Volley.newRequestQueue(this);
        mScannerView.resumeCameraPreview(this);


        // Prepare and parse the HTTP request into a string
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>()
        {
            /**
             * Get the response of the HTTP request and handle it
             * @param response The HTTP response
             */
            @Override
            public void onResponse(String response)
            {
                try
                {
                    // Set up an initial JSONObject to map the HTTP response since it returns JSON
                    JSONObject rootObject = new JSONObject(response);

                    // Set up items JSON array and '0' JSON object
                    JSONArray items = rootObject.getJSONArray("items");
                    JSONObject zero = items.getJSONObject(0);

                    /*
                     *  Get the movie title and strip it of special character that can sometimes
                     *  appear such as '()' '[]' using regex
                     */
                    String movieTitle = zero.getString("title").replaceAll("\\(.*?\\) ?", "");

                    // Call the tmdbAPIGetID method and pass the movie title into the URL
                    tmdbAPIGetID(getResources().getString(R.string.tmdbAPIGetID, movieTitle));
                }

                /*
                 * If the barcode isn't found then a JSONException error is thrown.
                 *
                 * Finish the activity and notify the user that the barcode wasn't found
                 */
                catch (JSONException e)
                {
                    finish();
                    _functions.createMessage(getApplicationContext(), "This Barcode Was Not Found In The Database");
                }
            }
        }, new Response.ErrorListener()
        {
            /*
             * If the HTTP request fails to establish a response then a VolleyError is thrown.
             *
             * Finish the activity and notify the user to check they have an active
             * Internet connection
             */
            @Override
            public void onErrorResponse(VolleyError error)
            {
                finish();
                _functions.createMessage(getApplicationContext(), "Can't Connect To API - Check You Are Connected To The Internet");
            }
        });

        // Add the HTTP request to the request queue
        mRequestQueue.add(stringRequest);
    }
}