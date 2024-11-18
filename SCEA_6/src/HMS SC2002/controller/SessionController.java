//controller/SessionController.java

package controller;

import entity.*;
import entity.enums.UserRole;
import repository.*;

public class SessionController {
    private static SessionController instance;
    private User currentUser;
    
    // Repositories
    private final PatientRepository patientRepository;
    private final StaffRepository staffRepository;
    private final AppointmentRepository appointmentRepository;
    private final MedicineRepository medicineRepository;
    
    /**
     * Private constructor for the SessionController, initializes repositories.
     */
    private SessionController() {
        this.patientRepository = PatientRepository.getInstance();
        this.staffRepository = StaffRepository.getInstance();
        this.appointmentRepository = AppointmentRepository.getInstance();
        this.medicineRepository = MedicineRepository.getInstance();
    }
    
    /**
     * Returns the singleton instance of the SessionController.
     * 
     * @return The single instance of the SessionController.
     */
    public static SessionController getInstance() {
        if (instance == null) {
            instance = new SessionController();
        }
        return instance;
    }
    
    /**
     * Sets the current logged-in user.
     * 
     * @param user The user to be set as the current user.
     * @throws IllegalArgumentException if the user is null.
     */
    public void setCurrentUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        this.currentUser = user;
    }
    
    /**
     * Clears the current user, effectively logging them out.
     */
    public void clearCurrentUser() {
        this.currentUser = null;
    }
    
    /**
     * Attempts to log in a user using their hospital ID and password.
     * 
     * @param hospitalId The hospital ID of the user.
     * @param password The password of the user.
     * @return true if the login is successful, false otherwise.
     */
    public boolean login(String hospitalId, String password) {
        if (hospitalId == null || password == null) {
            return false;
        }
        
        // First check staff repository
        User user = staffRepository.findById(hospitalId)
            .orElseGet(() -> patientRepository.findById(hospitalId).orElse(null));
            
        if (user != null && user.validatePassword(password)) {
            setCurrentUser(user);
            return true;
        }
        return false;
    }
    
    /**
     * Logs out the current user by clearing the current user session.
     */
    public void logout() {
        clearCurrentUser();
    }
    
    /**
     * Retrieves the current logged-in user.
     * 
     * @return The current logged-in user, or null if no user is logged in.
     */
    public User getCurrentUser() {
        return currentUser;
    }
    
    /**
     * Checks if there is a user currently logged in.
     * 
     * @return true if a user is logged in, false otherwise.
     */
    public boolean isLoggedIn() {
        return currentUser != null;
    }
    
    /**
     * Retrieves the role of the current logged-in user.
     * 
     * @return The role of the current user (Doctor, Patient, Pharmacist, Administrator),
     *         or null if no user is logged in.
     */
    public UserRole getCurrentUserRole() {
        if (currentUser == null) {
            return null;
        }
        
        if (currentUser instanceof Doctor) return UserRole.DOCTOR;
        if (currentUser instanceof Patient) return UserRole.PATIENT;
        if (currentUser instanceof Pharmacist) return UserRole.PHARMACIST;
        if (currentUser instanceof Administrator) return UserRole.ADMINISTRATOR;
        return null;
    }
    
    /**
     * Checks if the current user has the required role.
     * 
     * @param requiredRole The role to check for.
     * @return true if the current user has the required role, false otherwise.
     */
    public boolean isUserAuthorized(UserRole requiredRole) {
        UserRole currentRole = getCurrentUserRole();
        return currentRole != null && currentRole == requiredRole;
    }
    
    /**
     * Validates the session of the current user.
     * 
     * @return true if the user is logged in and exists in the repository, false otherwise.
     */
    public boolean validateSession() {
        if (!isLoggedIn()) {
            return false;
        }
        
        // Verify user still exists in repository
        String id = currentUser.getHospitalId();
        if (currentUser instanceof Patient) {
            return patientRepository.exists(id);
        } else {
            return staffRepository.exists(id);
        }
    }
    
    /**
     * Refreshes the current user's data from the repository to ensure they are up-to-date.
     * If the user no longer exists in the repository, the session is cleared.
     */
    public void refreshCurrentUser() {
        if (currentUser == null) {
            return;
        }
        
        String id = currentUser.getHospitalId();
        User refreshedUser;
        
        if (currentUser instanceof Patient) {
            refreshedUser = patientRepository.findById(id).orElse(null);
        } else {
            refreshedUser = staffRepository.findById(id).orElse(null);
        }
        
        if (refreshedUser != null) {
            setCurrentUser(refreshedUser);
        } else {
            clearCurrentUser();
        }
    }
}
