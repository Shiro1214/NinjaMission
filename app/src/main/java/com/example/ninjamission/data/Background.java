package com.example.ninjamission.data;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.RectF;

import com.example.ninjamission.R;
import com.example.ninjamission.data.Sprite;

public class Background extends Sprite {
    //private float screenH;
    private int index = 0;
    private Bitmap frame1;
    private Bitmap frame2;
    private RectF bound1;
    public Background(Resources res, float w,float h) {
        super(res,w,h);


        //loadAndScale();
    }
    public void draw(Canvas c) {
        //img = frames.get(index);
        c.drawBitmap(frame1,bound.left,bound.top,null);
        c.drawBitmap(frame2, bound1.left, bound1.top,null);
        //super.draw(c);
        //index = (index + 1) % 2;
    }

    @Override
    protected void loadAndScale() {


        var scaleW = (int)screenW*relativeWidth();
        var scaleH = (int)screenH*relativeWidth();
        frame1 = BitmapFactory.decodeResource(res, R.drawable.gamebg1);
        frame2 =  BitmapFactory.decodeResource(res,R.drawable.gamebg2);
        frame1 = Bitmap.createScaledBitmap(frame1,scaleW,scaleH,true);
        frame2 = Bitmap.createScaledBitmap(frame2,scaleW,scaleH,true);
       // bound.set(bound.left,bound.top,scaleW,scaleW);

        //bound1 = new RectF();
        bound1 = new RectF();
        bound.set(0,0,scaleW,scaleW);
        bound1.set(screenW,0,scaleW,scaleW);

        frames.add(frame1);
        frames.add(frame2);
    }

    @Override
    int relativeWidth() {
        return (int)1;
    }


    public void move(){

        bound.offset(-screenW*0.01f,0);
        bound1.offset(-screenW*0.01f,0);

        if (bound.right <= 0){
            bound.offsetTo(screenW,0);
        }

        if (bound1.left <= -screenW){
            bound1.offsetTo(screenW,0);
        }
    }
}
