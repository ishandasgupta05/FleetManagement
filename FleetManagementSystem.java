import java.io.*;
import java.util.*;
//=================================================================================================
/**
 * This class is the Fleet Management System that allows for adding and removing boats,
 * printing the fleet's data, and managing expenses
 */
public class FleetManagementSystem {
    //-------------------------------------------------------------------------------------------------
    private static final Scanner keyboard = new Scanner(System.in);
    private static final String DATABASE_FILE = "FleetData.db";
    private Fleet fleet;
    //-------------------------------------------------------------------------------------------------
    /**
     * Main method
     * @param args Command-line arguments. Loads data from CSV if provided
     */
    public static void main(String[] args) {
        FleetManagementSystem system = new FleetManagementSystem();
        system.start(args);
    }
    //-------------------------------------------------------------------------------------------------
    /**
     * Starts the system with provided CSV data. If CSV data is not loaded,
     * it will load from a saved db
     * @param args Command-line arguments
     */
    private void start(String[] args) {
        if (args.length > 0) {
            loadCSVFile(args[0]);
        } else {
            loadFromDatabase();
        }

        char choice;
        do {
            printMenu();
            choice = Character.toUpperCase(keyboard.nextLine().charAt(0));
            menuActionItem(choice);
        } while (choice != 'X');

        saveToDatabase();
        System.out.println("Exiting the Fleet Management System"); //exits the program if the user types X
    }
    //-------------------------------------------------------------------------------------------------
    /**
     * Prints the menu of options (Print, Add, Remove, Expense, Exit)
     */
    private void printMenu() {
        System.out.println("\n(P)rint, (A)dd, (R)emove, (E)xpense, e(X)it : ");
    }
    //-------------------------------------------------------------------------------------------------
    /**
     * Does the action item of the menu selection from the user
     * @param option The user's menu option
     */
    private void menuActionItem(char option) {
        switch (option) {
            case 'P':
                System.out.println(fleet.fleetReport());
                break;
            case 'A':
                addBoat();
                break;
            case 'R':
                removeBoat();
                break;
            case 'E':
                manageExpense();
                break;
            case 'X':
                break;
            default:
                System.out.println("Invalid menu option, try again.");
        }
    }
    //-------------------------------------------------------------------------------------------------
    /**
     * Adds a new boat to the fleet, uses addBoat method
     */
    private void addBoat() {
        System.out.print("Please enter the new boat CSV data: ");
        String csvData = keyboard.nextLine();
        if (fleet.addBoat(csvData)) {
            System.out.println("Boat added successfully.");
        } else {
            System.out.println("Failed to add boat. Check your input.");
        }
    }
    //-------------------------------------------------------------------------------------------------
    /**
     * Removes a boat from the fleet, uses removeBoat method
     */
    private void removeBoat() {
        System.out.print("Which boat do you want to remove? : ");
        String name = keyboard.nextLine();
        if (fleet.removeBoat(name)) {
            System.out.println("Boat removed successfully.");
        } else {
            System.out.println("Cannot find boat " + name);
        }
    }
    //-------------------------------------------------------------------------------------------------
    /**
     * Manages expenses of a boat by checking
     * if expense is authorized using addExpense method
     */
    private void manageExpense() {
        System.out.print("Which boat do you want to spend on? : ");
        String name = keyboard.nextLine();

        if (!fleet.boatExists(name)) {
            System.out.println("Cannot find boat " + name + ".");
            return;
        }

        System.out.print("How much do you want to spend?      : ");
        double amount = keyboard.nextDouble();
        keyboard.nextLine();

        // allowanceLeft to check if expense can be added and potentially add value onto the current expense
        double allowanceLeft = fleet.getAllowanceLeft(name);

        //checks if amount is valid
        if (allowanceLeft >= amount) {
            if (fleet.addExpense(name, amount)) {
                double totalSpent = fleet.findName(name).getExpenses();
                System.out.println("Expense authorized, $" + String.format("%.2f", totalSpent) + " spent.");
            }
        } else {
            // Show how much is left to spend is expense isn't valid
            System.out.printf("Expense not permitted, only $%.2f left to spend.\n", allowanceLeft);
        }
    }
    //-------------------------------------------------------------------------------------------------
    /**
     * Loads fleet data from CSV
     * @param fileName The CSV file with the data
     */
    private void loadCSVFile(String fileName) {
        try {
            fleet = new Fleet();
            fleet.loadCSVFile(fileName);
            System.out.println("Fleet data loaded from CSV file.");
        } catch (IOException e) {
            System.err.println("ERROR: Failed to load fleet data from CSV file.");
        }
    }
    //-------------------------------------------------------------------------------------------------
    /**
     * Loads fleet data from database file
     */
    private void loadFromDatabase() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(DATABASE_FILE))) {
            fleet = (Fleet) in.readObject();
            System.out.println("Fleet data loaded from database.");
        } catch (IOException | ClassNotFoundException e) {
            fleet = new Fleet();
            System.out.println("No existing fleet data found. Starting fresh.");
        }
    }
    //-------------------------------------------------------------------------------------------------
    /**
     * Saves the current fleet data to a database file
     */
    private void saveToDatabase() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(DATABASE_FILE))) {
            out.writeObject(fleet);
            System.out.println("Fleet data saved to database.");
        } catch (IOException e) {
            System.err.println("ERROR: Failed to save fleet data.");
        }
    }
//-------------------------------------------------------------------------------------------------
}
