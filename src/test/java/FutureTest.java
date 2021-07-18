/*import bgu.spl.mics.Future;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

public class FutureTest {
    private Future<Integer> future;
    private Future<String> future1;

    @Before
    public void setUp() throws Exception {
        this.future = new Future<Integer>();
        this.future1 = new Future<String>();
    }

    @After
    public void tearDown() throws Exception {
        future = null;
        future1 = null;
    }

    @Test
    public void get() {
        assertNull("expected null", future.get());

        future.resolve(5);
        assertEquals(new Integer(5), future.get());

        future1.resolve("Harry Potter");
        assertEquals("Harry Potter", future1.get());
    }

    @Test
    public void resolve() {
        future.resolve(5);
        assertEquals(new Integer(5), future.get());
    }

    @Test
    public void isDone() {
        assertFalse(future.isDone());
        future.resolve(5);
        assertTrue(future.isDone());
    }

    @Test
    public void get1() {
        TimeUnit time = TimeUnit.MILLISECONDS;
        Object o = null;
        o = future.get(3000,time);
        assertNull(o);

        future.resolve(new Integer(5));
        assertEquals("expected 5", new Integer(5), future.get(4000, time));
    }
}
*/