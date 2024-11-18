
//HospitalManagementSystem.java
package system;
import boundary.*;
import controller.*;
import entity.Administrator;
import entity.Doctor;
import entity.Patient;
import entity.Pharmacist;
import entity.User;
import repository.DataImportManager;

import java.nio.file.*;
import java.util.Scanner;

/**
 * The main class for the Hospital Management System.
 * This class initializes the system and handles the overall workflow
 * of loading data, user authentication, and presenting the appropriate user interfaces.
 */
public class HospitalManagementSystem {
    private final Scanner scanner;
    private final LoginUI loginUI;
    private final PatientUI patientUI;
    private final DoctorUI doctorUI;
    private final PharmacistUI pharmacistUI;
    private final AdministratorUI administratorUI;
    private final DataImportManager dataImportManager;
    private final AuthenticationController authController;

    /**
     * Constructor for the HospitalManagementSystem class.
     * Initializes necessary components for the system, including the user interface
     * components, controllers, and data import manager.
     */
    public HospitalManagementSystem() {
        this.scanner = new Scanner(System.in);

        // Initialize AuthenticationController first
        this.authController = new AuthenticationController();

        // Initialize other controllers
        DoctorAvailabilityController availabilityController = new DoctorAvailabilityController();
        AppointmentController appointmentController = new AppointmentController(availabilityController);
        MedicalRecordController medicalRecordController = new MedicalRecordController();
        PatientController patientController = new PatientController(appointmentController);

        // Initialize UIs with the same AuthenticationController instance
        this.loginUI = new LoginUI(scanner, authController);
        this.patientUI = new PatientUI(scanner, authController, appointmentController, 
            medicalRecordController, patientController);
        this.doctorUI = new DoctorUI(scanner, authController, appointmentController, 
            medicalRecordController, availabilityController);
        this.pharmacistUI = new PharmacistUI(scanner, authController, appointmentController);
        this.administratorUI = new AdministratorUI(scanner, authController, appointmentController);

        // Initialize DataImportManager with the same AuthenticationController instance
        this.dataImportManager = new DataImportManager(authController);
    }

    /**
     * Loads the initial data from CSV files into the system.
     * This method creates necessary directories if they do not exist and imports
     * data for medicines, patients, and staff.
     */
    private void loadInitialData() {
        try {
            // Create data directory if it doesn't exist
            Path dataDir = Paths.get("data");
            if (!Files.exists(dataDir)) {
                Files.createDirectories(dataDir);
            }

            System.out.println("Loading initial data...");
            dataImportManager.importAllData(
                "data/Medicine_List.csv",
                "data/Patient_List.csv",
                "data/Staff_List.csv"
            );

        } catch (Exception e) {
            System.err.println("Error loading initial data: " + e.getMessage());
            e.printStackTrace();
            System.err.println("Starting with empty system...");
        }
    }

    /**
     * Starts the Hospital Management System.
     * This method handles the overall system flow, including user login, user interaction,
     * and displaying the appropriate user interfaces for each user type.
     */
    public void start() {
        System.out.println("Initializing Hospital Management System...");
        loadInitialData();

        // Main loop for handling user interactions
        while (true) {
            User user = loginUI.show();
            if (user == null) {
                break;
            }

            // Set current user session
            SessionController.getInstance().setCurrentUser(user);

            // Switch between different user roles and show corresponding UI
            switch (user) {
                case Patient patient -> patientUI.show(patient);
                case Doctor doctor -> doctorUI.show(doctor);
                case Pharmacist pharmacist -> pharmacistUI.show(pharmacist);
                case Administrator admin -> administratorUI.show(admin);
                default -> System.out.println("Unknown user type!");
            }

            // Clear current user session after interaction
            SessionController.getInstance().clearCurrentUser();
        }

        scanner.close();
        System.out.println("System shutdown complete. Goodbye!");
    }

    /**
     * Main method to start the Hospital Management System.
     * Initializes the system and begins the user interaction process.
     *
     * @param args Command line arguments (not used).
     */
    public static void main(String[] args) {
        HospitalManagementSystem hms = new HospitalManagementSystem();
        hms.start();
    }
}
