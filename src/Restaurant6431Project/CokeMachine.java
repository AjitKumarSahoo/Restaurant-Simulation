package Restaurant6431Project;

/**
 * Author: Ajit Ku. Sahoo
 * Date: 11/13/2016.
 */
public class CokeMachine extends Machine {

    private static final int TIME_TO_PREPARE_COKE = 1; //in minutes

    public CokeMachine() {
        super(FoodItem.Coke.toString() + MACHINE, TIME_TO_PREPARE_COKE);
    }

    /**
     * processes food and returns the amount of time taken to process.
     *
     * @param quantity - no of Cokes
     * @return the amount of time taken to process.
     * @throws InterruptedException
     */
    @Override
    public int processFood(int quantity) throws InterruptedException {
        Thread.sleep(100);
        return quantity * getTimeToPrepareOneFoodItem();
    }
}
