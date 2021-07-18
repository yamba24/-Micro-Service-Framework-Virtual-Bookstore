package bgu.spl.mics.application.messages;
import bgu.spl.mics.application.passiveObjects.Customer;
import bgu.spl.mics.Event;
import bgu.spl.mics.application.passiveObjects.OrderReceipt;

import java.io.Serializable;

public class BookOrderEvent implements Event<OrderReceipt>, Serializable {
    private Customer customer; // the name of the customer that made the order
    private String book; // the name of the book
    private int orderTick;

    public BookOrderEvent (Customer customer, String book,int orderTick){
        this.customer = customer;
        this.book = book;
        this.orderTick = orderTick;
    }

    public Customer getCustomer(){
        return this.customer;
    }

    public String getBook(){
        return this.book;
    }

    public int getOrderTick() {
        return this.orderTick;
    }
}
