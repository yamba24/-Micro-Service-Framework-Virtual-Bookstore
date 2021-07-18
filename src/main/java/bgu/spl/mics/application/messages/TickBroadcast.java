package bgu.spl.mics.application.messages;
import bgu.spl.mics.Broadcast;
import java.io.Serializable;

public class TickBroadcast implements Broadcast, Serializable {
    private int currTick;
    private int duration;

    public TickBroadcast(int currTick, int duration){
        this.currTick = currTick;
        this.duration = duration;
    }
    public int getCurrTick(){
        return this.currTick;
    }
    public void setCurrTick(int currTick){
        this.currTick = currTick;
    }
    public int getDuration(){
        return this.duration;
    }

}