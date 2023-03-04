package com.example.ninjamission.data;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

import com.example.ninjamission.R;
import com.example.ninjamission.miscellenous.TickListener;

public class Shuriken extends Sprite{
    private int index = 0;
    private float slope,x,y;
    public Shuriken(Resources res, float w,float h,float x2, float y2) {
        super(res, w,h);
        var x1 = 0.1f*w;
        var y1 = h*0.8f;
        slope = (y2-y1)/(x2-x1);

        if ((x2 > x1) && (y2 < y1) ){
            x = 1;
            y = 1*slope;
        } else if((x2 < x1) && (y2 > y1)) {
            x = -1;
            y = x*slope;
        } else if ((x2 < x1) && (y2 < y1)) {
            x = -1;
            y = x*slope;
        } else if ((x2 > x1) && (y2 > y1)) {
            x = 1;
            y = 1*slope;
        }

        setPosition(0.1f * w, h * 0.8f);
        img = frames.get(index);
    }
    @Override
    protected void loadAndScale(){
        var scaleW = relativeWidth();
        var frame1 = BitmapFactory.decodeResource(res, R.drawable.shuriken2);
        var frame2 =  BitmapFactory.decodeResource(res,R.drawable.shuriken3);
        var frame3 =BitmapFactory.decodeResource(res,R.drawable.shuriken4);
        var frame4 =BitmapFactory.decodeResource(res,R.drawable.shuriken5);

        frame1 = Bitmap.createScaledBitmap(frame1,scaleW,scaleW,true);
        frame2 = Bitmap.createScaledBitmap(frame2,scaleW,scaleW,true);
        frame3 = Bitmap.createScaledBitmap(frame3,scaleW,scaleW,true);
        frame4 = Bitmap.createScaledBitmap(frame4,scaleW,scaleW,true);

        bound.set(bound.left,bound.top,scaleW,scaleW);
        frames.add(frame1);
        frames.add(frame2);
        frames.add(frame3);
        frames.add(frame4);
    }
    @Override
    int relativeWidth() {
        return (int)screenW/25;
    }
    @Override
    public void draw(Canvas c){
        img = frames.get(index);
        super.draw(c);
        index = (index +1) % 4;
    }
    @Override
    public void move(){

/*
        System.out.println("slope : " + slope);
        System.out.println("x is " + x);
        System.out.println("y is "+ y);
*/
  /*if (shurikens.isEmpty()) {
    var shuriken = new Shuriken(getResources(), w, h, x, y);
    ninjaSprite.shoot();
    shurikens.add(shuriken);
    float velocity = 500.0f; // pixels per second
    float targetX = w; // end position of the shuriken
    float deltaX = targetX - shuriken.getX();
    float deltaT = deltaX / velocity;
    float startTime = System.currentTimeMillis();
    tim.register(new TimerTask() {
        @Override
        public void run() {
            float elapsedTime = System.currentTimeMillis() - startTime;
            if (elapsedTime >= deltaT) {
                shuriken.setX(targetX);
                tim.cancel();
            } else {
                float currentX = shuriken.getX() + (elapsedTime / deltaT) * deltaX;
                shuriken.setX(currentX);
            }
        }
    });
}
*/
        bound.offset(x,y);
    }
}
