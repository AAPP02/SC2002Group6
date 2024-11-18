//controller/AppointmentController.java

package controller;

import java.time.*;
import java.util.*;

import controller.interfaces.*;
import entity.*;
import entity.enums.AppointmentStatus;
import entity.enums.PrescriptionStatus;
import repository.AppointmentRepository;

/**
 * Controller responsible for handling appointment-related operations,
 * such as scheduling, rescheduling, cancellation, and retrieving appointment details.
 */
public class AppointmentController implements AppointmentService {
    private final DoctorAvailabilityService availabilityService;
    private final AppointmentRepository appointmentRepository;
    
    /**
     * Constructor to initialize AppointmentController with the necessary services.
     * 
     * @param availabilityService Service to manage doctor's availability.
     */
    public AppointmentController(DoctorAvailabilityService availabilityService) {
        this.availabilityService = availabilityService;
        this.appointmentRepository = AppointmentRepository.getInstance();
    }

    /**
     * Gets the service that manages the availability of doctors.
     * 
     * @return DoctorAvailabilityService instance.
     */
    public DoctorAvailabilityService getAvailabilityService() {
        return availabilityService;
    }
    
    /**
     * Retrieves available appointment slots for a given doctor on a specific date.
     * Filters out slots that are already booked.
     * 
     * @param date The date for which to fetch available slots.
     * @param doctor The doctor for whom the slots need to be fetched.
     * @return A list of available AppointmentSlot objects.
     */
    @Override
    public List<AppointmentSlot> getAvailableSlots(LocalDate date, Doctor doctor) {
        DoctorAvailability availability = doctor.getAvailability(date);
        List<AppointmentSlot> slots = availabilityService.generateSlots(availability);
        
        // Get all appointments for this doctor on this date
        List<Appointment> existingAppointments = appointmentRepository.findAll().stream()
            .filter(apt -> apt.getDoctor().equals(doctor))
            .filter(apt -> apt.getDateTime().toLocalDate().equals(date))
            .filter(apt -> apt.getStatus() != AppointmentStatus.CANCELLED)
            .toList();
        
        // Filter out slots that are already booked
        return slots.stream()
            .filter(slot -> {
                LocalDateTime slotDateTime = slot.getDate().atTime(slot.getStartTime());
                return existingAppointments.stream()
                    .noneMatch(apt -> apt.getDateTime().equals(slotDateTime));
            })
            .toList();
    }
    
    /**
     * Schedules a new appointment for a patient with a given doctor on a specified slot.
     * 
     * @param patient The patient who is scheduling the appointment.
     * @param doctor The doctor for the appointment.
     * @param slot The available appointment slot.
     * @return The scheduled Appointment object, or null if the slot is unavailable.
     */
    @Override
    public Appointment scheduleAppointment(Patient patient, Doctor doctor, AppointmentSlot slot) {
        if (!slot.isAvailable()) {
            return null;
        }
        
        Appointment appointment = new Appointment(null, patient, doctor, 
            slot.getDate().atTime(slot.getStartTime()));
            
        if (!slot.tryBook(appointment)) {
            return null; // Slot was taken before we could book it
        }
        
        return appointmentRepository.save(appointment);
    }
    
    /**
     * Reschedules an existing appointment to a new available slot.
     * 
     * @param appointmentId The ID of the appointment to reschedule.
     * @param newSlot The new appointment slot.
     * @return True if the appointment was successfully rescheduled, false otherwise.
     */
    @Override
    public boolean rescheduleAppointment(String appointmentId, AppointmentSlot newSlot) {
        Optional<Appointment> optionalAppointment = appointmentRepository.findById(appointmentId);
        if (optionalAppointment.isEmpty()) {
            return false;
        }
        
        Appointment appointment = optionalAppointment.get();
        
        // Validate appointment can be rescheduled
        if (appointment.getStatus() == AppointmentStatus.COMPLETED || 
            appointment.getStatus() == AppointmentStatus.CANCELLED) {
            return false;
        }
        
        // Check if new slot is available
        if (!newSlot.isAvailable()) {
            return false;
        }
        
        // Make the old slot available again
        AppointmentSlot oldSlot = availabilityService.getSlotByDateTime(
            appointment.getDoctor(), 
            appointment.getDateTime().toLocalDate(),
            appointment.getDateTime().toLocalTime()
        );
        if (oldSlot != null) {
            oldSlot.setAvailable(true);
        }
        
        // Update appointment with new datetime and reset status to pending
        appointment.setDateTime(newSlot.getDate().atTime(newSlot.getStartTime()));
        appointment.setStatus(AppointmentStatus.PENDING_APPROVAL);
        newSlot.setAvailable(false);
        
        appointmentRepository.save(appointment);
        return true;
    }
    
    /**
     * Cancels an existing appointment.
     * 
     * @param appointmentId The ID of the appointment to cancel.
     * @return True if the appointment was successfully cancelled, false otherwise.
     */
    @Override
    public boolean cancelAppointment(String appointmentId) {
        Optional<Appointment> optionalAppointment = appointmentRepository.findById(appointmentId);
        if (optionalAppointment.isEmpty()) {
            return false;
        }
        
        Appointment appointment = optionalAppointment.get();
        
        // Validate appointment can be cancelled
        if (appointment.getStatus() == AppointmentStatus.COMPLETED || 
            appointment.getStatus() == AppointmentStatus.CANCELLED) {
            return false;
        }
        
        // Make the slot available again
        AppointmentSlot slot = availabilityService.getSlotByDateTime(
            appointment.getDoctor(), 
            appointment.getDateTime().toLocalDate(),
            appointment.getDateTime().toLocalTime()
        );
        if (slot != null) {
            slot.setAvailable(true);
        }
        
        appointment.setStatus(AppointmentStatus.CANCELLED);
        appointmentRepository.save(appointment);
        return true;
    }
    
    /**
     * Retrieves a list of scheduled appointments for a patient.
     * Filters out cancelled appointments and returns only future appointments.
     * 
     * @param patient The patient whose appointments are to be retrieved.
     * @return A list of upcoming appointments for the patient.
     */
    @Override
    public List<Appointment> getScheduledAppointments(Patient patient) {
        LocalDateTime now = LocalDateTime.now();
        
        return appointmentRepository.findByPatient(patient).stream()
            .filter(apt -> apt.getDateTime().isAfter(now))
            .filter(apt -> apt.getStatus() != AppointmentStatus.CANCELLED)
            .sorted(Comparator.comparing(Appointment::getDateTime))
            .toList();
    }
    
    /**
     * Retrieves a list of upcoming appointments for a doctor.
     * Filters out past and cancelled appointments.
     * 
     * @param doctor The doctor whose upcoming appointments are to be retrieved.
     * @return A list of upcoming appointments for the doctor.
     */
    @Override
    public List<Appointment> getUpcomingAppointments(Doctor doctor) {
        LocalDateTime now = LocalDateTime.now();
        
        return appointmentRepository.findByDoctor(doctor).stream()
            .filter(apt -> apt.getDateTime().isAfter(now))
            .filter(apt -> apt.getStatus() == AppointmentStatus.CONFIRMED || 
                        apt.getStatus() == AppointmentStatus.PENDING_APPROVAL)
            .sorted(Comparator.comparing(Appointment::getDateTime))
            .toList();
    }
    
    /**
     * Retrieves a list of pending approval appointments for a doctor.
     * 
     * @param doctor The doctor whose pending appointments are to be retrieved.
     * @return A list of pending appointments for the doctor.
     */
    @Override
    public List<Appointment> getPendingAppointments(Doctor doctor) {
        LocalDateTime now = LocalDateTime.now();
        
        return appointmentRepository.findByDoctor(doctor).stream()
            .filter(apt -> apt.getDateTime().isAfter(now))
            .filter(apt -> apt.getStatus() == AppointmentStatus.PENDING_APPROVAL)
            .sorted(Comparator.comparing(Appointment::getDateTime))
            .toList();
    }
    
    /**
     * Updates the status of a specific appointment.
     * Validates the status transition before making the update.
     * 
     * @param appointmentId The ID of the appointment to update.
     * @param status The new status to set for the appointment.
     * @return True if the status was successfully updated, false otherwise.
     */
    @Override
    public boolean updateAppointmentStatus(String appointmentId, AppointmentStatus status) {
        Optional<Appointment> optionalAppointment = appointmentRepository.findById(appointmentId);
        if (optionalAppointment.isEmpty()) {
            return false;
        }
        
        Appointment appointment = optionalAppointment.get();
        AppointmentStatus currentStatus = appointment.getStatus();
        
        // Check valid status transitions
        if (!isValidStatusTransition(currentStatus, status)) {
            return false;
        }
        
        // If cancelling, make the slot available again
        if (status == AppointmentStatus.CANCELLED) {
            AppointmentSlot slot = availabilityService.getSlotByDateTime(
                appointment.getDoctor(), 
                appointment.getDateTime().toLocalDate(),
                appointment.getDateTime().toLocalTime()
            );
            if (slot != null) {
                slot.setAvailable(true);
            }
        }
        
        appointment.setStatus(status);
        appointmentRepository.save(appointment);
        return true;
    }
    
    /**
     * Records the outcome of a confirmed appointment, including service type, prescriptions, and notes.
     * 
     * @param appointmentId The ID of the confirmed appointment.
     * @param serviceType The type of service provided during the appointment.
     * @param prescriptions List of prescriptions given during the appointment.
     * @param notes Additional notes about the appointment outcome.
     * @throws IllegalStateException If the appointment is not confirmed.
     */
    @Override
    public void recordAppointmentOutcome(String appointmentId, String serviceType,
                                    List<Prescription> prescriptions, String notes) {
        Optional<Appointment> optionalAppointment = appointmentRepository.findById(appointmentId);
        if (optionalAppointment.isEmpty() || 
            optionalAppointment.get().getStatus() != AppointmentStatus.CONFIRMED) {
            throw new IllegalStateException("Cannot record outcome for non-confirmed appointment");
        }
                                        
        Appointment appointment = optionalAppointment.get();
                                        
        // Initialize prescriptions with PENDING status
        prescriptions.forEach(p -> p.setStatus(PrescriptionStatus.PENDING));
                                        
        AppointmentOutcomeRecord outcome = new AppointmentOutcomeRecord(
            appointment.getDateTime().toLocalDate(),
            serviceType,
            new ArrayList<>(prescriptions),
            notes
        );
                                        
        appointment.setOutcomeRecord(outcome);
        appointment.setStatus(AppointmentStatus.COMPLETED);
                                        
        appointmentRepository.save(appointment);
    }
    
    /**
     * Retrieves all appointments for a specific doctor, ordered by date and time.
     * 
     * @param doctor The doctor whose appointments are to be retrieved.
     * @return A list of all appointments for the doctor.
     */
    @Override
    public List<Appointment> getAllAppointments(Doctor doctor) {
        return appointmentRepository.findByDoctor(doctor).stream()
            .sorted(Comparator.comparing(Appointment::getDateTime))
            .toList();
    }
    
    /**
     * Retrieves all appointments for a specific patient, ordered by date and time.
     * 
     * @param patient The patient whose appointments are to be retrieved.
     * @return A list of all appointments for the patient.
     */
    @Override
    public List<Appointment> getAllAppointments(Patient patient) {
        return appointmentRepository.findByPatient(patient).stream()
            .sorted(Comparator.comparing(Appointment::getDateTime))
            .toList();
    }
    
    /**
     * Validates if a status transition is allowed based on the current status.
     * 
     * @param current The current appointment status.
     * @param next The desired status to transition to.
     * @return True if the transition is valid, false otherwise.
     */
    private boolean isValidStatusTransition(AppointmentStatus current, AppointmentStatus next) {
        return switch (current) {
            case PENDING_APPROVAL -> 
                next == AppointmentStatus.CONFIRMED || 
                next == AppointmentStatus.CANCELLED;
            case CONFIRMED -> 
                next == AppointmentStatus.COMPLETED || 
                next == AppointmentStatus.CANCELLED;
            case COMPLETED, CANCELLED -> false;
        };
    }

    /**
     * Retrieves all appointments in the system, ordered by date and time.
     * 
     * @return A list of all appointments in the system.
     */
    @Override
    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll().stream()
            .sorted(Comparator.comparing(Appointment::getDateTime))
            .toList();
    }

    /**
     * Retrieves all appointments for a specific date, ordered by time.
     * 
     * @param date The date for which to retrieve the appointments.
     * @return A list of appointments for the specified date.
     */
    @Override
    public List<Appointment> getAppointmentsByDate(LocalDate date) {
        return appointmentRepository.findAll().stream()
            .filter(apt -> apt.getDateTime().toLocalDate().equals(date))
            .sorted(Comparator.comparing(Appointment::getDateTime))
            .toList();
    }

    /**
     * Retrieves an appointment by its ID.
     * 
     * @param appointmentId The ID of the appointment to retrieve.
     * @return An Optional containing the Appointment if found, otherwise empty.
     */
    @Override
    public Optional<Appointment> getAppointmentById(String appointmentId) {
        return appointmentRepository.findById(appointmentId);
    }

    /**
     * Retrieves appointments based on their status, ordered by date and time.
     * 
     * @param status The status of the appointments to retrieve.
     * @return A list of appointments with the specified status.
     */
    @Override
    public List<Appointment> getAppointmentsByStatus(AppointmentStatus status) {
        return appointmentRepository.findAll().stream()
            .filter(apt -> apt.getStatus() == status)
            .sorted(Comparator.comparing(Appointment::getDateTime))
            .toList();
    }
}
