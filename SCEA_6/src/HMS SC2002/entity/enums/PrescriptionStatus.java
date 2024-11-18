//entity/enums/PrescriptionStatus.java

package entity.enums;

/**
 * Enum representing the status of a prescription.
 */
public enum PrescriptionStatus {

    /**
     * Prescription is pending approval or fulfillment.
     */
    PENDING,

    /**
     * Prescription has been dispensed to the patient.
     */
    DISPENSED,

    /**
     * Prescription has been cancelled and is no longer valid.
     */
    CANCELLED
}
