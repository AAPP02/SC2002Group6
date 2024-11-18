//entity/enums/AppointmentStatus.java

package entity.enums;

/**
 * Enum representing the various statuses an appointment can have.
 */
public enum AppointmentStatus {
    
    /**
     * Appointment is awaiting approval.
     */
    PENDING_APPROVAL,
    
    /**
     * Appointment has been confirmed.
     */
    CONFIRMED,
    
    /**
     * Appointment has been cancelled.
     */
    CANCELLED,
    
    /**
     * Appointment has been completed.
     */
    COMPLETED
}
