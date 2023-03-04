package com.example.ninjamission.data;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.Typeface;

import com.example.ninjamission.R;
import com.example.ninjamission.ui.SettingsActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Formatter;

import kotlin.sequences.FlatteningSequence;

public class Ghost extends Sprite{
    private int index = 0;
    private Number answer;
    private Paint ghostPaint;
    private Context c;
    private float presetSpeed;
    private double timeNow,timeBefore,timeLeft;
    public static ArrayList<Float> POSITIONS;
    private static int POS_INDEX = 0;

    private float xPos,yPos;
    public Ghost(Resources res, float w,float h, Number ans,Context c) {
        super(res, w,h);
        img = frames.get(index);
        answer = ans;
        ghostPaint = new Paint(Paint.LINEAR_TEXT_FLAG);
        ghostPaint.setStrokeWidth(10);
        ghostPaint.setColor(Color.YELLOW);
        ghostPaint.setTextSize(w * 0.02f);
        ghostPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        timeBefore = System.currentTimeMillis();

        this.c = c;

        presetSpeed = -screenW* SettingsActivity.SettingsFragment.getDifficulty(c);
        velocity.set(presetSpeed,randomY());
    }

    @Override
    protected void loadAndScale() {
        var scaleW = relativeWidth();
        var frame1 = BitmapFactory.decodeResource(res, R.drawable.ghost1);
        var frame2 =  BitmapFactory.decodeResource(res,R.drawable.ghost2);
        var frame3 =BitmapFactory.decodeResource(res,R.drawable.ghost3);
        var frame4 =BitmapFactory.decodeResource(res,R.drawable.ghost4);
        frame1 = Bitmap.createScaledBitmap(frame1,scaleW,scaleW,true);
        frame2 = Bitmap.createScaledBitmap(frame2,scaleW,scaleW,true);
        frame3 = Bitmap.createScaledBitmap(frame3,scaleW,scaleW,true);
        frame4 = Bitmap.createScaledBitmap(frame4,scaleW,scaleW,true);
        bound.set(bound.left,bound.top,scaleW,scaleW);
        frames.add(frame1);
        frames.add(frame2);
        frames.add(frame3);
        frames.add(frame4);

        var maxLevel = screenH/relativeWidth() -1;

        if (POSITIONS == null) {
            POSITIONS = new ArrayList<>();

            float gap = 0.025f;
            var increment = relativeWidth()/(maxLevel*screenW);

            for (int i=0; i< maxLevel; i++){
                //TO FIX- Make sure there's exactly maxlevel -1 ghosts, and there are gaps bettween each ghost

                POSITIONS.add(((float)i/(float)maxLevel) + gap );

                gap += increment;

            }
            Collections.shuffle(POSITIONS);
        }

        //System.out.println("Max ghosts is "+ maxLevel );

        //TO DO
        xPos = screenW-scaleW;
        yPos = POSITIONS.get(POS_INDEX)*screenH /*+ (POS_INDEX * screenH*0.05f)*/;
        POS_INDEX = (POS_INDEX +1) % POSITIONS.size();

        setPosition(xPos,yPos);



/*        //recursiveYPos();
        A:
        while (true) {
            for (var rectf : GENERATED_POS) {
                if (RectF.intersects(rectf, bound)) {
                    while (RectF.intersects(rectf, bound)) {
                        bound.offsetTo(xPos, (float) Math.random() * (screenH * 0.5f) + screenH * 0.25f);
                    }
                    continue A;
                }
            }
            break;
        }
//TO DO - Divide into sections and generate positions

        GENERATED_POS.add(bound);*/

    }

    @Override
    int relativeWidth() {
        return (int)screenW/20;
    }

    public void draw(Canvas c){
        img = frames.get(index);


        var tmp= (answer instanceof  Integer )? answer.intValue():answer.floatValue();
        //var tmp = answer.floatValue(); //This is NUMBER
        Formatter formatter = new Formatter();

        //Hack :) Type Check int vs float
        if (tmp!= (int)tmp) {
        formatter.format("%.2f", tmp);
        c.drawText(""+formatter,bound.left-(bound.width()*1.15f), bound.top+(bound.height()/2),ghostPaint);
        }
        else
        {
            c.drawText(""+(int)(tmp),bound.left-(bound.width()*1.15f), bound.top+(bound.height()/2),ghostPaint);
        }


        super.draw(c);
        index = (index +1) % 4;
    }

    @Override
    public void move(){
        super.move();

        if (bound.top <= screenH*0.1f) {
            velocity.set(presetSpeed,Math.abs(randomY()));
        } else if (bound.bottom >= screenH*0.9f) {
            velocity.set(presetSpeed,(-1)*Math.abs(randomY()));
        }


        //else velocity.set(-40,randomY());

    }

    public boolean isIn(){
        return (bound.right < 0);
    }

    @Override
    public void tick(){
        move();
    }

    private float randomY(){
        if (Math.random()<0.5){
            return -10;
        }
        return 10;
    }

    public Number getAnswer(){
        return answer;
    }

    public int getPoint(){
        timeNow = System.currentTimeMillis();
        timeLeft = timeNow - timeBefore;
        var bonus = timeLeft/1000;

        var point = timeLeft <= 10000 ? 150 : 100;
        //System.out.println("Bonus "+ bonus);
        return (int) point;
    }


}
