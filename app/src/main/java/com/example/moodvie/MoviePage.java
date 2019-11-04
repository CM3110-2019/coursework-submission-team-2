package com.example.moodvie;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.ImageRequest;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class MoviePage extends AppCompatActivity
{
    protected <T extends View> T getView(int id) { return super.findViewById(id); }
    private functions _functions = new functions();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_page);

        // Get the bundle objects
        Bundle b = getIntent().getExtras();
        if (b == null)
            throw new AssertionError("Bundle cannot be empty");

        // Get the information from the bundle
        final String movieTitle = b.getString("movieTitle");
        final String movieOverview = b.getString("movieOverview");

        TextView title = getView(R.id.tvMovieTitle);
        title.setText(movieTitle);

        TextView overview = getView(R.id.tvMovieDesc);
        overview.setText(movieOverview);
    }
}
