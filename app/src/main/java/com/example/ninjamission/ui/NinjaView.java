package com.example.ninjamission.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.example.ninjamission.data.Background;
import com.example.ninjamission.data.Ghost;
import com.example.ninjamission.data.NinjaSprite;
import com.example.ninjamission.R;
import com.example.ninjamission.data.Shuriken;
import com.example.ninjamission.miscellenous.TickListener;
import com.example.ninjamission.miscellenous.UpdateTimer;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;
import java.util.Scanner;

public class NinjaView extends View implements TickListener {
    int w;
    int h;
    private boolean initialized;
    private UpdateTimer tim;
    private NinjaSprite ninjaSprite;
    private Background background;
    private ArrayList<Shuriken> shurikens;
    private Paint timerPaint;
    private ArrayList<Ghost> ghosts;
    private ArrayList<String> operators;
    private int inCount, onScreenTime,timeCount, wrongShoot;
    static int level = 1;
    private int timer ,a,b, score = 0;
    private boolean ansExist = false;

    private Number answer = 0;
    private String operator = "*";
    private MediaPlayer right_answer_fx;

    @Override
    public void tick() {
        //randomMathProb();
        for (Ghost g:ghosts
        ) {
            if (g.isIn() && (g.getAnswer() == answer)) {
                inCount++;
            }
        }
        //Create ghost if conditions met
        createGhosts();

        //Collisions
        detectCollisions();
        shurikens.removeIf(s -> s.isGone());
        ghosts.removeIf(g->g.isGone());

        //Invased!
        ghosts.removeIf(g->g.isIn());
        endGame();

        //timer
        timeCount++;
        if (timeCount>=10) {
            onScreenTime--;
            timeCount = 0;
        }

        //To Do - Timer 0 but last final answer;
        if (onScreenTime <=0 ) {
            onScreenTime = 0;
            if(ghosts.isEmpty()) nextLevelAccomplished();
        }
        invalidate();
    }

    /**
     * Constructor
     * @param context activity
     */
    public NinjaView(Context context) {
        super(context);
        readScore();
        right_answer_fx = new MediaPlayer();
        right_answer_fx = MediaPlayer.create(getContext(), R.raw.right_answer);
        operators = new ArrayList<>(Arrays.asList("+","-","*","/"));
        initialized = false;


        //TO DO
        /*
        1. Ghost Speed- easy, medium, hard
        2. Next level- saved level- Options
        3. Sound effects
        4. Background music
        5. Get Score - The faster the higher- DONE!
        6. Maybe losing if hitting the wrong answer level -1 times
        7. Different maps for levels
        8. Player registry
         */
    }

    private void readScore() {
/*        try(Scanner s = new Scanner(getContext().openFileInput("level.txt"));) {
            level = s.nextInt();
            //s.close();
        } catch (FileNotFoundException e) {
            level = 1;//new install
        }*/
        level = SettingsActivity.SettingsFragment.getLevelPref(getContext());

        if (level > 10 ) level = 10;
    }


    @Override
    public void onDraw(Canvas c){
        if (!initialized) {
           playAgain();



            //OnScreenTimer Paint
            timerPaint = new Paint();
            timerPaint.setColor(Color.RED);
            timerPaint.setTextSize(w * 0.03f);
            timerPaint.setStrokeWidth(5);
            timerPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));


            initialized = true;
        }

        background.draw(c);

        //Timer
        var minute = onScreenTime / 60;
        var seconds = Math.floorMod(onScreenTime, 60);
        timerPaint.setColor(Color.RED);
        c.drawText(/*getResources().getString(R.string.time)+*/"Time: " + minute + ":" + String.format("%02d", seconds), w * 0.05f, h * 0.1f, timerPaint);

        //Math
        timerPaint.setColor(Color.BLUE);
        c.drawText(/*getResources().getString(R.string.time)+*/"Problem: " + a + operator + b + " = ?", w * 0.4f, h * 0.1f, timerPaint);

        //Score
        timerPaint.setColor(Color.YELLOW);
        c.drawText("SCORE: " +score, w * 0.8f, h * 0.1f, timerPaint);

        ninjaSprite.draw(c);
        shurikens.forEach(s -> s.draw(c));
        ghosts.forEach(g->g.draw(c));

    }


    @Override
    public boolean onTouchEvent(MotionEvent e){
        //shurikens = new ArrayList<>();
        var x = e.getX();
        var y = e.getY();

        //Only 18% screen in to shoot
        if (x >= 0.175*w) {
            if (e.getAction() == MotionEvent.ACTION_DOWN) {
                //index++;
                if (shurikens.isEmpty()) {

                    var shuriken = new Shuriken(getResources(), w, h, x, y);
                    //shuriken.setPosition(0.1f * w, h * 0.8f);
                    ninjaSprite.shoot();
                    shurikens.add(shuriken);
                    //Try using timer dx dt for shuriken
                    //for (int j = 0; j< 100; j++)
                    tim.register(shuriken);
                   // Log.d("Index ++", "Index inc5rease");



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
                }
            }
        }
        return true;
    }



    private void detectCollisions(){
        for (var g: ghosts) {
            for (var s : shurikens){
                if (g.intersect(s)) {
                    if (g.getAnswer().equals(answer)) {
                        if (right_answer_fx!=null) {
                            right_answer_fx.start();
                            score += g.getPoint();
                        }
                        //right_answer_fx.release();
                    }
                    else score -= g.getPoint()*0.25;
                    g.gone();
                    s.gone();
                }
            }
        }
    }

    //Generating ghosts randomly
    private void createGhosts(){
        int maxLevel = getHeight()/(getWidth()/20) - 1;
        //System.out.println("maxLevel is : " + maxLevel);
        if (level > maxLevel) level = maxLevel;
        else if (level < 1 ) level = 1;
        if (ghosts.isEmpty() && onScreenTime != 0){
            ansExist = false;
            answer   = randomMathProb();
            //System.out.println(""+a+" "+operator+" "+b+" = "+answer );
            Ghost ghost = null;
            for (int i =0 ; i<=level ; i++) {
                if (i!= level)
                    ghost = new Ghost(getResources(), w, h,randomMathProb(),getContext());
                else  ghost = new Ghost(getResources(), w, h,answer,getContext());
                // ghost.setPosition((float) Math.random() * (w * 0.5f - ghost.bound.width()) + (w * 0.5f), (float) Math.random() * (h * 0.5f) + h * 0.25f);
                tim.register(ghost);
                ghosts.add(ghost);
            }
        } else if (!ghosts.get(ghosts.size()-1).getAnswer().equals(answer)) {
/*            int maxLevel = getHeight()/(getWidth()/20) -1 ;
            //System.out.println("maxLevel is : " + maxLevel);
            if (level > maxLevel) level = maxLevel;*/
            ghosts.stream().forEach(g -> g.gone());
            ghosts.clear();
            if (onScreenTime != 0){
                ansExist = false;
                answer   = randomMathProb();
                //System.out.println(""+a+" "+operator+" "+b+" = "+answer );
                Ghost ghost = null;
                for (int i = 0; i <= level; i++) {
                    if (i != level)
                        ghost = new Ghost(getResources(), w, h, randomMathProb(), getContext());
                    else ghost = new Ghost(getResources(), w, h, answer, getContext());
                    // ghost.setPosition((float) Math.random() * (w * 0.5f - ghost.bound.width()) + (w * 0.5f), (float) Math.random() * (h * 0.5f) + h * 0.25f);
                    tim.register(ghost);
                    ghosts.add(ghost);
                }
            }
        }
    }

    /**
     * Check if game is ended
     */
    private void endGame(){
        if (inCount>=5 || wrongShoot>= 5){
            AlertDialog.Builder ab = new AlertDialog.Builder(getContext());
            ab.setCancelable(false)
                    .setTitle("YOU LOST :(!")
                    .setPositiveButton("Play Again",(d,i)-> playAgain())
                    .setNegativeButton("Stop",(d,i)->((Activity)getContext()).finish());
            tim.removeMessages(0);
            var ad = ab.create();
            ad.show();
        }
    }

    private void playAgain(){
        onScreenTime = 20;

        shurikens = new ArrayList<>();
        ghosts = new ArrayList<>();

        inCount = 0;
        wrongShoot = 0;
        w = getWidth();
        h = getHeight();

        ninjaSprite = new NinjaSprite(getResources(),w,h);
        ninjaSprite.setPosition(w*0.05f, h*0.7f);
        background = new Background(getResources(),w,h);

        tim = new UpdateTimer();

        tim.register(ninjaSprite);
        tim.register(background);
        tim.register(this);
    }

    private void nextLevelAccomplished(){
        tim.removeMessages(0);
        level++;

        try(var fos = getContext().openFileOutput("level.txt",Context.MODE_PRIVATE)) {
            fos.write((""+level + " ").getBytes());
        } catch (IOException e){
            e.printStackTrace();
        }


        AlertDialog.Builder ab = new AlertDialog.Builder(getContext());
        ab.setCancelable(false)
                .setTitle("Going to level " + (level-1))
                .setPositiveButton("Continue",(d,i)-> playAgain())
                .setNegativeButton("Stay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        level--;
                        playAgain();
                    }
                });
                //.setNegativeButton("Stop",(d,i)->((Activity)getContext()).finish());
        var ad = ab.create();
        ad.show();
    }

    private Number randomMathProb(){
        var mathMax = SettingsActivity.SettingsFragment.mathLevel(getContext());
        int c;
        Collections.shuffle(operators);
        if (ansExist==false) {
            ansExist = true;
            this.a = new Random().nextInt(mathMax)+1;
            this.b = new Random().nextInt(mathMax)+1;
            operator = operators.get(0);
            switch (operator){
                case "+":
                    c = this.a+this.b;
                    break;
                case "*": c = this.a * this.b;
                    break;
                case "-":
                    c = a-b;
                    break;
                case "/":
                    float d = (float)a/(float)b;
                    return d;
                default:
                    c = 0;
                    break;
            }
        } else {
            int a = new Random().nextInt(mathMax)+1;
            int b = new Random().nextInt(mathMax)+1;
            switch (operators.get(0)){
                case "+":
                    c = a+b;
                    break;
                case "*": c = a * b;
                    break;
                case "-":
                    c = a-b;
                    break;
                case "/":
                    float d = (float)a/(float)b;
                    return d;
                default:
                    c = 0;
                    break;
            }
        }

        return c;

    }

    public void releaseFXs() {
    }

    public void resumeTimer() {
    }

    public void pauseTimer() {
    }
}
