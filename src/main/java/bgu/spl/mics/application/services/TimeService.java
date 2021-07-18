package bgu.spl.mics.application.services;

import bgu.spl.mics.MessageBusImpl;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.TerminateBroadcast;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.passiveObjects.Inventory;
import bgu.spl.mics.application.passiveObjects.MoneyRegister;
import bgu.spl.mics.application.passiveObjects.ResourcesHolder;

import java.sql.Time;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * TimeService is the global system timer There is only one instance of this micro-service.
 * It keeps track of the amount of ticks passed since initialization and notifies
 * all other micro-services about the current time tick using {@link TickBroadcast}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link ResourcesHolder}, {@link MoneyRegister}, {@link Inventory}.
 * <p>
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class TimeService extends MicroService {
    public int speed;
    public int duration; // the last tick
    public AtomicInteger currTick;
    public long start;

    public TimeService(int speed, int duration) {
        super("Time Service");
        this.speed = speed;
        this.duration = duration;
        this.currTick = new AtomicInteger();
        currTick.set(1);
        this.start = 0;
    }

    @Override
    protected void initialize() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (currTick.get() < duration) {
                    sendBroadcast(new TickBroadcast(currTick.getAndIncrement(), duration));}

               else if (currTick.get() == duration) {
                    sendBroadcast(new TerminateBroadcast());
                    timer.cancel();
                    timer.purge();}
            }
        }, 0, speed);
        subscribeBroadcast(TerminateBroadcast.class,broadcast->{terminate(); });
    }
}