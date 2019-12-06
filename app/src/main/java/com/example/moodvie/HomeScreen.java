package com.example.moodvie;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import com.example.databases.movies;
import com.example.objects.Person;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

public class HomeScreen extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
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
        setContentView(R.layout.activity_side_menu);

        // De-serialise the person object stored in the Intent extras
        person = (Person) getIntent().getSerializableExtra("personClass");

        // Set the TextView on the page to display who owns the movies
        TextView ownedBy = getView(R.id.movies_owned_by);
        ownedBy.setText(getString(R.string.movies_owned_by, person.getUsername()));

        // Create the toolbar and side navigation drawer
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

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
            Picasso.get().load(getString(R.string.image_path, moviePosters.get(index))).resize(0, 600).into(imageButton);

            // Space out the ImageButton with some padding
            imageButton.setPadding(0, 0, 12, 20);

            // Create the layout parameters of the grid and add it to the view
            GridLayout.LayoutParams gridParam = new GridLayout.LayoutParams(GridLayout.spec(GridLayout.UNDEFINED, 300), GridLayout.spec(GridLayout.UNDEFINED, 300));
            gridLayout.addView(imageButton, gridParam);
        }
    }

    @Override
    public void onBackPressed()
    {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START))
            drawer.closeDrawer(GravityCompat.START);
        else
            super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.side_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_settings)
        {
            overridePendingTransition( 0, 0);
            startActivity(new Intent(getApplicationContext(), SettingsPage.class).putExtra("personClass", person));
            overridePendingTransition( 0, 0);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item)
    {
        // Handle the button clicks on the navigation menu
        int id = item.getItemId();

        switch(id)
        {
            // If the home button was pressed
            case R.id.nav_home:
                overridePendingTransition( 0, 0);
                startActivity(new Intent(getApplicationContext(), HomeScreen.class).putExtra("personClass", person));
                finish();
                overridePendingTransition( 0, 0);
                break;

            // If the face scanner button was pressed
            case R.id.nav_face:
                if(_functions.checkCameraHardware(getApplicationContext()))
                {
                    overridePendingTransition( 0, 0);
                    startActivity(new Intent(getApplicationContext(), FaceScanner.class).putExtra("personClass", person));
                    overridePendingTransition( 0, 0);
                }
                else
                    _functions.createMessage(getApplicationContext(), getString(R.string.no_camera_available));
                break;

            // If the barcode scanner button was pressed
            case R.id.nav_barcode:
                if(_functions.checkCameraHardware(getApplicationContext()))
                {
                    overridePendingTransition( 0, 0);
                    startActivity(new Intent(getApplicationContext(), BarcodeScanner.class).putExtra("personClass", person));
                    overridePendingTransition( 0, 0);
                }
                else
                    _functions.createMessage(getApplicationContext(), getString(R.string.no_camera_available));
                break;
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
