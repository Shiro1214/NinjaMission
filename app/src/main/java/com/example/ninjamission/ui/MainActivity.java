package com.example.ninjamission.ui;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;

import com.example.ninjamission.R;

public class MainActivity extends Activity {
    private MediaPlayer bg = new MediaPlayer();

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

        bg.start();


        var nj = new NinjaView(this);
        setContentView(nj);
    }
}