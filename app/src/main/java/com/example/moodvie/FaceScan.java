package com.example.moodvie;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
//import android.media.Image;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.net.wifi.hotspot2.pps.Credential;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
//import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;

import java.util.List;


//google imports
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.fasterxml.jackson.core.JsonFactory;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.InputStreamContent;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.storage.StorageScopes;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.BucketInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.google.cloud.vision.v1.AnnotateImageRequest;
import com.google.cloud.vision.v1.AnnotateImageResponse;
import com.google.cloud.vision.v1.BatchAnnotateImagesResponse;
import com.google.cloud.vision.v1.FaceAnnotation;
import com.google.cloud.vision.v1.Feature;
import com.google.cloud.vision.v1.Feature.Type;
import com.google.cloud.vision.v1.Image;
import com.google.cloud.vision.v1.ImageAnnotatorClient;

import static java.lang.System.getProperties;
import static java.nio.charset.StandardCharsets.UTF_8;

import com.google.api.services.storage.StorageScopes;
import com.google.api.services.storage.model.StorageObject;

import com.google.protobuf.ByteString;

import org.json.JSONObject;

import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;

//end google imports






public class FaceScan extends AppCompatActivity
{
    functions _functions = new functions();
    private ImageView mimageView;
    private static final int REQUEST_IMAGE_CAPTURE = 101;

    public FaceScan() throws IOException {
    }

    protected <T extends View> T getView(int id) { return super.findViewById(id); }


    //FACE SCAN CODE -----------------------------------------------------------------
    private static final String TARGET_URL =
            "https://vision.googleapis.com/v1/images:annotate?";
    private static final String API_KEY =
            "key=3cc1f5d8e982451e13dcb53e751cf6ef7b793f03";

    URL serverUrl = new URL(TARGET_URL + API_KEY);
    URLConnection urlConnection = serverUrl.openConnection();
    HttpURLConnection httpConnection = (HttpURLConnection)urlConnection;


    private static Properties properties;
    private static Storage storage;

    private static final String PROJECT_ID_PROPERTY = "tough-star-256909";
    private static final String APPLICATION_NAME_PROPERTY = "Moodvie";
    private static final String ACCOUNT_ID_PROPERTY = "moodvie@tough-star-256909.iam.gserviceaccount.com";
    private static final String PRIVATE_KEY_PATH_PROPERTY = "H:\\APP DEV\\coursework-submission-team-2\\app\\src\\main\\resources\\tough-star-256909-1296aac295fb.json";


    //FACE SCAN CODE FINISHED --------------------------------------------------------------------------------


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_scan);
        mimageView = findViewById(R.id.camera);
        getView(R.id.takePicture).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                takePicture(view);
            }
        });
        getView(R.id.use_image).setOnClickListener(new View.OnClickListener()
        {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view)
            {

            uploadCloud();

            }
        });

    }

    public void takePicture(View view)
    {
        Intent imageTakeIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(imageTakeIntent.resolveActivity(getPackageManager())!= null)
        {
            startActivityForResult(imageTakeIntent, REQUEST_IMAGE_CAPTURE);
        }
    }


    public void uploadCloud(){
//        Storage storage = StorageOptions.getDefaultInstance().getService();
//        BlobId blobId = BlobId.of("my-images-bucket-1604113", "blob_name");
//        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("text/plain").build();
//        Blob blob = storage.create(blobInfo, "Hello, Cloud Storage!".getBytes(UTF_8));
        ImageView bmp = getView(R.id.bitmapImage);
        bmp.setImageDrawable(getDrawable(R.drawable.face));




        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                try  {
                    Storage storage = StorageOptions.getDefaultInstance().getService();

// The name of a bucket, e.g. "my-bucket"
                    String bucketName = "my-images-bucket-1604113";

// Select all fields
// Fields can be selected individually e.g. Storage.BucketField.NAME
                    Bucket bucket = storage.get(bucketName, Storage.BucketGetOption.fields(Storage.BucketField.values()));
                    Log.d("myApp", "HERE");

                    File fi = new File("H:\\APP DEV\\coursework-submission-team-2\\app\\src\\main\\res\\drawable\\face.jpg");
                    Log.d("myApp", fi.getAbsolutePath());
                    Path path = fi.toPath();

                    BlobId blobId = BlobId.of(bucketName, "blob_name_image");
                    BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("image/jpeg").build();
                    Blob blob = storage.create(blobInfo, Files.readAllBytes(fi.toPath()));


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();


    }




    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static void detectFaces(String filePath, PrintStream out) throws Exception, IOException {
        List<AnnotateImageRequest> requests = new ArrayList<>();

        ByteString imgBytes = ByteString.readFrom(new FileInputStream(filePath));

        Image img = Image.newBuilder().setContent(imgBytes).build();
        Feature feat = Feature.newBuilder().setType(Type.FACE_DETECTION).build();
        AnnotateImageRequest request =
                AnnotateImageRequest.newBuilder().addFeatures(feat).setImage(img).build();
        requests.add(request);

        try (ImageAnnotatorClient client = ImageAnnotatorClient.create()) {
            BatchAnnotateImagesResponse response = client.batchAnnotateImages(requests);
            List<AnnotateImageResponse> responses = response.getResponsesList();

            for (AnnotateImageResponse res : responses) {
                if (res.hasError()) {
                    out.printf("Error: %s\n", res.getError().getMessage());
                    return;
                }

                // For full list of available annotations, see http://g.co/cloud/vision/docs
                for (FaceAnnotation annotation : res.getFaceAnnotationsList()) {
                    out.printf(
                            "anger: %s\njoy: %s\nsurprise: %s\nposition: %s",
                            annotation.getAngerLikelihood(),
                            annotation.getJoyLikelihood(),
                            annotation.getSurpriseLikelihood(),
                            annotation.getBoundingPoly());
                    String output = (
                            "anger: %s\njoy: %s\nsurprise: %s\nposition: %s" +
                    annotation.getAngerLikelihood()+
                            annotation.getJoyLikelihood()+
                            annotation.getSurpriseLikelihood()+
                            annotation.getBoundingPoly());

                    Log.d("MyApp",output);
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode==REQUEST_IMAGE_CAPTURE &&resultCode==RESULT_OK)
        {
            mimageView.setVisibility(View.VISIBLE);
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            mimageView.setImageBitmap(imageBitmap);
            findViewById(R.id.use_image).setVisibility(View.VISIBLE);
        }
    }

}
