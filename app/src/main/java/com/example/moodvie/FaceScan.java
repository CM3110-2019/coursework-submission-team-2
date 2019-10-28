package com.example.moodvie;

import android.content.Intent;
import android.graphics.Bitmap;
//import android.media.Image;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
//import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Scanner;




//google imports
import com.google.cloud.vision.v1.AnnotateImageRequest;
import com.google.cloud.vision.v1.AnnotateImageResponse;
import com.google.cloud.vision.v1.BatchAnnotateImagesResponse;
import com.google.cloud.vision.v1.FaceAnnotation;
import com.google.cloud.vision.v1.Feature;
import com.google.cloud.vision.v1.Feature.Type;
import com.google.cloud.vision.v1.Image;
import com.google.cloud.vision.v1.ImageAnnotatorClient;

import com.google.protobuf.ByteString;

import java.util.List;

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
//                try {
//                    //scanImage();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
                PrintStream ps = new PrintStream(System.out);
                File imgFile = new  File("drawable/face_smile.jpg");
                //Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                String path = imgFile.getPath();
                try {
                    String[] args = {"faces", path};


                    argsHelper(args, System.out);


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    public static void argsHelper(String[] args, PrintStream out) throws Exception, IOException {
        if (args.length < 1) {
            out.println("Usage:");
            out.printf(
                    "\tmvn exec:java -DDetect -Dexec.args=\"<command> <path-to-image>\"\n"
                            + "\tmvn exec:java -DDetect -Dexec.args=\"ocr <path-to-file> <path-to-destination>\""
                            + "\n"
                            + "Commands:\n"
                            + "\tfaces | labels | landmarks | logos | text | safe-search | properties"
                            + "| web | web-entities | web-entities-include-geo | crop | ocr \n"
                            + "| object-localization \n"
                            + "Path:\n\tA file path (ex: ./resources/wakeupcat.jpg) or a URI for a Cloud Storage "
                            + "resource (gs://...)\n"
                            + "Path to File:\n\tA path to the remote file on Cloud Storage (gs://...)\n"
                            + "Path to Destination\n\tA path to the remote destination on Cloud Storage for the"
                            + " file to be saved. (gs://BUCKET_NAME/PREFIX/)\n");
            return;
        }

        String command = args[0];
        String path = args.length > 1 ? args[1] : "";
        Log.d("myApp", "outside faces");
        if (command.equals("faces")) {
            Log.d("myApp", "in faces");


            if (path.startsWith("gs://")) {
                Log.d("MyApp", "cloud image");
            } else {
                detectFaces(path, out);

            }
        }
    }
    public void takePicture(View view)
    {
        Intent imageTakeIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(imageTakeIntent.resolveActivity(getPackageManager())!= null)
        {
            startActivityForResult(imageTakeIntent, REQUEST_IMAGE_CAPTURE);
        }
    }



    public void scanImage() throws IOException {

        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {

                try  {
                    Log.d("MyApp","I tried");
                    httpConnection.setRequestMethod("POST");
                    httpConnection.setRequestProperty("Content-Type", "application/json");
                    httpConnection.setDoOutput(true);

                    //buffered writer
                    BufferedWriter httpRequestBodyWriter = new BufferedWriter(new OutputStreamWriter(httpConnection.getOutputStream()));
                    httpRequestBodyWriter.write("{\"requests\":  [{ \"features\":  [ {\"type\": \"LABEL_DETECTION\""+"}], \"image\": {\"source\": { \"gcsImageUri\":"+" \"gs://vision-sample-images/4_Kittens.jpg\"}}}]}");
                    httpRequestBodyWriter.close();
                    Log.d("MyApp","\\\"requests\\\":  [{ \\\"features\\\":  [ {\\\"type\\\": \\\"LABEL_DETECTION\\\"\"+\"}], \\\"image\\\": {\\\"source\\\": { \\\"gcsImageUri\\\":\"+\" \\\"gs://vision-sample-images/4_Kittens.jpg\\\"}}}]}\")");


                    //get response
                    String response = httpConnection.getResponseMessage();
                    Log.d("MyApp","I tried 3");
                    Log.d("MyApp",response);
                    //check response
                    if (httpConnection.getInputStream() == null) {
                        System.out.println("No stream");
                        Log.d("MyApp","No stream");
                        return;
                    }else{
                        Log.d("MyApp","I tried 3.5");
                    }
                    Log.d("MyApp","I tried 4");
                    //create scanner
                    Scanner httpResponseScanner = new Scanner(httpConnection.getInputStream());

                    //create response string
                    String resp = "";

                    //parse scanner results and add to resp string
                    while (httpResponseScanner.hasNext()) {
                        String line = httpResponseScanner.nextLine();
                        resp += line;
                        System.out.println(line);  //  alternatively, print the line of response
                    }

                    //close scanner
                    httpResponseScanner.close();
                } catch (Exception e) {
                    Log.d("MyApp","went into catch");
                    e.printStackTrace();

                }
            }
        });

        _functions.createMessage(getApplicationContext(), "Running Face Scan");
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
