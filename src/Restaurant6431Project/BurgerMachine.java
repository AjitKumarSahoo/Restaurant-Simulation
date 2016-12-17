package Restaurant6431Project;

/**
 * Author: Ajit Ku. Sahoo
 * Date: 11/13/2016.
 */
public class BurgerMachine extends Machine {

    private static final int TIME_TO_PREPARE_BURGER = 5; //in minutes

    public BurgerMachine() {
        super(FoodItem.Burger.toString() + MACHINE, TIME_TO_PREPARE_BURGER);
    }

    /**
     * processes food and returns the amount of time taken to process.
     *
     * @param quantity - no of burgers
     * @return the amount of time taken to process.
     * @throws InterruptedException
     */
    @Override
    public int processFood(int quantity) throws InterruptedException {
        Thread.sleep(100);
        return quantity * getTimeToPrepareOneFoodItem();
    }


}
