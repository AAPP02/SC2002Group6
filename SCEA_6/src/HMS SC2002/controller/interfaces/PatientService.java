//controller/interfaces/PatientService.java

package controller.interfaces;

import entity.*;
import java.util.List;
import java.util.Optional;

/**
 * Interface for managing patient-related operations, including appointment handling,
 * patient records, and doctor-patient interactions.
 */
public interface PatientService {

    /**
     * Retrieves a list of patients currently under the care of a specific doctor.
     *
     * @param doctor The doctor whose patients are being retrieved.
     * @return A list of patients under the care of the specified doctor.
     */
    List<Patient> getPatientsUnderCare(Doctor doctor);

    /**
     * Retrieves a list of past appointment records for a specific patient.
     *
     * @param patient The patient whose past appointment records are being retrieved.
     * @return A list of AppointmentOutcomeRecord objects representing the patient's past appointments.
     */
    List<AppointmentOutcomeRecord> getPastAppointmentRecords(Patient patient);

    /**
     * Adds a new patient to the system.
     *
     * @param patient The Patient object to be added.
     */
    void addPatient(Patient patient);

    /**
     * Retrieves a patient by their unique patient ID.
     *
     * @param patientId The unique ID of the patient to be retrieved.
     * @return The Patient object associated with the provided patient ID.
     */
    Patient getPatient(String patientId);

    /**
     * Retrieves a list of all patients in the system.
     *
     * @return A list of all Patient objects.
     */
    List<Patient> getAllPatients();

    /**
     * Checks if a patient exists in the system based on their patient ID.
     *
     * @param patientId The patient ID to be checked.
     * @return True if the patient exists, false otherwise.
     */
    boolean existsPatient(String patientId);

    /**
     * Retrieves a list of upcoming appointments for a specific patient.
     *
     * @param patientId The ID of the patient whose upcoming appointments are to be retrieved.
     * @return A list of Appointment objects representing the patient's upcoming appointments.
     */
    List<Appointment> getUpcomingAppointments(String patientId);

    /**
     * Retrieves a list of all appointments for a specific patient.
     *
     * @param patientId The ID of the patient whose appointments are to be retrieved.
     * @return A list of all Appointment objects for the specified patient.
     */
    List<Appointment> getAllAppointments(String patientId);

    /**
     * Checks if a specific patient has any pending appointments.
     *
     * @param patientId The ID of the patient to be checked.
     * @return True if the patient has any pending appointments, false otherwise.
     */
    boolean hasAnyPendingAppointments(String patientId);

    /**
     * Checks if a specific patient has any confirmed appointments.
     *
     * @param patientId The ID of the patient to be checked.
     * @return True if the patient has any confirmed appointments, false otherwise.
     */
    boolean hasAnyConfirmedAppointments(String patientId);

    /**
     * Retrieves the next upcoming appointment for a specific patient.
     *
     * @param patientId The ID of the patient whose next appointment is to be retrieved.
     * @return An Optional containing the next appointment if available, or an empty Optional if no upcoming appointments exist.
     */
    Optional<Appointment> getNextAppointment(String patientId);

    /**
     * Checks if a specific patient can schedule a new appointment based on their current appointment status.
     *
     * @param patientId The ID of the patient to be checked.
     * @return True if the patient can schedule a new appointment, false otherwise.
     */
    boolean canScheduleNewAppointment(String patientId);

    /**
     * Retrieves a list of appointments for a specific patient with a specific doctor.
     *
     * @param patientId The ID of the patient whose appointments with the specified doctor are to be retrieved.
     * @param doctor    The doctor whose appointments with the patient are being retrieved.
     * @return A list of Appointment objects representing the patient's appointments with the specified doctor.
     */
    List<Appointment> getAppointmentsWithDoctor(String patientId, Doctor doctor);

    /**
     * Cancels all pending appointments for a specific patient.
     *
     * @param patientId The ID of the patient whose pending appointments are to be cancelled.
     */
    void cancelAllPendingAppointments(String patientId);

    /**
     * Validates the existence of a patient using their patient ID.
     *
     * @param patientId The ID of the patient to be validated.
     */
    void validatePatient(String patientId);
}
