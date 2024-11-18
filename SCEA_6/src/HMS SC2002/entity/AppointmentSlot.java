//entity/AppointmentSlot.java

package entity;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Represents a time slot for an appointment with a doctor on a specific date.
 * Each slot can either be available or booked, and contains the start time, end time,
 * and the associated doctor.
 */
public class AppointmentSlot {
    
    /** Unique identifier for the appointment slot. */
    private final String slotId;

    /** The start time of the appointment slot. */
    private final LocalTime startTime;

    /** The end time of the appointment slot. */
    private final LocalTime endTime;

    /** The doctor associated with the appointment slot. */
    private final Doctor doctor;

    /** The date of the appointment slot. */
    private final LocalDate date;

    /** Flag indicating if the slot is available for booking. */
    private boolean available;

    /** The appointment booked for this slot, if any. */
    private Appointment bookedAppointment;

    /**
     * Constructs a new appointment slot with the given details.
     * 
     * @param slotId The unique identifier for the slot.
     * @param startTime The start time of the slot.
     * @param endTime The end time of the slot.
     * @param doctor The doctor assigned to the slot.
     * @param date The date the slot is scheduled for.
     */
    public AppointmentSlot(String slotId, LocalTime startTime, LocalTime endTime,
                          Doctor doctor, LocalDate date) {
        this.slotId = slotId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.doctor = doctor;
        this.date = date;
        this.available = true;
    }

    /**
     * Attempts to book this appointment slot for a given appointment. If the slot is already
     * booked, the booking attempt will fail.
     * 
     * @param appointment The appointment to book in the slot.
     * @return true if the slot was successfully booked, false if it was already booked.
     */
    public synchronized boolean tryBook(Appointment appointment) {
        if (!available) {
            return false;
        }
        available = false;
        bookedAppointment = appointment;
        return true;
    }

    /**
     * Releases the appointment slot, making it available for re-booking.
     */
    public synchronized void release() {
        available = true;
        bookedAppointment = null;
    }

    /**
     * Returns the unique identifier for this appointment slot.
     * 
     * @return The slot identifier.
     */
    public String getSlotId() {
        return slotId;
    }

    /**
     * Returns the start time of the appointment slot.
     * 
     * @return The start time.
     */
    public LocalTime getStartTime() {
        return startTime;
    }

    /**
     * Returns the end time of the appointment slot.
     * 
     * @return The end time.
     */
    public LocalTime getEndTime() {
        return endTime;
    }

    /**
     * Returns the doctor associated with the appointment slot.
     * 
     * @return The doctor.
     */
    public Doctor getDoctor() {
        return doctor;
    }

    /**
     * Returns the date of the appointment slot.
     * 
     * @return The date.
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * Returns whether the appointment slot is available for booking.
     * 
     * @return true if the slot is available, false otherwise.
     */
    public boolean isAvailable() {
        return available;
    }

    /**
     * Sets the availability of the appointment slot.
     * 
     * @param available The new availability status.
     */
    public void setAvailable(boolean available) {
        this.available = available;
    }

    /**
     * Returns the appointment booked in this slot, if any.
     * 
     * @return The booked appointment, or null if no appointment is booked.
     */
    public Appointment getBookedAppointment() {
        return bookedAppointment;
    }
}
