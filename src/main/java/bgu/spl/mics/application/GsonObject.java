package bgu.spl.mics.application;
class GsonObject {
    BookJson[] initialInventory;
    ResourceJson[] initialResources;
    ServicesJson services;

    public class BookJson {
        public String bookTitle;
        public int amount;
        public int price;
    }

    public class ResourceJson {
        public VehicleJson[] vehicles;
    }

    public class VehicleJson {
         public int license;
         public int speed;
    }

    public class ServicesJson {
        public class TimeJson {
            public int speed;
            public int duration;
        }
        public class CustomerJson {
            public class CreditCardJson {
                public int number;
                public int amount;
            }
            public class BookOrderEventJson {
                public String bookTitle;
                public int tick;
            }
            public int id;
            public String name;
            public String address;
            public int distance;
            public CreditCardJson creditCard;
            public BookOrderEventJson[] orderSchedule;
        }
        public TimeJson time;
        public int selling;
        public int inventoryService;
        public int logistics;
        public int resourcesService;
        public CustomerJson[] customers;

    }
}
