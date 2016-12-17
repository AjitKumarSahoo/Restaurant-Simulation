package Restaurant6431Project;

/**
 * Author: Ajit Ku. Sahoo
 * Date: 11/14/2016.
 */
public class Diner implements Runnable, Comparable<Diner> {

    public Table getTable() {
        return table;
    }

    private Table table;

    private Order order;
    private int dinerID;
    private int dinerArrivalTime;
    private int dinerSeatingTime;
    private int dinerLeavingTime;
    private ResourceHandler handler;

    public Diner(int dinerID, int dinerArrivalTime, Order order, ResourceHandler handler) {
        this.dinerID = dinerID;
        this.dinerArrivalTime = dinerArrivalTime;
        this.handler = handler;
        this.order = order;
    }

    @Override
    public void run() {

        handler.assignTableToDiner(this);
        setDinerFinishedTime();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String formattedTime = Restaurant6431.Clock.getFormattedTime(dinerLeavingTime);
        System.out.println(formattedTime + "  Diner " + dinerID + " finishes. Diner " + dinerID + " leaves the restaurant.");
        handler.leaveTable(this);
    }

    private void setDinerFinishedTime() {
        dinerLeavingTime = table.getDinerTime() + 30;
    }

    public void reserveTable(Table table) {
        this.table = table;
        int tableTime = table.getTableTime();
        if (tableTime < dinerArrivalTime) {
            table.setTableTime(dinerArrivalTime);
        }
        setDinerSeatedTime(table.getTableTime());
        table.setDiner(this.getDinerID());

        String formattedTime = Restaurant6431.Clock.getFormattedTime(dinerArrivalTime);
        System.out.println(formattedTime + "  Diner " + dinerID + " arrives.");

        formattedTime = Restaurant6431.Clock.getFormattedTime(dinerSeatingTime);
        System.out.println(formattedTime + "  Diner " + dinerID + " is seated at table " + table.get_ID() + ".");
        this.table.placeOrder(order);

    }

    private void setDinerSeatedTime(int tableTime) {
        this.dinerSeatingTime = tableTime;
    }

    @Override
    public int compareTo(Diner obj) {
        if (this.dinerArrivalTime == obj.dinerArrivalTime) {
            return this.dinerID > obj.dinerID ? 1 : -1;
        }
        return this.dinerArrivalTime > obj.dinerArrivalTime ? 1 : -1;
    }

    public Order getOrder() {
        return order;
    }

    public int getDinerID() {
        return dinerID;
    }

    public int getDinerArrivalTime() {
        return dinerArrivalTime;
    }

    public int getDinerLeavingTime() {
        return dinerLeavingTime;
    }

    @Override
    public String toString() {
        return String.valueOf(dinerID);
    }
}

