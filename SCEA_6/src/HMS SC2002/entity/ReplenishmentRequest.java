//entity/ReplenishmentRequest.java

package entity;

import java.time.LocalDateTime;
import entity.enums.ReplenishmentStatus;

/**
 * Represents a request for replenishment of a specific medicine, including details about the requested quantity, 
 * the pharmacist who requested it, and its processing status.
 */
public class ReplenishmentRequest {

    /** The medicine for which replenishment is requested. */
    private final Medicine medicine;

    /** The quantity of the medicine requested for replenishment. */
    private final int requestedQuantity;

    /** The pharmacist who requested the replenishment. */
    private final Pharmacist requestedBy;

    /** The date and time when the replenishment request was made. */
    private final LocalDateTime requestDateTime;

    /** The administrator who processed the replenishment request, if applicable. */
    private Administrator processedBy;

    /** The date and time when the replenishment request was processed, if applicable. */
    private LocalDateTime processedDateTime;

    /** The current status of the replenishment request (e.g., PENDING, APPROVED, REJECTED). */
    private ReplenishmentStatus status;

    /**
     * Constructs a new replenishment request with the specified medicine, requested quantity, 
     * pharmacist, and request date/time. The status is initially set to PENDING.
     *
     * @param medicine The medicine being requested for replenishment.
     * @param requestedQuantity The quantity of the medicine being requested.
     * @param requestedBy The pharmacist who requested the replenishment.
     * @param requestDateTime The date and time when the replenishment request was made.
     */
    public ReplenishmentRequest(Medicine medicine, int requestedQuantity, 
                                Pharmacist requestedBy, LocalDateTime requestDateTime) {
        this.medicine = medicine;
        this.requestedQuantity = requestedQuantity;
        this.requestedBy = requestedBy;
        this.requestDateTime = requestDateTime;
        this.status = ReplenishmentStatus.PENDING;
    }

    /**
     * Gets the medicine for which replenishment is requested.
     *
     * @return The medicine.
     */
    public Medicine getMedicine() {
        return medicine;
    }

    /**
     * Gets the requested quantity of the medicine.
     *
     * @return The requested quantity.
     */
    public int getRequestedQuantity() {
        return requestedQuantity;
    }

    /**
     * Gets the pharmacist who made the replenishment request.
     *
     * @return The pharmacist who made the request.
     */
    public Pharmacist getRequestedBy() {
        return requestedBy;
    }

    /**
     * Gets the date and time when the replenishment request was made.
     *
     * @return The date and time of the request.
     */
    public LocalDateTime getRequestDateTime() {
        return requestDateTime;
    }

    /**
     * Gets the administrator who processed the replenishment request.
     *
     * @return The administrator who processed the request, or {@code null} if not yet processed.
     */
    public Administrator getProcessedBy() {
        return processedBy;
    }

    /**
     * Sets the administrator who processed the replenishment request.
     *
     * @param processedBy The administrator who processed the request.
     */
    public void setProcessedBy(Administrator processedBy) {
        this.processedBy = processedBy;
    }

    /**
     * Gets the date and time when the replenishment request was processed.
     *
     * @return The date and time of processing, or {@code null} if not yet processed.
     */
    public LocalDateTime getProcessedDateTime() {
        return processedDateTime;
    }

    /**
     * Sets the date and time when the replenishment request was processed.
     *
     * @param processedDateTime The date and time of processing.
     */
    public void setProcessedDateTime(LocalDateTime processedDateTime) {
        this.processedDateTime = processedDateTime;
    }

    /**
     * Gets the current status of the replenishment request.
     *
     * @return The status of the request.
     */
    public ReplenishmentStatus getStatus() {
        return status;
    }

    /**
     * Sets the status of the replenishment request.
     *
     * @param status The new status of the request.
     */
    public void setStatus(ReplenishmentStatus status) {
        this.status = status;
    }
}
