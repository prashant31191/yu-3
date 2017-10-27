package com.ktube.ui;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.devbrackets.android.exomedia.listener.OnErrorListener;
import com.devbrackets.android.exomedia.listener.OnPreparedListener;
import com.devbrackets.android.exomedia.ui.widget.VideoView;
import com.ktube.App;
import com.ktube.R;

/**
 * Created by prashant.patel on 10/27/2017.
 */

public class ActExoPlayer extends Activity
{
    VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_exo_player);
        //ViewGroup.inflate(this, R.layout.act_home, ll_SubMainContainer);

        try{
            setupVideoView();
        }catch (Exception e){
            e.printStackTrace();
        }


    }

    private void setupVideoView() {
        // Make sure to use the correct VideoView import
        videoView = (VideoView)findViewById(R.id.video_view);

        videoView.setOnPreparedListener(new OnPreparedListener() {
            @Override
            public void onPrepared() {
                videoView.start();
            }
        });

        videoView.setOnErrorListener(new OnErrorListener() {
            @Override
            public boolean onError(Exception e) {
                e.printStackTrace();
                App.showLog("===setOnErrorListener====onError==");
                return false;
            }
        });

        //For now we just picked an arbitrary item to play
        videoView.setVideoURI(Uri.parse("https://archive.org/download/Popeye_forPresident/Popeye_forPresident_512kb.mp4"));
        //videoView.setVideoURI(Uri.parse("https://www.youtube.com/watch?v=2itbQceTQGI"));
    }
}
