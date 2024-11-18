//entity/Pharmacist.java

package entity;

import java.util.*;
import java.time.LocalDateTime;
import entity.enums.*;

/**
 * Represents a pharmacist in the system, responsible for dispensing medications and handling replenishment requests.
 */
public class Pharmacist extends User {

    /** The list of medications dispensed by the pharmacist. */
    private List<Medicine> dispensedMedications;

    /** The list of replenishment requests created by the pharmacist. */
    private List<ReplenishmentRequest> replenishmentRequests;

    /**
     * Constructs a Pharmacist with specified hospital ID, password, and name.
     *
     * @param hospitalId The unique ID of the pharmacist within the hospital system.
     * @param password The pharmacist's password for authentication.
     * @param name The name of the pharmacist.
     */
    public Pharmacist(String hospitalId, String password, String name) {
        super(hospitalId, password, name);
        this.dispensedMedications = new ArrayList<>();
        this.replenishmentRequests = new ArrayList<>();
    }

    /**
     * Dispenses medication based on a prescription, updating the stock and prescription status.
     *
     * @param prescription The prescription to be dispensed.
     * @return {@code true} if the medication is successfully dispensed; {@code false} otherwise.
     */
    public boolean dispenseMedication(Prescription prescription) {
        if (prescription.getStatus() != PrescriptionStatus.PENDING) {
            return false;
        }

        Medicine medicine = prescription.getMedicine();
        if (!medicine.canFulfillQuantity(prescription.getQuantity())) {
            return false;
        }

        medicine.updateStock(-prescription.getQuantity());
        prescription.setStatus(PrescriptionStatus.DISPENSED);
        prescription.setDispensedBy(this);
        prescription.setDispensedDateTime(LocalDateTime.now());

        dispensedMedications.add(medicine);
        return true;
    }

    /**
     * Creates a replenishment request for a specific medicine and adds it to the list of requests.
     *
     * @param medicine The medicine for which the replenishment is requested.
     * @param requestedQuantity The quantity requested for replenishment.
     * @return The created {@code ReplenishmentRequest} instance.
     * @throws IllegalArgumentException if the medicine is invalid, the quantity is non-positive, or the request exceeds the maximum stock.
     * @throws IllegalStateException if replenishment is already requested for this medicine.
     */
    public ReplenishmentRequest createReplenishmentRequest(Medicine medicine, int requestedQuantity) {
        if (medicine == null || requestedQuantity <= 0) {
            throw new IllegalArgumentException("Invalid medicine or quantity");
        }

        if (!medicine.isLowStock() && medicine.getCurrentStock() + requestedQuantity > medicine.getMaxStock()) {
            throw new IllegalArgumentException("Medicine is not low on stock or request exceeds max stock");
        }

        if (medicine.isReplenishmentRequested()) {
            throw new IllegalStateException("Replenishment already requested for this medicine");
        }

        ReplenishmentRequest request = new ReplenishmentRequest(
            medicine,
            requestedQuantity,
            this,
            LocalDateTime.now()
        );

        medicine.requestReplenishment();
        replenishmentRequests.add(request);
        return request;
    }

    /**
     * Gets a list of all medications dispensed by the pharmacist.
     *
     * @return A list of dispensed medications.
     */
    public List<Medicine> getDispensedMedications() {
        return new ArrayList<>(dispensedMedications);
    }

    /**
     * Gets a list of all replenishment requests created by the pharmacist.
     *
     * @return A list of replenishment requests.
     */
    public List<ReplenishmentRequest> getReplenishmentRequests() {
        return new ArrayList<>(replenishmentRequests);
    }

    /**
     * Gets a list of pending replenishment requests created by the pharmacist.
     *
     * @return A list of pending replenishment requests.
     */
    public List<ReplenishmentRequest> getPendingReplenishmentRequests() {
        return replenishmentRequests.stream()
            .filter(request -> request.getStatus() == ReplenishmentStatus.PENDING)
            .toList();
    }
}
