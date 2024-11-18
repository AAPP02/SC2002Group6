//controller/MedicalRecordController.java

package controller;

import java.util.*;

import controller.interfaces.MedicalRecordService;
import entity.*;
import entity.enums.BloodType;
import repository.MedicalRecordRepository;
import repository.PatientRepository;

public class MedicalRecordController implements MedicalRecordService {
    private final MedicalRecordRepository medicalRecordRepository;
    private final PatientRepository patientRepository;
    
    /**
     * Constructor to initialize the MedicalRecordController with necessary repositories.
     */
    public MedicalRecordController() {
        this.medicalRecordRepository = MedicalRecordRepository.getInstance();
        this.patientRepository = PatientRepository.getInstance();
    }
    
    /**
     * Retrieves the medical record for a patient based on their ID.
     * 
     * @param patientId The ID of the patient whose medical record is being retrieved.
     * @return The patient's medical record.
     * @throws IllegalArgumentException if no medical record exists for the specified patient.
     */
    @Override
    public MedicalRecord getMedicalRecord(String patientId) {
        return medicalRecordRepository.findById(patientId)
            .orElseThrow(() -> new IllegalArgumentException("Medical record not found for patient: " + patientId));
    }
    
    /**
     * Updates the contact information (phone and email) for a patient's medical record.
     * 
     * @param patientId The ID of the patient whose contact information is being updated.
     * @param phone The new phone number.
     * @param email The new email address.
     * @throws IllegalArgumentException if the phone or email is null or empty.
     */
    @Override
    public void updateContactInfo(String patientId, String phone, String email) {
        // Validate input
        if (phone == null || email == null) {
            throw new IllegalArgumentException("Phone and email cannot be null");
        }
        
        if (phone.trim().isEmpty() || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Phone and email cannot be empty");
        }
        
        // Get and update medical record
        MedicalRecord record = getMedicalRecord(patientId);
        record.getContactInfo().updateContactInfo(phone, email);
        medicalRecordRepository.save(record);
        
        // Update patient's medical record reference as well
        Optional<Patient> patient = patientRepository.findById(patientId);
        if (patient.isPresent()) {
            Patient updatedPatient = patient.get();
            updatedPatient.setMedicalRecord(record);
            patientRepository.save(updatedPatient);
        }
    }

    /**
     * Adds a diagnosis and corresponding treatment to a patient's medical record.
     * 
     * @param patientId The ID of the patient for whom the diagnosis is being added.
     * @param diagnosis A description of the diagnosis.
     * @param treatment A description of the treatment provided.
     * @throws IllegalArgumentException if the diagnosis or treatment is null or empty.
     */
    @Override
    public void addDiagnosis(String patientId, String diagnosis, String treatment) {
        // Validate input
        if (diagnosis == null || treatment == null) {
            throw new IllegalArgumentException("Diagnosis and treatment cannot be null");
        }
        
        if (diagnosis.trim().isEmpty() || treatment.trim().isEmpty()) {
            throw new IllegalArgumentException("Diagnosis and treatment cannot be empty");
        }
        
        MedicalRecord record = getMedicalRecord(patientId);
        
        // Create new Treatment and Diagnosis objects
        Treatment newTreatment = new Treatment(treatment);
        Diagnosis newDiagnosis = new Diagnosis(diagnosis, newTreatment);
        
        // Add to patient's medical record
        record.addDiagnosis(newDiagnosis);
        
        // Save updated record
        medicalRecordRepository.save(record);
        
        // Update patient's medical record reference
        Optional<Patient> patient = patientRepository.findById(patientId);
        if (patient.isPresent()) {
            Patient updatedPatient = patient.get();
            updatedPatient.setMedicalRecord(record);
            patientRepository.save(updatedPatient);
        }
    }
    
    /**
     * Retrieves the full diagnosis history for a patient.
     * 
     * @param patientId The ID of the patient whose diagnosis history is being retrieved.
     * @return A list of diagnoses associated with the patient.
     */
    @Override
    public List<Diagnosis> getDiagnosisHistory(String patientId) {
        MedicalRecord record = getMedicalRecord(patientId);
        return new ArrayList<>(record.getDiagnosisHistory());
    }
    
    /**
     * Adds a new medical record to the system for a specific patient.
     * 
     * @param patientId The ID of the patient for whom the medical record is being added.
     * @param record The medical record to be added.
     * @throws IllegalArgumentException if a medical record already exists for the specified patient.
     */
    public void addMedicalRecord(String patientId, MedicalRecord record) {
        if (medicalRecordRepository.exists(patientId)) {
            throw new IllegalArgumentException("Medical record already exists for patient: " + patientId);
        }
        
        medicalRecordRepository.save(record);
        
        // Update patient's medical record reference
        Optional<Patient> patient = patientRepository.findById(patientId);
        if (patient.isPresent()) {
            Patient updatedPatient = patient.get();
            updatedPatient.setMedicalRecord(record);
            patientRepository.save(updatedPatient);
        }
    }
    
    /**
     * Retrieves all medical records in the system.
     * 
     * @return A list of all medical records.
     */
    public List<MedicalRecord> getAllMedicalRecords() {
        return medicalRecordRepository.findAll();
    }
    
    /**
     * Checks if a medical record exists for a patient based on their ID.
     * 
     * @param patientId The ID of the patient to check.
     * @return true if the medical record exists, false otherwise.
     */
    public boolean hasMedicalRecord(String patientId) {
        return medicalRecordRepository.exists(patientId);
    }
    
    /**
     * Retrieves a list of medical records that match a specified blood type.
     * 
     * @param bloodType The blood type to search for.
     * @return A list of medical records matching the specified blood type.
     */
    public List<MedicalRecord> getMedicalRecordsByBloodType(BloodType bloodType) {
        return medicalRecordRepository.findAll().stream()
            .filter(record -> record.getBloodType() == bloodType)
            .toList();
    }
    
    /**
     * Searches medical records for a specific diagnosis keyword.
     * 
     * @param diagnosisKeyword The keyword to search for in diagnosis descriptions.
     * @return A list of medical records containing the diagnosis keyword.
     * @throws IllegalArgumentException if the search keyword is null or empty.
     */
    public List<MedicalRecord> searchMedicalRecordsByDiagnosis(String diagnosisKeyword) {
        if (diagnosisKeyword == null || diagnosisKeyword.trim().isEmpty()) {
            throw new IllegalArgumentException("Search keyword cannot be null or empty");
        }
        
        String keyword = diagnosisKeyword.toLowerCase().trim();
        
        return medicalRecordRepository.findAll().stream()
            .filter(record -> record.getDiagnosisHistory().stream()
                .anyMatch(diagnosis -> 
                    diagnosis.getDescription().toLowerCase().contains(keyword)))
            .toList();
    }
    
    /**
     * Removes a medical record from the system for a specific patient.
     * 
     * @param patientId The ID of the patient whose medical record is to be removed.
     * @return true if the medical record was successfully removed, false if the record didn't exist.
     */
    public boolean removeMedicalRecord(String patientId) {
        if (!medicalRecordRepository.exists(patientId)) {
            return false;
        }
        
        medicalRecordRepository.delete(patientId);
        
        // Update patient reference
        Optional<Patient> patient = patientRepository.findById(patientId);
        if (patient.isPresent()) {
            Patient updatedPatient = patient.get();
            updatedPatient.setMedicalRecord(null);
            patientRepository.save(updatedPatient);
        }
        
        return true;
    }
}
