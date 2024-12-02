import java.io.*;
import java.util.*;
//=================================================================================================
/**
 * This class allows user to add boats, remove boats,
 * and manage expenses through methods that will be used on the FleetManagementSystem.
 * Using a file scanner, it allows user to load
 * data from a CSV file and save the fleet to a database
 */
public class Fleet implements Serializable {
    //-------------------------------------------------------------------------------------------------
    private final List<Boat> fleet = new ArrayList<>();
    //-------------------------------------------------------------------------------------------------
    /**
     * Loads fleet data from a CSV file
     * @param fileName name of CSV file
     * @throws IOException if file cannot be read
     */
    public void loadCSVFile(String fileName) throws IOException {
        try (Scanner fileScanner = new Scanner(new File(fileName))) {
            while (fileScanner.hasNextLine()) {
                addBoat(fileScanner.nextLine());
            }
        }
    }
    //-------------------------------------------------------------------------------------------------
    /**
     * Adds boat to the fleet
     * @param csvString String from CSV showing boat's info
     * @return true if the boat was added, false if there was an error
     */
    public boolean addBoat(String csvString) {
        try {
            String[] parts = csvString.split(",");
            BoatType type = BoatType.valueOf(parts[0].toUpperCase());
            String name = parts[1];
            int year = Integer.parseInt(parts[2]);
            String makeModel = parts[3];
            double length = Double.parseDouble(parts[4]);
            double price = Double.parseDouble(parts[5]);

            fleet.add(new Boat(type, name, year, makeModel, length, price));
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    //-------------------------------------------------------------------------------------------------
    /**
     * Checks if a boat exists in the fleet
     * @param name The name of the boat
     * @return true if the boat exists, false if not
     */
    public boolean boatExists(String name) {
        for (Boat boat : fleet) {
            if (boat.getName().equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }
    //-------------------------------------------------------------------------------------------------
    /**
     * Finds a boat by name
     * @param name The name of the boat
     * @return The Boat object if found, or null if not
     */
    public Boat findName(String name) {
        for (Boat boat : fleet) {
            if (boat.getName().equalsIgnoreCase(name)) {
                return boat;
            }
        }
        return null; // Return null if no boat is found
    }
    //-------------------------------------------------------------------------------------------------
    /**
     * Removes a boat from the fleet by its name
     * @param name The name of boat being removed
     * @return true if boat was removed, false if not found
     */
    public boolean removeBoat(String name) {
        return fleet.removeIf(boat -> boat.getName().equalsIgnoreCase(name));
    }
    //-------------------------------------------------------------------------------------------------
    /**
     * Adds expense to a boat's account
     * @param name The name of the boat
     * @param amount The amount to spend
     * @return true if the expense was added, false if not
     */
    public boolean addExpense(String name, double amount) {
        Boat boat = findName(name);
        if (boat != null) {
            double allowanceLeft = boat.addExpense(amount);  // Call Boat's addExpense method
            if (allowanceLeft >= 0) {
                return true;
            } else {
                System.out.printf("Expense not permitted, only $%.2f left to spend.\n", -allowanceLeft);  // Show remaining balance
            }
        }
        return false;
    }
    //-------------------------------------------------------------------------------------------------
    /**
     * Gets boat's allowance left for its purchase price and expenses
     * @param name The name of the boat
     * @return The remaining allowance for the boat, or -1 if the boat not found
     */
    public double getAllowanceLeft(String name) {
        Boat boat = findName(name);
        if (boat != null) {
            return boat.getPrice() - boat.getExpenses();
        }
        return -1.0; // Indicate that the boat was not found
    }
    //-------------------------------------------------------------------------------------------------
    /**
     * Generates a report of the entire fleet with expenses
     * @return A string containing the fleet's report
     */
    public String fleetReport() {
        StringBuilder report = new StringBuilder("Fleet report:\n");
        double totPrice = 0, totExpenses = 0;

        for (Boat boat : fleet) {
            report.append(boat).append("\n");
            totPrice += boat.getPrice();
            totExpenses += boat.getExpenses();
        }

        report.append(String.format("Total : Paid $ %.2f : Spent $ %.2f\n", totPrice, totExpenses));
        return report.toString();
    }
//-------------------------------------------------------------------------------------------------
}
//=================================================================================================