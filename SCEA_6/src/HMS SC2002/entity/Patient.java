//entity/Patient.java

package entity;

/**
 * Represents a patient in the system, extending the User class and containing a medical record.
 */
public class Patient extends User {
    
    /** The medical record associated with the patient. */
    private MedicalRecord medicalRecord;
    
    /**
     * Constructs a Patient with specified hospital ID, password, name, and medical record.
     *
     * @param hospitalId The unique ID of the patient within the hospital system.
     * @param password The patient's password for authentication.
     * @param name The name of the patient.
     * @param medicalRecord The medical record associated with the patient.
     */
    public Patient(String hospitalId, String password, String name, MedicalRecord medicalRecord) {
        super(hospitalId, password, name);
        this.medicalRecord = medicalRecord;
    }
    
    /**
     * Gets the medical record of the patient.
     *
     * @return The patient's medical record.
     */
    public MedicalRecord getMedicalRecord() {
        return medicalRecord;
    }
    
    /**
     * Sets a new medical record for the patient.
     *
     * @param medicalRecord The new medical record to associate with the patient.
     */
    public void setMedicalRecord(MedicalRecord medicalRecord) {
        this.medicalRecord = medicalRecord;
    }
}
