//repository/DoctoryAvailabilityRepository.java

package repository;

import entity.*;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The {@code DoctorAvailabilityRepository} class is responsible for storing and managing the availability information
 * of doctors in a concurrent map. It supports common repository operations such as saving, finding, deleting, 
 * and checking existence of doctor availability records. This repository also provides methods to query availability
 * by doctor and date.
 */
public class DoctorAvailabilityRepository implements Repository<DoctorAvailability, String> {
    private static DoctorAvailabilityRepository instance;
    private final Map<String, DoctorAvailability> availabilities;
    
    /**
     * Private constructor to initialize the availability repository.
     * Uses a {@code ConcurrentHashMap} to store doctor availability records.
     */
    private DoctorAvailabilityRepository() {
        this.availabilities = new ConcurrentHashMap<>();
    }
    
    /**
     * Returns the singleton instance of the {@code DoctorAvailabilityRepository}.
     * If the instance doesn't exist, it will be created.
     *
     * @return The singleton instance of {@code DoctorAvailabilityRepository}.
     */
    public static DoctorAvailabilityRepository getInstance() {
        if (instance == null) {
            instance = new DoctorAvailabilityRepository();
        }
        return instance;
    }
    
    /**
     * Saves a {@code DoctorAvailability} record in the repository.
     * If an availability with the same ID already exists, it will be overwritten.
     *
     * @param availability The {@code DoctorAvailability} record to save.
     * @return The saved {@code DoctorAvailability} record.
     */
    @Override
    public DoctorAvailability save(DoctorAvailability availability) {
        availabilities.put(availability.getId(), availability);
        return availability;
    }
    
    /**
     * Finds a {@code DoctorAvailability} by its ID.
     *
     * @param id The ID of the {@code DoctorAvailability} to find.
     * @return An {@code Optional} containing the {@code DoctorAvailability} if found, or an empty {@code Optional} if not.
     */
    @Override
    public Optional<DoctorAvailability> findById(String id) {
        return Optional.ofNullable(availabilities.get(id));
    }
    
    /**
     * Retrieves all {@code DoctorAvailability} records in the repository.
     *
     * @return A list of all {@code DoctorAvailability} records.
     */
    @Override
    public List<DoctorAvailability> findAll() {
        return new ArrayList<>(availabilities.values());
    }
    
    /**
     * Deletes a {@code DoctorAvailability} by its ID.
     *
     * @param id The ID of the {@code DoctorAvailability} to delete.
     */
    @Override
    public void delete(String id) {
        availabilities.remove(id);
    }
    
    /**
     * Checks if a {@code DoctorAvailability} with the specified ID exists in the repository.
     *
     * @param id The ID to check for existence.
     * @return {@code true} if the {@code DoctorAvailability} exists, {@code false} otherwise.
     */
    @Override
    public boolean exists(String id) {
        return availabilities.containsKey(id);
    }
    
    /**
     * Finds a {@code DoctorAvailability} record for a specific doctor on a specific date.
     *
     * @param doctor The {@code Doctor} whose availability to search for.
     * @param date The {@code LocalDate} for which the availability is being checked.
     * @return An {@code Optional} containing the matching {@code DoctorAvailability} if found, or an empty {@code Optional} if not.
     */
    public Optional<DoctorAvailability> findByDoctorAndDate(Doctor doctor, LocalDate date) {
        return availabilities.values().stream()
            .filter(a -> a.getDoctor().equals(doctor) && a.getDate().equals(date))
            .findFirst();
    }
    
    /**
     * Finds all {@code DoctorAvailability} records for a specific doctor, sorted by date.
     *
     * @param doctor The {@code Doctor} whose availability records to search for.
     * @return A list of {@code DoctorAvailability} records for the specified doctor, sorted by date.
     */
    public List<DoctorAvailability> findByDoctor(Doctor doctor) {
        return availabilities.values().stream()
            .filter(a -> a.getDoctor().equals(doctor))
            .sorted(Comparator.comparing(DoctorAvailability::getDate))
            .toList();
    }
    
    /**
     * Finds all {@code DoctorAvailability} records for a specific date, sorted by doctor's name.
     *
     * @param date The {@code LocalDate} for which the availability records are being searched.
     * @return A list of {@code DoctorAvailability} records for the specified date, sorted by the doctor's name.
     */
    public List<DoctorAvailability> findByDate(LocalDate date) {
        return availabilities.values().stream()
            .filter(a -> a.getDate().equals(date))
            .sorted(Comparator.comparing(a -> a.getDoctor().getName()))
            .toList();
    }

    /**
     * Clears all the doctor availability records from the repository.
     */
    public void clearAll() {
        availabilities.clear();
    }
}
