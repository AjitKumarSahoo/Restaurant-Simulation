package Restaurant6431Project;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author: Ajit Ku. Sahoo
 * Date: 11/13/2016.
 */
public class Order {

    private Map<FoodItem, Integer> quantityMap;
    private Map<FoodItem, Boolean> statusMap;

    Order(int burgers, int fries, int coke) {
        quantityMap = new HashMap<>();
        populateQuantityMap(burgers, fries, coke);

        statusMap = new HashMap<>();
        initializeStatusMap();
    }

    private void populateQuantityMap(int burgers, int fries, int coke) {
        quantityMap.put(FoodItem.Burger, burgers);
        quantityMap.put(FoodItem.Fries, fries);
        quantityMap.put(FoodItem.Coke, coke);
    }

    private void initializeStatusMap() {
        statusMap.put(FoodItem.Burger, false);
        statusMap.put(FoodItem.Fries, quantityMap.get(FoodItem.Fries) == 0);
        statusMap.put(FoodItem.Coke, quantityMap.get(FoodItem.Coke) == 0);
    }

    public void processingFinished(FoodItem item) {
        statusMap.put(item, true);
    }

    public boolean isOrderFinished() {
        return !statusMap.values().contains(true);
    }

    public int getQuantity(FoodItem item) {
        return quantityMap.get(item);
    }


    public List<FoodItem> getUnprocessedFoodItems() {
        List<FoodItem> itemsList = new ArrayList<>();

        for (FoodItem item : FoodItem.values()) {
            if (!statusMap.get(item)) {
                itemsList.add(item);
            }
        }
        return itemsList;
    }
}
