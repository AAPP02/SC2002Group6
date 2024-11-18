//entity/enums/BloodType.java

package entity.enums;

/**
 * Enum representing the different blood types.
 */
public enum BloodType {

    /**
     * Blood type A positive.
     */
    A_POSITIVE("A+"),

    /**
     * Blood type A negative.
     */
    A_NEGATIVE("A-"),

    /**
     * Blood type B positive.
     */
    B_POSITIVE("B+"),

    /**
     * Blood type B negative.
     */
    B_NEGATIVE("B-"),

    /**
     * Blood type O positive.
     */
    O_POSITIVE("O+"),

    /**
     * Blood type O negative.
     */
    O_NEGATIVE("O-"),

    /**
     * Blood type AB positive.
     */
    AB_POSITIVE("AB+"),

    /**
     * Blood type AB negative.
     */
    AB_NEGATIVE("AB-");

    private final String display;

    /**
     * Constructor to associate a display string with the blood type.
     * @param display The string representation of the blood type (e.g., "A+", "O-", etc.)
     */
    BloodType(String display) {
        this.display = display;
    }

    /**
     * Returns the string representation of the blood type.
     * @return The blood type as a string (e.g., "A+", "B-", etc.)
     */
    @Override
    public String toString() {
        return display;
    }

    /**
     * Converts a string to its corresponding BloodType.
     * @param text The string to convert (e.g., "A+", "O-", etc.)
     * @return The corresponding BloodType enum value.
     * @throws IllegalArgumentException If the text does not match any valid blood type.
     */
    public static BloodType fromString(String text) {
        for (BloodType bt : BloodType.values()) {
            if (bt.display.equalsIgnoreCase(text)) {
                return bt;
            }
        }
        throw new IllegalArgumentException("Invalid blood type: " + text);
    }
}

