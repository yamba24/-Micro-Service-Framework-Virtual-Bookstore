/*package bgu.spl.mics.application.passiveObjects;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Vector;

import static org.junit.Assert.*;

public class InventoryTest {
    Inventory inv = Inventory.getInstance();
    Vector<BookInventoryInfo> bookList;

    @Before
    public void setUp() throws Exception {
        bookList= new Vector <>();
        bookList.add(new BookInventoryInfo("Harry Potter",20, 30));
        bookList.add(new BookInventoryInfo("Lord of the rings",10, 5));
    }

    @After
    public void tearDown() throws Exception { // we can't do tear down in the inventory
    }

    @Test
    public void getInstance() {assertNotNull(inv);}

    @Test
    public void load() {
        BookInventoryInfo[] b = new BookInventoryInfo[2];
        b[0] = bookList.get(0);
        b[1] = bookList.get(1);
        try {
            inv.load(b);
        } catch (Exception E) {
            fail("LOAD FAILED");
        };
    }

    @Test
    public void take() {
        BookInventoryInfo[] b = new BookInventoryInfo[2];
        b[0] = bookList.get(0);
        b[1] = bookList.get(1);
        inv.load(b);
        assertTrue(inv.take("Lord of the rings 2") == OrderResult.NOT_IN_STOCK);
        assertEquals( "",inv.take(b[1].getBookTitle()) , OrderResult.SUCCESSFULLY_TAKEN);
    }

    @Test
    public void checkAvailabiltyAndGetPrice() {
        assertEquals(inv.checkAvailabiltyAndGetPrice("Harry Potter"),20);
        assertEquals(inv.checkAvailabiltyAndGetPrice("Lord of the rings 2"),-1);
        assertEquals(inv.checkAvailabiltyAndGetPrice("Lord of the rings"),10);
    }

    @Test
    public void printInventoryToFile() {
    }
}

*/