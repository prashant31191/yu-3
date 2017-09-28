package com.videbut01.ui;

/**
 * Created by Admin on 6/29/2016.
 */
import android.os.Bundle;
import android.widget.Toast;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.videbut01.App;
import com.videbut01.R;
import com.videbut01.utils.Temp;

public class ActPlay extends YouTubeBaseActivity{

    private YouTubePlayerView playerView;


    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        setContentView(R.layout.act_youtube_player);

        playerView = (YouTubePlayerView)findViewById(R.id.player_view);
        playerView.initialize(Temp.apiKey, new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                if(!b){
                    String videoId = getIntent().getExtras().getString("videoID");
                    App.showLog("=====videoId===>>"+videoId);
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
