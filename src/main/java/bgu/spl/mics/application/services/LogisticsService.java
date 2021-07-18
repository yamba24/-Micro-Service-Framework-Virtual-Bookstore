package bgu.spl.mics.application.services;

import bgu.spl.mics.Future;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.*;
import bgu.spl.mics.application.passiveObjects.DeliveryVehicle;
import bgu.spl.mics.application.passiveObjects.Inventory;
import bgu.spl.mics.application.passiveObjects.MoneyRegister;
import bgu.spl.mics.application.passiveObjects.ResourcesHolder;

import java.util.concurrent.CountDownLatch;

/**
 * Logistic service in charge of delivering books that have been purchased to customers.
 * Handles {@link DeliveryEvent}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link ResourcesHolder}, {@link MoneyRegister}, {@link Inventory}.
 * 
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class LogisticsService extends MicroService {
	private int currTick=0;
	CountDownLatch counter;
	Object lock;
	public LogisticsService(int num, CountDownLatch counter) {

	    super("Logistics Service " + num);
	    this.counter=counter;
	    lock =new Object();
	}

	@Override
	protected void initialize() {
		this.subscribeBroadcast(TickBroadcast.class, broadcast -> {
			this.currTick=broadcast.getCurrTick();
		});
		this.subscribeEvent(DeliveryEvent.class, event->{
			Future<Future<DeliveryVehicle>> vehicleFuture = this.sendEvent(new DeliveryAcquire());
			if(vehicleFuture!= null) {
				Future<DeliveryVehicle> future = vehicleFuture.get();
				if (future != null) {
					DeliveryVehicle deliveryVehicle = future.get();
					if (deliveryVehicle != null) {
						deliveryVehicle.deliver(event.getAddress(), event.getDistance());
						sendEvent(new DeliveryCarRelease(deliveryVehicle));
					} else complete(event, null);
				} else complete(event, null);
			}
			else complete(event,null);
		});
		this.subscribeBroadcast(TerminateBroadcast.class, broadcast->{
			terminate();
		});
		counter.countDown();
	}

}
