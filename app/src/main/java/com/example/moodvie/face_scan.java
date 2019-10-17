package com.example.moodvie;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


public class face_scan extends AppCompatActivity
{
    functions _functions = new functions();
    private ImageView mimageView;
    private static final int REQUEST_IMAGE_CAPTURE = 101;
    protected <T extends View> T getView(int id) { return super.findViewById(id); }
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.face_scan);
        mimageView = findViewById(R.id.camera);
        getView(R.id.takePicture).setOnClickListener(new View.OnClickListener()
        {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view)
            {
                if(_functions.checkCameraHardware(getApplicationContext()))
                {
                    if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
                        ActivityCompat.requestPermissions(face_scan.this, new String[]{Manifest.permission.CAMERA}, 1);
                    else
                        takePicture(view);
                }
                else
                    _functions.createMessage(getApplicationContext(), "No Camera Available");
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
