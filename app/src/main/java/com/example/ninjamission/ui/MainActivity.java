package com.example.ninjamission.ui;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;

import com.example.ninjamission.R;

public class MainActivity extends Activity {
    private MediaPlayer bg = new MediaPlayer();
    boolean musicOn = false;
    NinjaView nj;
    @Override
    protected void onDestroy() {
        super.onDestroy();
        bg.release();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

       // AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
       // audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 20, 0);

        //Music
        bg = MediaPlayer.create(this, R.raw.ninja_background);
        bg.setLooping(true);

        if (SettingsActivity.SettingsFragment.getBgMusicPrefs(this)) {
            bg.start();
            musicOn = true;
        }

        nj = new NinjaView(this);
        setContentView(nj);
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            nj.resumeTimer();
        }catch (NullPointerException e){}
        if(musicOn){
            try{
                bg.start();}
            catch (IllegalStateException e){}
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            nj.pauseTimer();
        } catch (NullPointerException e) {
        }
        if (musicOn) {
            try {
                bg.pause();
            } catch (IllegalStateException e) {
            }
        }
    }
}