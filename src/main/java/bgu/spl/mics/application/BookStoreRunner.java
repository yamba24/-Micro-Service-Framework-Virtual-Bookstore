package bgu.spl.mics.application;

import bgu.spl.mics.MessageBusImpl;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.BookOrderEvent;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.passiveObjects.*;
import bgu.spl.mics.application.services.*;
import com.google.gson.Gson;

import javax.naming.LimitExceededException;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;

/**
 * This is the Main class of the application. You should parse the input file,
 * create the different instances of the objects, and run the system.
 * In the end, you should output serialized objects.
 */
public class BookStoreRunner {
    public static void main(String[] args) {
        CountDownLatch counter;
        LinkedList<Thread> Threads = new LinkedList<>();
        Vector<MicroService> microServices = new Vector<>();
        try {

            String file = new String(Files.readAllBytes(Paths.get(args[0])));
            GsonObject data = new Gson().fromJson(file, GsonObject.class);

            BookInventoryInfo[] books = new BookInventoryInfo[data.initialInventory.length];
            for (int i = 0; i < data.initialInventory.length; i++) {
                books[i] = new BookInventoryInfo(data.initialInventory[i].bookTitle, data.initialInventory[i].price, data.initialInventory[i].amount);
            }
            Inventory.getInstance().load(books);

            DeliveryVehicle[] vehicles = new DeliveryVehicle[data.initialResources[0].vehicles.length];
            for (int i = 0; i < data.initialResources[0].vehicles.length; i++) {
                vehicles[i] = new DeliveryVehicle(data.initialResources[0].vehicles[i].license, data.initialResources[0].vehicles[i].speed);
            }
            ResourcesHolder.getInstance().load(vehicles);

            int servicseSum = data.services.selling + data.services.inventoryService + data.services.logistics + data.services.resourcesService;
            counter = new CountDownLatch(servicseSum);

            for (int i = 0; i < data.services.selling; i++) {
                microServices.add(new SellingService(i, counter));
            }

            for (int i = 0; i < data.services.logistics; i++) {
                microServices.add(new LogisticsService(i, counter));
            }
            for (int i = 0; i < data.services.inventoryService; i++) {
                microServices.add(new InventoryService(i, counter));
            }
            for (int i = 0; i < data.services.resourcesService; i++) {
                microServices.add(new ResourceService(i, counter));
            }

            HashMap<Integer, Customer> customerHashMap = new HashMap<>();
            Customer[] customers = new Customer[data.services.customers.length];
            for (int i = 0; i < data.services.customers.length; i++) {
                int id = data.services.customers[i].id;
                int distance = data.services.customers[i].distance;
                String adresss = data.services.customers[i].address;
                String name = data.services.customers[i].name;
                int credit = data.services.customers[i].creditCard.number;
                int amount = data.services.customers[i].creditCard.amount;
                Customer cToAdd = new Customer(id, name, adresss, distance, credit, amount);
                customers[i] = cToAdd;
                LinkedList<BookOrderEvent> order = new LinkedList<>();
                for(int j = 0; j<data.services.customers[i].orderSchedule.length; j++){
                    String book = data.services.customers[i].orderSchedule[j].bookTitle;
                    int tick = data.services.customers[i].orderSchedule[j].tick;
                   BookOrderEvent orderToAdd = new BookOrderEvent(cToAdd, book, tick);
                    order.add(orderToAdd);
                }
                cToAdd.setOrderSchedule(order);
                microServices.addElement(new APIService(i, cToAdd, counter));
                customerHashMap.put(cToAdd.getId(), cToAdd);
            }

            TimeService timeService = new TimeService(data.services.time.speed, data.services.time.duration);

            for (MicroService ms : microServices) {
                Threads.addLast(new Thread(ms));
                Threads.getLast().start();
            }
            counter.await();
            Thread time = new Thread(timeService);
            time.start();

            for (Thread t : Threads){
                t.join();
            }
            time.join();


            printToFile(customerHashMap, args[1]);
            Inventory.getInstance().printInventoryToFile(args[2]);
            MoneyRegister.getInstance().printOrderReceipts(args[3]);
            printToFile(MoneyRegister.getInstance(), args[4]);

        } catch (InterruptedException e) {
            e.printStackTrace();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
        public static void printToFile(Object toPrint, String filename){
            try {
                 FileOutputStream fos = new FileOutputStream(filename,false);
                 ObjectOutputStream oos = new ObjectOutputStream(fos);
                 oos.writeObject(toPrint);
                 oos.close();
                 fos.close();
            } catch (IOException e) {
            }

        }
    }