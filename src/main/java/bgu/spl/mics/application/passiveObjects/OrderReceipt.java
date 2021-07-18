package bgu.spl.mics.application.passiveObjects;

import org.hamcrest.core.Is;

import java.io.Serializable;

/**
 * Passive data-object representing a receipt that should 
 * be sent to a customer after the completion of a BookOrderEvent.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You may add fields and methods to this class as you see fit (including public methods).
 */
public class OrderReceipt implements Serializable {
	private int id;
	private String seller;
	private int idCustomer;
	private String title;
	private int price;
	private int orderTick;
	private int IssuedTick;
	private int tick;

	public OrderReceipt(int id, String seller, int idCustomer, String title, int price, int orderTick, int IssuedTick, int tick){
		this.id = id;
		this.seller = seller;
		this.idCustomer = idCustomer;
		this.title = title;
		this.price = price;
		this.orderTick = orderTick;
		this.IssuedTick = IssuedTick;
		this.tick = tick;
	}
	/**
     * Retrieves the orderId of this receipt.
     */
	public int getOrderId() {
	    return this.id;
	}
	
	/**
     * Retrieves the name of the selling service which handled the order.
     */
	public String getSeller() {
		return this.seller;
	}
	
	/**
     * Retrieves the ID of the customer to which this receipt is issued to.
     * <p>
     * @return the ID of the customer
     */
	public int getCustomerId() {
		return this.idCustomer;
	}
	
	/**
     * Retrieves the name of the book which was bought.
     */
	public String getBookTitle() {
		return this.title;
	}
	
	/**
     * Retrieves the price the customer paid for the book.
     */
	public int getPrice() {
		return this.price;
	}
	
	/**
     * Retrieves the tick in which this receipt was issued.
     */
	public int getIssuedTick() {
		return this.IssuedTick;
	}
	
	/**
     * Retrieves the tick in which the customer sent the purchase request.
     */
	public int getOrderTick() {
		return this.orderTick;
	}
	
	/**
     * Retrieves the tick in which the treating selling service started 
     * processing the order.
     */
	public int getProcessTick() {
		return this.tick;
	}
}
