//repository/DataImportManager.java

package repository;

import service.*;
import entity.*;
import controller.AuthenticationController;
import java.io.IOException;
import java.util.List;

/**
 * The {@code DataImportManager} class handles the clearing and importing of data for medicines, patients, and staff.
 * It utilizes specific import services for each data type and manages the process of saving the imported data to the respective repositories.
 */
public class DataImportManager {
    private final MedicineImportService medicineImportService;
    private final PatientImportService patientImportService;
    private final StaffImportService staffImportService;
    private final AuthenticationController authController;
    
    /**
     * Constructs a {@code DataImportManager} with the specified {@code AuthenticationController}.
     * Initializes the import services for medicine, patient, and staff data.
     *
     * @param authController The authentication controller for managing user-related data.
     */
    public DataImportManager(AuthenticationController authController) {
        this.medicineImportService = new MedicineImportService();
        this.patientImportService = new PatientImportService();
        this.staffImportService = new StaffImportService();
        this.authController = authController;
    }
    
    /**
     * Clears all existing data from the repositories, including medicines, patients, staff, medical records, 
     * and user authentication data.
     * Logs the progress of the data clearing.
     */
    public void clearAllData() {
        System.out.println("Clearing existing data...");
        MedicineRepository.getInstance().clearAll();
        PatientRepository.getInstance().clearAll();
        StaffRepository.getInstance().clearAll();
        MedicalRecordRepository.getInstance().clearAll();
        authController.clearAllUsers();
        System.out.println("All data cleared successfully.");
    }
    
    /**
     * Imports all data from the specified files: medicines, patients, and staff.
     * The method first clears existing data and then proceeds to import each data type.
     * For each data type, the method attempts to import the data and handle any errors that may occur.
     *
     * @param medicineFile The file containing medicine data to be imported.
     * @param patientFile The file containing patient data to be imported.
     * @param staffFile The file containing staff data to be imported.
     * @throws IOException If an error occurs while reading the input files.
     */
    public void importAllData(String medicineFile, String patientFile, String staffFile) 
            throws IOException {
        // Clear existing data first
        clearAllData();
        
        System.out.println("Starting data import...");
        
        // Import medicines first
        try {
            List<Medicine> medicines = medicineImportService.importData(medicineFile);
            MedicineRepository medicineRepo = MedicineRepository.getInstance();
            for (Medicine medicine : medicines) {
                medicineRepo.save(medicine);
            }
            System.out.println("Imported " + medicines.size() + " medicines");
        } catch (Exception e) {
            System.err.println("Error importing medicines: " + e.getMessage());
        }
        
        // Import staff next
        try {
            List<User> staff = staffImportService.importData(staffFile);
            StaffRepository staffRepo = StaffRepository.getInstance();
            for (User staffMember : staff) {
                staffRepo.save(staffMember);
                authController.addUser(staffMember);
            }
            System.out.println("Imported " + staff.size() + " staff members");
        } catch (Exception e) {
            System.err.println("Error importing staff: " + e.getMessage());
        }
        
        // Import patients last
        try {
            List<Patient> patients = patientImportService.importData(patientFile);
            PatientRepository patientRepo = PatientRepository.getInstance();
            MedicalRecordRepository medicalRecordRepo = MedicalRecordRepository.getInstance();
            for (Patient patient : patients) {
                patientRepo.save(patient);
                medicalRecordRepo.save(patient.getMedicalRecord());
                authController.addUser(patient);
            }
            System.out.println("Imported " + patients.size() + " patients");
        } catch (Exception e) {
            System.err.println("Error importing patients: " + e.getMessage());
        }
        
        System.out.println("Data import completed.");
    }
}
