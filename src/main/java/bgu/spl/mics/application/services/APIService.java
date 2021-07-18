package bgu.spl.mics.application.services;

import bgu.spl.mics.Broadcast;
import bgu.spl.mics.Future;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.BookOrderEvent;
import bgu.spl.mics.application.messages.DeliveryEvent;
import bgu.spl.mics.application.messages.TerminateBroadcast;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.passiveObjects.*;

import java.util.Comparator;
import java.util.concurrent.CountDownLatch;

/**
 * APIService is in charge of the connection between a client and the store.
 * It informs the store about desired purchases using {@link BookOrderEvent}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link ResourcesHolder}, {@link MoneyRegister}, {@link Inventory}.
 * <p>
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class APIService extends MicroService {
    private Customer customer;
    private int currTick=0;
    CountDownLatch counter;
    public APIService(int num, Customer customer, CountDownLatch counter) {
        super("API Service " + num);
        this.customer = customer;
        // sort according to the ticks
        this.customer.getOrderSchedule().sort(Comparator.comparing(BookOrderEvent::getOrderTick));
        this.counter=counter;
    }

    @Override
    protected void initialize() {
        this.subscribeBroadcast(TickBroadcast.class, broadcast -> {
            this.currTick=broadcast.getCurrTick();
                while (!customer.getOrderSchedule().isEmpty() && customer.getOrderSchedule().peek().getOrderTick() <= broadcast.getCurrTick()) {
                    BookOrderEvent BOE = customer.getOrderSchedule().poll();
                    Future<OrderReceipt> future = sendEvent(BOE);
                    if (future!=null){
                    OrderReceipt or=future.get();
                    if (or != null) {
                        this.sendEvent(new DeliveryEvent(customer.getAddress(), customer.getDistance()));
                    }}
                }
        });

        this.subscribeBroadcast(TerminateBroadcast.class, broadcast->{
            terminate();
        });
        counter.countDown();
    }

}
