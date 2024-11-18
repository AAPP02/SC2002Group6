//service/PatientImportService.java

package service;

import entity.*;
import entity.enums.*;
import util.CSVReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

/**
 * {@code PatientImportService} is a service that implements the {@link DataImportService} interface
 * to import patient data from a CSV file and convert it into a list of {@link Patient} entities.
 * The data in the CSV file is expected to contain patient details including ID, name, date of birth, 
 * gender, blood type, email, and phone number.
 */
public class PatientImportService implements DataImportService<Patient> {

    /**
     * Imports patient data from a CSV file and converts it into a list of {@link Patient} objects.
     * The CSV file is expected to have rows with the following columns:
     * <ul>
     *   <li>Patient ID (String)</li>
     *   <li>Name (String)</li>
     *   <li>Date of Birth (String, format: yyyy-MM-dd)</li>
     *   <li>Gender (String, should match {@link Gender} enum)</li>
     *   <li>Blood Type (String, should match {@link BloodType} format)</li>
     *   <li>Email (String)</li>
     *   <li>Phone (String, optional)</li>
     * </ul>
     *
     * @param filename The name of the CSV file to import data from.
     * @return A list of {@link Patient} objects parsed from the CSV file.
     * @throws IOException If an error occurs while reading the file or processing its content.
     */
    @Override
    public List<Patient> importData(String filename) throws IOException {
        CSVReader reader = new CSVReader(filename);
        List<Patient> patients = new ArrayList<>();

        // Iterate through each row in the CSV file and create Patient objects
        for (String[] row : reader.getData()) {
            String patientId = row[0].trim();
            String name = row[1].trim();
            LocalDate dob = LocalDate.parse(row[2].trim());
            Gender gender = Gender.valueOf(row[3].trim().toUpperCase());
            BloodType bloodType = parseBloodType(row[4].trim());
            String email = row[5].trim();
            String phone = row.length > 6 ? row[6].trim() : "Not Provided";

            // Create contact info
            ContactInfo contactInfo = new ContactInfo(phone, email);
            
            // Create medical record
            MedicalRecord medicalRecord = new MedicalRecord(
                patientId, name, dob, gender, bloodType, contactInfo);

            // Create patient with default password
            Patient patient = new Patient(patientId, "password", name, medicalRecord);
            patients.add(patient);
        }

        return patients;
    }

    /**
     * Parses a blood type string into its corresponding {@link BloodType} enum.
     * The method handles different formats, including possible "+" and "-" signs for positive/negative blood types.
     * 
     * @param bloodType The blood type string to be parsed (e.g., "A+", "B-", "O+", etc.).
     * @return The corresponding {@link BloodType} enum value.
     * @throws IllegalArgumentException If the blood type is in an invalid format.
     */
    private BloodType parseBloodType(String bloodType) {
        // Remove any spaces and convert to uppercase
        String cleanType = bloodType.replace(" ", "").replace("+", "_POSITIVE").replace("-", "_NEGATIVE").toUpperCase();
        
        return switch (cleanType) {
            case "A_POSITIVE", "APOSITIVE" -> BloodType.A_POSITIVE;
            case "A_NEGATIVE", "ANEGATIVE" -> BloodType.A_NEGATIVE;
            case "B_POSITIVE", "BPOSITIVE" -> BloodType.B_POSITIVE;
            case "B_NEGATIVE", "BNEGATIVE" -> BloodType.B_NEGATIVE;
            case "O_POSITIVE", "OPOSITIVE" -> BloodType.O_POSITIVE;
            case "O_NEGATIVE", "ONEGATIVE" -> BloodType.O_NEGATIVE;
            case "AB_POSITIVE", "ABPOSITIVE" -> BloodType.AB_POSITIVE;
            case "AB_NEGATIVE", "ABNEGATIVE" -> BloodType.AB_NEGATIVE;
            default -> throw new IllegalArgumentException("Invalid blood type format: " + bloodType + 
                ". Expected format: A+, A-, B+, B-, O+, O-, AB+, or AB-");
        };
    }
}

