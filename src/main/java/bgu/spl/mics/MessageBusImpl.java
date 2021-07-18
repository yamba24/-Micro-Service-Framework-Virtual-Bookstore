package bgu.spl.mics;

import java.util.Arrays;
import java.util.concurrent.*;

/**
 * The {@link MessageBusImpl class is the implementation of the MessageBus interface.
 * Write your implementation here!
 * Only private fields and methods can be added to this class.
 */
public class MessageBusImpl implements MessageBus {
   // static private MessageBusImpl msgBus;
    private ConcurrentHashMap<Class<? extends Event>, BlockingQueue<MicroService>> EventHashMap;
    private ConcurrentHashMap<Class<? extends Broadcast>, BlockingQueue<MicroService>> BroadCastHashMap;
    private ConcurrentHashMap<MicroService, BlockingQueue<Message>> msgHashMap;
    private ConcurrentHashMap<Event, Future> futures;

    private static class SingletonHolder {
        private static MessageBusImpl instance = new MessageBusImpl();
    }

    public static MessageBusImpl getInstance() {
        return SingletonHolder.instance;
    }

    private MessageBusImpl() {
        EventHashMap = new ConcurrentHashMap<>();
        msgHashMap = new ConcurrentHashMap<>();
        BroadCastHashMap = new ConcurrentHashMap<>();
        futures = new ConcurrentHashMap<>();
        Object o=new Object();

    }



    @Override
    // add the MicroService to the queue
    public  <T> void subscribeEvent(Class<? extends Event<T>> type, MicroService m) {
        EventHashMap.putIfAbsent(type, new LinkedBlockingQueue<>());
            EventHashMap.get(type).add(m);
    }

    @Override
    public void subscribeBroadcast(Class<? extends Broadcast> type, MicroService m) {
        BroadCastHashMap.putIfAbsent(type, new LinkedBlockingQueue<MicroService>());
        /**if (!BroadCastHashMap.containsKey(type)) { // if it doesn't have Micro service queue than we will create new one
            BlockingQueue<MicroService> queue = new LinkedBlockingDeque<>();
            BroadCastHashMap.put(type, queue);
            BroadCastHashMap.get(type).add(m); */
        BroadCastHashMap.get(type).add(m);
    }

    @Override
    public <T> void complete(Event<T> e, T result) {
        //synchronized (futures) { // block this hashmap
            if (futures.get(e) != null){
            futures.get(e).resolve(result);
            futures.remove(e);}
        //}
    }

    @Override
    public void sendBroadcast(Broadcast b) {
            if (BroadCastHashMap.containsKey(b.getClass()) && !BroadCastHashMap.get(b.getClass()).isEmpty()) {
                synchronized (BroadCastHashMap.get(b.getClass())) {
                for (MicroService m : BroadCastHashMap.get(b.getClass())) {
                    try {
                        if(msgHashMap.get(m)!=null)
                            msgHashMap.get(m).put(b);
                        } catch(InterruptedException e){
                        }
                    }
                }
            }

    }
    @Override
    public synchronized  <T> Future<T> sendEvent(Event<T> e) {
        if (!EventHashMap.containsKey(e.getClass()) || EventHashMap.get(e.getClass()).isEmpty() ||EventHashMap.get(e.getClass())==null) { // if some micro service is register to this specific event
            return null;
        }
        Future<T> f = new Future<>();
        futures.put(e, f);
        MicroService m = EventHashMap.get(e.getClass()).poll(); // we will take the first micro service that is register to the specific event
        BlockingQueue<Message> queue = msgHashMap.get(m);
        queue.add(e);
        EventHashMap.get(e.getClass()).add(m); // we will put it last in the queue
        return futures.get(e);
    }

    @Override
    public void register(MicroService m) {
        msgHashMap.putIfAbsent(m,new LinkedBlockingQueue<>());
    }

    @Override
    public synchronized void unregister(MicroService m) {
            if (msgHashMap.contains(m)) { // if the micro service is exist
                synchronized (msgHashMap.get(m)) {
                for (Message mg : msgHashMap.get(m)) {
                    if (mg instanceof Event) {
                        synchronized (EventHashMap.get(mg)) {
                            EventHashMap.get(mg).remove(m);
                            futures.get(mg).resolve(null);
                        }
                    }
                    if (mg instanceof Broadcast) {
                            BroadCastHashMap.get(mg).remove(m);
                        }
                    }

                    while (!msgHashMap.get(m).isEmpty()) { // delete all the messages inside of the queue
                        msgHashMap.get(m).remove();

                    }
                    msgHashMap.remove(msgHashMap.get(m)); // delete the queue from the hashmap
                }
            }
        }



    @Override
    public  Message awaitMessage(MicroService m) throws InterruptedException {
        BlockingQueue<Message> messages = msgHashMap.get(m);
        if (messages == null){
        return null;
        } else {
            Message msg = messages.take();
            if (msg == null){
            return null;
            }
            else {
                return msg;
            }
        }
    }
}
