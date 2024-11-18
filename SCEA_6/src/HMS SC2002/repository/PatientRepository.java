//repository/PatientRepository.java

package repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import entity.Patient;

/**
 * The {@code PatientRepository} class is responsible for managing a collection of patient records.
 * It allows for adding, retrieving, deleting, and querying patients by their unique identifier (hospital ID).
 * This class provides a singleton pattern for global access and implements the {@code Repository} interface.
 */
public class PatientRepository implements Repository<Patient, String> {
    private final Map<String, Patient> patients = new ConcurrentHashMap<>();
    
    private static PatientRepository instance;
    
    /**
     * Private constructor to initialize the patient repository.
     * Uses a {@code ConcurrentHashMap} to store patient records.
     */
    private PatientRepository() {}
    
    /**
     * Returns the singleton instance of the {@code PatientRepository}.
     * If the instance does not exist, it will be created.
     *
     * @return The singleton instance of {@code PatientRepository}.
     */
    public static PatientRepository getInstance() {
        if (instance == null) {
            instance = new PatientRepository();
        }
        return instance;
    }
    
    /**
     * Saves a {@code Patient} in the repository.
     * If a patient with the same hospital ID already exists, it will be overwritten.
     *
     * @param patient The {@code Patient} to save.
     * @return The saved {@code Patient}.
     */
    @Override
    public Patient save(Patient patient) {
        patients.put(patient.getHospitalId(), patient);
        return patient;
    }
    
    /**
     * Finds a {@code Patient} by their hospital ID.
     *
     * @param id The hospital ID of the patient to find.
     * @return An {@code Optional} containing the {@code Patient} if found, or an empty {@code Optional} if not.
     */
    @Override
    public Optional<Patient> findById(String id) {
        return Optional.ofNullable(patients.get(id));
    }
    
    /**
     * Retrieves all {@code Patient} entries from the repository.
     *
     * @return A list of all {@code Patient} records.
     */
    @Override
    public List<Patient> findAll() {
        return new ArrayList<>(patients.values());
    }
    
    /**
     * Deletes a {@code Patient} by their hospital ID.
     *
     * @param id The hospital ID of the patient to delete.
     */
    @Override
    public void delete(String id) {
        patients.remove(id);
    }
    
    /**
     * Checks if a {@code Patient} with the specified hospital ID exists in the repository.
     *
     * @param id The hospital ID of the patient to check for existence.
     * @return {@code true} if the {@code Patient} exists, {@code false} otherwise.
     */
    @Override
    public boolean exists(String id) {
        return patients.containsKey(id);
    }

    /**
     * Clears all {@code Patient} records from the repository.
     */
    @Override
    public void clearAll() {
        patients.clear();
    }
}

