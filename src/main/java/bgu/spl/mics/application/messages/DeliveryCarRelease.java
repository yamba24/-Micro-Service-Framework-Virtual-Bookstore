package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;
import bgu.spl.mics.application.passiveObjects.DeliveryVehicle;

import java.io.Serializable;

public class DeliveryCarRelease implements Event<Object>, Serializable {
    private DeliveryVehicle car;

    public DeliveryCarRelease(DeliveryVehicle car){
        this.car = car;
    }

    public DeliveryVehicle getCar(){
        return this.car;
    }
}
