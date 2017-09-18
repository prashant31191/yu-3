package com.sen;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.widget.TextView;

import com.sen.zip.Unzipper;

import java.io.File;
import java.io.IOException;
import java.util.zip.ZipException;

/**
 * Created by Adminsss on 10-02-2016.
 */
public class UnzipResources extends Activity {

    private String LOG_TAG = MainActivity.class.getSimpleName();

    private File zipFile;
    private File destination;

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_unzip_files);

        textView = (TextView) findViewById(R.id.textView);
        textView.setGravity(Gravity.CENTER);

        if ( initialize() ) {
            zipFile = new File(destination, "test.zip");
            try {
                Unzipper.unzip(zipFile, destination);
                textView.setText("Extracted to \n"+destination.getAbsolutePath());
            } catch (ZipException e) {
                Log.e(LOG_TAG, e.getMessage());
            } catch (IOException e) {
                Log.e(LOG_TAG, e.getMessage());
            }
        } else {
            textView.setText("Unable to initialize sd card.");
        }
    }

    public boolean initialize() {
        boolean result = false;
        File sdCard = new File(Environment.getExternalStorageDirectory()+"/zip/");
        //File sdCard = Environment.getExternalStorageDirectory();
        if ( sdCard != null ) {
            destination = sdCard;
            if ( !destination.exists() ) {
                if ( destination.mkdir() ) {
                    result = true;
                }
            } else {
                result = true;
            }
        }

        return result;
    }

}
