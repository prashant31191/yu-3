package com.ktube.ui.base;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.ktube.R;
import com.ktube.ui.ActHome;
import com.ktube.ui.ActYuMain;

public class SplashActivity extends AppCompatActivity {



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //Intent intent=new Intent(SplashActivity.this,ActYuMain.class);
                Intent intent=new Intent(SplashActivity.this,ActHome.class);
                startActivity(intent);
            }
        },getResources().getInteger(R.integer.splashDuration));

    }
}
