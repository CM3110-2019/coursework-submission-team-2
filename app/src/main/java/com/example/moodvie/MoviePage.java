package com.example.moodvie;

import androidx.appcompat.app.AppCompatActivity;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.databases.movies;
import com.iarcuschin.simpleratingbar.SimpleRatingBar;
import com.squareup.picasso.Picasso;

public class MoviePage extends AppCompatActivity
{
    protected <T extends View> T getView(int id) { return super.findViewById(id); }
    private functions _functions = new functions();
    private movies mdb = new movies(this);

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
        final String movieCast = b.getString("movieCast");
        final String movieGenres = b.getString("movieGenres");
        final String username = b.getString("username");

        // Grab the tvMovieTitle (TextView) and set the movie title
        final TextView title = getView(R.id.tvMovieTitle);
        title.setText(movieTitle);

        // Grab the tvMovieDesc (TextView) and set the movie synopsis
        final TextView overview = getView(R.id.tvMovieDesc);
        overview.setText(movieOverview);

        // Grab the ivMovePoster (ImageView) and set the movie poster
        ImageView movieImage = getView(R.id.ivMovePoster);
        Picasso.get().load("https://image.tmdb.org/t/p/w185"+ moviePoster).resize(277,219).into(movieImage);

        // Grab the movieRatingBar (SimpleRatingBar) and animate it
        final SimpleRatingBar rating = getView(R.id.movieRatingBar);
        animateRatingBar(rating, Math.round(movieRating)/2f);

        // Grab the tvCast (TextView) and set the movie cast
        final TextView cast = getView(R.id.tvCast);
        cast.setText(movieCast);

        ImageButton addMovie = getView(R.id.btnAddMovie);
        addMovie.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(mdb.addData(movieTitle, movieOverview, movieCast, movieGenres, String.valueOf(rating.getRating()), moviePoster, username))
                {
                    _functions.createMessage(getApplicationContext(), "Added Movie Successfully");
                    finish();
                }
            }
        });
        //_functions.createMessage(getApplicationContext(), movieGenres);
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
                .setInterpolator(new FastOutSlowInInterpolator());
        builder.start();
        ratingBar.setRating(target);
    }
}
