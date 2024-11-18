//entity/User.java

// Base user class that other roles will inherit from
package entity;

/**
 * The {@code User} class is an abstract base class that represents a user in the system.
 * It contains common attributes and behaviors shared by all types of users (e.g., doctors, pharmacists).
 * Other roles will inherit from this class.
 */
public abstract class User {
    
    /** The hospital ID of the user. */
    protected final String hospitalId;
    
    /** The password for the user account. */
    protected String password;
    
    /** The name of the user. */
    protected String name;
    
    /**
     * Constructs a {@code User} with the specified hospital ID, password, and name.
     * 
     * @param hospitalId The hospital ID of the user.
     * @param password The password for the user account.
     * @param name The name of the user.
     */
    public User(String hospitalId, String password, String name) {
        this.hospitalId = hospitalId;
        this.password = password;
        this.name = name;
    }
    
    /**
     * Validates the user's password.
     * 
     * @param inputPassword The password to validate.
     * @return {@code true} if the input password matches the stored password, {@code false} otherwise.
     */
    public boolean validatePassword(String inputPassword) {
        return this.password.equals(inputPassword);
    }
    
    /**
     * Changes the user's password if the old password is correct and the new password is valid.
     * 
     * @param oldPassword The current password to verify.
     * @param newPassword The new password to set.
     * @return {@code true} if the password was changed successfully, {@code false} otherwise.
     */
    public boolean changePassword(String oldPassword, String newPassword) {
        if (!validatePassword(oldPassword) || newPassword.length() < 8 || oldPassword.equals(newPassword)) {
            return false;
        }
        this.password = newPassword;
        return true;
    }

    /**
     * Gets the hospital ID of the user.
     * 
     * @return The hospital ID.
     */
    public String getHospitalId() {
        return hospitalId;
    }

    /**
     * Gets the password of the user.
     * 
     * @return The password.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets a new password for the user.
     * 
     * @param password The new password.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Gets the name of the user.
     * 
     * @return The name of the user.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets a new name for the user.
     * 
     * @param name The new name.
     */
    public void setName(String name) {
        this.name = name;
    }
}
