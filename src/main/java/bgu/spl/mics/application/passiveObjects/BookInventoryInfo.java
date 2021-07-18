package bgu.spl.mics.application.passiveObjects;

import java.io.Serializable;
import java.util.concurrent.Semaphore;

/**
 * Passive data-object representing a information about a certain book in the inventory.
 * You must not alter any of the given public methods of this class. 
 * <p>
 * You may add fields and methods to this class as you see fit (including public methods).
 */
public class BookInventoryInfo {
	// fields
	private String name;
	private int price;
	private int amount;

	// constructor
	public BookInventoryInfo(String name, int price, int amount){
		this.name = name;
		this.price = price;
		this.amount = amount;
	}


	/**
     * Retrieves the title of this book.
     * <p>
     * @return The title of this book.   
     */
	public String getBookTitle() {
		return this.name;
	}

	/**
     * Retrieves the amount of books of this type in the inventory.
     * <p>
     * @return amount of available books.      
     */
	public int getAmountInInventory() {
		return amount;
	}

	/**
     * Retrieves the price for  book.
     * <p>
     * @return the price of the book.
     */
	public int getPrice() {
		return price;
	}

	public void setAmount (){
		amount = amount-1;
	}
}
