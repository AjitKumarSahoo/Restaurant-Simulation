package Restaurant6431Project;

/**
 * Author: Ajit Ku. Sahoo
 * Date: 11/16/2016.
 */

public class InputDataValidator {

    private final InputData data;
    private static final String NO_ERROR = "";

    public InputDataValidator(InputData data) {
        this.data = data;
    }

    public void validate() {
        String errorMsg = getValidationErrorString();
        if (!errorMsg.equals(NO_ERROR)) {
            System.err.println("Input Data Validation Failed.");
            System.err.println("Reason: " + errorMsg);
            System.err.println("Program is terminated gracefully.");
            System.exit(0);
        }

    }

    private String getValidationErrorString() {
        if (data.getInputDiners() <= 0) {
            return "No Diner at the restaurant.";
        }

        if (data.getNoOfTables() <= 0) {
            return "At least 1 table is needed at the restaurant.";
        }

        if (data.getNoOfCooks() <= 0) {
            return "At least 1 cook is needed at the restaurant.";
        }

        for (int diner = 1; diner <= data.getNoOfDiners(); diner++) {

            if (data.getArrivalTime(diner) < 0 || data.getArrivalTime(diner) > 120) {
                return "Invalid arrival time for Diner " + diner + ". Valid time range = 0-120. ";
            }

            /*if (data.getArrivalTime(diner) > 120) {
                System.err.println("Diner " + diner + " will not be served. Arrival time > 120");
                data.removeDiner(diner); //remove the diner from our list of diners to serve
                return NO_ERROR; //need to serve other diners
            }*/

            if (data.getFoodItemQuantity(diner, FoodItem.Burger) <= 0) {
                return "At least 1 burger needs to be ordered for Diner " + diner + ".";
            }

            if (data.getFoodItemQuantity(diner, FoodItem.Fries) < 0) {
                return "Invalid fries order for Diner " + diner + ". Fries order must be >=0";
            }

            int cokes = data.getFoodItemQuantity(diner, FoodItem.Coke);
            if (!(cokes == 0 || cokes == 1)) {
                return "No. of glasses of coke can be either 0 or 1 for Diner " + diner + ".";
            }

        }

        return NO_ERROR;
    }
}
