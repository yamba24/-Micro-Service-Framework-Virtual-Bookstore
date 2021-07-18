package bgu.spl.mics.application.passiveObjects;

import bgu.spl.mics.application.messages.BookOrderEvent;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Passive data-object representing a customer of the store.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You may add fields and methods to this class as you see fit (including public methods).
 */
public class Customer implements Serializable {
	private String name;
	private int id;
	private String address;
	private int distance;
	private  int CreditNumber;
	private int cashLeft;
	private List <OrderReceipt> orderReceipts;
	private LinkedList<BookOrderEvent> orderSchedule;

	public Customer (int id, String name, String address, int distance, int CreditNumber, int cashLeft){
		this.name = name;
		this.id = id;
		this.address = address;
		this.distance = distance;
		this.CreditNumber = CreditNumber;
		this.cashLeft = cashLeft;
		this.orderReceipts = new ArrayList<>();
		this.orderSchedule = new LinkedList<>();
	}

	public LinkedList<BookOrderEvent> getOrderSchedule() {
		return orderSchedule;
	}

	public void setOrderSchedule(LinkedList<BookOrderEvent> orderSchedule) {

		this.orderSchedule = orderSchedule;
	}

	/**
     * Retrieves the name of the customer.
     */
	public String getName() {
		return this.name;
	}

	/**
     * Retrieves the ID of the customer  . 
     */
	public int getId() {
		return this.id;
	}
	
	/**
     * Retrieves the address of the customer.  
     */
	public String getAddress() {
		return this.address;
	}
	
	/**
     * Retrieves the distance of the customer from the store.  
     */
	public int getDistance() {
		return this.distance;
	}

	
	/**
     * Retrieves a list of receipts for the purchases this customer has made.
     * <p>
     * @return A list of receipts.
     */
	public List<OrderReceipt> getCustomerReceiptList() {
	    return this.orderReceipts;
	}

	public void addOrderReceipt(OrderReceipt order){
	    orderReceipts.add(order);
    }
	
	/**
     * Retrieves the amount of money left on this customers credit card.
     * <p>
     * @return Amount of money left.   
     */
	public int getAvailableCreditAmount() {
		return this.cashLeft;
	}
	
	/**
     * Retrieves this customers credit card serial number.    
     */
	public int getCreditNumber() {
	    return this.CreditNumber;
	}
	public void setAmount(int amount){ // this method is using in the MoneyRegister
	    this.cashLeft = cashLeft - amount;
    }
}
