//service/DataImportService.java

package service;

import java.io.IOException;
import java.util.List;

/**
 * {@code DataImportService} is a generic interface that defines the contract for importing data from a file.
 * Implementations of this interface are responsible for reading and parsing data from a given file
 * and converting it into a list of entities of type {@code T}.
 *
 * @param <T> The type of entity to be imported (e.g., {@link Doctor}, {@link Patient}, etc.).
 */
public interface DataImportService<T> {
    
    /**
     * Imports data from the specified CSV file and converts it into a list of entities.
     * The data in the file is expected to be in a format that can be parsed into a list of {@code T} entities.
     * 
     * @param filename The name of the CSV file to import data from.
     * @return A list of entities parsed from the CSV file.
     * @throws IOException If an error occurs while reading the file or processing its content.
     */
    List<T> importData(String filename) throws IOException;
}
