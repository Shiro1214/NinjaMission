package com.example.ninjamission.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.RectF;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.example.ninjamission.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.io.IOException;

public class SplashScreen extends Activity {
    private MediaPlayer bgMusic = new MediaPlayer();
    ImageView bg;
    @Override
    protected void onCreate(@Nullable Bundle b) {
        super.onCreate(b);

        // Calculate the desired height as 90% of the screen height
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int screenHeight = displayMetrics.heightPixels;
        int desiredHeight = (int) (screenHeight * .85);

        // Create layout parameters for the views
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, // Width
                LinearLayout.LayoutParams.WRAP_CONTENT    // Height
        );

        bg = new ImageView(this);
        bg.setImageResource(R.drawable.welcome_screen);

        //Stretch
        bg.setScaleType(ImageView.ScaleType.FIT_XY);
        bg.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                 desiredHeight// Set the height you want for the background view
        ));


        //AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
       // audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 20, 0);

        //Music
        bgMusic = MediaPlayer.create(this,R.raw.ninja_background);
        bgMusic.setLooping(true);
        if (SettingsActivity.SettingsFragment.getBgMusicPrefs(this)) {
            bgMusic.start();
        }

        //Goole Ad
        AdView adView = new AdView(this);

        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId("ca-app-pub-3940256099942544/6300978111"); //replace AdUnitId
        adView.setLayoutParams(layoutParams);

// Add adView to your view hierarchy.
        LinearLayout l = new LinearLayout(this);
        l.addView(bg);
        l.addView(adView);
        l.setOrientation(LinearLayout.VERTICAL);
        l.setGravity(Gravity.CENTER_HORIZONTAL);
        setContentView(l);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e){
        var x = e.getX();
        var y = e.getY();
        var w = bg.getWidth();
        var h = bg.getHeight();

        float xP = x/w;
        float yP = y/h;

        var infoBox = new RectF();
        infoBox.set((0.015594482f*w),0.020887587f*h,0.084350586f*w,0.09728326f*h);

        var startBox = new RectF();
        startBox.set((0.07342529f*w),0.80559623f*h,0.49996948f*w,0.9472317f*h);

        var settingBox = new RectF();
        settingBox.set((0.8937073f*w),0.876397f*h,0.98043823f*w,0.97642684f*h);

        var restartBox = new RectF();
        restartBox.set((0.929657f*w),0.023668077f*h,0.98825073f*w,0.0986735f*h);


        if (e.getAction() == MotionEvent.ACTION_DOWN){
            //Log.d("XY", "onTouchEvent: x is "+ xP + " and Y is " + yP );
            if (infoBox.contains(x,y)){

                var ab = new AlertDialog.Builder(this);
                ab.setTitle(getResources().getString(R.string.Info));
                ab.setMessage(getResources().getString(R.string.infoTexts));
                ab.setCancelable(false);
                ab.setPositiveButton(getResources().getString(R.string.Okay), ( d, i) -> {});
                var box = ab.create();
                box.show();
            }
            if (startBox.contains(x,y)){

                var intent = new Intent(this,MainActivity.class);
                startActivity(intent);
                finish();
            }

            if (settingBox.contains(x,y)){

                var intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);

            }

            if (restartBox.contains(x,y)){
                var rb = new AlertDialog.Builder(this);
                rb.setTitle("Restart Level")
                        .setPositiveButton(getResources().getString(R.string.Yes),(d , i) ->         {
                            try(var fos = this.openFileOutput("level.txt",Context.MODE_PRIVATE)) {
                    fos.write(("1"+ " ").getBytes());
                } catch (IOException ex){
                    ex.printStackTrace();
                }
                        }
)               .setNegativeButton(getResources().getString(R.string.No),null)
                        .setMessage(getResources().getString(R.string.RestartInfo));
                AlertDialog restartB = rb.create();
                restartB.show();

            }
        }

        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bgMusic.release();
    }
    @Override
    protected void onResume() {
        super.onResume();
        bgMusic.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        bgMusic.pause();
    }
}
