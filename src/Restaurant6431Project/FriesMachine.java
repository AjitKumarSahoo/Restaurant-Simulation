package Restaurant6431Project;

/**
 * Author: Ajit Ku. Sahoo
 * Date: 11/13/2016.
 */
public class FriesMachine extends Machine {

    private static final int TIME_TO_PREPARE_FRIES = 3; //in minutes

    public FriesMachine() {
        super(FoodItem.Fries.toString() + MACHINE, TIME_TO_PREPARE_FRIES);
    }

    /**
     * processes food and returns the amount of time taken to process.
     *
     * @param quantity - no of orders for Fries
     * @return the amount of time taken to process.
     * @throws InterruptedException
     */
    @Override
    public int processFood(int quantity) throws InterruptedException {
        Thread.sleep(100);
        return quantity * getTimeToPrepareOneFoodItem();
    }

}
