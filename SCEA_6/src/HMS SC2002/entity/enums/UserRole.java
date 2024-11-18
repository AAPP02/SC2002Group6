//entity/enums/UserRole.java

package entity.enums;

/**
 * Enum representing the different roles a user can have in the system.
 */
public enum UserRole {

    /**
     * Represents a doctor role in the system.
     * A doctor can manage appointments, diagnose patients, and provide treatments.
     */
    DOCTOR,

    /**
     * Represents a patient role in the system.
     * A patient can book appointments, receive treatments, and access their health records.
     */
    PATIENT,

    /**
     * Represents a pharmacist role in the system.
     * A pharmacist can dispense medication and manage prescriptions.
     */
    PHARMACIST,

    /**
     * Represents an administrator role in the system.
     * An administrator can manage users, roles, and other administrative functions.
     */
    ADMINISTRATOR
}
