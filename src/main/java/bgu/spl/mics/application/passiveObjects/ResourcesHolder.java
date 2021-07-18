package bgu.spl.mics.application.passiveObjects;

import bgu.spl.mics.Future;

import java.io.Serializable;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Passive object representing the resource manager.
 * You must not alter any of the given public methods of this class.
 * <p>
 * This class must be implemented safely as a thread-safe singleton.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You can add ONLY private methods and fields to this class.
 */
public class ResourcesHolder implements Serializable {
	private static ResourcesHolder holder;
	private ConcurrentLinkedQueue <DeliveryVehicle> vehicles;
	private ConcurrentLinkedQueue <Future<DeliveryVehicle>> wait2vehicles;
	/**
     * Retrieves the single instance of this class.
     */
	public ResourcesHolder(){
		vehicles = new ConcurrentLinkedQueue<>();
		wait2vehicles=new ConcurrentLinkedQueue<>();
	}
	public static ResourcesHolder getInstance() {
		if(holder == null){
			holder = new ResourcesHolder();
		}
		return holder;
	}
	
	/**
     * Tries to acquire a vehicle and gives a future object which will
     * resolve to a vehicle.
     * <p>
     * @return 	{@link Future<DeliveryVehicle>} object which will resolve to a 
     * 			{@link DeliveryVehicle} when completed.   
     */
	public Future<DeliveryVehicle> acquireVehicle() {
		Future<DeliveryVehicle> result = new Future<>(); // create new future object of type Delivery Vehicle
			DeliveryVehicle vehicleToSend = vehicles.poll();
			if(vehicleToSend != null){
				result.resolve(vehicleToSend);
			}
			else wait2vehicles.add(result);
			return result;
	}
	
	/**
     * Releases a specified vehicle, opening it again for the possibility of
     * acquisition.
     * <p>
     * @param vehicle	{@link DeliveryVehicle} to be released.
     */
	public void releaseVehicle (DeliveryVehicle vehicle) {
		synchronized (wait2vehicles) {
			if (wait2vehicles.isEmpty()) {
				System.out.println(vehicle.getLicense() + " is being released");
				vehicles.add(vehicle);
			}
			else {
				wait2vehicles.poll().resolve(vehicle);
			}
		}
	}


	
	/**
     * Receives a collection of vehicles and stores them.
     * <p>
     * @param vehicles	Array of {@link DeliveryVehicle} instances to store.
     */
	public void load(DeliveryVehicle[] vehicles){
			for (DeliveryVehicle v : vehicles) {
				this.vehicles.add(v);
			}
		}
}
