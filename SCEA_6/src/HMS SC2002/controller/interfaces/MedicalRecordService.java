//controller/interfaces/MedicalRecordService.java

package controller.interfaces;

import java.util.List;

import entity.Diagnosis;
import entity.MedicalRecord;

/**
 * Interface for managing medical records of patients.
 */
public interface MedicalRecordService {

    /**
     * Retrieves the medical record for a specific patient.
     *
     * @param patientId The ID of the patient whose medical record is to be retrieved.
     * @return The MedicalRecord object associated with the given patient ID.
     */
    MedicalRecord getMedicalRecord(String patientId);

    /**
     * Updates the contact information (phone and email) for a specific patient.
     *
     * @param patientId The ID of the patient whose contact information is being updated.
     * @param phone     The new phone number for the patient.
     * @param email     The new email address for the patient.
     */
    void updateContactInfo(String patientId, String phone, String email);

    /**
     * Adds a new diagnosis entry to a patient's medical record.
     *
     * @param patientId The ID of the patient to whom the diagnosis is being added.
     * @param diagnosis The diagnosis description for the patient.
     * @param treatment The prescribed treatment for the diagnosis.
     */
    void addDiagnosis(String patientId, String diagnosis, String treatment);

    /**
     * Retrieves the history of diagnoses for a specific patient.
     *
     * @param patientId The ID of the patient whose diagnosis history is being retrieved.
     * @return A list of Diagnosis objects representing the patient's past diagnoses.
     */
    List<Diagnosis> getDiagnosisHistory(String patientId);
}
