//entity/Appointment.java

package entity;

import java.time.LocalDateTime;
import entity.enums.AppointmentStatus;

/**
 * Represents an appointment between a patient and a doctor.
 * The appointment has a unique ID, a scheduled date and time, a status, and an optional outcome record.
 */
public class Appointment {

    /** The unique ID of the appointment. */
    private final String appointmentId;

    /** The patient associated with the appointment. */
    private final Patient patient;

    /** The doctor associated with the appointment. */
    private final Doctor doctor;

    /** The scheduled date and time of the appointment. */
    private LocalDateTime dateTime;

    /** The current status of the appointment. */
    private AppointmentStatus status;

    /** The outcome record of the appointment, if any. */
    private AppointmentOutcomeRecord outcomeRecord;

    /**
     * Constructs a new appointment with the specified appointment ID, patient, doctor, and date/time.
     * The initial status of the appointment is set to PENDING_APPROVAL.
     * 
     * @param appointmentId The unique ID for the appointment.
     * @param patient The patient associated with the appointment.
     * @param doctor The doctor associated with the appointment.
     * @param dateTime The scheduled date and time of the appointment.
     */
    public Appointment(String appointmentId, Patient patient, Doctor doctor, 
                      LocalDateTime dateTime) {
        this.appointmentId = appointmentId;
        this.patient = patient;
        this.doctor = doctor;
        this.dateTime = dateTime;
        this.status = AppointmentStatus.PENDING_APPROVAL;
    }

    /**
     * Sets a new date and time for the appointment.
     * 
     * @param newDateTime The new date and time for the appointment.
     */
    public void setDateTime(LocalDateTime newDateTime) {
        this.dateTime = newDateTime;
    }

    /**
     * Sets the status of the appointment.
     * 
     * @param status The new status of the appointment.
     */
    public void setStatus(AppointmentStatus status) {
        this.status = status;
    }

    /**
     * Returns a string representation of the appointment, including its ID, patient, doctor, date/time, and status.
     * 
     * @return A string describing the appointment.
     */
    @Override
    public String toString() {
        return String.format("Appointment ID: %s\nPatient: %s\nDoctor: %s\nDate/Time: %s\nStatus: %s",
            appointmentId, patient.getName(), doctor.getName(), dateTime, status);
    }

    /**
     * Returns the appointment ID.
     * 
     * @return The appointment ID.
     */
    public String getAppointmentId() {
        return appointmentId;
    }

    /**
     * Returns the patient associated with the appointment.
     * 
     * @return The patient associated with the appointment.
     */
    public Patient getPatient() {
        return patient;
    }

    /**
     * Returns the doctor associated with the appointment.
     * 
     * @return The doctor associated with the appointment.
     */
    public Doctor getDoctor() {
        return doctor;
    }

    /**
     * Returns the scheduled date and time of the appointment.
     * 
     * @return The scheduled date and time of the appointment.
     */
    public LocalDateTime getDateTime() {
        return dateTime;
    }

    /**
     * Returns the current status of the appointment.
     * 
     * @return The status of the appointment.
     */
    public AppointmentStatus getStatus() {
        return status;
    }

    /**
     * Returns the outcome record of the appointment, if any.
     * 
     * @return The outcome record of the appointment.
     */
    public AppointmentOutcomeRecord getOutcomeRecord() {
        return outcomeRecord;
    }

    /**
     * Sets the outcome record for the appointment.
     * 
     * @param record The outcome record to associate with the appointment.
     */
    public void setOutcomeRecord(AppointmentOutcomeRecord record) {
        this.outcomeRecord = record;
    }

    /**
     * Validates if the outcome record is valid for the appointment.
     * The outcome record is considered valid only if the appointment status is COMPLETED.
     * 
     * @return True if the outcome record is valid, false otherwise.
     */
    public boolean hasValidOutcomeRecord() {
        return this.outcomeRecord != null && 
               this.status == AppointmentStatus.COMPLETED;
    }
}
