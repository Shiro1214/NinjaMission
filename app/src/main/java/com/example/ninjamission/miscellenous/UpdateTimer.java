package com.example.ninjamission.miscellenous;

import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

import com.example.ninjamission.data.Ghost;
import com.example.ninjamission.data.Shuriken;

import java.util.ArrayList;
import java.util.function.Predicate;

public class UpdateTimer extends Handler implements Predicate<TickListener> {
    static final int DELAY_TIME = 50;
    private boolean run;
    private ArrayList<TickListener> subscribes;
    private ArrayList<TickListener> tmp;
    private boolean registering = false;
    public UpdateTimer(){
        run = true;
        subscribes = new ArrayList<>();
        tmp  = new ArrayList<>();
        sendMessageDelayed(obtainMessage(),DELAY_TIME);
    }


    @Override
    public void handleMessage(@NonNull Message m) {
        super.handleMessage(m);
        if (run) {
            sendMessageDelayed(obtainMessage(), DELAY_TIME);
            //removing gones
            removeGones();
            //registering -- avoid adding while looping (VIEW)- ConcurrentModification
            //Remove -> Add -> Loop
            if (registering) {
                subscribes.addAll(tmp);
                tmp.clear();
                registering = false;
            }

            //updates
            //subscribes.forEach(TickListener::tick);
            for (var ticker : subscribes) {
                if (ticker instanceof Shuriken) {
                    var s = (Shuriken) ticker;
                    for (int i = 0; i < DELAY_TIME; i++) {
                        s.tick();
                    }
                } else ticker.tick();
            }
        }
        //System.out.println("Size of Listeners is :" + subscribes.size());
        //Log.d("SIZE Listneres", "handleMessage: There are "+subscribes.size());
    }

    public void register(TickListener t){
        registering = true;
        tmp.add(t);
        //subscribes.add(t);
    }

    public void deRegister(TickListener t){
        subscribes.remove(t);
    }

    private void removeGones(){
        subscribes.removeIf(this);
    }



    //It's a poly array where each has different method :/ so implement predicate
    @Override
    public boolean test(TickListener t) {

        if (t instanceof Shuriken){
            var s = (Shuriken)t;
            return (s.isGone());
        }
        else if (t instanceof Ghost){
            var g = (Ghost)t;
            return (g.isIn() || g.isGone());
        }
        return false;
    }
    public void resume(){
        run = true;
        sendMessageDelayed(obtainMessage(), DELAY_TIME);
    }
    public void pause(){
        run = false;
    }
}
