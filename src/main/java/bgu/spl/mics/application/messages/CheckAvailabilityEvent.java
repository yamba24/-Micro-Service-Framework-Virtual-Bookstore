package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;

import java.io.Serializable;

public class CheckAvailabilityEvent implements Event<Integer>, Serializable {
    private String book;

    public CheckAvailabilityEvent(String book){
        this.book = book;
    }
    public String getBook(){
        return this.book;
    }
}
