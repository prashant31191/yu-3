package com.sen;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;

/**
 * Created by Adminsss on 15-02-2016.
 */
public class SpyScreen extends Activity
{
    Button btnClick;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);

        // ...but notify us that it happened.
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
                WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH);


        super.onCreate(savedInstanceState);

        setContentView(R.layout.act_spy_screen);


        btnClick = (Button)findViewById(R.id.toggle);

    }

    /*// Method to start the service
    public void startService(View view) {
        startService(new Intent(getBaseContext(), MyService.class));
    }

    // Method to stop the service
    public void stopService(View view) {
        stopService(new Intent(getBaseContext(), MyService.class));
    }*/

    @Override
    protected void onStart() {
        startService(new Intent(getBaseContext(), MyService.class));
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopService(new Intent(getBaseContext(), MyService.class));
    }
}
