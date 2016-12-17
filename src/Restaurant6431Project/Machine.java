package Restaurant6431Project;

/**
 * Author: Ajit Ku. Sahoo
 * Date: 11/13/2016.
 */
public abstract class Machine {

    private String name;
    private int time;
    private final int timeToPrepareFoodItem; //in minutes
    static final String MACHINE = " Machine";

    protected Machine(String name, int foodPreparingTime) {
        this.name = name;
        timeToPrepareFoodItem = foodPreparingTime;
        time = 0;
    }

    public abstract int processFood(int quantity) throws InterruptedException;

    public String getName() {
        return name;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getTimeToPrepareOneFoodItem() {
        return timeToPrepareFoodItem;
    }

}
