package com.yuapps.ui;

/**
 * Created by Admin on 6/29/2016.
 */
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.yuapps.R;

import java.io.IOException;

public class ActYouTubePlayer extends YouTubeBaseActivity{

    private YouTubePlayerView playerView;


    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        setContentView(R.layout.act_youtube_player);




        playerView = (YouTubePlayerView)findViewById(R.id.player_view);
        playerView.initialize("Your API Key", new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                if(!b){
                    String videoId = getIntent().getExtras().getString("videoID");
                    youTubePlayer.cueVideo(videoId);
                }
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                Toast.makeText(getApplicationContext(), "Fail-->"+getString(R.string.app_name), Toast.LENGTH_LONG).show();
            }
        });
    }

}
