//entity/Doctor.java

package entity;

import java.time.LocalDate;
import java.util.*;

/**
 * Represents a doctor in the healthcare system with a specialization,
 * a list of patients, and availability for appointments.
 */
public class Doctor extends User {

    /** The specialization of the doctor (e.g., cardiology, dermatology). */
    private final String specialization;
    
    /** The list of patients assigned to this doctor. */
    private final List<Patient> patients;
    
    /** The doctor's availability for appointments, mapped by date. */
    private final Map<LocalDate, DoctorAvailability> availabilities;

    /**
     * Constructs a new Doctor with the specified hospital ID, password, name, and specialization.
     * 
     * @param hospitalId The unique identifier for the doctor.
     * @param password The password for the doctor's account.
     * @param name The name of the doctor.
     * @param specialization The medical specialization of the doctor.
     */
    public Doctor(String hospitalId, String password, String name, String specialization) {
        super(hospitalId, password, name);
        this.specialization = specialization;
        this.patients = new ArrayList<>();
        this.availabilities = new HashMap<>();
    }

    /**
     * Retrieves the doctor's availability for a specific date.
     * 
     * @param date The date for which availability is requested.
     * @return The availability for the specified date, or null if unavailable.
     */
    public DoctorAvailability getAvailability(LocalDate date) {
        return availabilities.get(date);
    }

    /**
     * Sets the doctor's availability for a specific date.
     * 
     * @param availability The availability to set for the doctor.
     */
    public void setAvailability(DoctorAvailability availability) {
        availabilities.put(availability.getDate(), availability);
    }

    /**
     * Adds a patient to the doctor's list of patients if not already assigned.
     * 
     * @param patient The patient to add to the doctor's care.
     */
    public void addPatient(Patient patient) {
        if (!patients.contains(patient)) {
            patients.add(patient);
        }
    }

    /**
     * Retrieves the list of patients under the doctor's care.
     * 
     * @return A list of the doctor's patients.
     */
    public List<Patient> getPatients() {
        return new ArrayList<>(patients); // Return a copy to maintain encapsulation
    }

    /**
     * Gets the medical specialization of the doctor.
     * 
     * @return The doctor's specialization.
     */
    public String getSpecialization() {
        return specialization;
    }

    /**
     * Retrieves the doctor's availability map, with each date associated with an availability record.
     * 
     * @return A map of dates to availability records.
     */
    public Map<LocalDate, DoctorAvailability> getAvailabilities() {
        return availabilities;
    }
}
