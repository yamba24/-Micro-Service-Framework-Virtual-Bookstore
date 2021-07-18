package bgu.spl.mics.application.services;

import bgu.spl.mics.Future;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.DeliveryAcquire;
import bgu.spl.mics.application.messages.DeliveryCarRelease;
import bgu.spl.mics.application.messages.TerminateBroadcast;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.passiveObjects.DeliveryVehicle;
import bgu.spl.mics.application.passiveObjects.Inventory;
import bgu.spl.mics.application.passiveObjects.MoneyRegister;
import bgu.spl.mics.application.passiveObjects.ResourcesHolder;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;

/**
 * ResourceService is in charge of the store resources - the delivery vehicles.
 * Holds a reference to the {@link ResourcesHolder} singleton of the store.
 * This class may not hold references for objects which it is not responsible for:
 * {@link MoneyRegister}, {@link Inventory}.
 * 
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class ResourceService extends MicroService{
	static private ResourcesHolder holder;
	private int currTick=0;
	private ConcurrentLinkedQueue <Future<DeliveryVehicle>> wait2vehicles;
	CountDownLatch counter;


	public ResourceService(int num,CountDownLatch counter) {
		super("Resource Service " + num);
		this.holder = holder.getInstance();
		this.counter=counter;
		this.wait2vehicles = new ConcurrentLinkedQueue<>();
	}

	@Override
	protected void initialize() {
		this.subscribeBroadcast(TickBroadcast.class, broadcast -> {
			this.currTick=broadcast.getCurrTick();
		});
		this.subscribeBroadcast(TerminateBroadcast.class, broadcast->{
			for(Future<DeliveryVehicle> f:wait2vehicles) {
				if (!f.isDone())
					f.resolve(null);
			}
			terminate();
		});

		this.subscribeEvent(DeliveryAcquire.class, event->{
			Future<DeliveryVehicle> future=holder.acquireVehicle();
			wait2vehicles.add(future);
			complete(event,future);
		});
		this.subscribeEvent(DeliveryCarRelease.class,event->{
			ResourcesHolder.getInstance().releaseVehicle(event.getCar());
			complete(event,true);
		});
		counter.countDown();
	}

}
