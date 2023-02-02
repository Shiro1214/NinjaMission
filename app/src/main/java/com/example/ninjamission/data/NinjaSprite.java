package com.example.ninjamission.data;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import com.example.ninjamission.R;

public class NinjaSprite extends Sprite{

    private int index = 0;
    private Bitmap frame1;
    private Bitmap frame2;
    private Boolean shooting =false;

    private int count = 0;
    public NinjaSprite(Resources res,float w,float h){
        super(res,w,h);
        img = frames.get(index);
    }

    @Override
    protected void loadAndScale(){
        var scaleW = relativeWidth();
         frame1 = BitmapFactory.decodeResource(res, R.drawable.ninja01);
         frame2 =  BitmapFactory.decodeResource(res,R.drawable.ninja02);
        frame1 = Bitmap.createScaledBitmap(frame1,scaleW,scaleW,true);
        frame2 = Bitmap.createScaledBitmap(frame2,scaleW,scaleW,true);
        bound.set(bound.left,bound.top,scaleW,scaleW);
        frames.add(frame1);
        frames.add(frame2);
    }

    @Override
    int relativeWidth() {
        return (int) screenW/8;
    }

    @Override
    public void draw(Canvas c){
        img = frames.get(index);
        super.draw(c);
        index = (index +1) % 2;

        if (shooting) {
            count++;
            if (count>=2) {
                shooting =false;
                var scaleW = relativeWidth();
                frame1 = BitmapFactory.decodeResource(res,R.drawable.ninja01);
                frame2 =  BitmapFactory.decodeResource(res,R.drawable.ninja02);
                frames.set(0,Bitmap.createScaledBitmap(frame1,scaleW,scaleW,true));
                frames.set(1,Bitmap.createScaledBitmap(frame2,scaleW,scaleW,true));
                count = 0;

            }
        }
       // System.out.println("count is " + count);
    }

    public void shoot(){
        var scaleW = relativeWidth();
        frame1 = BitmapFactory.decodeResource(res,R.drawable.ninja4);
        frame2 = BitmapFactory.decodeResource(res,R.drawable.ninja3);
        frames.set(0,Bitmap.createScaledBitmap(frame1,scaleW,scaleW,true));
        frames.set(1,Bitmap.createScaledBitmap(frame2,scaleW,scaleW,true));
        shooting= true;
    }

}
