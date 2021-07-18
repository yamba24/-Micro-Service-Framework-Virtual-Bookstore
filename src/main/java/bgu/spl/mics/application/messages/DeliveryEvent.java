package bgu.spl.mics.application.messages;
import bgu.spl.mics.Event;

import java.io.Serializable;

public class DeliveryEvent implements Event<Object>, Serializable {
    private String address;
    private int distance;

    public DeliveryEvent(String address, int distance){
        this.address = address;
        this.distance = distance;
    }
    public String getAddress(){
        return this.address;
    }
    public int getDistance(){
        return this.distance;
    }

}
