//entity/Prescription.java

package entity;

import java.time.LocalDateTime;
import entity.enums.*;

/**
 * Represents a prescription for a specific medicine with a quantity and dispensing status.
 */
public class Prescription {

    /** The prescribed medicine. */
    private final Medicine medicine;

    /** The quantity of the prescribed medicine. */
    private final int quantity;

    /** The status of the prescription (e.g., PENDING, DISPENSED). */
    private PrescriptionStatus status;

    /** The pharmacist who dispensed the prescription, if applicable. */
    private Pharmacist dispensedBy;

    /** The date and time the prescription was dispensed, if applicable. */
    private LocalDateTime dispensedDateTime;

    /**
     * Constructs a new Prescription with specified medicine and quantity.
     * Sets the initial status to PENDING.
     *
     * @param medicine The medicine prescribed.
     * @param quantity The quantity of the medicine prescribed.
     */
    public Prescription(Medicine medicine, int quantity) {
        this.medicine = medicine;
        this.quantity = quantity;
        this.status = PrescriptionStatus.PENDING;
    }

    /**
     * Gets the prescribed medicine.
     *
     * @return The medicine.
     */
    public Medicine getMedicine() {
        return medicine;
    }

    /**
     * Sets the pharmacist who dispensed the prescription.
     *
     * @param pharmacist The pharmacist who dispensed the prescription.
     */
    public void setDispensedBy(Pharmacist pharmacist) {
        this.dispensedBy = pharmacist;
    }

    /**
     * Sets the date and time when the prescription was dispensed.
     *
     * @param dateTime The date and time of dispensing.
     */
    public void setDispensedDateTime(LocalDateTime dateTime) {
        this.dispensedDateTime = dateTime;
    }

    /**
     * Returns a string representation of the prescription, including medicine name, quantity, status, 
     * and dispensing information if available.
     *
     * @return A string representing the prescription.
     */
    @Override
    public String toString() {
        return String.format("Medication: %s, Quantity: %d, Status: %s, %s",
            medicine.getName(), quantity, status,
            dispensedBy != null ? "Dispensed by: " + dispensedBy.getName() +
            " at " + dispensedDateTime : "Not yet dispensed");
    }

    /**
     * Gets the name of the prescribed medicine.
     *
     * @return The name of the medicine.
     */
    public String getMedicineName() {
        return medicine.getName();
    }

    /**
     * Gets the quantity of the prescribed medicine.
     *
     * @return The quantity of the medicine.
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Gets the current status of the prescription.
     *
     * @return The prescription status.
     */
    public PrescriptionStatus getStatus() {
        return status;
    }

    /**
     * Sets the status of the prescription.
     *
     * @param status The new prescription status.
     */
    public void setStatus(PrescriptionStatus status) {
        this.status = status;
    }

    /**
     * Gets the pharmacist who dispensed the prescription.
     *
     * @return The pharmacist who dispensed the prescription, or {@code null} if not yet dispensed.
     */
    public Pharmacist getDispensedBy() {
        return dispensedBy;
    }

    /**
     * Gets the date and time when the prescription was dispensed.
     *
     * @return The dispensing date and time, or {@code null} if not yet dispensed.
     */
    public LocalDateTime getDispensedDateTime() {
        return dispensedDateTime;
    }
}
