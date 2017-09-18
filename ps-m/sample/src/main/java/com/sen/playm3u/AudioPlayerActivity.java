package com.sen.playm3u;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Adminsss on 10-02-2016.
 */
public class AudioPlayerActivity extends Activity {
    public static Context context;
    public static AudioPlayerActivity activity;
    public ListPlayer listPlayer;
    public LinearLayout root;
    private TextView artistInfo;
    private TextView trackInfo;
    private ImageView image;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        activity = this;

        createView();
        setContentView(root);

        Intent intent = getIntent();
        // Are we called from main or by our M3U intent?
        if (intent.getAction().equals(Intent.ACTION_MAIN)) {
            System.out.println("Is main");
            new DownloadM3U(this).execute("http://192.168.1.9/music/test.m3u");
        } else
        if (intent != null && intent.getData() != null) {
            new DownloadM3U(this).execute(intent.getData().toString());
        }
    }

    private void createView() {
        root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);

        LinearLayout buttonBox = new LinearLayout(this);

        artistInfo = new TextView(this);
        artistInfo.setText("");
        trackInfo = new TextView(this);
        trackInfo.setText("");
        image = new ImageView(this);

        buttonBox.setOrientation(LinearLayout.HORIZONTAL);
        Button stopBtn = new Button(this);
        stopBtn.setText("Stop");
        Button FFBtn = new Button(this);
        FFBtn.setText("Next");

        root.addView(image);
        root.addView(artistInfo);
        root.addView(trackInfo);
        root.addView(buttonBox);

        buttonBox.addView(FFBtn);
        buttonBox.addView(stopBtn);

        stopBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                listPlayer.stopPlaying();
            }
        });

        FFBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                listPlayer.stopCurrentTrack();
            }
        });
    }

    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        System.out.println("External Player finished");
        synchronized(listPlayer) {
            listPlayer.notifyAll();
        }
    }

    public void setTrackInfo(String artist, String track, Bitmap image) {
        artistInfo.setText(artist);
        trackInfo.setText(track);
        this.image.setImageBitmap(image);
    }
}
