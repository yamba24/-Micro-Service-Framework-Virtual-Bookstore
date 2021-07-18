package bgu.spl.mics.application.services;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.CheckAvailabilityEvent;
import bgu.spl.mics.application.messages.TakeBookEvent;
import bgu.spl.mics.application.messages.TerminateBroadcast;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.passiveObjects.Inventory;
import bgu.spl.mics.application.passiveObjects.MoneyRegister;
import bgu.spl.mics.application.passiveObjects.ResourcesHolder;

import java.util.concurrent.CountDownLatch;

/**
 * InventoryService is in charge of the book inventory and stock.
 * Holds a reference to the {@link Inventory} singleton of the store.
 * This class may not hold references for objects which it is not responsible for:
 * {@link ResourcesHolder}, {@link MoneyRegister}.
 * 
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */

public class InventoryService extends MicroService{
	static private Inventory inv;
	CountDownLatch counter;
	public InventoryService(int num, CountDownLatch counter) {
		super("Inventory Service " + num);
		inv = inv.getInstance();
		this.counter=counter;
	}

	@Override
	protected void initialize() {
		this.subscribeEvent(CheckAvailabilityEvent.class, event->{
			this.complete(event, inv.checkAvailabiltyAndGetPrice(event.getBook()));
		});
		this.subscribeEvent(TakeBookEvent.class, event->{
                this.complete(event, inv.take(event.getBook()));
        });
		this.subscribeBroadcast(TerminateBroadcast.class, broadcast->{
			terminate();
		});
		counter.countDown();
	}

}
