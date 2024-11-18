//controller/interfaces/AuthenticationService.java

package controller.interfaces;

import entity.*;

/**
 * Interface defining methods for authentication services, including login and password management.
 */
public interface AuthenticationService {

    /**
     * Authenticates a user using their hospital ID and password.
     *
     * @param hospitalId The unique hospital ID of the user.
     * @param password   The password associated with the hospital ID.
     * @return The authenticated User object if login is successful, or null if authentication fails.
     */
    User login(String hospitalId, String password);

    /**
     * Changes the password of a user after verifying the old password.
     *
     * @param user        The User requesting a password change.
     * @param oldPassword The current password of the user.
     * @param newPassword The new password to set for the user.
     * @return true if the password was successfully changed, false otherwise.
     */
    boolean changePassword(User user, String oldPassword, String newPassword);
}
