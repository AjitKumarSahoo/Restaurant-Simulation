package Restaurant6431Project;

import java.util.HashMap;
import java.util.Map;

/**
 * Author: Ajit Ku. Sahoo
 * Date: 11/13/2016.
 */
public class MachineManager {

    private Map<FoodItem, Machine> machineMap;

    MachineManager() {
        machineMap = new HashMap<>();
        populateMachines();
    }

    private void populateMachines() {
        machineMap.put(FoodItem.Burger, new BurgerMachine());
        machineMap.put(FoodItem.Fries, new FriesMachine());
        machineMap.put(FoodItem.Coke, new CokeMachine());
    }

    public Machine getMachine(FoodItem item) {
        return machineMap.get(item);
    }

//    public boolean isMachineAvailable(FoodItem item) {
//        return !getMachine(item).isBusy();
//    }

}
