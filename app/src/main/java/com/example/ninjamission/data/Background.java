package com.example.ninjamission.data;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.RectF;

import com.example.ninjamission.R;
import com.example.ninjamission.data.Sprite;

import java.util.ArrayList;

public class Background extends Sprite {
    //private float screenH;
    private int index = 0;
    private Bitmap frame1;
    private Bitmap frame2;
    private Bitmap frame3;
    private Bitmap frame4;
    private Bitmap frame5;
    private Bitmap frame6;
    private Bitmap frame7;
    private Bitmap frame8;
    private Bitmap frame9;
    private RectF bound1;
    private ArrayList<RectF> bounds;
    static final  int NUM_FRAME =  9;
    private static  Background singleton;
    private Background(Resources res, float w,float h) {
        super(res,w,h);
        bounds = new ArrayList<>();
        for (int i = 0 ; i < NUM_FRAME; i++){
            bounds.add(new RectF());
        }
        loadAndScale();
        //loadAndScale();
    }
    public static Background getSingleton(Resources res, float w,float h){
        if (singleton==null){
            singleton = new Background(res,w,h);
        }
        return singleton;
    }

    public void draw(Canvas c) {
        //img = frames.get(index);
        /*c.drawBitmap(frame1,bound.left,bound.top,null);
        c.drawBitmap(frame2, bound1.left, bound1.top,null);
        */
        for (int i = 0 ; i<NUM_FRAME ; i++) {
            c.drawBitmap(frames.get(i),bounds.get(i).left,bounds.get(i).top,null);
        }

        //super.draw(c);
        //index = (index + 1) % 2;
    }

    @Override
    protected void loadAndScale() {


        var scaleW = (int)screenW*relativeWidth();
        var scaleH = (int)screenH*relativeWidth();
        frame1 = BitmapFactory.decodeResource(res, R.drawable.gamebg1);
        frame2 =  BitmapFactory.decodeResource(res,R.drawable.gamebg2);
        frame3 = BitmapFactory.decodeResource(res, R.drawable.bg3);
        frame4 =  BitmapFactory.decodeResource(res,R.drawable.bg4);
        frame5 = BitmapFactory.decodeResource(res, R.drawable.bg5);
        frame6 =  BitmapFactory.decodeResource(res,R.drawable.bg6);
        frame7 = BitmapFactory.decodeResource(res, R.drawable.bg7);
        frame8 =  BitmapFactory.decodeResource(res,R.drawable.bg8);
        frame9 = BitmapFactory.decodeResource(res, R.drawable.bg9);

        frame1 = Bitmap.createScaledBitmap(frame1,scaleW,scaleH,true);
        frame2 = Bitmap.createScaledBitmap(frame2,scaleW,scaleH,true);
        frame3 = Bitmap.createScaledBitmap(frame3,scaleW,scaleH,true);
        frame4 = Bitmap.createScaledBitmap(frame4,scaleW,scaleH,true);
        frame5 = Bitmap.createScaledBitmap(frame5,scaleW,scaleH,true);
        frame6 = Bitmap.createScaledBitmap(frame6,scaleW,scaleH,true);
        frame7 = Bitmap.createScaledBitmap(frame7,scaleW,scaleH,true);
        frame8 = Bitmap.createScaledBitmap(frame8,scaleW,scaleH,true);
        frame9 = Bitmap.createScaledBitmap(frame9,scaleW,scaleH,true);

       // bound.set(bound.left,bound.top,scaleW,scaleW);

        //bound1 = new RectF();
/*        bound1 = new RectF();
        bound.set(0,0,scaleW,scaleW);
        bound1.set(screenW,0,scaleW,scaleW);*/

        for (int i = 0; i<9 ; i++){
            bounds.get(i).set(i*screenW,0,scaleW,scaleW);
        }

        frames.add(frame1);
        frames.add(frame2);
        frames.add(frame3);
        frames.add(frame4);
        frames.add(frame5);
        frames.add(frame6);
        frames.add(frame7);
        frames.add(frame8);
        frames.add(frame9);

    }

    @Override
    int relativeWidth() {
        return (int)1;
    }


    public void move(){

        for (var box : bounds) {
            box.offset(-screenW*0.01f,0);
        }
/*        bound.offset(-screenW*0.01f,0);
        bound1.offset(-screenW*0.01f,0);*/


        for (int i = 0 ; i< NUM_FRAME ; i ++ ){
            if (bounds.get(i).left <= -(NUM_FRAME-1)*screenW) {
                bounds.get(i).offsetTo(screenW,0);
            }

        }

        /*if (bound.right <= 0){
            bound.offsetTo(screenW,0);
        }

        if (bound1.left <= -screenW){
            bound1.offsetTo(screenW,0);
        }*/
    }
}
