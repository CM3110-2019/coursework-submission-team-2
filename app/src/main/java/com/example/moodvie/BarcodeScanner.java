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

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class BarcodeScanner extends Activity implements ZXingScannerView.ResultHandler {
    private ZXingScannerView mScannerView;
    functions _functions = new functions();

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        mScannerView = new ZXingScannerView(this); // Programmatically initialize the scanner view
        setContentView(mScannerView); // Set the scanner view as the content view
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this); // Register a handler for scan results.
        mScannerView.startCamera(); // Start camera on resume
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera(); // Stop camera on pause
    }

    @Override
    public void handleResult(Result result)
    {
        upcitemdbAPI(getResources().getString(R.string.upcitemdbAPI, result.getText()));

    }

    public void tmdbAPI(String url)
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
                    // Set up a JSONObject to map the HTTP response since it returns JSON
                    JSONObject rootObject = new JSONObject(response);
                    JSONArray results = rootObject.getJSONArray("results");
                    JSONObject zero = results.getJSONObject(0);

                    String title = zero.getString("title");
                    String overview = zero.getString("overview");
                    String poster = zero.getString("poster_path");
                    String vote_average = zero.getString("vote_average");

                    Intent i = new Intent(BarcodeScanner.this, MoviePage.class);
                    i.putExtra("movieTitle", title);
                    i.putExtra("movieOverview", overview);
                    i.putExtra("moviePoster", poster);
                    i.putExtra("movieRating", vote_average);
                    // Start the activity
                    startActivity(i);
                }

                /*
                    If the barcode isn't found then a JSONException error is thrown, this error
                    will be handled by finishing the activity and notifying the user that the
                    barcode wasn't found
                 */ catch (JSONException e) {
                    finish();
                    _functions.createMessage(getApplicationContext(), "Barcode Not Found In Database");
                }
            }
        }, new Response.ErrorListener() {
            /*
                If HTTP request fails to establish a response then we need to handle the exception
                by finishing the activity and notifying the user to check they have an active
                Internet connection
             */
            @Override
            public void onErrorResponse(VolleyError error) {
                finish();
                _functions.createMessage(getApplicationContext(), "Can't Connect To API");
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
                    // Set up a JSONObject to map the HTTP response since it returns JSON
                    JSONObject rootObject = new JSONObject(response);
                    JSONArray items = rootObject.getJSONArray("items");
                    JSONObject zero = items.getJSONObject(0);

                    String movieTitle = zero.getString("title").replaceAll("\\(.*?\\) ?", "");

                    tmdbAPI(getResources().getString(R.string.tmdbAPI, movieTitle));
                }

                /*
                    If the barcode isn't found then a JSONException error is thrown, this error
                    will be handled by finishing the activity and notifying the user that the
                    barcode wasn't found
                 */ catch (JSONException e) {
                    finish();
                    _functions.createMessage(getApplicationContext(), "Barcode Not Found In Database");
                }
            }
        }, new Response.ErrorListener() {
            /*
                If HTTP request fails to establish a response then we need to handle the exception
                by finishing the activity and notifying the user to check they have an active
                Internet connection
             */
            @Override
            public void onErrorResponse(VolleyError error) {
                finish();
                _functions.createMessage(getApplicationContext(), "Can't Connect To API");
            }
        });

        // Add the HTTP request to the request queue
        mRequestQueue.add(stringRequest);
    }
}