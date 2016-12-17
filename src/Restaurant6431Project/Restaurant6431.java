package Restaurant6431Project;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Author: Ajit Ku. Sahoo
 * Date: 11/13/2016.
 */
public class Restaurant6431 {

    public static class Clock {
        public static String getFormattedTime(int minutes) {
            int hours = minutes / 60;
            minutes = minutes % 60;
            return String.format("%02d:%02d", hours, minutes);
        }
    }

    private ResourceHandler handler;
    private InputData inputData;

    public Restaurant6431(InputData inputData) {
        this.inputData = inputData;
        validateInput(inputData);
        handler = new ResourceHandler(inputData);
    }

    private void validateInput(InputData inputData) {
        InputDataValidator validator = new InputDataValidator(inputData);
        validator.validate();
    }

    public void process() {

        for (int dinerNo : inputData.getDiners()) {
            int dinerArrivalTime = inputData.getArrivalTime(dinerNo);
            Order order = createOrder(dinerNo, inputData);
            Diner diner = new Diner(dinerNo, dinerArrivalTime, order, handler);
            new Thread(diner).start();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private Order createOrder(int dinerNo, InputData inputData) {
        int numBurgers = inputData.getFoodItemQuantity(dinerNo, FoodItem.Burger);
        int numFries = inputData.getFoodItemQuantity(dinerNo, FoodItem.Fries);
        int numCokes = inputData.getFoodItemQuantity(dinerNo, FoodItem.Coke);
        return new Order(numBurgers, numFries, numCokes);
    }


    public static void main(String[] args) {
        String filename = args.length == 0 ? "input.txt" : args[0];
        File file = new File("c:\\restaurant\\" + filename);
        InputData inputData = getInputData(file);
        Restaurant6431 restaurant = new Restaurant6431(inputData);
        restaurant.process();
    }

    private static InputData getInputData(File file) {
        InputData inputData = null;
        try (Scanner scanner = new Scanner(file)) {
            inputData = new InputData();
            inputData.processInputData(scanner);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return inputData;
    }
}
