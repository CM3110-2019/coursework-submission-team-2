package com.example.moodvie;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Interpolator;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.BounceInterpolator;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.ImageRequest;
import com.iarcuschin.simpleratingbar.SimpleRatingBar;
import com.squareup.picasso.Picasso;

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
        final String moviePoster = b.getString("moviePoster");
        final float movieRating = Float.parseFloat(b.getString("movieRating"));

        // Grab the tvMovieTitle (TextView) and set the movie title
        TextView title = getView(R.id.tvMovieTitle);
        title.setText(movieTitle);

        // Grab the tvMovieDesc (TextView) and set the movie synopsis
        TextView overview = getView(R.id.tvMovieDesc);
        overview.setText(movieOverview);

        // Grab the ivMovePoster (ImageView) and set the movie poster
        ImageView movieImage = getView(R.id.ivMovePoster);
        Picasso.get().load("https://image.tmdb.org/t/p/w185"+ moviePoster).resize(277,219).into(movieImage);

        // Grab the movieRatingBar (SimpleRatingBar) and animate it
        SimpleRatingBar rating = getView(R.id.movieRatingBar);
        animateRatingBar(rating, Math.round(movieRating)/2f);

        //_functions.createMessage(getApplicationContext(), String.valueOf(rating.getRating()));
    }

    /**
     * Adds a 2 second animation to a SimpleRatingBar rating bar that wont repeat and
     * has a bounce effect
     *
     * @param ratingBar the rating bar to animate
     * @param target the number of stars
     */
    private void animateRatingBar(SimpleRatingBar ratingBar, Float target)
    {
        SimpleRatingBar.AnimationBuilder builder = ratingBar.getAnimationBuilder()
                .setRatingTarget(target)
                .setDuration(2000)
                .setRepeatCount(0)
                .setInterpolator(new BounceInterpolator());
        builder.start();
        ratingBar.setRating(target);
    }
}
