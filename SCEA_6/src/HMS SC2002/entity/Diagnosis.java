//entity/Diagnosis.java

package entity;

import java.time.LocalDate;

/**
 * Represents a medical diagnosis for a patient, including the date of diagnosis,
 * description of the condition, and recommended treatment.
 */
public class Diagnosis {
    
    /** The date on which the diagnosis was made. */
    private final LocalDate date;
    
    /** The description of the diagnosed condition. */
    private final String description;
    
    /** The treatment recommended for the diagnosis. */
    private final Treatment treatment;

    /**
     * Constructs a new Diagnosis with the specified description and treatment.
     * The diagnosis date is automatically set to the current date.
     * 
     * @param description The description of the diagnosis.
     * @param treatment The treatment recommended for the diagnosis.
     */
    public Diagnosis(String description, Treatment treatment) {
        this.date = LocalDate.now();
        this.description = description;
        this.treatment = treatment;
    }

    /**
     * Returns a string representation of the diagnosis, including date, description, and treatment.
     * 
     * @return A string representation of the diagnosis.
     */
    @Override
    public String toString() {
        return String.format("Date: %s\nDiagnosis: %s\nTreatment: %s", 
            date, description, treatment);
    }

    /**
     * Gets the date of the diagnosis.
     * 
     * @return The date of the diagnosis.
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * Gets the description of the diagnosed condition.
     * 
     * @return The description of the diagnosis.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Gets the treatment recommended for this diagnosis.
     * 
     * @return The recommended treatment.
     */
    public Treatment getTreatment() {
        return treatment;
    }
}
