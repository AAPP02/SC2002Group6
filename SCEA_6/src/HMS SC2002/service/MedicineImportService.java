//service/MedicineImportService.java

package service;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import entity.Medicine;
import util.CSVReader;

/**
 * {@code MedicineImportService} is a service that implements the {@link DataImportService} interface
 * for importing a list of {@link Medicine} entities from a CSV file.
 * The data in the file is expected to contain medicine details like name, initial stock, and low stock alert.
 */
public class MedicineImportService implements DataImportService<Medicine> {

    /**
     * Imports data from a CSV file and converts it into a list of {@link Medicine} objects.
     * The CSV file is expected to have rows with the following columns:
     * <ul>
     *   <li>Medicine name (String)</li>
     *   <li>Initial stock (Integer)</li>
     *   <li>Low stock alert (Integer)</li>
     * </ul>
     * 
     * @param filename The name of the CSV file to import data from.
     * @return A list of {@link Medicine} objects parsed from the CSV file.
     * @throws IOException If an error occurs while reading the file or processing its content.
     */
    @Override
    public List<Medicine> importData(String filename) throws IOException {
        CSVReader reader = new CSVReader(filename);
        List<Medicine> medicines = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // Iterate through each row in the CSV file and create a Medicine object
        for (String[] row : reader.getData()) {
            String name = row[0].trim();
            int initialStock = Integer.parseInt(row[1].trim());
            int lowStockAlert = Integer.parseInt(row[2].trim());

            // Create and add the Medicine object to the list
            Medicine medicine = new Medicine(name, initialStock, lowStockAlert);
            medicines.add(medicine);
        }

        return medicines;
    }
}
