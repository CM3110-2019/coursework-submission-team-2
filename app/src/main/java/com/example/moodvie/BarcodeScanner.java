package com.example.moodvie;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class BarcodeScanner extends Activity implements ZXingScannerView.ResultHandler
{
    private ZXingScannerView mScannerView;
    functions _functions = new functions();

    @Override
    public void onCreate(Bundle state)
    {
        super.onCreate(state);
        mScannerView = new ZXingScannerView(this); // Programmatically initialize the scanner view
        setContentView(mScannerView); // Set the scanner view as the content view
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this); // Register a handler for scan results.
        mScannerView.startCamera(); // Start camera on resume
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera(); // Stop camera on pause
    }

    @Override
    public void handleResult(Result result)
    {
        _functions.createMessage(getApplicationContext(), result.getText());

        mScannerView.resumeCameraPreview(this);
    }
}