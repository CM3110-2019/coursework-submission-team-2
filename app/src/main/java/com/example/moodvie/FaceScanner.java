package com.example.moodvie;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.common.collect.ImmutableList;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.face.FirebaseVisionFace;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetector;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetectorOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FaceScanner extends AppCompatActivity
{
    // Return the super class of a views ID
    private <T extends View> T getView(int id) { return super.findViewById(id); }

    // Include the functions class
    private final functions _functions = new functions();

    // Create a placeholder for an ImageView
    private ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_scan);

        openCamera();

        // Click Listener for the retake image button
        getView(R.id.retakeImage).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                openCamera();
            }
        });
    }

    /**
     * A method used to open the camera of the phone
     */
    private void openCamera()
    {
        Intent imageTakeIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(imageTakeIntent.resolveActivity(getPackageManager())!= null)
        {
            startActivityForResult(imageTakeIntent, 101);
        }
    }
    
   /**
     * Capture the result of the image taken from the camera
     *
     * @param requestCode The camera request code
     * @param resultCode The result code of the camera
     * @param data The intent returned by the camera
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        // If the camera takes a photo
        if (requestCode == 101 && resultCode == RESULT_OK)
        {
            try
            {
                // Convert the image taken to a Bitmap and set the ImageView on the activity to it
                image = getView(R.id.bitmapImage);
                Bitmap photo = (Bitmap) Objects.requireNonNull(data.getExtras()).get("data");
                image.setImageBitmap(photo);

                // Run facial recognition on the Bitmap image
                runFaceScanner(photo);
            }
            catch(Exception e)
            {
                // do nothing
            }
        }

        // If the camera doesn't take a photo
        else
        {
            _functions.createMessage(getApplicationContext(), "Failed To Take The Image");
            finish();
        }

    }

    /**
     * Use Machine Learning to scan someones face that is found in a Bitmap image so that a smile
     * probability can be generated to determine a movie genre recommendation.
     *
     * @param img The bitmap image to run facial recognition on
     */
    private void runFaceScanner(final Bitmap img)
    {
        /*
         * Sets up the options of the face detector;
         *
         * - setClassificationMode(FirebaseVisionFaceDetectorOptions.ACCURATE) sets the classification
         *   to be accurate
         *
         * - setLandmarkMode(FirebaseVisionFaceDetectorOptions.ALL_LANDMARKS) detects the landmarks
         *   for a given face
         *
         * - setClassificationMode(FirebaseVisionFaceDetectorOptions.ALL_CLASSIFICATIONS) performs
         *   'eyes open' and 'smiling' classification
         *
         * - enableTracking() tracks faces between frames
         */
        FirebaseVisionFaceDetectorOptions options =
                new FirebaseVisionFaceDetectorOptions.Builder()
                        .setClassificationMode(FirebaseVisionFaceDetectorOptions.ACCURATE)
                        .setLandmarkMode(FirebaseVisionFaceDetectorOptions.ALL_LANDMARKS)
                        .setClassificationMode(FirebaseVisionFaceDetectorOptions.ALL_CLASSIFICATIONS)
                        .enableTracking()
                        .build();


        /*
         * Creates a FirebaseVisionImage from the Bitmap representation of an Image object where the
         * object in the image is already up-right (portrait mode) and no rotation is needed.
         */
        FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(img);


        /*
         * Create a detector to find human faces in an image and include the options that have
         * just been set for the detector
         */
        FirebaseVisionFaceDetector detector = FirebaseVision.getInstance().getVisionFaceDetector(options);
        detector.detectInImage(image).addOnSuccessListener(
                new OnSuccessListener<List<FirebaseVisionFace>>()
                {
                    // Get the required TextViews
                    final TextView smileProbability = getView(R.id.smileProbability);
                    final TextView recommended = getView(R.id.recommendedMovieGenre);

                    // Setup the genre ArrayLists
                    final ArrayList<String> allGenres = new ArrayList<>(ImmutableList.of("Comedy", "Action", "Thriller", "Mystery", "Romance", "Crime", "War", "Adventure", "Animation", "Documentary", "Drama", "Musical", "Sci-Fi", "Fantasy", "Western"));
                    final ArrayList<String> happyGenres = new ArrayList<>(ImmutableList.of("Comedy", "Action", "Thriller", "Adventure", "Animation", "Drama", "Musical", "Sci-Fi", "Fantasy", "Western"));
                    final ArrayList<String> neutralGenres = new ArrayList<>(ImmutableList.of("Action", "Mystery", "Crime", "War", "Adventure", "Animation", "Documentary", "Drama", "Musical", "Sci-Fi", "Fantasy", "Western"));
                    final ArrayList<String> cheerUpGenres = new ArrayList<>(ImmutableList.of("Comedy", "Action", "Thriller", "Adventure", "Musical"));


                    @Override
                    public void onSuccess(List<FirebaseVisionFace> faces)
                    {
                        // Loop through each of the faces found and get the smile probability
                        for(FirebaseVisionFace face: faces)
                        {
                            // Get the faces smile probability and set the text of the smileProbability TextView
                            float probability = face.getSmilingProbability();
                            smileProbability.setText(getString(R.string.smile_probability, Float.toString(probability)));

                            /*
                             * If the smile probability is <= 0.4 then we are just going to assume that the
                             * person in question is not in a good mood ie. Sad, Angry, Frustrated so they will
                             * be recommended a random movie genre from the 'cheerUpGenres' ArrayList.
                             *
                             * If the smile probability is > 0.4  and < 0.6 then a random genre will be recommended from the
                             * 'neutralGenres' ArrayList because they are in some-what of a neutral mood.
                             *
                             * If the smile probability is > 0.6  then a random genre will be recommended from the
                             * 'happyGenres' ArrayList because they are in some-what of a neutral mood.
                             *
                             * Once a genre has been determined then the text of the recommended TextView will be
                             * updated accordingly.
                             */
                            if (probability >= 0 && probability <= 0.4)
                                recommended.setText(getString(R.string.recommended_movie_genre, cheerUpGenres.get((int) (Math.random() * cheerUpGenres.size()))));
                            else if (probability > 0.4 && probability <= 0.6)
                                recommended.setText(getString(R.string.recommended_movie_genre, neutralGenres.get((int) (Math.random() * allGenres.size()))));
                            else
                                recommended.setText(getString(R.string.recommended_movie_genre, happyGenres.get((int) (Math.random() * allGenres.size()))));

                        }
                    }
                });
    }
}
