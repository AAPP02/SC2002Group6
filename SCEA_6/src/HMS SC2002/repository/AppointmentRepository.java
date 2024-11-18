//repository/AppointmentRepository.java

package repository;

import entity.*;
import entity.enums.AppointmentStatus;

import java.time.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * {@code AppointmentRepository} is a singleton class that manages the collection of appointments.
 * It allows for saving, finding, deleting, and querying appointments based on various criteria such as patient, doctor, date, and status.
 */
public class AppointmentRepository implements Repository<Appointment, String> {
    private final Map<String, Appointment> appointments;
    private static AppointmentRepository instance;
    private int nextAppointmentNumber = 1;
    
    /**
     * Private constructor to initialize the appointment repository.
     * Initializes the appointments map to store appointment data.
     */
    private AppointmentRepository() {
        this.appointments = new ConcurrentHashMap<>();
    }
    
    /**
     * Gets the singleton instance of the {@code AppointmentRepository}.
     * If the instance does not exist, it is created.
     * 
     * @return The singleton instance of the {@code AppointmentRepository}.
     */
    public static AppointmentRepository getInstance() {
        if (instance == null) {
            instance = new AppointmentRepository();
        }
        return instance;
    }
    
    /**
     * Generates a unique appointment ID by incrementing the {@code nextAppointmentNumber}.
     * The ID is in the format "A#####".
     * 
     * @return A unique appointment ID.
     */
    private String generateAppointmentId() {
        String appointmentId;
        do {
            appointmentId = "A" + String.format("%05d", nextAppointmentNumber++);
        } while (appointments.containsKey(appointmentId));
        return appointmentId;
    }
    
    /**
     * Saves the provided appointment to the repository. If the appointment does not have an ID, a new ID is generated.
     * 
     * @param appointment The appointment to be saved.
     * @return The saved appointment, now with an ID if it did not have one.
     */
    @Override
    public Appointment save(Appointment appointment) {
        if (appointment.getAppointmentId() == null) {
            String newId = generateAppointmentId();
            // Create new appointment with generated ID
            appointment = new Appointment(
                newId,
                appointment.getPatient(),
                appointment.getDoctor(),
                appointment.getDateTime()
            );
        }
        appointments.put(appointment.getAppointmentId(), appointment);
        return appointment;
    }
    
    /**
     * Finds an appointment by its ID.
     * 
     * @param id The ID of the appointment to find.
     * @return An {@link Optional} containing the appointment if found, or an empty {@link Optional} if not found.
     */
    @Override
    public Optional<Appointment> findById(String id) {
        return Optional.ofNullable(appointments.get(id));
    }
    
    /**
     * Finds all appointments in the repository.
     * 
     * @return A list of all appointments.
     */
    @Override
    public List<Appointment> findAll() {
        return new ArrayList<>(appointments.values());
    }
    
    /**
     * Deletes the appointment with the specified ID.
     * 
     * @param id The ID of the appointment to be deleted.
     */
    @Override
    public void delete(String id) {
        appointments.remove(id);
    }
    
    /**
     * Checks if an appointment with the specified ID exists in the repository.
     * 
     * @param id The ID of the appointment to check.
     * @return {@code true} if the appointment exists, {@code false} otherwise.
     */
    @Override
    public boolean exists(String id) {
        return appointments.containsKey(id);
    }
    
    /**
     * Finds all appointments for a specific doctor.
     * 
     * @param doctor The doctor whose appointments are to be found.
     * @return A list of appointments for the specified doctor, sorted by date and time.
     */
    public List<Appointment> findByDoctor(Doctor doctor) {
        return appointments.values().stream()
            .filter(apt -> apt.getDoctor().equals(doctor))
            .sorted(Comparator.comparing(Appointment::getDateTime))
            .collect(Collectors.toList());
    }
    
    /**
     * Finds all appointments for a specific patient.
     * 
     * @param patient The patient whose appointments are to be found.
     * @return A list of appointments for the specified patient, sorted by date and time.
     */
    public List<Appointment> findByPatient(Patient patient) {
        return appointments.values().stream()
            .filter(apt -> apt.getPatient().equals(patient))
            .sorted(Comparator.comparing(Appointment::getDateTime))
            .collect(Collectors.toList());
    }
    
    /**
     * Finds all appointments with a specific status.
     * 
     * @param status The status of the appointments to find.
     * @return A list of appointments with the specified status, sorted by date and time.
     */
    public List<Appointment> findByStatus(AppointmentStatus status) {
        return appointments.values().stream()
            .filter(apt -> apt.getStatus() == status)
            .sorted(Comparator.comparing(Appointment::getDateTime))
            .collect(Collectors.toList());
    }
    
    /**
     * Finds all future appointments for a specific doctor.
     * 
     * @param doctor The doctor whose future appointments are to be found.
     * @return A list of future appointments for the specified doctor, sorted by date and time.
     */
    public List<Appointment> findFutureAppointmentsByDoctor(Doctor doctor) {
        LocalDateTime now = LocalDateTime.now();
        return appointments.values().stream()
            .filter(apt -> apt.getDoctor().equals(doctor))
            .filter(apt -> apt.getDateTime().isAfter(now))
            .sorted(Comparator.comparing(Appointment::getDateTime))
            .collect(Collectors.toList());
    }
    
    /**
     * Finds all future appointments for a specific patient.
     * 
     * @param patient The patient whose future appointments are to be found.
     * @return A list of future appointments for the specified patient, sorted by date and time.
     */
    public List<Appointment> findFutureAppointmentsByPatient(Patient patient) {
        LocalDateTime now = LocalDateTime.now();
        return appointments.values().stream()
            .filter(apt -> apt.getPatient().equals(patient))
            .filter(apt -> apt.getDateTime().isAfter(now))
            .sorted(Comparator.comparing(Appointment::getDateTime))
            .collect(Collectors.toList());
    }
    
    /**
     * Finds all past appointments for a specific doctor.
     * 
     * @param doctor The doctor whose past appointments are to be found.
     * @return A list of past appointments for the specified doctor, sorted by date and time (in reverse order).
     */
    public List<Appointment> findPastAppointmentsByDoctor(Doctor doctor) {
        LocalDateTime now = LocalDateTime.now();
        return appointments.values().stream()
            .filter(apt -> apt.getDoctor().equals(doctor))
            .filter(apt -> apt.getDateTime().isBefore(now))
            .sorted(Comparator.comparing(Appointment::getDateTime).reversed())
            .collect(Collectors.toList());
    }
    
    /**
     * Finds all past appointments for a specific patient.
     * 
     * @param patient The patient whose past appointments are to be found.
     * @return A list of past appointments for the specified patient, sorted by date and time (in reverse order).
     */
    public List<Appointment> findPastAppointmentsByPatient(Patient patient) {
        LocalDateTime now = LocalDateTime.now();
        return appointments.values().stream()
            .filter(apt -> apt.getPatient().equals(patient))
            .filter(apt -> apt.getDateTime().isBefore(now))
            .sorted(Comparator.comparing(Appointment::getDateTime).reversed())
            .collect(Collectors.toList());
    }
    
    /**
     * Finds all appointments for a specific date.
     * 
     * @param date The date of the appointments to find.
     * @return A list of appointments on the specified date, sorted by date and time.
     */
    public List<Appointment> findByDate(LocalDate date) {
        return appointments.values().stream()
            .filter(apt -> apt.getDateTime().toLocalDate().equals(date))
            .sorted(Comparator.comparing(Appointment::getDateTime))
            .collect(Collectors.toList());
    }
    
    /**
     * Finds all appointments within a specific date range.
     * 
     * @param startDate The start of the date range.
     * @param endDate The end of the date range.
     * @return A list of appointments within the specified date range, sorted by date and time.
     */
    public List<Appointment> findByDateRange(LocalDate startDate, LocalDate endDate) {
        return appointments.values().stream()
            .filter(apt -> {
                LocalDate aptDate = apt.getDateTime().toLocalDate();
                return !aptDate.isBefore(startDate) && !aptDate.isAfter(endDate);
            })
            .sorted(Comparator.comparing(Appointment::getDateTime))
            .collect(Collectors.toList());
    }
    
    /**
     * Finds all pending appointments for a specific doctor.
     * 
     * @param doctor The doctor whose pending appointments are to be found.
     * @return A list of pending appointments for the specified doctor, sorted by date and time.
     */
    public List<Appointment> findPendingAppointmentsByDoctor(Doctor doctor) {
        LocalDateTime now = LocalDateTime.now();
        return appointments.values().stream()
            .filter(apt -> apt.getDoctor().equals(doctor))
            .filter(apt -> apt.getDateTime().isAfter(now))
            .filter(apt -> apt.getStatus() == AppointmentStatus.PENDING_APPROVAL)
            .sorted(Comparator.comparing(Appointment::getDateTime))
            .collect(Collectors.toList());
    }
    
    /**
     * Finds all confirmed appointments for a specific doctor.
     * 
     * @param doctor The doctor whose confirmed appointments are to be found.
     * @return A list of confirmed appointments for the specified doctor, sorted by date and time.
     */
    public List<Appointment> findConfirmedAppointmentsByDoctor(Doctor doctor) {
        LocalDateTime now = LocalDateTime.now();
        return appointments.values().stream()
            .filter(apt -> apt.getDoctor().equals(doctor))
            .filter(apt -> apt.getDateTime().isAfter(now))
            .filter(apt -> apt.getStatus() == AppointmentStatus.CONFIRMED)
            .sorted(Comparator.comparing(Appointment::getDateTime))
            .collect(Collectors.toList());
    }
    
    /**
     * Finds the next confirmed appointment for a specific patient.
     * 
     * @param patient The patient whose next confirmed appointment is to be found.
     * @return An {@link Optional} containing the next confirmed appointment for the patient if found, otherwise empty.
     */
    public Optional<Appointment> findNextAppointmentForPatient(Patient patient) {
        LocalDateTime now = LocalDateTime.now();
        return appointments.values().stream()
            .filter(apt -> apt.getPatient().equals(patient))
            .filter(apt -> apt.getDateTime().isAfter(now))
            .filter(apt -> apt.getStatus() == AppointmentStatus.CONFIRMED)
            .min(Comparator.comparing(Appointment::getDateTime));
    }
    
    /**
     * Finds the next confirmed appointment for a specific doctor.
     * 
     * @param doctor The doctor whose next confirmed appointment is to be found.
     * @return An {@link Optional} containing the next confirmed appointment for the doctor if found, otherwise empty.
     */
    public Optional<Appointment> findNextAppointmentForDoctor(Doctor doctor) {
        LocalDateTime now = LocalDateTime.now();
        return appointments.values().stream()
            .filter(apt -> apt.getDoctor().equals(doctor))
            .filter(apt -> apt.getDateTime().isAfter(now))
            .filter(apt -> apt.getStatus() == AppointmentStatus.CONFIRMED)
            .min(Comparator.comparing(Appointment::getDateTime));
    }
    
    /**
     * Counts the number of appointments by status for a specific doctor.
     * 
     * @param doctor The doctor whose appointments are to be counted by status.
     * @return A map where the keys are appointment statuses and the values are the counts of appointments with each status.
     */
    public Map<AppointmentStatus, Long> getAppointmentCountsByStatusForDoctor(Doctor doctor) {
        return appointments.values().stream()
            .filter(apt -> apt.getDoctor().equals(doctor))
            .collect(Collectors.groupingBy(
                Appointment::getStatus,
                Collectors.counting()
            ));
    }
    
    /**
     * Deletes all cancelled appointments older than the specified date.
     * 
     * @param before The date before which cancelled appointments should be removed.
     */
    public void cleanupOldCancelledAppointments(LocalDate before) {
        appointments.values().removeIf(apt -> 
            apt.getStatus() == AppointmentStatus.CANCELLED &&
            apt.getDateTime().toLocalDate().isBefore(before)
        );
    }
    
    /**
     * Checks if a specific doctor is available for an appointment at a specific date and time.
     * 
     * @param doctor The doctor whose availability is to be checked.
     * @param dateTime The date and time to check for availability.
     * @return {@code true} if the doctor is available at the specified date and time, {@code false} otherwise.
     */
    public boolean isDoctorAvailable(Doctor doctor, LocalDateTime dateTime) {
        return appointments.values().stream()
            .filter(apt -> apt.getDoctor().equals(doctor))
            .filter(apt -> apt.getStatus() != AppointmentStatus.CANCELLED)
            .noneMatch(apt -> apt.getDateTime().equals(dateTime));
    }

    /**
     * Clears all appointments from the repository.
     */
    @Override
    public void clearAll() {
        appointments.clear();
    }
}
