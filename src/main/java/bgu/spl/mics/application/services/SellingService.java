package bgu.spl.mics.application.services;

import bgu.spl.mics.Future;
import bgu.spl.mics.MessageBusImpl;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.*;
import bgu.spl.mics.application.passiveObjects.*;

import java.util.concurrent.CountDownLatch;

import static bgu.spl.mics.application.passiveObjects.OrderResult.NOT_IN_STOCK;
import static bgu.spl.mics.application.passiveObjects.OrderResult.SUCCESSFULLY_TAKEN;

/**
 * Selling service in charge of taking orders from customers.
 * Holds a reference to the {@link MoneyRegister} singleton of the store.
 * Handles {@link BookOrderEvent}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link ResourcesHolder}, {@link Inventory}.
 * 
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class SellingService extends MicroService{
	static private MoneyRegister moneyRegister;
    private int currTick=0;
    CountDownLatch counter;
	public SellingService(int num,CountDownLatch counter) {
	    super("Selling Service " + num);
		moneyRegister = moneyRegister.getInstance();
		this.counter=counter;
	}

	@Override
	protected void initialize() {

        this.subscribeBroadcast(TickBroadcast.class, broadcast -> {
            this.currTick=broadcast.getCurrTick();
        });
        this.subscribeBroadcast(TerminateBroadcast.class,broadcast->{
            terminate();
        });
		this.subscribeEvent(BookOrderEvent.class, bookOrderEvent ->{
            synchronized (bookOrderEvent.getCustomer()) {
                Future<Integer> check = this.sendEvent(new CheckAvailabilityEvent(bookOrderEvent.getBook())); // check if the book is available
                if (check != null) {
                    Integer price = check.get();
                    if (price != null && price != -1) {
                        if (bookOrderEvent.getCustomer().getAvailableCreditAmount() >= price) { // we want to check if the customer has enough money to complete the order
                            Future<OrderResult> orderResultFuture = sendEvent(new TakeBookEvent(bookOrderEvent.getBook()));
                            if (orderResultFuture==null){complete(bookOrderEvent,null);}
                            else{
                                OrderResult orderResult=orderResultFuture.get();
                                if (orderResult.equals(SUCCESSFULLY_TAKEN)) {
                                    moneyRegister.chargeCreditCard(bookOrderEvent.getCustomer(), price); // than make the charge
                                    OrderReceipt o = new OrderReceipt(moneyRegister.getNumReceipt(), this.getName(), bookOrderEvent.getCustomer().getId(), bookOrderEvent.getBook(), price, bookOrderEvent.getOrderTick(), currTick, currTick);
                                    bookOrderEvent.getCustomer().addOrderReceipt(o);
                                    moneyRegister.file(o);
                                    complete(bookOrderEvent, o);
                                } else {
                                    complete(bookOrderEvent, null);
                                }}
                        } else {
                            complete(bookOrderEvent, null);
                        }
                    } else {
                        complete(bookOrderEvent, null);
                    }
                }
                else{complete(bookOrderEvent,null);}
            }
		});
		counter.countDown();
	}
}
