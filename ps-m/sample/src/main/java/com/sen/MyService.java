package com.sen;

import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Date;

/**
 * Created by Adminsss on 15-02-2016.
 */
public class MyService extends Service {

   Handler handler ;
    Runnable myRunnable;
    int i=0;

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Let it continue running until it is stopped.
        Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();
        Log.d("=======11111======","============111111=====");
         handler = new Handler();



         myRunnable = new Runnable() {
            public void run() {
                //Some interesting task

                Log.d("=======myRunnable======","==000==="+i);
                i = i+1;
                if(handler !=null )
                {
                    System.out.print("========Running============");
                    Log.d("=======myRunnable======","============111=====");
                    handler.postDelayed(myRunnable,100);
                }
            }
        };

        handler.postDelayed(myRunnable,100);

       /* handler.postDelayed(new Runnable() {
            @Override
            public void run() {
               System.out.print("========Running============");
                Log.d("=======22222======","============2222=====");
                handler.postDelayed(this,30);
            }
        }, 1000);*/


        //takeScreenshotRooted();
        return START_STICKY;
    }

    private void takeScreenshotRooted()
    {
        try {
            Process sh = Runtime.getRuntime().exec("su", null, null);

            OutputStream os = sh.getOutputStream();
            os.write(("/system/bin/screencap -p " + "/sdcard/img.png").getBytes("ASCII"));
            os.flush();

            os.close();
            sh.waitFor();

            //then read img.png as bitmap and convert it jpg as follows

            Bitmap screen = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + File.separator + "img.png");

//my code for saving
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            screen.compress(Bitmap.CompressFormat.JPEG, 15, bytes);

//you can create a new file name "test.jpg" in sdcard folder.

            File f = new File(Environment.getExternalStorageDirectory() + File.separator + "test.jpg");
            f.createNewFile();
//write the bytes in file
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
// remember close de FileOutput

            fo.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }


    private void takeScreenshot(View v) {
        Date now = new Date();
        android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);

        try {
            // image naming and path  to include sd card  appending name you choose for file
            String mPath = Environment.getExternalStorageDirectory().toString() + "/" + now + ".jpg";

            // create bitmap screen capture
            View v1 =v;// getWindow().getDecorView().getRootView();
            v1.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
            v1.setDrawingCacheEnabled(false);

            File imageFile = new File(mPath);

            FileOutputStream outputStream = new FileOutputStream(imageFile);
            int quality = 100;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            outputStream.flush();
            outputStream.close();

            //openScreenshot(imageFile);
        } catch (Throwable e) {
            // Several error may come out with file handling or OOM
            e.printStackTrace();
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();

        //handler.removeCallbacksAndMessages(null);
        handler.removeCallbacks(myRunnable);
        handler = null;
        myRunnable = null;


    }
}
