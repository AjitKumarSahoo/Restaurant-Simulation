package Restaurant6431Project;


import java.util.List;
import java.util.PriorityQueue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Author: Ajit Ku. Sahoo
 * Date: 11/14/2016.
 */

public class ResourceHandler {

    private MachineManager machineManager;

    private AtomicInteger remainingDiners;
    private PriorityQueue<Diner> totalDinersQueue;

    private PriorityQueue<Table> tablesQueue;
    private BlockingQueue<Table> tablesToServeQueue;

    private int totalNoOfCooks;
    private PriorityQueue<Cook> freeCooksQueue;
    private PriorityQueue<Cook> busyCooksQueue;

    private AtomicInteger OrdersUnderProcess;
    private int lastExit = 0;

    public ResourceHandler(InputData inputData) {
        machineManager = new MachineManager();
        initialize(inputData);
    }

    private void initialize(InputData inputData) {
        OrdersUnderProcess = new AtomicInteger(1);
        initializeDinerDS(inputData);
        initializeTableDS(inputData);
        initializeCookDS(inputData);
    }

    private void initializeCookDS(InputData inputData) {
        totalNoOfCooks = inputData.getNoOfCooks();
        busyCooksQueue = new PriorityQueue<>();
        freeCooksQueue = new PriorityQueue<>();
        for (int cookNo = 1; cookNo <= totalNoOfCooks; cookNo++) {
            Cook cook = new Cook(cookNo, this);
            new Thread(cook).start();
        }
    }

    private void initializeTableDS(InputData inputData) {
        int noOfTables = inputData.getNoOfTables();
        tablesToServeQueue = new ArrayBlockingQueue<>(noOfTables, true);
        tablesQueue = new PriorityQueue<>(noOfTables);
        for (int tableNo = 1; tableNo <= noOfTables; tableNo++) {
            tablesQueue.add(new Table(tableNo));
        }
    }

    private void initializeDinerDS(InputData inputData) {
        int numberOfDiners = inputData.getNoOfDiners();
        totalDinersQueue = new PriorityQueue<>();
        remainingDiners = new AtomicInteger(numberOfDiners);
    }

    /**
     * With the given cook, it tries to find the available machines and list of unprocessed food items to prepare the food
     * @param cook
     * @throws InterruptedException
     */
    public void prepareFood(Cook cook) throws InterruptedException {
        Machine machine;
        do {
            handleCooks(cook);
            FoodItem item = null;
            machine = null;
            List<FoodItem> unprocessedFoodItems = cook.getOrder().getUnprocessedFoodItems();
            for (FoodItem food : unprocessedFoodItems) {
                if (cook.getOrder().getQuantity(food) == 0) {
                    continue;
                }
                if (machine == null || (machine.getTime() > getMachine(food).getTime())) {
                    machine = getMachine(food);
                    item = food;
                }
            }

            if (item != null) {
                cook.cook(machine, item);
                if (isNextOrderAvailable(cook)) {
                    handleNextOrder();
                }
            }
        } while (machine != null);

        handlePostFinishPreparation();
    }


    public void assignTableToDiner(Diner diner) {
        synchronized (totalDinersQueue) {
            totalDinersQueue.offer(diner);
            totalDinersQueue.notifyAll();
            try {
                while (shallDinerWait(diner)) {
                    totalDinersQueue.wait();
                }
                totalDinersQueue.poll().getDinerID();

                Table table = tablesQueue.poll();
                diner.reserveTable(table);
                tablesToServeQueue.put(table);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean shallDinerWait(Diner diner) {
        return totalDinersQueue.size() < remainingDiners.get() - OrdersUnderProcess.get() + 1 || diner != totalDinersQueue.peek();
    }

    public void leaveTable(Diner diner) {
        Table table = diner.getTable();
        int dinerLeavingTime = diner.getDinerLeavingTime();
        table.setTableTime(dinerLeavingTime);

        if (lastExit < dinerLeavingTime)
            lastExit = dinerLeavingTime;

        synchronized (tablesQueue) {
            tablesQueue.add(table);
        }

        synchronized (totalDinersQueue) {
            if (remainingDiners.decrementAndGet() == 0) {
                System.out.println(Restaurant6431.Clock.getFormattedTime(lastExit) + "  Last Diner leaves the restaurant.");
                shutDown();
            }
            totalDinersQueue.notifyAll();
        }
    }

    private void shutDown() {
        try {
            Thread.sleep(500); //allow other threads a sec to finish their ongoing activity
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.exit(0);
    }

    public void assignTableToCook(Cook cook) {
        try {
            synchronized (freeCooksQueue) {
                freeCooksQueue.add(cook);
                freeCooksQueue.notifyAll();

                while (shouldWait(cook)) {
                    freeCooksQueue.wait();
                }
                freeCooksQueue.poll();
            }

            synchronized (tablesToServeQueue) {
                cook.setTable(tablesToServeQueue.take());
            }
        } catch (InterruptedException e) {
        }
    }

    private boolean shouldWait(Cook cook) {
        return freeCooksQueue.size() < totalNoOfCooks - OrdersUnderProcess.get() + 1 || freeCooksQueue.peek() != cook;
    }

    public void handleCooks(Cook cook) throws InterruptedException {
        synchronized (busyCooksQueue) {
            busyCooksQueue.add(cook);
            busyCooksQueue.notifyAll();
            while (busyCooksQueue.size() < OrdersUnderProcess.get() || cook != busyCooksQueue.peek()) {
                busyCooksQueue.wait();
            }
            busyCooksQueue.poll();
        }
    }

    public boolean isNextOrderAvailable(Cook cook) {
        Diner diner = totalDinersQueue.peek();
        Cook nextCook = freeCooksQueue.peek();
        Table table = tablesQueue.peek();

        if (diner == null || nextCook == null || table == null) return false;

        return nextCook.getCurrentTime() < cook.getCurrentTime() &&
                diner.getDinerArrivalTime() < cook.getCurrentTime() &&
                table.getTableTime() < cook.getCurrentTime();
    }

    public Machine getMachine(FoodItem foodType) {
        return machineManager.getMachine(foodType);
    }

    public void handleNextOrder() {
        increaseOrdersUnderProcess();
        notifyDiners();
        notifyFreeCooks();
    }

    private void increaseOrdersUnderProcess() {
        OrdersUnderProcess.incrementAndGet();
    }

    private void notifyDiners() {
        synchronized (totalDinersQueue) {
            totalDinersQueue.notifyAll();
        }
    }

    public void handlePostFinishPreparation() {
        decreaseOrderUnderProcess();
        notifyBusyCooks();
    }

    public void notifyBusyCooks() {
        synchronized (busyCooksQueue) {
            busyCooksQueue.notifyAll();
        }
    }

    private void notifyFreeCooks() {
        synchronized (freeCooksQueue) {
            freeCooksQueue.notifyAll();
        }
    }

    private void decreaseOrderUnderProcess() {
        if (OrdersUnderProcess.get() > 1)
            OrdersUnderProcess.decrementAndGet();
    }
}
   
