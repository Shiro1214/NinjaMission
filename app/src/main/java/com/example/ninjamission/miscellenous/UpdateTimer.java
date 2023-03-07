package com.example.ninjamission.miscellenous;

import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

import com.example.ninjamission.data.Ghost;
import com.example.ninjamission.data.Shuriken;

import java.util.ArrayList;
import java.util.function.Predicate;

public class UpdateTimer extends Handler implements Predicate<TickListener> {
    static final int DELAY_TIME = 100;
    private ArrayList<TickListener> subscribes;
    private ArrayList<TickListener> tmp;
    private boolean registering = false;
    public UpdateTimer(){
        subscribes = new ArrayList<>();
        tmp  = new ArrayList<>();
        sendMessageDelayed(obtainMessage(),DELAY_TIME);
    }


    @Override
    public void handleMessage(@NonNull Message m) {
        super.handleMessage(m);
        sendMessageDelayed(obtainMessage(),DELAY_TIME);
        //removing gones
        removeGones();
        //registering
        if (registering) {
            subscribes.addAll(tmp);
            tmp.clear();
            registering = false;
        }

        //updates
        //subscribes.forEach(TickListener::tick);
        for (var ticker : subscribes ) {
            if (ticker instanceof Shuriken) {
                var s = (Shuriken)ticker;
                for (int i = 0 ; i < 100; i++) {
                    s.tick();
                }
            } else ticker.tick();
        }
        System.out.println("Size of Listeners is :" + subscribes.size());
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
}
