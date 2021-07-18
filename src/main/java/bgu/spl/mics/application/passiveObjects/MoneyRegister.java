package bgu.spl.mics.application.passiveObjects;


import bgu.spl.mics.MessageBusImpl;
import bgu.spl.mics.application.BookStoreRunner;

import java.io.*;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Passive object representing the store finance management. 
 * It should hold a list of receipts issued by the store.
 * <p>
 * This class must be implemented safely as a thread-safe singleton.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You can add ONLY private fields and methods to this class as you see fit.
 */
public class MoneyRegister implements Serializable {

	private Vector<OrderReceipt> receipts;
	/**
     * Retrieves the single instance of this class.
     */

	//constructor
	private MoneyRegister() {
		receipts = new Vector<>();

	}

	private static class SingletonHolder {
		private static MoneyRegister moneyRegister=new MoneyRegister();
	}

	public static MoneyRegister getInstance() {
		return SingletonHolder.moneyRegister;
	}


	
	/**
     * Saves an order receipt in the money register.
     * <p>   
     * @param r		The receipt to save in the money register.
     */
	public void file (OrderReceipt r) {
		receipts.add(r);
	}
	
	/**
     * Retrieves the current total earnings of the store.  
     */
	public int getTotalEarnings() {
	    int sum = 0;
	    for(int i = 0; i<this.receipts.size(); i++){
	        sum = sum + receipts.get(i).getPrice();
        }
		return sum;
	}
	
	/**
     * Charges the credit card of the customer a certain amount of money.
     * <p>
     * @param amount 	amount to charge
     */
	public synchronized void chargeCreditCard(Customer c, int amount) {
        if (amount <= c.getAvailableCreditAmount()) {
            c.setAmount(amount);
        }
	}

	public int getNumReceipt(){
		return this.receipts.size();
	}

	public Vector<OrderReceipt> getOrderReceipts() {
		return receipts;
	}

	/**
     * Prints to a file named @filename a serialized object List<OrderReceipt> which holds all the order receipts 
     * currently in the MoneyRegister
     * This method is called by the main method in order to generate the output.. 
     */
	public void printOrderReceipts(String filename) {
		BookStoreRunner.printToFile(this.receipts, filename);
	}
}