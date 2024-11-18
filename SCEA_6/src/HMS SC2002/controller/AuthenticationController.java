//controller/AuthenticationController.java

package controller;

import controller.interfaces.AuthenticationService;
import entity.*;
import repository.*;
import java.util.Optional;

/**
 * Controller responsible for handling user authentication-related operations
 * such as login, logout, password changes, and user management.
 */
public class AuthenticationController implements AuthenticationService {
    private final StaffRepository staffRepository;
    private final PatientRepository patientRepository;
    private User currentUser;
    
    /**
     * Constructor to initialize the AuthenticationController with the necessary repositories.
     */
    public AuthenticationController() {
        this.staffRepository = StaffRepository.getInstance();
        this.patientRepository = PatientRepository.getInstance();
    }

    /**
     * Clears all users from the repositories and resets the current user.
     */
    public void clearAllUsers() {
        staffRepository.clearAll();
        patientRepository.clearAll();
        currentUser = null;
    }

    /**
     * Adds a new user (Patient or Staff) to the appropriate repository.
     * If the user is a Patient, they are added to the patient repository, 
     * otherwise to the staff repository.
     * 
     * @param user The user to be added to the repository.
     * @throws IllegalArgumentException if the user is null.
     */
    public void addUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        
        // Add or update in appropriate repository
        if (user instanceof Patient) {
            patientRepository.save((Patient) user);
        } else {
            staffRepository.save(user);
        }
    }

    /**
     * Logs in a user using their hospital ID and password. 
     * The system first checks the staff repository and then the patient repository.
     * 
     * @param hospitalId The hospital ID of the user.
     * @param password The password provided by the user.
     * @return The logged-in User if credentials are correct, or null if login fails.
     */
    @Override
    public User login(String hospitalId, String password) {
        // First check staff repository
        Optional<User> staffUser = staffRepository.findById(hospitalId);
        if (staffUser.isPresent()) {
            User user = staffUser.get();
            if (user.validatePassword(password)) {
                currentUser = user;
                return user;
            }
            return null;
        }
        
        // Then check patient repository
        Optional<Patient> patientUser = patientRepository.findById(hospitalId);
        if (patientUser.isPresent()) {
            Patient patient = patientUser.get();
            if (patient.validatePassword(password)) {
                currentUser = patient;
                return patient;
            }
        }
        
        return null;
    }
    
    /**
     * Changes the password for a user, provided the old password matches the current one.
     * The password change is only applied if successful.
     * 
     * @param user The user whose password is being changed.
     * @param oldPassword The current password of the user.
     * @param newPassword The new password to set for the user.
     * @return true if the password was successfully changed, false otherwise.
     */
    @Override
    public boolean changePassword(User user, String oldPassword, String newPassword) {
        // Validate input
        if (user == null || oldPassword == null || newPassword == null) {
            return false;
        }
        
        // Attempt password change
        boolean changed = user.changePassword(oldPassword, newPassword);
        
        // If successful, update in appropriate repository
        if (changed) {
            if (user instanceof Patient) {
                patientRepository.save((Patient) user);
            } else {
                staffRepository.save(user);
            }
        }
        
        return changed;
    }
    
    /**
     * Retrieves the currently logged-in user.
     * 
     * @return The current User if logged in, or null if no user is logged in.
     */
    public User getCurrentUser() {
        return currentUser;
    }
    
    /**
     * Logs out the current user by setting the currentUser to null.
     */
    public void logout() {
        currentUser = null;
    }
    
    /**
     * Checks if a user is currently logged in.
     * 
     * @return true if a user is logged in, false otherwise.
     */
    public boolean isLoggedIn() {
        return currentUser != null;
    }
    
    /**
     * Gets the type of the current logged-in user, such as "Doctor", "Patient", etc.
     * 
     * @return A string representing the type of the current user, or null if no user is logged in.
     */
    public String getUserType() {
        if (currentUser == null) {
            return null;
        }
        
        if (currentUser instanceof Doctor) return "Doctor";
        if (currentUser instanceof Patient) return "Patient";
        if (currentUser instanceof Pharmacist) return "Pharmacist";
        if (currentUser instanceof Administrator) return "Administrator";
        
        return "Unknown";
    }
    
    /**
     * Checks if a user exists in either the staff or patient repositories.
     * 
     * @param hospitalId The hospital ID to check for existence.
     * @return true if the user exists, false otherwise.
     */
    public boolean userExists(String hospitalId) {
        return staffRepository.exists(hospitalId) || patientRepository.exists(hospitalId);
    }
   
    /**
     * Removes a user from either the staff or patient repository.
     * 
     * @param hospitalId The hospital ID of the user to be removed.
     * @return true if the user was successfully removed, false otherwise.
     */
    public boolean removeUser(String hospitalId) {
        if (staffRepository.exists(hospitalId)) {
            staffRepository.delete(hospitalId);
            return true;
        }
        if (patientRepository.exists(hospitalId)) {
            patientRepository.delete(hospitalId);
            return true;
        }
        return false;
    }
    
    /**
     * Retrieves a user (either staff or patient) by their hospital ID.
     * 
     * @param hospitalId The hospital ID of the user to retrieve.
     * @return An Optional containing the User if found, or empty if no user exists with the given ID.
     */
    public Optional<User> getUser(String hospitalId) {
        Optional<User> staffUser = staffRepository.findById(hospitalId);
        if (staffUser.isPresent()) {
            return staffUser;
        }
        return patientRepository.findById(hospitalId).map(patient -> (User) patient);
    }
    
    /**
     * Resets the password of a user to a default password ("password").
     * This is typically used for password recovery or reset purposes.
     * 
     * @param hospitalId The hospital ID of the user whose password is to be reset.
     * @return true if the password was successfully reset, false if the user was not found.
     */
    public boolean resetPassword(String hospitalId) {
        Optional<User> user = getUser(hospitalId);
        if (user.isPresent()) {
            User u = user.get();
            u.setPassword("password"); // Default password
            
            if (u instanceof Patient) {
                patientRepository.save((Patient) u);
            } else {
                staffRepository.save(u);
            }
            return true;
        }
        return false;
    }
}
