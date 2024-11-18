//repository/StaffRepository.java

package repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import entity.Doctor;
import entity.Pharmacist;
import entity.User;

/**
 * {@code StaffRepository} is a singleton class responsible for managing staff members, 
 * including both doctors and pharmacists. It implements the {@link Repository} interface
 * for managing {@link User} entities, allowing for CRUD operations such as saving, finding, 
 * deleting, and checking the existence of staff members.
 */
public class StaffRepository implements Repository<User, String> {
    
    private final Map<String, User> staff = new ConcurrentHashMap<>();
    
    private static StaffRepository instance;
    
    /**
     * Private constructor to prevent direct instantiation.
     */
    private StaffRepository() {}
    
    /**
     * Gets the singleton instance of {@code StaffRepository}.
     * 
     * @return The singleton instance of the repository.
     */
    public static StaffRepository getInstance() {
        if (instance == null) {
            instance = new StaffRepository();
        }
        return instance;
    }
    
    /**
     * Saves the given staff member to the repository.
     * 
     * @param user The staff member to be saved (could be a {@link Doctor} or {@link Pharmacist}).
     * @return The saved staff member.
     */
    @Override
    public User save(User user) {
        staff.put(user.getHospitalId(), user);
        return user;
    }
    
    /**
     * Finds a staff member by their unique hospital ID.
     * 
     * @param id The hospital ID of the staff member.
     * @return An {@link Optional} containing the staff member if found, or an empty {@link Optional} if not found.
     */
    @Override
    public Optional<User> findById(String id) {
        return Optional.ofNullable(staff.get(id));
    }
    
    /**
     * Retrieves all staff members in the repository.
     * 
     * @return A list containing all staff members.
     */
    @Override
    public List<User> findAll() {
        return new ArrayList<>(staff.values());
    }
    
    /**
     * Deletes a staff member from the repository by their unique hospital ID.
     * 
     * @param id The hospital ID of the staff member to be deleted.
     */
    @Override
    public void delete(String id) {
        staff.remove(id);
    }
    
    /**
     * Checks if a staff member exists in the repository by their unique hospital ID.
     * 
     * @param id The hospital ID of the staff member to check.
     * @return {@code true} if the staff member exists, {@code false} otherwise.
     */
    @Override
    public boolean exists(String id) {
        return staff.containsKey(id);
    }
    
    /**
     * Retrieves a list of all doctors in the repository.
     * 
     * @return A list containing all doctors in the staff.
     */
    public List<Doctor> findAllDoctors() {
        return staff.values().stream()
            .filter(user -> user instanceof Doctor)
            .map(user -> (Doctor) user)
            .toList();
    }
    
    /**
     * Retrieves a list of all pharmacists in the repository.
     * 
     * @return A list containing all pharmacists in the staff.
     */
    public List<Pharmacist> findAllPharmacists() {
        return staff.values().stream()
            .filter(user -> user instanceof Pharmacist)
            .map(user -> (Pharmacist) user)
            .toList();
    }
    
    /**
     * Clears all staff members from the repository.
     * This will remove all saved staff data.
     */
    @Override
    public void clearAll() {
        staff.clear();
    }
}
