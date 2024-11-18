//entity/MedicalRecord.java
package entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import entity.enums.*;

/**
 * Represents a patient's medical record, including personal details, 
 * contact information, and diagnosis history.
 */
public class MedicalRecord {
    
    /** Unique identifier for the patient. */
    private final String patientId;
    
    /** Name of the patient. */
    private final String name;
    
    /** Patient's date of birth. */
    private final LocalDate dateOfBirth;
    
    /** Gender of the patient. */
    private final Gender gender;
    
    /** Blood type of the patient. */
    private final BloodType bloodType;
    
    /** Contact information for the patient. */
    private final ContactInfo contactInfo;
    
    /** List of diagnoses in the patient's medical history. */
    private final List<Diagnosis> diagnosisHistory;

    /**
     * Constructs a MedicalRecord with the specified patient details.
     * 
     * @param patientId Unique identifier for the patient.
     * @param name Name of the patient.
     * @param dateOfBirth Date of birth of the patient.
     * @param gender Gender of the patient.
     * @param bloodType Blood type of the patient.
     * @param contactInfo Contact information for the patient.
     */
    public MedicalRecord(String patientId, String name, LocalDate dateOfBirth, 
                        Gender gender, BloodType bloodType, ContactInfo contactInfo) {
        this.patientId = patientId;
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.bloodType = bloodType;
        this.contactInfo = contactInfo;
        this.diagnosisHistory = new ArrayList<>();
    }

    /**
     * Adds a diagnosis to the patient's medical record history.
     * 
     * @param diagnosis The diagnosis to be added.
     */
    public void addDiagnosis(Diagnosis diagnosis) {
        diagnosisHistory.add(diagnosis);
    }
    
    /**
     * Returns a formatted string representation of the patient's medical record.
     * 
     * @return A string containing the patient's details and diagnosis history.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Patient ID: %s\n", patientId));
        sb.append(String.format("Name: %s\n", name));
        sb.append(String.format("Date of Birth: %s\n", dateOfBirth));
        sb.append(String.format("Gender: %s\n", gender));
        sb.append(String.format("Blood Type: %s\n", bloodType));
        sb.append(String.format("Contact Information:\n%s\n", contactInfo));
        sb.append("\nDiagnosis History:\n");
        diagnosisHistory.forEach(d -> sb.append(d.toString()).append("\n"));
        return sb.toString();
    }

    /**
     * Gets the patient's unique identifier.
     * 
     * @return The patient ID.
     */
    public String getPatientId() {
        return patientId;
    }

    /**
     * Gets the name of the patient.
     * 
     * @return The patient's name.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the patient's date of birth.
     * 
     * @return The patient's date of birth.
     */
    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    /**
     * Gets the gender of the patient.
     * 
     * @return The patient's gender.
     */
    public Gender getGender() {
        return gender;
    }

    /**
     * Gets the blood type of the patient.
     * 
     * @return The patient's blood type.
     */
    public BloodType getBloodType() {
        return bloodType;
    }

    /**
     * Gets the contact information of the patient.
     * 
     * @return The patient's contact information.
     */
    public ContactInfo getContactInfo() {
        return contactInfo;
    }

    /**
     * Gets the list of diagnoses in the patient's medical record history.
     * 
     * @return The list of diagnoses in the patient's history.
     */
    public List<Diagnosis> getDiagnosisHistory() {
        return diagnosisHistory;
    }
}
