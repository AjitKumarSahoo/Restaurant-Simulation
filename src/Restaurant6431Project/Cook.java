package Restaurant6431Project;

/**
 * Author: Ajit Ku. Sahoo
 * Date: 11/13/2016.
 */
public class Cook implements Runnable, Comparable<Cook> {

    private int _ID;
    private Table table;
    private ResourceHandler handler;
    private int cookingFinishedTime;

    public Cook(int id, ResourceHandler handler) {
        this._ID = id;
        this.handler = handler;
        cookingFinishedTime = 0;
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                handler.assignTableToCook(this);
                handler.prepareFood(this);
                table.setDinerTime(cookingFinishedTime);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int compareTo(Cook cook) {
        if (cookingFinishedTime > cook.cookingFinishedTime)
            return 1;
        else if (cookingFinishedTime < cook.cookingFinishedTime)
            return -1;
        else
            return this._ID > cook._ID ? 1 : -1;
    }

    public void cook(Machine machine, FoodItem item) throws InterruptedException {
        int startTime = getCookingStartTime(machine);
        System.out.println(Restaurant6431.Clock.getFormattedTime(startTime) + "  Cook " + _ID + " uses " + machine.getName() + ".");

        int processingTime = machine.processFood(table.getOrder().getQuantity(item));
        updateTimeUponFinishingCooking(machine, startTime + processingTime);
        table.getOrder().processingFinished(item);
    }

    private int getCookingStartTime(Machine machine) {
        return cookingFinishedTime > machine.getTime() ? cookingFinishedTime : machine.getTime();
    }

    private void updateTimeUponFinishingCooking(Machine machine, int finishTime) throws InterruptedException {
        machine.setTime(finishTime);
        cookingFinishedTime = finishTime;
    }

    public void setTable(Table table) {
        this.table = table;
        updateTime(table);
        String formattedTime = Restaurant6431.Clock.getFormattedTime(cookingFinishedTime);
        System.out.println(formattedTime + "  Cook " + _ID + " processes Diner " + table.getDinerID() + "'s order.");
    }

    private void updateTime(Table table) {
        int tableTime = table.getTableTime();
        if (cookingFinishedTime > tableTime) {
            table.setTableTime(cookingFinishedTime);
        } else {
            cookingFinishedTime = tableTime;
        }
    }

    public int getCurrentTime() {
        return cookingFinishedTime;
    }

    public Order getOrder() {
        return table.getOrder();
    }
}
