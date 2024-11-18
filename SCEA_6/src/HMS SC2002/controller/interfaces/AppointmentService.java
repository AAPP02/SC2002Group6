// controller/interfaces/AppointmentService.java

package controller.interfaces;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import entity.*;
import entity.enums.*;

/**
 * Interface defining operations for managing appointments between patients and doctors.
 */
public interface AppointmentService {

    /**
     * Retrieves available appointment slots for a given doctor on a specific date.
     *
     * @param date   The date for which to check available slots.
     * @param doctor The doctor whose available slots are to be retrieved.
     * @return A list of available appointment slots.
     */
    List<AppointmentSlot> getAvailableSlots(LocalDate date, Doctor doctor);

    /**
     * Schedules an appointment for a patient with a specified doctor at a specific slot.
     *
     * @param patient The patient scheduling the appointment.
     * @param doctor  The doctor with whom the appointment is being scheduled.
     * @param slot    The time slot for the appointment.
     * @return The scheduled Appointment object.
     */
    Appointment scheduleAppointment(Patient patient, Doctor doctor, AppointmentSlot slot);

    /**
     * Reschedules an existing appointment to a new time slot.
     *
     * @param appointmentId The ID of the appointment to reschedule.
     * @param newSlot       The new appointment slot.
     * @return true if the appointment was successfully rescheduled, false otherwise.
     */
    boolean rescheduleAppointment(String appointmentId, AppointmentSlot newSlot);

    /**
     * Cancels an appointment by its ID.
     *
     * @param appointmentId The ID of the appointment to cancel.
     * @return true if the appointment was successfully canceled, false otherwise.
     */
    boolean cancelAppointment(String appointmentId);

    /**
     * Retrieves the scheduled appointments for a specific patient.
     *
     * @param patient The patient whose scheduled appointments are to be retrieved.
     * @return A list of the patient's scheduled appointments.
     */
    List<Appointment> getScheduledAppointments(Patient patient);

    /**
     * Retrieves upcoming appointments for a given doctor.
     *
     * @param doctor The doctor whose upcoming appointments are to be retrieved.
     * @return A list of the doctor's upcoming appointments.
     */
    List<Appointment> getUpcomingAppointments(Doctor doctor);

    /**
     * Retrieves pending appointments for a specific doctor.
     *
     * @param doctor The doctor whose pending appointments are to be retrieved.
     * @return A list of the doctor's pending appointments.
     */
    List<Appointment> getPendingAppointments(Doctor doctor);

    /**
     * Updates the status of an appointment by its ID.
     *
     * @param appointmentId The ID of the appointment to update.
     * @param status        The new status for the appointment.
     * @return true if the appointment status was successfully updated, false otherwise.
     */
    boolean updateAppointmentStatus(String appointmentId, AppointmentStatus status);

    /**
     * Retrieves all appointments associated with a specific doctor.
     *
     * @param doctor The doctor whose appointments are to be retrieved.
     * @return A list of all appointments for the specified doctor.
     */
    List<Appointment> getAllAppointments(Doctor doctor);

    /**
     * Retrieves all appointments associated with a specific patient.
     *
     * @param patient The patient whose appointments are to be retrieved.
     * @return A list of all appointments for the specified patient.
     */
    List<Appointment> getAllAppointments(Patient patient);

    /**
     * Retrieves all appointments across all doctors and patients.
     *
     * @return A list of all appointments.
     */
    List<Appointment> getAllAppointments();

    /**
     * Retrieves appointments scheduled on a specific date.
     *
     * @param date The date for which appointments are to be retrieved.
     * @return A list of appointments on the specified date.
     */
    List<Appointment> getAppointmentsByDate(LocalDate date);

    /**
     * Retrieves an appointment by its unique ID.
     *
     * @param appointmentId The ID of the appointment to retrieve.
     * @return An Optional containing the appointment if found, or an empty Optional otherwise.
     */
    Optional<Appointment> getAppointmentById(String appointmentId);

    /**
     * Retrieves appointments based on their status.
     *
     * @param status The status of the appointments to retrieve.
     * @return A list of appointments with the specified status.
     */
    List<Appointment> getAppointmentsByStatus(AppointmentStatus status);

    /**
     * Records the outcome of an appointment, including service type, prescriptions, and notes.
     *
     * @param appointmentId The ID of the appointment.
     * @param serviceType   The type of service provided during the appointment.
     * @param prescriptions The list of prescriptions issued during the appointment.
     * @param notes         Additional consultation notes.
     */
    void recordAppointmentOutcome(String appointmentId, String serviceType,
                                  List<Prescription> prescriptions, String notes);
}
