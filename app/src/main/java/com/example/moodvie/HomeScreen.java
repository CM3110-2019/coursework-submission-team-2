package com.example.moodvie;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.databases.movies;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Objects;

public class HomeScreen extends AppCompatActivity
{
    protected <T extends View> T getView(int id) { return super.findViewById(id);}
    functions _functions = new functions();
    private movies mdb = new movies(this);
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        // Get the bundle objects and set the username
        Bundle b = getIntent().getExtras();
        username = Objects.requireNonNull(b).getString("id");

        // Click listener for Settings button
        getView(R.id.HomeScreen_settingButton).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(getApplicationContext(), SettingsPage.class));
            }
        });

        // Click listener for Scan Movie button
        getView(R.id.scanButton).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(_functions.checkCameraHardware(getApplicationContext()))
                    startActivity(new Intent(getApplicationContext(), BarcodeScanner.class).putExtra("id", username));
                else
                    _functions.createMessage(getApplicationContext(), "No Camera Available");
            }
        });

        // Click listener for Face Scan button
        getView(R.id.faceButton).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(_functions.checkCameraHardware(getApplicationContext()))
                    startActivity(new Intent(getApplicationContext(), FaceScan.class));
                else
                    _functions.createMessage(getApplicationContext(), "No Camera Available");
            }
        });

        // Click listener for Search Movie button
        getView(R.id.searchButton).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                TextView search = getView(R.id.etSearchMovie);
                if(_functions.isBlank(null,null,null,search.getText().toString()))
                    _functions.createMessage(getApplicationContext(), "Enter a search query");
                else
                    _functions.createMessage(getApplicationContext(), "Do something");
            }
        });

        //_functions.createMessage(getApplicationContext(), username);
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        // Create a series of ArrayLists to hold movie information
        final ArrayList<String> movieNames = new ArrayList<>();
        final ArrayList<String> movieOverviews = new ArrayList<>();
        final ArrayList<String> movieCast = new ArrayList<>();
        final ArrayList<String> movieGenres = new ArrayList<>();
        final ArrayList<String> movieRatings = new ArrayList<>();
        final ArrayList<String> moviePosters = new ArrayList<>();

        // Get all of the user's stored movies from the movie database as a Cursor object
        final Cursor getAll = mdb.getAllData(username);

        // Iterate over the Cursor object and add the movie information to corresponding ArrayLists
        while(getAll != null && getAll.moveToNext())
        {
            movieNames.add(getAll.getString(0));
            movieOverviews.add(getAll.getString(1));
            movieCast.add(getAll.getString(2));
            movieGenres.add(getAll.getString(3));
            movieRatings.add(getAll.getString(4));
            moviePosters.add(getAll.getString(5));
        }

        // Get the grid layout and remove the old one so a new one can be created on each resume
        androidx.gridlayout.widget.GridLayout gridLayout = getView(R.id.gridLayout);
        gridLayout.removeAllViews();

        // Get how many ImageButtons need to be created
        int total = mdb.getNumberOfRows(username);

         /*
          * Set up the columns and rows for the grid layout.
          *
          * When it comes to creating the rows, Java will round a number down when integer division
          * occurs between 2 integer values therefore +1 will need to be added to it to create an
          * additional row.
          *
          * Example;
          * 5 movies are stored in the movie database and 3 movie posters are needed in each column
          * therefore the number rows needed are 5/3 ~=  1.6 rows this rounds to 1 because of
          * integer division so an additional row will need to be added (+1) to span 5 movies
          * across two rows.
          */
        int column = 3;
        int row = total / column + 1;
        gridLayout.setColumnCount(column);
        gridLayout.setRowCount(row);

        /*
         * Create a loop that iterates for the number of movies that someone has so that a grid view
         * can be dynamically added to with movie posters and information
         */
        for (int i = 0; i < total; i++)
        {
            // Create a temporary index to hold the value of i
            final int index = i;

            // Create a new ImageButton on each loop
            ImageButton imageButton = new ImageButton(this);

            // Click listener for when the ImageButton is clicked
            imageButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    /*
                     *  When the ImageButton is clicked open the MoviePage activity
                     *  and populate it with the movies information based on the index position
                     *  in the ArrayLists
                     */
                    Intent intent = new Intent(HomeScreen.this, MoviePage.class);
                    intent.putExtra("movieTitle", movieNames.get(index));
                    intent.putExtra("movieOverview", movieOverviews.get(index));
                    intent.putExtra("moviePoster", moviePosters.get(index));
                    intent.putExtra("movieRating", movieRatings.get(index));
                    intent.putExtra("movieCast", movieCast.get(index));
                    intent.putExtra("movieGenres", movieGenres.get(index));
                    intent.putExtra("username", username);
                    intent.putExtra("caller", "HomeScreen");
                    startActivity(intent);
                }
            });

            // Load the movie poster as the ImageButton image using Picasso
            Picasso.get().load("https://image.tmdb.org/t/p/w185" + moviePosters.get(index)).resize(0, 600).into(imageButton);

            // Space out the ImageButton with some padding
            imageButton.setPadding(0, 0, 12, 20);

            // Create the layout parameters of the grid and add it to the view
            GridLayout.LayoutParams gridParam = new GridLayout.LayoutParams(GridLayout.spec(GridLayout.UNDEFINED, 300), GridLayout.spec(GridLayout.UNDEFINED, 300));
            gridLayout.addView(imageButton, gridParam);
        }
    }
}