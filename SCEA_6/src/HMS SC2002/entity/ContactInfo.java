//entity/ContactInfo.java

package entity;

/**
 * Represents the contact information for a user, including phone number and email address.
 */
public class ContactInfo {
    
    /** The phone number associated with the contact information. */
    private String phoneNumber;

    /** The email address associated with the contact information. */
    private String email;

    /**
     * Constructs a new ContactInfo object with the specified phone number and email address.
     * 
     * @param phoneNumber The phone number to be associated with this contact.
     * @param email The email address to be associated with this contact.
     */
    public ContactInfo(String phoneNumber, String email) {
        this.phoneNumber = phoneNumber;
        this.email = email;
    }

    /**
     * Updates the contact information with a new phone number and email address.
     * 
     * @param phoneNumber The new phone number.
     * @param email The new email address.
     */
    public void updateContactInfo(String phoneNumber, String email) {
        this.phoneNumber = phoneNumber;
        this.email = email;
    }

    /**
     * Returns a string representation of the contact information.
     * 
     * @return A string in the format "Phone: [phoneNumber], Email: [email]".
     */
    @Override
    public String toString() {
        return String.format("Phone: %s, Email: %s", phoneNumber, email);
    }

    /**
     * Returns the phone number associated with this contact information.
     * 
     * @return The phone number.
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Sets a new phone number for this contact information.
     * 
     * @param phoneNumber The new phone number.
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * Returns the email address associated with this contact information.
     * 
     * @return The email address.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets a new email address for this contact information.
     * 
     * @param email The new email address.
     */
    public void setEmail(String email) {
        this.email = email;
    }
}
