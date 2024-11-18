//util/CSVReader.java

package util;

import java.io.*;
import java.nio.file.*;
import java.util.*;

/**
 * {@code CSVReader} is a utility class designed to read CSV files and parse their content into a list of string arrays.
 * It reads the file, extracts the headers (first line), and stores the rest of the lines as data. The class also provides
 * methods to retrieve the headers and data separately.
 */
public class CSVReader {
    private final String filename;
    private final List<String[]> data;
    private final String[] headers;

    /**
     * Constructs a new {@code CSVReader} instance and reads the data from the specified CSV file.
     * The first line of the CSV file is treated as the header, and the subsequent lines are treated as the data.
     * 
     * @param filename The name of the CSV file to read.
     * @throws IOException If an error occurs while reading the file or if the file is empty.
     */
    public CSVReader(String filename) throws IOException {
        this.filename = filename;
        List<String> lines = Files.readAllLines(Paths.get(filename));
        
        // Throw an exception if the CSV file is empty
        if (lines.isEmpty()) {
            throw new IOException("Empty CSV file: " + filename);
        }

        // Parse the first line as headers
        this.headers = parseCSVLine(lines.get(0));
        
        // Parse remaining lines as data
        this.data = new ArrayList<>();
        for (int i = 1; i < lines.size(); i++) {
            data.add(parseCSVLine(lines.get(i)));
        }
    }

    /**
     * Parses a single CSV line and splits it into an array of strings. The method handles cases of quoted commas within
     * values by using a regular expression.
     * 
     * @param line The CSV line to parse.
     * @return An array of strings representing the values in the CSV line.
     */
    private String[] parseCSVLine(String line) {
        return line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
    }

    /**
     * Returns a copy of the header row from the CSV file.
     * 
     * @return An array of strings representing the headers of the CSV file.
     */
    public String[] getHeaders() {
        return headers.clone();
    }

    /**
     * Returns a list of data rows from the CSV file. Each row is represented as an array of strings.
     * 
     * @return A list of string arrays where each array represents a row of data in the CSV file.
     */
    public List<String[]> getData() {
        return new ArrayList<>(data);
    }
}
