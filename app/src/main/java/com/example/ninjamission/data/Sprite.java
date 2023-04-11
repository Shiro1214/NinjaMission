package com.example.ninjamission.data;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;

import com.example.ninjamission.miscellenous.TickListener;

import java.util.ArrayList;

public abstract class Sprite implements TickListener {
    protected Bitmap img;
    protected RectF bound;
    protected PointF pos;
    protected float screenW,screenH;
    protected ArrayList<Bitmap> frames;
    protected Resources res;
    protected PointF velocity;


    public Sprite(Resources res, float w,float h){
        img = null;
        this.res = res;
        screenW = w;
        screenH = h;
        velocity = new PointF(0,0);
        //pos = new PointF();
        bound = new RectF();
        frames = new ArrayList<>();

    }

    public void draw(Canvas c){
        c.drawBitmap(img,bound.left,bound.top,null);
        var p = new Paint();
        p.setColor(Color.GREEN);
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(5);
        //c.drawRect(bound,p);
    }

    protected abstract void loadAndScale();

    abstract int relativeWidth();

    public void setPosition(float x, float y){
        bound.offsetTo(x,y);
    }

    public Boolean intersect(Sprite other){
        return (RectF.intersects(this.bound,other.bound));
    }

    public void gone(){
        bound.offsetTo(0,-screenH);
    }

    public Boolean isGone(){
        return ((bound.top < 0 || bound.bottom > screenH) || (bound.right > screenW));
    }


    @Override
    public void tick() {
        move();
    }

    protected void move(){
        bound.offset(velocity.x,velocity.y);
    }
}
