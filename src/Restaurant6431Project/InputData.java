package Restaurant6431Project;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

/**
 * Author: Ajit Ku. Sahoo
 * Date: 11/14/2016.
 */
public class InputData {

    private int inputDiners;
    private int noOfTables;
    private int noOfCooks;
    private Map<Integer, Integer> dinerArrivalTimeMap;
    private Map<Integer, Map<FoodItem, Integer>> dinerToFoodItemToQuantityMap;

    public InputData() {
        dinerArrivalTimeMap = new HashMap<>();
        dinerToFoodItemToQuantityMap = new HashMap<>();
    }

    public void processInputData(Scanner scanner) {
//        scanner.useDelimiter("\\n");
        inputDiners = scanner.nextInt();
        noOfTables = Integer.valueOf(scanner.next());
        noOfCooks = Integer.valueOf(scanner.next());
        for (int i = 1; i <= inputDiners; i++) {
            String line = scanner.next();
            String[] values = line.split(",");
            dinerArrivalTimeMap.put(i, Integer.valueOf(values[0]));
            HashMap<FoodItem, Integer> itemsQuantity = new HashMap<>();
            itemsQuantity.put(FoodItem.Burger, Integer.valueOf(values[1]));
            itemsQuantity.put(FoodItem.Fries, Integer.valueOf(values[2]));
            itemsQuantity.put(FoodItem.Coke, Integer.valueOf(values[3]));
            dinerToFoodItemToQuantityMap.put(i, itemsQuantity);
        }

    }

    //this method should never be used anywhere else apart from validation
    public int getInputDiners() { //number of diners input by user
        return inputDiners;
    }

    public int getNoOfDiners() {
        return getDiners().size(); //bcoz diners reaching later than 120 will be removed from map
    }

    public Set<Integer> getDiners() {
        return dinerToFoodItemToQuantityMap.keySet();
    }

    public int getNoOfTables() {
        return noOfTables;
    }

    public int getNoOfCooks() {
        return noOfCooks;
    }

    public int getArrivalTime(int diner) {
        return dinerArrivalTimeMap.get(diner);
    }

    public void removeDiner(int diner) {
        dinerArrivalTimeMap.remove(diner);
        dinerToFoodItemToQuantityMap.remove(diner);
    }

    public int getFoodItemQuantity(int diner, FoodItem foodItem) {
        return dinerToFoodItemToQuantityMap.get(diner).get(foodItem);
    }

}
