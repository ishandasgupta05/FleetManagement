import java.io.Serializable;
//=================================================================================================
/**
 * The Boat class stores the following information about a boat: type, name, year, make,
 * price, and expenses. The methods in this class allow a printed representation of the boat
 * as well as adding expenses on the boat
 */
public class Boat implements Serializable {
    //-------------------------------------------------------------------------------------------------
    private final BoatType type;
    private final String name;
    private final int year;
    private final String make;
    private final double length;
    private final double price;
    private double expenses;
    //-------------------------------------------------------------------------------------------------
    /**
     * Constructor to create a new boat.
     * @param type The boat's type (Sailing, Power)
     * @param name The boat's name
     * @param year The boat's year of manufacture
     * @param make The boat's make
     * @param length The boat's length
     * @param price The boat's price
     * The boats expenses are initialized at 0
     */
    public Boat(BoatType type, String name, int year, String make, double length, double price) {
        this.type = type;
        this.name = name;
        this.year = year;
        this.make = make;
        this.length = length;
        this.price = price;
        this.expenses = 0.0;
    }
    //-------------------------------------------------------------------------------------------------
    /**
     * @return The boat's name
     */
    public String getName() {
        return name;
    }
    //-------------------------------------------------------------------------------------------------
    /**
     * @return The boat's price
     */
    public double getPrice() {
        return price;
    }
    //-------------------------------------------------------------------------------------------------
    /**
     * @return The boat's total expenses
     */
    public double getExpenses() {
        return expenses;
    }
    //-------------------------------------------------------------------------------------------------
    /**
     * Adds an expense to the boat and returns the remaining balance if expense is within the allowed limit
     * @param amount The amount to add as an expense
     * @return The remaining allowance for spending
     */
    public double addExpense(double amount) {
        double allowanceLeft = price - expenses; //Calculate balance by subtracting expenses from price
        if (amount <= allowanceLeft) {
            expenses += amount;  // Add expense if it's allowed
            return expenses;  // Return the new total
        }
        return allowanceLeft;
    }
    //-------------------------------------------------------------------------------------------------
    /**
     * String representation of the boat's information
     * @return A string of boat's details
     */
    public String toString() {
        return String.format("%-8s %-20s %4d %-12s %3.0f' : Paid $ %8.2f : Spent $ %8.2f",
                type, name, year, make, length, price, expenses);
    }
//-------------------------------------------------------------------------------------------------
}
//=================================================================================================