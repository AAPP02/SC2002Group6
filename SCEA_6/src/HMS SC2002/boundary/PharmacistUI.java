// boundary/PharmacistUI.java
package boundary;

import controller.*;
import entity.*;
import entity.enums.*;
import repository.*;
import java.util.*;

/**
 * User interface for pharmacist functionalities, including managing prescriptions, 
 * viewing appointment outcomes, and handling inventory and replenishment requests.
 */
public class PharmacistUI {
    private final Scanner scanner;
    private final AuthenticationController authController;
    private final AppointmentController appointmentController;
    private MedicineRepository medicineRepository;

    /**
     * Constructs a PharmacistUI instance with necessary controllers and repositories.
     *
     * @param scanner The Scanner object for user input.
     * @param authController The controller handling authentication operations.
     * @param appointmentController The controller managing appointments.
     */
    public PharmacistUI(Scanner scanner, AuthenticationController authController, 
                       AppointmentController appointmentController) {
        this.scanner = scanner;
        this.authController = authController;
        this.appointmentController = appointmentController;
        this.medicineRepository = MedicineRepository.getInstance();
    }

    /**
     * Displays the pharmacist menu and processes user actions based on input.
     *
     * @param pharmacist The Pharmacist object representing the current user.
     */
    public void show(Pharmacist pharmacist) {
        while (true) {
            System.out.println("\nPharmacist Menu");
            System.out.println("1. Change Password");
            System.out.println("2. View Appointment Outcome Records");
            System.out.println("3. Update Prescription Status");
            System.out.println("4. View Medication Inventory");
            System.out.println("5. Submit Replenishment Request");
            System.out.println("6. Logout");
            
            System.out.print("Enter your choice (1-6): ");
            int choice = Integer.parseInt(scanner.nextLine());
            
            switch (choice) {
                case 1 -> changePassword(pharmacist);
                case 2 -> viewAppointmentOutcomes();
                case 3 -> updatePrescriptionStatus();
                case 4 -> viewMedicationInventory();
                case 5 -> submitReplenishmentRequest(pharmacist);
                case 6 -> {
                    return;
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    /**
     * Allows a pharmacist to change their password by validating the old password 
     * and confirming the new password.
     *
     * @param pharmacist The pharmacist requesting the password change.
     */
    private void changePassword(Pharmacist pharmacist) {
        System.out.print("Enter old password: ");
        String oldPassword = scanner.nextLine();
        
        System.out.print("Enter new password: ");
        String newPassword = scanner.nextLine();
        
        System.out.print("Confirm new password: ");
        String confirmPassword = scanner.nextLine();
        
        if (!newPassword.equals(confirmPassword)) {
            System.out.println("Passwords do not match!");
            return;
        }
        
        if (newPassword.length() < 8) {
            System.out.println("New password must be at least 8 characters long!");
            return;
        }
        
        if (authController.changePassword(pharmacist, oldPassword, newPassword)) {
            System.out.println("Password changed successfully!");
        } else {
            System.out.println("Failed to change password. Please check your old password.");
        }
    }

    /**
     * Displays all completed appointment outcomes with prescriptions that are pending
     * dispensing or awaiting further action.
     */
    private void viewAppointmentOutcomes() {
        System.out.println("\nAppointment Outcomes with Pending Prescriptions:");
        List<Appointment> completedAppointments = appointmentController.getAllAppointments().stream()
            .filter(apt -> apt.getStatus() == AppointmentStatus.COMPLETED)
            .filter(apt -> apt.getOutcomeRecord() != null)
            .toList();
            
        if (completedAppointments.isEmpty()) {
            System.out.println("No completed appointments found.");
            return;
        }
    
        for (Appointment apt : completedAppointments) {
            System.out.println("\n=================================");
            System.out.printf("Appointment ID: %s%n", apt.getAppointmentId());
            System.out.printf("Date: %s%n", apt.getDateTime().toLocalDate());
            System.out.printf("Patient: %s%n", apt.getPatient().getName());
            System.out.printf("Doctor: %s%n", apt.getDoctor().getName());
            
            AppointmentOutcomeRecord outcome = apt.getOutcomeRecord();
            System.out.printf("Service: %s%n", outcome.getServiceType());
            System.out.println("\nPrescriptions:");
            
            if (outcome.getPrescriptions().isEmpty()) {
                System.out.println("No prescriptions");
            } else {
                outcome.getPrescriptions().forEach(p -> 
                    System.out.printf("- %s (Quantity: %d) - Status: %s%n",
                        p.getMedicine().getName(),
                        p.getQuantity(),
                        p.getStatus()));
            }
            
            System.out.printf("\nNotes: %s%n", outcome.getConsultationNotes());
        }
    }

    /**
     * Updates the status of a specific prescription for a given appointment. Verifies
     * the prescription and updates it to dispensed if stock is available.
     */
    private void updatePrescriptionStatus() {
        System.out.print("Enter Appointment ID: ");
        String appointmentId = scanner.nextLine();
        
        Optional<Appointment> optAppointment = appointmentController.getAppointmentById(appointmentId);
        
        if (optAppointment.isEmpty()) {
            System.out.println("Appointment not found!");
            return;
        }
        
        Appointment appointment = optAppointment.get();
        
        if (appointment.getStatus() != AppointmentStatus.COMPLETED || 
            appointment.getOutcomeRecord() == null ||
            appointment.getOutcomeRecord().getPrescriptions().isEmpty()) {
            System.out.println("No prescriptions found for this appointment.");
            return;
        }
        
        AppointmentOutcomeRecord outcome = appointment.getOutcomeRecord();
        List<Prescription> prescriptions = outcome.getPrescriptions();
        
        System.out.println("\nPrescriptions for Appointment " + appointmentId + ":");
        for (int i = 0; i < prescriptions.size(); i++) {
            Prescription p = prescriptions.get(i);
            System.out.printf("%d. %s (Quantity: %d) - Status: %s%n",
                i + 1,
                p.getMedicine().getName(),
                p.getQuantity(),
                p.getStatus());
        }
        
        System.out.print("\nSelect prescription to update (1-" + prescriptions.size() + "): ");
        try {
            int choice = Integer.parseInt(scanner.nextLine());
            if (choice < 1 || choice > prescriptions.size()) {
                System.out.println("Invalid choice!");
                return;
            }
            
            Prescription selectedPrescription = prescriptions.get(choice - 1);
            
            if (selectedPrescription.getStatus() == PrescriptionStatus.DISPENSED) {
                System.out.println("This prescription has already been dispensed.");
                return;
            }
            
            // Check medicine stock
            Medicine medicine = selectedPrescription.getMedicine();
            if (!medicine.canFulfillQuantity(selectedPrescription.getQuantity())) {
                System.out.println("Insufficient stock to dispense this prescription!");
                return;
            }
            
            if (this.dispenseMedication(selectedPrescription)) {
                System.out.println("Prescription status updated to DISPENSED successfully!");
                System.out.printf("Updated stock for %s: %d%n", 
                    medicine.getName(), 
                    medicine.getCurrentStock());
            } else {
                System.out.println("Failed to dispense medication!");
            }
            
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid number!");
        }
    }

    /**
     * Displays the current inventory of medications, highlighting those with low stock.
     */
    private void viewMedicationInventory() {
        System.out.println("\nCurrent Medication Inventory:");
        List<Medicine> medicines = medicineRepository.findAll();
        medicines.forEach(med -> {
            System.out.println("\n" + med);
            if (med.isLowStock()) {
                System.out.println("*** LOW STOCK ALERT ***");
            }
        });
    }

    /**
     * Allows the pharmacist to request replenishment for medicines that are low on stock.
     *
     * @param pharmacist The pharmacist submitting the replenishment request.
     */
    private void submitReplenishmentRequest(Pharmacist pharmacist) {
        try {
            System.out.println("\nLow Stock Medicines:");
            List<Medicine> lowStockMeds = medicineRepository.findAll().stream()
                .filter(Medicine::isLowStock)
                .filter(med -> !med.isReplenishmentRequested())
                .toList();
                
            if (lowStockMeds.isEmpty()) {
                System.out.println("No medicines require replenishment!");
                return;
            }
            
            for (int i = 0; i < lowStockMeds.size(); i++) {
                System.out.printf("%d. %s%n", i + 1, lowStockMeds.get(i));
            }
            
            // Medicine selection with validation
            int medicineNum;
            while (true) {
                System.out.print("Enter medicine number to request replenishment (1-" + 
                    lowStockMeds.size() + ") or 'cancel' to go back: ");
                String input = scanner.nextLine().trim();
                
                // Check for cancellation
                if (input.equalsIgnoreCase("cancel")) {
                    System.out.println("Operation cancelled.");
                    return;
                }
                
                // Validate medicine number
                try {
                    medicineNum = Integer.parseInt(input);
                    if (medicineNum >= 1 && medicineNum <= lowStockMeds.size()) {
                        break; // Valid input, exit loop
                    } else {
                        System.out.println("Please enter a number between 1 and " + lowStockMeds.size());
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Please enter a valid number or 'cancel'");
                }
            }
            
            // Simple quantity input
            System.out.print("Enter quantity to request: ");
            String quantityInput = scanner.nextLine().trim();
            if (quantityInput.isEmpty()) {
                System.out.println("Operation cancelled - no quantity entered.");
                return;
            }
    
            try {
                int quantity = Integer.parseInt(quantityInput);
                Medicine medicine = lowStockMeds.get(medicineNum - 1);
                
                ReplenishmentRequest request = pharmacist.createReplenishmentRequest(medicine, quantity);
                System.out.println("\nReplenishment request submitted successfully!");
                System.out.println("Request details:");
                System.out.println("Medicine: " + request.getMedicine().getName());
                System.out.println("Requested Quantity: " + request.getRequestedQuantity());
                System.out.println("Status: " + request.getStatus());
                System.out.println("Request Time: " + request.getRequestDateTime());
                
            } catch (NumberFormatException e) {
                System.out.println("Invalid quantity entered. Please enter a valid number.");
            } catch (IllegalArgumentException | IllegalStateException e) {
                System.out.println("Error creating request: " + e.getMessage());
            }
            
        } catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
            System.out.println("Please try again.");
        }
    }

    /**
     * Dispenses the medication for a prescription if sufficient stock is available.
     * Updates stock and prescription status as necessary.
     *
     * @param prescription The prescription to be dispensed.
     * @return true if dispensing is successful; false otherwise.
     */
    private boolean dispenseMedication(Prescription prescription) {
        try {
            // Get the medicine and required quantity
            Medicine medicine = prescription.getMedicine();
            int requestedQuantity = prescription.getQuantity();
            
            // Verify there's sufficient stock
            if (!medicine.canFulfillQuantity(requestedQuantity)) {
                System.out.println("Error: Insufficient stock available");
                return false;
            }
            
            // Update medicine stock (reduce by using negative quantity)
            if (!medicine.updateStock(-requestedQuantity)) {
                System.out.println("Error: Failed to update medicine stock");
                return false;
            }
            
            // Update prescription status to dispensed
            prescription.setStatus(PrescriptionStatus.DISPENSED);
            
            // Save changes to medicine repository
            medicineRepository.save(medicine);
            
            return true;
        } catch (Exception e) {
            System.out.println("Error occurred while dispensing medication: " + e.getMessage());
            return false;
        }
    }
}
