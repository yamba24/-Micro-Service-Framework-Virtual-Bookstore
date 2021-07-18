package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;
import bgu.spl.mics.Future;
import bgu.spl.mics.application.passiveObjects.DeliveryVehicle;

import java.io.Serializable;

public class DeliveryAcquire implements Event<Future<DeliveryVehicle>>, Serializable {

}
