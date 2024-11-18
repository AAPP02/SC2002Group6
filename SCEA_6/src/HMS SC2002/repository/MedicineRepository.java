//repository/MedicineRepository.java
package repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import entity.Medicine;

/**
 * The {@code MedicineRepository} class is responsible for managing a collection of medicine records.
 * It allows for adding, retrieving, deleting, and querying medicines based on their properties.
 * This class provides methods for finding medicines by name, checking for existence, and finding low stock items.
 * It also supports clearing all records and provides a singleton pattern for global access.
 */
public class MedicineRepository implements Repository<Medicine, String> {
    private final Map<String, Medicine> medicines = new ConcurrentHashMap<>();
    
    private static MedicineRepository instance;
    
    /**
     * Private constructor to initialize the medicine repository.
     * Uses a {@code ConcurrentHashMap} to store medicine records.
     */
    private MedicineRepository() {}
    
    /**
     * Returns the singleton instance of the {@code MedicineRepository}.
     * If the instance does not exist, it will be created.
     *
     * @return The singleton instance of {@code MedicineRepository}.
     */
    public static MedicineRepository getInstance() {
        if (instance == null) {
            instance = new MedicineRepository();
        }
        return instance;
    }
    
    /**
     * Saves a {@code Medicine} in the repository.
     * If a medicine with the same name already exists, it will be overwritten.
     *
     * @param medicine The {@code Medicine} to save.
     * @return The saved {@code Medicine}.
     */
    @Override
    public Medicine save(Medicine medicine) {
        medicines.put(medicine.getName(), medicine);
        return medicine;
    }
    
    /**
     * Finds a {@code Medicine} by its name.
     *
     * @param name The name of the medicine to find.
     * @return An {@code Optional} containing the {@code Medicine} if found, or an empty {@code Optional} if not.
     */
    @Override
    public Optional<Medicine> findById(String name) {
        return Optional.ofNullable(medicines.get(name));
    }
    
    /**
     * Retrieves all {@code Medicine} entries from the repository.
     *
     * @return A list of all {@code Medicine} records.
     */
    @Override
    public List<Medicine> findAll() {
        return new ArrayList<>(medicines.values());
    }
    
    /**
     * Deletes a {@code Medicine} by its name.
     *
     * @param name The name of the medicine to delete.
     */
    @Override
    public void delete(String name) {
        medicines.remove(name);
    }
    
    /**
     * Checks if a {@code Medicine} with the specified name exists in the repository.
     *
     * @param name The name of the medicine to check for existence.
     * @return {@code true} if the {@code Medicine} exists, {@code false} otherwise.
     */
    @Override
    public boolean exists(String name) {
        return medicines.containsKey(name);
    }

    /**
     * Finds a {@code Medicine} by its name (case-insensitive).
     *
     * @param name The name of the medicine to search for.
     * @return An {@code Optional} containing the {@code Medicine} if found, or an empty {@code Optional} if not.
     */
    public Optional<Medicine> findByName(String name) {
        return findAll().stream()
            .filter(medicine -> medicine.getName().equalsIgnoreCase(name))
            .findFirst();
    }
    
    /**
     * Finds all medicines that have low stock based on their stock level and low stock alert threshold.
     *
     * @return A list of {@code Medicine} entries that are in low stock.
     */
    public List<Medicine> findLowStock() {
        return medicines.values().stream()
            .filter(medicine -> medicine.getCurrentStock() <= medicine.getLowStockAlert())
            .toList();
    }

    /**
     * Clears all {@code Medicine} records from the repository.
     */
    @Override
    public void clearAll() {
        medicines.clear();
    }
}
