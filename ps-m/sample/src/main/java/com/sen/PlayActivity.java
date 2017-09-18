package com.sen;

import android.app.Activity;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.VideoView;

import java.net.URL;

/**
 * Created by Adminsss on 03-02-2016.
 */
public class PlayActivity extends Activity {

    private String urlStream;
    private VideoView myVideoView;
    private URL url;
    ProgressBar progressBar;
    SearchView searchView;
    String Tag="======Paly====Url=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_play);//***************
        myVideoView = (VideoView)this.findViewById(R.id.myVideoView);

        searchView = (SearchView)this.findViewById(R.id.searchView);
        progressBar = (ProgressBar)this.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        MediaController mc = new MediaController(this);
        myVideoView.setMediaController(mc);
        //urlStream = "http://devimages.apple.com/iphone/samples/bipbop/bipbopall.m3u8";
        urlStream = "https://www.youtube.com/embed/iZqDdvhTZj0";
                //"https://www.youtube.com/watch?v=iZqDdvhTZj0";


                //"http://devimages.apple.com/iphone/samples/bipbop/bipbopall.m3u8";




        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String s) {

                urlStream = s;
                myVideoView.stopPlayback();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.e(Tag,urlStream);
                        progressBar.setVisibility(View.VISIBLE);

                         myVideoView.setVideoURI(Uri.parse(urlStream));
                    }
                });
                return false;
            }

            /**
             * Called when the query text is changed by the user.
             *
             * @param newText the new content of the query text field.
             * @return false if the SearchView should perform the default action of showing any
             * suggestions if available, true if the action was handled by the listener.
             */
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });



       // myVideoView.stopPlayback();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.e(Tag,urlStream);
                progressBar.setVisibility(View.VISIBLE);
                myVideoView.setVideoURI(Uri.parse(urlStream));

            }
        });



        myVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                // TODO Auto-generated method stub
                //mp.prepareAsync();//last
                mp.start();
                mp.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
                    @Override
                    public void onVideoSizeChanged(MediaPlayer mp, int arg1,
                                                   int arg2) {
                        // TODO Auto-generated method stub
                        progressBar.setVisibility(View.GONE);
                        mp.start();
                    }
                });

            }
        });



    }

    @Override
    protected void onDestroy() {


        super.onDestroy();
    }
}
