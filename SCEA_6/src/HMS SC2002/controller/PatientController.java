//controller/PatientController.java

package controller;

import entity.*;
import entity.enums.AppointmentStatus;
import repository.PatientRepository;
import java.util.*;
import controller.interfaces.*;

public class PatientController implements PatientService {
    private final AppointmentService appointmentService;
    private final PatientRepository patientRepository;
    
    /**
     * Constructor to initialize the PatientController with the required services.
     * 
     * @param appointmentService The service responsible for handling patient appointments.
     */
    public PatientController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
        this.patientRepository = PatientRepository.getInstance();
    }

    /**
     * Adds a new patient to the system.
     * 
     * @param patient The patient to be added.
     * @throws IllegalArgumentException if a patient with the same ID already exists.
     */
    @Override
    public void addPatient(Patient patient) {
        if (patientRepository.exists(patient.getHospitalId())) {
            throw new IllegalArgumentException("Patient with ID " + 
                patient.getHospitalId() + " already exists");
        }
        patientRepository.save(patient);
    }

    /**
     * Retrieves all patients in the system.
     * 
     * @return A list of all patients.
     */
    @Override
    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    /**
     * Checks if a patient exists in the system based on their ID.
     * 
     * @param patientId The ID of the patient to check.
     * @return true if the patient exists, false otherwise.
     */
    @Override
    public boolean existsPatient(String patientId) {
        return patientRepository.exists(patientId);
    }

    /**
     * Retrieves a specific patient's record based on their ID.
     * 
     * @param patientId The ID of the patient to retrieve.
     * @return The patient's record.
     * @throws IllegalArgumentException if the patient does not exist.
     */
    @Override
    public Patient getPatient(String patientId) {
        return patientRepository.findById(patientId)
            .orElseThrow(() -> new IllegalArgumentException("Patient not found with ID: " + patientId));
    }

    /**
     * Validates the existence of a patient based on their ID.
     * 
     * @param patientId The ID of the patient to validate.
     * @throws IllegalArgumentException if the patient does not exist.
     */
    @Override
    public void validatePatient(String patientId) {
        if (!patientRepository.exists(patientId)) {
            throw new IllegalArgumentException("Patient not found with ID: " + patientId);
        }
    }

    /**
     * Retrieves a list of patients under the care of a specific doctor.
     * 
     * @param doctor The doctor whose patients are being retrieved.
     * @return A list of patients under the doctor's care, sorted by patient name.
     */
    @Override
    public List<Patient> getPatientsUnderCare(Doctor doctor) {
        // Get all confirmed and upcoming appointments for the doctor
        List<Appointment> doctorAppointments = appointmentService.getUpcomingAppointments(doctor);
        
        // Add all completed appointments to get full patient history
        List<Appointment> allAppointments = appointmentService.getAllAppointments(doctor);
        
        // Create a set of unique patients from all appointments
        Set<Patient> uniquePatients = new HashSet<>();
        
        // Add patients from upcoming appointments
        for (Appointment apt : doctorAppointments) {
            uniquePatients.add(apt.getPatient());
        }
        
        // Add patients from historical appointments
        for (Appointment apt : allAppointments) {
            if (apt.getStatus() == AppointmentStatus.COMPLETED) {
                uniquePatients.add(apt.getPatient());
            }
        }
        
        // Convert to list and sort by patient name
        List<Patient> patientList = new ArrayList<>(uniquePatients);
        patientList.sort(Comparator.comparing(Patient::getName));
        
        return patientList;
    }

    /**
     * Retrieves a list of past appointment outcome records for a specific patient.
     * 
     * @param patient The patient whose past appointment records are being retrieved.
     * @return A list of completed appointment outcome records, sorted by appointment date.
     */
    @Override
    public List<AppointmentOutcomeRecord> getPastAppointmentRecords(Patient patient) {
        List<AppointmentOutcomeRecord> records = new ArrayList<>();
        
        List<Appointment> allAppointments = appointmentService.getAllAppointments(patient);
        
        for (Appointment apt : allAppointments) {
            if (apt.getStatus() == AppointmentStatus.COMPLETED && 
                apt.getOutcomeRecord() != null) {
                records.add(apt.getOutcomeRecord());
            }
        }
        
        records.sort((r1, r2) -> r2.getAppointmentDate().compareTo(r1.getAppointmentDate()));
        return records;
    }

    /**
     * Retrieves a list of upcoming appointments for a specific patient.
     * 
     * @param patientId The ID of the patient whose upcoming appointments are being retrieved.
     * @return A list of the patient's upcoming appointments.
     * @throws IllegalArgumentException if the patient does not exist.
     */
    public List<Appointment> getUpcomingAppointments(String patientId) {
        validatePatient(patientId);
        Patient patient = getPatient(patientId);
        return appointmentService.getScheduledAppointments(patient);
    }

    /**
     * Retrieves all appointments for a specific patient.
     * 
     * @param patientId The ID of the patient whose appointments are being retrieved.
     * @return A list of the patient's all appointments.
     * @throws IllegalArgumentException if the patient does not exist.
     */
    public List<Appointment> getAllAppointments(String patientId) {
        validatePatient(patientId);
        Patient patient = getPatient(patientId);
        return appointmentService.getAllAppointments(patient);
    }

    /**
     * Checks if a patient has any pending appointments.
     * 
     * @param patientId The ID of the patient to check.
     * @return true if the patient has any pending appointments, false otherwise.
     * @throws IllegalArgumentException if the patient does not exist.
     */
    public boolean hasAnyPendingAppointments(String patientId) {
        validatePatient(patientId);
        return getUpcomingAppointments(patientId).stream()
            .anyMatch(apt -> apt.getStatus() == AppointmentStatus.PENDING_APPROVAL);
    }

    /**
     * Checks if a patient has any confirmed appointments.
     * 
     * @param patientId The ID of the patient to check.
     * @return true if the patient has any confirmed appointments, false otherwise.
     * @throws IllegalArgumentException if the patient does not exist.
     */
    public boolean hasAnyConfirmedAppointments(String patientId) {
        validatePatient(patientId);
        return getUpcomingAppointments(patientId).stream()
            .anyMatch(apt -> apt.getStatus() == AppointmentStatus.CONFIRMED);
    }

    /**
     * Retrieves the next confirmed appointment for a patient.
     * 
     * @param patientId The ID of the patient whose next confirmed appointment is being retrieved.
     * @return An optional containing the next confirmed appointment, or an empty optional if none exist.
     * @throws IllegalArgumentException if the patient does not exist.
     */
    public Optional<Appointment> getNextAppointment(String patientId) {
        validatePatient(patientId);
        return getUpcomingAppointments(patientId).stream()
            .filter(apt -> apt.getStatus() == AppointmentStatus.CONFIRMED)
            .min(Comparator.comparing(Appointment::getDateTime));
    }

    /**
     * Checks if a patient can schedule a new appointment.
     * 
     * @param patientId The ID of the patient to check.
     * @return true if the patient can schedule a new appointment, false otherwise.
     * @throws IllegalArgumentException if the patient does not exist.
     */
    @Override
    public boolean canScheduleNewAppointment(String patientId) {
        try {
            validatePatient(patientId);
            List<Appointment> upcomingAppointments = getUpcomingAppointments(patientId);
            
            long pendingAndConfirmed = upcomingAppointments.stream()
                .filter(apt -> apt.getStatus() == AppointmentStatus.PENDING_APPROVAL || 
                             apt.getStatus() == AppointmentStatus.CONFIRMED)
                .count();
            
            return pendingAndConfirmed < 3;
        } catch (IllegalArgumentException e) {
            System.out.println("Error checking appointment eligibility: " + e.getMessage());
            return false;
        }
    }

    /**
     * Retrieves a list of appointments with a specific doctor for a patient.
     * 
     * @param patientId The ID of the patient whose appointments are being retrieved.
     * @param doctor The doctor whose appointments are being retrieved.
     * @return A list of the patient's appointments with the specified doctor, sorted by appointment date.
     * @throws IllegalArgumentException if the patient does not exist.
     */
    public List<Appointment> getAppointmentsWithDoctor(String patientId, Doctor doctor) {
        validatePatient(patientId);
        return getAllAppointments(patientId).stream()
            .filter(apt -> apt.getDoctor().equals(doctor))
            .sorted(Comparator.comparing(Appointment::getDateTime))
            .toList();
    }

    /**
     * Cancels all pending appointments for a patient.
     * 
     * @param patientId The ID of the patient whose pending appointments are to be canceled.
     * @throws IllegalArgumentException if the patient does not exist.
     */
    public void cancelAllPendingAppointments(String patientId) {
        validatePatient(patientId);
        getUpcomingAppointments(patientId).stream()
            .filter(apt -> apt.getStatus() == AppointmentStatus.PENDING_APPROVAL)
            .forEach(apt -> appointmentService.cancelAppointment(apt.getAppointmentId()));
    }
}
