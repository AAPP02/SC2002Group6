//entity/Medicine.java

package entity;

/**
 * Represents a medicine with information about its name, current stock,
 * low stock alert level, replenishment status, and maximum stock.
 */
public class Medicine {
    
    /** Name of the medicine. */
    private final String name;
    
    /** Current stock level of the medicine. */
    private int currentStock;
    
    /** Stock level at which a low stock alert is triggered. */
    private int lowStockAlert;
    
    /** Flag indicating if a replenishment request has been made. */
    private boolean replenishmentRequested;
    
    /** Maximum stock level allowed for the medicine. */
    private final int maxStock;

    /**
     * Constructs a new Medicine instance with the specified name, initial stock,
     * and low stock alert level. Sets the max stock as double the initial stock.
     *
     * @param name The name of the medicine.
     * @param initialStock The initial stock level.
     * @param lowStockAlert The stock level at which a low stock alert is triggered.
     */
    public Medicine(String name, int initialStock, int lowStockAlert) {
        this.name = name;
        this.currentStock = initialStock;
        this.lowStockAlert = lowStockAlert;
        this.maxStock = initialStock * 2;
        this.replenishmentRequested = false;
    }

    /**
     * Sets a new low stock alert level.
     *
     * @param newAlertLevel The new low stock alert level.
     * @throws IllegalArgumentException if the new alert level is negative.
     */
    public void setLowStockAlert(int newAlertLevel) {
        if (newAlertLevel < 0) {
            throw new IllegalArgumentException("Alert level cannot be negative");
        }
        this.lowStockAlert = newAlertLevel;
    }

    /**
     * Returns the maximum stock level for the medicine.
     *
     * @return The maximum stock level.
     */
    public int getMaxStock() {
        return maxStock;
    }

    /**
     * Updates the stock level by a specified quantity. If the resulting stock
     * would be negative, no change is made.
     *
     * @param quantity The quantity to add (positive) or remove (negative).
     * @return true if the stock was successfully updated, false otherwise.
     */
    public boolean updateStock(int quantity) {
        int newStock = this.currentStock + quantity;
        if (newStock < 0) {
            return false;
        }
        this.currentStock = newStock;
        return true;
    }

    /**
     * Checks if the current stock is at or below the low stock alert level.
     *
     * @return true if the stock is low, false otherwise.
     */
    public boolean isLowStock() {
        return currentStock <= lowStockAlert;
    }

    /**
     * Requests replenishment for the medicine. If a request is already made,
     * no new request is created.
     *
     * @return true if a replenishment request was successfully made, false if one already exists.
     */
    public boolean requestReplenishment() {
        if (replenishmentRequested) {
            return false;
        }
        replenishmentRequested = true;
        return true;
    }

    /**
     * Fulfills a replenishment request by adding a specified quantity to the stock.
     * If no replenishment request exists or the quantity is invalid, no change is made.
     *
     * @param quantity The quantity of stock to add.
     * @return true if replenishment was fulfilled, false otherwise.
     */
    public boolean fulfillReplenishment(int quantity) {
        if (!replenishmentRequested || quantity <= 0) {
            return false;
        }
        currentStock += quantity;
        replenishmentRequested = false;
        return true;
    }

    /**
     * Cancels an existing replenishment request.
     *
     * @return true if the request was successfully canceled, false if no request existed.
     */
    public boolean cancelReplenishmentRequest() {
        if (!replenishmentRequested) {
            return false;
        }
        replenishmentRequested = false;
        return true;
    }

    /**
     * Checks if a specified quantity can be fulfilled from the current stock.
     *
     * @param quantity The quantity requested.
     * @return true if the quantity can be fulfilled, false otherwise.
     */
    public boolean canFulfillQuantity(int quantity) {
        return quantity > 0 && quantity <= currentStock;
    }

    /**
     * Returns a string representation of the medicine, including its name, stock levels,
     * low stock alert, and replenishment status.
     *
     * @return A string representation of the medicine.
     */
    @Override
    public String toString() {
        return String.format("Medicine: %s%nCurrent Stock: %d%nLow Stock Alert Level: %d%n" +
                           "Replenishment Requested: %s%nStock Status: %s",
            name, currentStock, lowStockAlert, 
            replenishmentRequested ? "Yes" : "No",
            isLowStock() ? "LOW STOCK" : "Normal");
    }

    /**
     * Gets the name of the medicine.
     *
     * @return The medicine name.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the current stock level of the medicine.
     *
     * @return The current stock level.
     */
    public int getCurrentStock() {
        return currentStock;
    }

    /**
     * Gets the low stock alert level of the medicine.
     *
     * @return The low stock alert level.
     */
    public int getLowStockAlert() {
        return lowStockAlert;
    }

    /**
     * Checks if a replenishment request has been made.
     *
     * @return true if a replenishment request exists, false otherwise.
     */
    public boolean isReplenishmentRequested() {
        return replenishmentRequested;
    }

    /**
     * Sets the current stock level.
     *
     * @param currentStock The stock level to set.
     */
    public void setCurrentStock(int currentStock) {
        this.currentStock = currentStock;
    }

    /**
     * Sets the replenishment request status.
     *
     * @param replenishmentRequested true to request replenishment, false otherwise.
     */
    public void setReplenishmentRequested(boolean replenishmentRequested) {
        this.replenishmentRequested = replenishmentRequested;
    }
}
