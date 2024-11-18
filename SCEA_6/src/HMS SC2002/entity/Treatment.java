//entity/Treatment.java

package entity;

/**
 * The {@code Treatment} class represents a medical treatment with a description.
 * It is used to store information about the treatment prescribed to a patient.
 */
public class Treatment {
    
    /** The description of the treatment. */
    private final String description;
    
    /**
     * Constructs a {@code Treatment} with the specified description.
     * 
     * @param description The description of the treatment.
     */
    public Treatment(String description) {
        this.description = description;
    }
    
    /**
     * Returns a string representation of the treatment, which is the description.
     * 
     * @return The description of the treatment.
     */
    @Override
    public String toString() {
        return description;
    }

    /**
     * Gets the description of the treatment.
     * 
     * @return The description of the treatment.
     */
    public String getDescription() {
        return description;
    }
}
