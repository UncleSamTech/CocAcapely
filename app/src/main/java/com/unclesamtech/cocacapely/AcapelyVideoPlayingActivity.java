package com.unclesamtech.cocacapely;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;

import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

public class AcapelyVideoPlayingActivity extends AppCompatActivity {
    YouTubePlayerView rhemYoutubePlay;

    private Context c = AcapelyVideoPlayingActivity.this;
    private String videoId;
    private RhemaHiveYoutubeVideosModel youtubeVideosModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acapely_video_playing);
        try {
            rhemYoutubePlay = findViewById(R.id.rhem_youtube_view);
            getLifecycle().addObserver(rhemYoutubePlay);
            //Intent intent  = getIntent();
            // youtubeVideosModel = intent.getParcelableExtra()
            videoId = retrVideoId("rhem_video_id",MODE_PRIVATE,"rhem_vid_id");
            //getAuto().getToast(c, "video")


            rhemYoutubePlay.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                @Override
                public void onReady(com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer youTubePlayer) {
                    if(!TextUtils.isEmpty(videoId)){
                        youTubePlayer.cueVideo(videoId,0);
                    }



                }
            });
        } catch (Exception e) {
            getAuto().getToast(c, CocAcapelyConstants.ERROR_MESS + e.getLocalizedMessage(), CocAcapelyConstants.TOAST_LONG_LEN).show();
        }


    }

    private Button getBut(int id){
        Button but = findViewById(id);
        return but;
    }

    public AcapaleyAutoUtils getAuto(){
        return new AcapaleyAutoUtils();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        rhemYoutubePlay.release();

    }

    public String retrVideoId(String key,  int mode,String prefKey){
        SharedPreferences sharedPreferences = getSharedPreferences(prefKey,mode);
        //if(sharedPreferences.contains("user_church")){}
        return sharedPreferences.getString(key,null);
    }
}
