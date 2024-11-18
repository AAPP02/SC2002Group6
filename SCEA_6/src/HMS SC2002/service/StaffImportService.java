//service/StaffImportService.java

// Staff Import Service
package service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import entity.*;
import util.CSVReader;

/**
 * {@code StaffImportService} is a service that implements the {@link DataImportService} interface
 * to import staff data from a CSV file and convert it into a list of {@link User} entities.
 * The data in the CSV file is expected to contain staff details including ID, name, role, gender, and age.
 */
public class StaffImportService implements DataImportService<User> {

    /**
     * Imports staff data from a CSV file and converts it into a list of {@link User} objects.
     * The CSV file is expected to have rows with the following columns:
     * <ul>
     *   <li>Staff ID (String)</li>
     *   <li>Name (String)</li>
     *   <li>Role (String, should match one of the following roles: DOCTOR, PHARMACIST, ADMINISTRATOR)</li>
     *   <li>Gender (String)</li>
     *   <li>Age (Integer)</li>
     * </ul>
     * 
     * Depending on the role, different types of {@link User} will be created, such as:
     * <ul>
     *   <li>{@link Doctor}</li>
     *   <li>{@link Pharmacist}</li>
     *   <li>{@link Administrator}</li>
     * </ul>
     * 
     * @param filename The name of the CSV file to import data from.
     * @return A list of {@link User} objects parsed from the CSV file.
     * @throws IOException If an error occurs while reading the file or processing its content.
     */
    @Override
    public List<User> importData(String filename) throws IOException {
        CSVReader reader = new CSVReader(filename);
        List<User> staff = new ArrayList<>();

        // Iterate through each row in the CSV file and create User objects based on the role
        for (String[] row : reader.getData()) {
            String staffId = row[0].trim();
            String name = row[1].trim();
            String role = row[2].trim().toUpperCase();
            String gender = row[3].trim();
            int age = Integer.parseInt(row[4].trim());

            // Create user based on the role
            User user = switch (role) {
                case "DOCTOR" -> new Doctor(staffId, "password", name, "General Medicine");
                case "PHARMACIST" -> new Pharmacist(staffId, "password", name);
                case "ADMINISTRATOR" -> new Administrator(staffId, "password", name);
                default -> throw new IllegalArgumentException("Invalid role: " + role);
            };

            staff.add(user);
        }

        return staff;
    }
}
