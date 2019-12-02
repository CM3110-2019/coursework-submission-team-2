package com.example.moodvie;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.databases.movies;
import com.example.objects.Person;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.logging.XMLFormatter;

public class HomeScreen extends AppCompatActivity
{
    // Return the super class of a views ID
    private <T extends View> T getView(int id) { return super.findViewById(id);}

    // Instantiate the movies database and functions class
    private final functions _functions = new functions();
    private final movies mdb = new movies(this);

    // Reference variable for a Person object
    private Person person;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        // De-serialise the person object stored in the Intent extras
        person = (Person) getIntent().getSerializableExtra("personClass");

        // Click listener for Settings button
        getView(R.id.HomeScreen_settingButton).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // Start the settings page and pass the person object to it
                startActivity(new Intent(getApplicationContext(), SettingsPage.class).putExtra("personClass", person));
            }
        });

        // Click listener for Scan Movie button
        getView(R.id.scanButton).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                /*
                 * If the user has a camera on their phone then open the barcode scanner otherwise
                 * notify them that there's no camera available to use
                 */
                if(_functions.checkCameraHardware(getApplicationContext()))
                    startActivity(new Intent(getApplicationContext(), BarcodeScanner.class).putExtra("personClass", person));
                else
                    _functions.createMessage(getApplicationContext(), getString(R.string.no_camera_available));
            }
        });

        // Click listener for Face Scan button
        getView(R.id.faceButton).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                /*
                 * If the user has a camera on their phone then open the face scanner otherwise
                 * notify them that there's no camera available to use
                 */
                if(_functions.checkCameraHardware(getApplicationContext()))
                    startActivity(new Intent(getApplicationContext(), FaceScan.class));
                else
                    _functions.createMessage(getApplicationContext(), getString(R.string.no_camera_available));
            }
        });

        // Click listener for Search Movie button
        getView(R.id.searchButton).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                // Get the search bar TextView and convert it into a String value
                TextView search = getView(R.id.etSearchMovie);
                String searchText = search.getText().toString();

                /*
                 * If the search bar is blank and the search button is clicked create a movie grid
                 * that displays all of the movies otherwise created a movie grid with the filtered
                 * movies
                 */
                if(_functions.isBlank(searchText))
                    // Create the movie grid with all the movies owned by the user
                    createMovieGrid(mdb.getAllData(person.getUsername()), mdb.getNumberOfRows(person.getUsername()));
                else
                    // Create the movie grid with the filtered movies owned by the user
                    createMovieGrid(mdb.filterMovies(person.getUsername(), searchText), mdb.filterCount(person.getUsername(), searchText));

            }
        });
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        // Create the movie grid with all the movies owned by the user
        createMovieGrid(mdb.getAllData(person.getUsername()), mdb.getNumberOfRows(person.getUsername()));
    }


    /**
     * Create a grid layout that will be dynamically populated with clickable movie posters so that
     * when the movie poster is clicked it will display the information about the movie
     *
     * @param movieCursor A Cursor object returned from a SQL query
     * @param total       The number of ImageButtons needing to be created
     */
    private void createMovieGrid(Cursor movieCursor, int total)
    {
        // Get the grid layout and remove the old one so a new one can be created on each resume
        androidx.gridlayout.widget.GridLayout gridLayout = getView(R.id.gridLayout);
        gridLayout.removeAllViews();

        // Create a series of ArrayLists to hold movie information
        final ArrayList<String> movieNames = new ArrayList<>();
        final ArrayList<String> movieOverviews = new ArrayList<>();
        final ArrayList<String> movieCast = new ArrayList<>();
        final ArrayList<String> movieGenres = new ArrayList<>();
        final ArrayList<String> movieRatings = new ArrayList<>();
        final ArrayList<String> moviePosters = new ArrayList<>();

        // Iterate over the Cursor object and add the movie information to corresponding ArrayLists
        while (movieCursor != null && movieCursor.moveToNext()) {
            movieNames.add(movieCursor.getString(0));
            movieOverviews.add(movieCursor.getString(1));
            movieCast.add(movieCursor.getString(2));
            movieGenres.add(movieCursor.getString(3));
            movieRatings.add(movieCursor.getString(4));
            moviePosters.add(movieCursor.getString(5));
        }

        /*
         * Set up the columns and rows for the grid layout.
         *
         * When it comes to creating the rows, Java will round a number down when integer division
         * occurs between 2 integer values therefore +1 will need to be added to it to create an
         * additional row.
         *
         * Example;
         * 5 movies are stored in the movie database and 3 movie posters are needed per row
         * therefore the number of rows needed is 5/3 ~=  1.6 this gets rounded to 1 because of
         * integer division so an additional row will need to be added (+1) to span 5 movies
         * across two rows.
         */
        int column = 3;
        int row = total / column + 1;
        gridLayout.setColumnCount(column);
        gridLayout.setRowCount(row);

        /*
         * Create a loop that iterates for the number of movies the user has so that the grid view
         * can be dynamically populated with movie posters and movie information
         */
        for (int i = 0; i < total; i++) {
            // Create a temporary index to hold the value of i
            final int index = i;

            // Create a new ImageButton on each loop
            ImageButton imageButton = new ImageButton(this);

            // Click listener for when the ImageButton is clicked
            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*
                     *  When the ImageButton is clicked open the MoviePage activity
                     *  and populate it with the movies information based on the index position
                     *  of the ArrayLists
                     */
                    Intent intent = new Intent(HomeScreen.this, MoviePage.class);
                    intent.putExtra("movieTitle", movieNames.get(index));
                    intent.putExtra("movieOverview", movieOverviews.get(index));
                    intent.putExtra("moviePoster", moviePosters.get(index));
                    intent.putExtra("movieRating", movieRatings.get(index));
                    intent.putExtra("movieCast", movieCast.get(index));
                    intent.putExtra("movieGenres", movieGenres.get(index));
                    intent.putExtra("personClass", person);
                    intent.putExtra("caller", "HomeScreen");
                    startActivity(intent);
                }
            });

            // Set the ImageButton image as the movie poster using Picasso
            Picasso.get().load("https://image.tmdb.org/t/p/w185" + moviePosters.get(index)).resize(0, 600).into(imageButton);

            // Space out the ImageButton with some padding
            imageButton.setPadding(0, 0, 12, 20);

            // Create the layout parameters of the grid and add it to the view
            GridLayout.LayoutParams gridParam = new GridLayout.LayoutParams(GridLayout.spec(GridLayout.UNDEFINED, 300), GridLayout.spec(GridLayout.UNDEFINED, 300));
            gridLayout.addView(imageButton, gridParam);
        }
    }
}