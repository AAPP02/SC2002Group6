//entity/Administrator.java

package entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import entity.enums.InventoryActionType;
import entity.enums.ReplenishmentStatus;

/**
 * Represents an Administrator in the system who manages staff, processes replenishment requests,
 * and oversees inventory actions related to medicines.
 */
public class Administrator extends User {

    /** The set of users that this administrator manages (staff members). */
    private final Set<User> managedStaff;

    /** The list of replenishment requests that have been processed by this administrator. */
    private final List<ReplenishmentRequest> processedRequests;

    /** The list of medicine inventory actions performed by this administrator. */
    private final List<MedicineInventoryAction> inventoryActions;

    /**
     * Constructs a new Administrator with the given hospital ID, password, and name.
     * 
     * @param hospitalId The hospital ID of the administrator.
     * @param password The password of the administrator.
     * @param name The name of the administrator.
     */
    public Administrator(String hospitalId, String password, String name) {
        super(hospitalId, password, name);
        this.managedStaff = new HashSet<>();
        this.processedRequests = new ArrayList<>();
        this.inventoryActions = new ArrayList<>();
    }

    /**
     * Adds a staff member to the list of managed staff.
     * 
     * @param staff The staff member to be added.
     * @throws IllegalArgumentException if the staff member is invalid or is an Administrator.
     */
    public void addStaffMember(User staff) {
        if (staff == null || staff instanceof Administrator) {
            throw new IllegalArgumentException("Invalid staff member");
        }
        if (!managedStaff.add(staff)) {
            throw new IllegalStateException("Staff member already exists");
        }
    }

    /**
     * Removes a staff member from the list of managed staff.
     * 
     * @param staff The staff member to be removed.
     * @throws IllegalArgumentException if the staff member does not exist in the managed staff.
     */
    public void removeStaffMember(User staff) {
        if (staff == null) {
            throw new IllegalArgumentException("Staff member cannot be null");
        }
        if (!managedStaff.remove(staff)) {
            throw new IllegalArgumentException("Staff member not found in managed staff");
        }
    }

    /**
     * Updates a staff member's details in the managed staff list.
     * 
     * @param staff The staff member to be updated.
     * @throws IllegalArgumentException if the staff member does not exist in the managed staff.
     */
    public void updateStaffMember(User staff) {
        if (staff == null) {
            throw new IllegalArgumentException("Staff member cannot be null");
        }
        // Remove old version if it exists (using hospital ID for comparison)
        managedStaff.removeIf(existingStaff -> 
            existingStaff.getHospitalId().equals(staff.getHospitalId()));
        // Add updated version
        managedStaff.add(staff);
    }

    /**
     * Processes a replenishment request by either approving or rejecting it.
     * 
     * @param request The replenishment request to process.
     * @param approve A boolean indicating whether to approve or reject the request.
     * @throws IllegalArgumentException if the request is invalid or already processed.
     */
    public void processReplenishmentRequest(ReplenishmentRequest request, boolean approve) {
        if (request == null || request.getStatus() != ReplenishmentStatus.PENDING) {
            throw new IllegalArgumentException("Invalid or already processed request");
        }

        Medicine medicine = request.getMedicine();
        if (approve) {
            medicine.fulfillReplenishment(request.getRequestedQuantity());
            request.setStatus(ReplenishmentStatus.APPROVED);
            
            MedicineInventoryAction action = new MedicineInventoryAction(
                medicine,
                request.getRequestedQuantity(),
                InventoryActionType.REPLENISHMENT,
                LocalDateTime.now(),
                this
            );
            inventoryActions.add(action);
        } else {
            medicine.cancelReplenishmentRequest();
            request.setStatus(ReplenishmentStatus.REJECTED);
        }

        request.setProcessedBy(this);
        request.setProcessedDateTime(LocalDateTime.now());
        processedRequests.add(request);
    }

    /**
     * Updates the stock of a medicine by a certain quantity and records the action.
     * 
     * @param medicine The medicine whose stock is being updated.
     * @param quantity The quantity to add or subtract from the medicine stock.
     * @param reason The reason for the stock update.
     * @throws IllegalArgumentException if the parameters are invalid.
     */
    public void updateMedicineStock(Medicine medicine, int quantity, String reason) {
        if (medicine == null || reason == null || reason.trim().isEmpty()) {
            throw new IllegalArgumentException("Invalid parameters");
        }

        medicine.updateStock(quantity);
        
        MedicineInventoryAction action = new MedicineInventoryAction(
            medicine,
            quantity,
            quantity > 0 ? InventoryActionType.ADDITION : InventoryActionType.REDUCTION,
            LocalDateTime.now(),
            this,
            reason
        );
        inventoryActions.add(action);
    }

    /**
     * Updates the low stock alert level for a given medicine.
     * 
     * @param medicine The medicine whose low stock alert is being updated.
     * @param newAlertLevel The new alert level for the medicine's stock.
     * @throws IllegalArgumentException if the parameters are invalid.
     */
    public void updateMedicineLowStockAlert(Medicine medicine, int newAlertLevel) {
        if (medicine == null || newAlertLevel < 0) {
            throw new IllegalArgumentException("Invalid parameters");
        }
        medicine.setLowStockAlert(newAlertLevel);
    }

    /**
     * Returns a list of all staff members managed by this administrator.
     * 
     * @return A list of managed staff members.
     */
    public List<User> getManagedStaff() {
        return new ArrayList<>(managedStaff);
    }

    /**
     * Returns a list of staff members managed by this administrator filtered by role.
     * 
     * @param role The role of staff members to filter by.
     * @return A list of managed staff members with the specified role.
     */
    public List<User> getManagedStaffByRole(Class<? extends User> role) {
        return managedStaff.stream()
            .filter(staff -> role.isInstance(staff))
            .toList();
    }

    /**
     * Returns a list of all replenishment requests that have been processed by this administrator.
     * 
     * @return A list of processed replenishment requests.
     */
    public List<ReplenishmentRequest> getProcessedRequests() {
        return new ArrayList<>(processedRequests);
    }

    /**
     * Returns a list of all medicine inventory actions performed by this administrator.
     * 
     * @return A list of inventory actions.
     */
    public List<MedicineInventoryAction> getInventoryActions() {
        return new ArrayList<>(inventoryActions);
    }

    /**
     * Returns a list of medicine inventory actions performed by this administrator within a given date range.
     * 
     * @param startDate The start date of the range.
     * @param endDate The end date of the range.
     * @return A list of inventory actions performed within the specified date range.
     */
    public List<MedicineInventoryAction> getInventoryActionsByDate(LocalDateTime startDate, LocalDateTime endDate) {
        return inventoryActions.stream()
            .filter(action -> {
                LocalDateTime actionDate = action.getActionDateTime();
                return !actionDate.isBefore(startDate) && !actionDate.isAfter(endDate);
            })
            .toList();
    }

    /**
     * Returns a list of medicine inventory actions performed by this administrator for a specific medicine.
     * 
     * @param medicine The medicine to filter actions by.
     * @return A list of inventory actions for the specified medicine.
     */
    public List<MedicineInventoryAction> getInventoryActionsByMedicine(Medicine medicine) {
        return inventoryActions.stream()
            .filter(action -> action.getMedicine().equals(medicine))
            .toList();
    }
}
