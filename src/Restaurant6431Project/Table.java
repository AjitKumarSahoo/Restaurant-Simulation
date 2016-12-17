package Restaurant6431Project;

import java.util.concurrent.SynchronousQueue;

/**
 * Author: Ajit Ku. Sahoo
 * Date: 11/13/2016.
 */
public class Table implements Comparable<Table> {

    private int _ID;
    private Order order;
    private int tableTime;
    private int dinerID;
    private SynchronousQueue<Integer> dinerTime;

    public Table(int id) {
        _ID = id;
        tableTime = -1;
        dinerTime = new SynchronousQueue<>();
    }

    public int getDinerTime() {
        int time = -1;
        try {
            time = dinerTime.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return time;
    }

    public void setDinerTime(int time) throws InterruptedException {
        dinerTime.put(time);
        String formattedTime = Restaurant6431.Clock.getFormattedTime(time);
        System.out.println(formattedTime + "  Diner " + dinerID + "'s order is ready. Diner " + dinerID + " starts eating.");
    }

    public int getTableTime() {
        return tableTime;
    }

    public void setTableTime(int time) {
        tableTime = time;
    }

    public Order getOrder() {
        return order;
    }

    public int get_ID() {
        return _ID;
    }

    public void placeOrder(Order order) {
        this.order = order;
    }

    @Override
    public int compareTo(Table table) {
        if (tableTime > table.tableTime)
            return 1;
        else if (tableTime < table.tableTime)
            return -1;
        else
            return 0;
    }

    public int getDinerID() {
        return dinerID;
    }

    public void setDiner(int dinerID) {
        this.dinerID = dinerID;
    }
}
