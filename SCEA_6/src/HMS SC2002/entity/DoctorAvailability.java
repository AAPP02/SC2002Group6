//entity/DoctorAvailability.java

package entity;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

/**
 * Represents a doctor's availability for appointments on a specific date and time range.
 */
public class DoctorAvailability {

    /** Unique identifier for the availability slot. */
    private final String id;
    
    /** The doctor associated with this availability slot. */
    private final Doctor doctor;
    
    /** The date of the doctor's availability. */
    private final LocalDate date;
    
    /** The start time of the doctor's availability on the specified date. */
    private final LocalTime startTime;
    
    /** The end time of the doctor's availability on the specified date. */
    private final LocalTime endTime;

    /**
     * Constructs a new DoctorAvailability with the specified doctor, date, start time, and end time.
     * Generates a unique identifier for the availability slot.
     * 
     * @param doctor The doctor for whom this availability applies.
     * @param date The date of the doctor's availability.
     * @param startTime The start time of the availability.
     * @param endTime The end time of the availability.
     */
    public DoctorAvailability(Doctor doctor, LocalDate date, 
                            LocalTime startTime, LocalTime endTime) {
        this.id = UUID.randomUUID().toString(); // Generate unique ID
        this.doctor = doctor;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    /**
     * Gets the unique identifier for this availability slot.
     * 
     * @return The unique ID of the availability slot.
     */
    public String getId() {
        return id;
    }

    /**
     * Gets the doctor associated with this availability slot.
     * 
     * @return The doctor for whom this availability is set.
     */
    public Doctor getDoctor() {
        return doctor;
    }

    /**
     * Gets the date of the doctor's availability.
     * 
     * @return The date of availability.
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * Gets the start time of the doctor's availability.
     * 
     * @return The start time of availability on the specified date.
     */
    public LocalTime getStartTime() {
        return startTime;
    }

    /**
     * Gets the end time of the doctor's availability.
     * 
     * @return The end time of availability on the specified date.
     */
    public LocalTime getEndTime() {
        return endTime;
    }
}
