package com.example.moodvie;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActionBar;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.databases.movies;
import com.squareup.picasso.Picasso;

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

        Bundle b = getIntent().getExtras();
        username = Objects.requireNonNull(b).getString("id");

        // Click listener for settings button
        getView(R.id.HomeScreen_settingButton).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(getApplicationContext(), SettingsPage.class));
            }
        });

        // Click listener for scan movie button
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

        // Click listener for face scan button
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
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        int width = size.x;
        int height = size.y;

        androidx.gridlayout.widget.GridLayout gridLayout = getView(R.id.gridLayout);

        gridLayout.removeAllViews();
        int total = mdb.getNumberOfRows(username);
        int column = 3;
        int row = total / column;
        gridLayout.setColumnCount(column);
        gridLayout.setRowCount(row + 1);

        for (int i = 0, c = 0, r = 0; i < total; i++, c++)
        {
            if (c == column)
            {
                c = 0;
                r++;
            }

            gridLayout.removeAllViews();
            final Cursor getAll = mdb.getAllData(username);
            while(getAll != null && getAll.moveToNext())
            {

                ImageView oImageView = new ImageView(this);
                TextView oTextView = new TextView(this);

                Picasso.get().load("https://image.tmdb.org/t/p/w185" + getAll.getString(5)).resize(width, 600).into(oImageView);
                oImageView.setPadding(15, 0, 15, 15);

                GridLayout.Spec rowSpan = GridLayout.spec(GridLayout.UNDEFINED, 300);
                GridLayout.Spec colspan = GridLayout.spec(GridLayout.UNDEFINED, 300);
                if (r == 0 && c == 0) {
                    colspan = GridLayout.spec(GridLayout.UNDEFINED, 300);
                    rowSpan = GridLayout.spec(GridLayout.UNDEFINED, 300);
                }
                GridLayout.LayoutParams gridParam = new GridLayout.LayoutParams(rowSpan, colspan);
                gridLayout.addView(oImageView, gridParam);

                oImageView.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        _functions.createMessage(getApplicationContext(), getAll.getString(0));

                    }
                });
            }
        }
    }
}