package entity;

import java.time.LocalDateTime;
import entity.enums.InventoryActionType;

/**
 * Represents an action performed on the inventory of a specific medicine,
 * detailing the quantity, type of action, date/time, administrator, and reason.
 */
public class MedicineInventoryAction {
    
    /** The medicine on which the action was performed. */
    private final Medicine medicine;
    
    /** The quantity of medicine affected by the action. */
    private final int quantity;
    
    /** The type of inventory action performed (e.g., addition, removal). */
    private final InventoryActionType actionType;
    
    /** The date and time when the action was performed. */
    private final LocalDateTime actionDateTime;
    
    /** The administrator who performed the action. */
    private final Administrator performedBy;
    
    /** The reason for the action, if applicable. */
    private final String reason;

    /**
     * Constructs a MedicineInventoryAction without a specified reason.
     *
     * @param medicine The medicine involved in the action.
     * @param quantity The quantity affected by the action.
     * @param actionType The type of action performed.
     * @param actionDateTime The date and time of the action.
     * @param performedBy The administrator who performed the action.
     */
    public MedicineInventoryAction(Medicine medicine, int quantity, 
                                   InventoryActionType actionType,
                                   LocalDateTime actionDateTime, 
                                   Administrator performedBy) {
        this(medicine, quantity, actionType, actionDateTime, performedBy, null);
    }

    /**
     * Constructs a MedicineInventoryAction with all specified parameters.
     *
     * @param medicine The medicine involved in the action.
     * @param quantity The quantity affected by the action.
     * @param actionType The type of action performed.
     * @param actionDateTime The date and time of the action.
     * @param performedBy The administrator who performed the action.
     * @param reason The reason for the action.
     */
    public MedicineInventoryAction(Medicine medicine, int quantity, 
                                   InventoryActionType actionType,
                                   LocalDateTime actionDateTime, 
                                   Administrator performedBy,
                                   String reason) {
        this.medicine = medicine;
        this.quantity = quantity;
        this.actionType = actionType;
        this.actionDateTime = actionDateTime;
        this.performedBy = performedBy;
        this.reason = reason;
    }

    /**
     * Gets the medicine involved in the action.
     *
     * @return The medicine associated with the action.
     */
    public Medicine getMedicine() {
        return medicine;
    }

    /**
     * Gets the quantity of medicine affected by the action.
     *
     * @return The quantity affected by the action.
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Gets the type of inventory action performed.
     *
     * @return The type of action (e.g., ADDITION, REMOVAL).
     */
    public InventoryActionType getActionType() {
        return actionType;
    }

    /**
     * Gets the date and time when the action was performed.
     *
     * @return The date and time of the action.
     */
    public LocalDateTime getActionDateTime() {
        return actionDateTime;
    }

    /**
     * Gets the administrator who performed the action.
     *
     * @return The administrator responsible for the action.
     */
    public Administrator getPerformedBy() {
        return performedBy;
    }

    /**
     * Gets the reason for the action, if provided.
     *
     * @return The reason for the action, or null if none was provided.
     */
    public String getReason() {
        return reason;
    }
}
