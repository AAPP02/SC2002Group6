//repository/MedicalRecordRepository.java

package repository;

import entity.*;
import entity.enums.BloodType;
import entity.enums.Gender;

import java.time.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * The {@code MedicalRecordRepository} class is responsible for storing and managing medical records for patients. 
 * It provides various methods to perform operations such as saving, finding, deleting, and querying medical records 
 * based on different attributes (e.g., gender, blood type, age range, diagnosis, contact information).
 * It also supports batch operations and statistical reports.
 */
public class MedicalRecordRepository implements Repository<MedicalRecord, String> {
    private final Map<String, MedicalRecord> records;
    private static MedicalRecordRepository instance;
    
    /**
     * Private constructor to initialize the medical record repository.
     * Uses a {@code ConcurrentHashMap} to store medical records.
     */
    private MedicalRecordRepository() {
        this.records = new ConcurrentHashMap<>();
    }
    
    /**
     * Returns the singleton instance of the {@code MedicalRecordRepository}.
     * If the instance doesn't exist, it will be created.
     *
     * @return The singleton instance of {@code MedicalRecordRepository}.
     */
    public static MedicalRecordRepository getInstance() {
        if (instance == null) {
            instance = new MedicalRecordRepository();
        }
        return instance;
    }
    
    /**
     * Saves a {@code MedicalRecord} in the repository.
     * If a record with the same patient ID already exists, it will be overwritten.
     *
     * @param record The {@code MedicalRecord} to save.
     * @return The saved {@code MedicalRecord}.
     * @throws IllegalArgumentException if the record is null.
     */
    @Override
    public MedicalRecord save(MedicalRecord record) {
        if (record == null) {
            throw new IllegalArgumentException("Medical record cannot be null");
        }
        records.put(record.getPatientId(), record);
        return record;
    }
    
    /**
     * Finds a {@code MedicalRecord} by patient ID.
     *
     * @param patientId The patient ID of the record to find.
     * @return An {@code Optional} containing the {@code MedicalRecord} if found, or an empty {@code Optional} if not.
     * @throws IllegalArgumentException if the patient ID is null.
     */
    @Override
    public Optional<MedicalRecord> findById(String patientId) {
        if (patientId == null) {
            throw new IllegalArgumentException("Patient ID cannot be null");
        }
        return Optional.ofNullable(records.get(patientId));
    }
    
    /**
     * Retrieves all {@code MedicalRecord} entries from the repository.
     *
     * @return A list of all {@code MedicalRecord} records.
     */
    @Override
    public List<MedicalRecord> findAll() {
        return new ArrayList<>(records.values());
    }
    
    /**
     * Deletes a {@code MedicalRecord} by patient ID.
     *
     * @param patientId The patient ID of the record to delete.
     * @throws IllegalArgumentException if the patient ID is null.
     */
    @Override
    public void delete(String patientId) {
        if (patientId == null) {
            throw new IllegalArgumentException("Patient ID cannot be null");
        }
        records.remove(patientId);
    }
    
    /**
     * Checks if a {@code MedicalRecord} with the specified patient ID exists in the repository.
     *
     * @param patientId The patient ID to check for existence.
     * @return {@code true} if the {@code MedicalRecord} exists, {@code false} otherwise.
     * @throws IllegalArgumentException if the patient ID is null.
     */
    @Override
    public boolean exists(String patientId) {
        if (patientId == null) {
            throw new IllegalArgumentException("Patient ID cannot be null");
        }
        return records.containsKey(patientId);
    }
    
    /**
     * Finds medical records by gender.
     *
     * @param gender The gender to search for.
     * @return A list of {@code MedicalRecord} entries matching the specified gender.
     */
    public List<MedicalRecord> findByGender(Gender gender) {
        return records.values().stream()
            .filter(record -> record.getGender() == gender)
            .collect(Collectors.toList());
    }
    
    /**
     * Finds medical records by blood type.
     *
     * @param bloodType The blood type to search for.
     * @return A list of {@code MedicalRecord} entries matching the specified blood type.
     */
    public List<MedicalRecord> findByBloodType(BloodType bloodType) {
        return records.values().stream()
            .filter(record -> record.getBloodType() == bloodType)
            .collect(Collectors.toList());
    }
    
    /**
     * Finds medical records by age range.
     *
     * @param minAge The minimum age.
     * @param maxAge The maximum age.
     * @return A list of {@code MedicalRecord} entries within the specified age range.
     * @throws IllegalArgumentException if the age range is invalid.
     */
    public List<MedicalRecord> findByAgeRange(int minAge, int maxAge) {
        if (minAge < 0 || maxAge < minAge) {
            throw new IllegalArgumentException("Invalid age range");
        }
        
        return records.values().stream()
            .filter(record -> {
                int age = Period.between(record.getDateOfBirth(), LocalDate.now()).getYears();
                return age >= minAge && age <= maxAge;
            })
            .collect(Collectors.toList());
    }
    
    /**
     * Finds medical records that contain a specific diagnosis keyword.
     *
     * @param keyword The keyword to search for in diagnosis descriptions.
     * @return A list of {@code MedicalRecord} entries containing the diagnosis keyword.
     * @throws IllegalArgumentException if the keyword is null or empty.
     */
    public List<MedicalRecord> findByDiagnosisKeyword(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            throw new IllegalArgumentException("Search keyword cannot be null or empty");
        }
        
        String lowercaseKeyword = keyword.toLowerCase().trim();
        return records.values().stream()
            .filter(record -> record.getDiagnosisHistory().stream()
                .anyMatch(diagnosis -> 
                    diagnosis.getDescription().toLowerCase().contains(lowercaseKeyword)))
            .collect(Collectors.toList());
    }
    
    /**
     * Finds medical records within a diagnosis count range.
     *
     * @param minCount The minimum diagnosis count.
     * @param maxCount The maximum diagnosis count.
     * @return A list of {@code MedicalRecord} entries whose diagnosis count falls within the specified range.
     * @throws IllegalArgumentException if the count range is invalid.
     */
    public List<MedicalRecord> findByDiagnosisCountRange(int minCount, int maxCount) {
        if (minCount < 0 || maxCount < minCount) {
            throw new IllegalArgumentException("Invalid count range");
        }
        
        return records.values().stream()
            .filter(record -> {
                int diagnosisCount = record.getDiagnosisHistory().size();
                return diagnosisCount >= minCount && diagnosisCount <= maxCount;
            })
            .collect(Collectors.toList());
    }
    
    /**
     * Finds medical records with diagnoses in a specific date range.
     *
     * @param startDate The start date of the range.
     * @param endDate The end date of the range.
     * @return A list of {@code MedicalRecord} entries with diagnoses within the date range.
     * @throws IllegalArgumentException if the date range is invalid.
     */
    public List<MedicalRecord> findByDiagnosisDateRange(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null || endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("Invalid date range");
        }
        
        return records.values().stream()
            .filter(record -> record.getDiagnosisHistory().stream()
                .anyMatch(diagnosis -> {
                    LocalDate diagnosisDate = diagnosis.getDate();
                    return !diagnosisDate.isBefore(startDate) && !diagnosisDate.isAfter(endDate);
                }))
            .collect(Collectors.toList());
    }
    
    /**
     * Finds medical records that have been recently updated within a certain number of days.
     *
     * @param days The number of days to consider for recent updates.
     * @return A list of {@code MedicalRecord} entries updated within the specified number of days.
     * @throws IllegalArgumentException if the number of days is non-positive.
     */
    public List<MedicalRecord> findRecentlyUpdated(int days) {
        if (days <= 0) {
            throw new IllegalArgumentException("Days must be positive");
        }
        
        LocalDate cutoffDate = LocalDate.now().minusDays(days);
        
        return records.values().stream()
            .filter(record -> record.getDiagnosisHistory().stream()
                .anyMatch(diagnosis -> !diagnosis.getDate().isBefore(cutoffDate)))
            .collect(Collectors.toList());
    }
    
    /**
     * Finds medical records based on contact information (email or phone number).
     *
     * @param searchTerm The term to search for in the contact information.
     * @return A list of {@code MedicalRecord} entries matching the search term in contact info.
     * @throws IllegalArgumentException if the search term is null or empty.
     */
    public List<MedicalRecord> findByContactInfo(String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            throw new IllegalArgumentException("Search term cannot be null or empty");
        }
        
        String term = searchTerm.toLowerCase().trim();
        return records.values().stream()
            .filter(record -> {
                ContactInfo contactInfo = record.getContactInfo();
                return contactInfo.getEmail().toLowerCase().contains(term) ||
                       contactInfo.getPhoneNumber().contains(term);
            })
            .collect(Collectors.toList());
    }
    
    /**
     * Saves multiple medical records in batch.
     *
     * @param medicalRecords The list of medical records to save.
     * @throws IllegalArgumentException if the medical records list is null.
     */
    public void saveAll(List<MedicalRecord> medicalRecords) {
        if (medicalRecords == null) {
            throw new IllegalArgumentException("Medical records list cannot be null");
        }
        
        medicalRecords.forEach(this::save);
    }
    
    /**
     * Deletes multiple medical records in batch.
     *
     * @param patientIds The list of patient IDs to delete.
     * @throws IllegalArgumentException if the patient IDs list is null.
     */
    public void deleteAll(List<String> patientIds) {
        if (patientIds == null) {
            throw new IllegalArgumentException("Patient IDs list cannot be null");
        }
        
        patientIds.forEach(this::delete);
    }
    
    /**
     * Gets the total count of medical records in the repository.
     *
     * @return The total number of medical records.
     */
    public int getTotalCount() {
        return records.size();
    }
    
    /**
     * Gets statistics about the medical records in the repository, including counts by gender, blood type, and average age.
     *
     * @return A map containing the statistics.
     */
    public Map<String, Long> getStatistics() {
        Map<String, Long> stats = new HashMap<>();
        
        // Count by gender
        Map<Gender, Long> genderStats = records.values().stream()
            .collect(Collectors.groupingBy(MedicalRecord::getGender, Collectors.counting()));
        genderStats.forEach((gender, count) -> stats.put("gender_" + gender, count));
        
        // Count by blood type
        Map<BloodType, Long> bloodTypeStats = records.values().stream()
            .collect(Collectors.groupingBy(MedicalRecord::getBloodType, Collectors.counting()));
        bloodTypeStats.forEach((bloodType, count) -> stats.put("bloodType_" + bloodType, count));
        
        // Average age
        double avgAge = records.values().stream()
            .mapToInt(record -> Period.between(record.getDateOfBirth(), LocalDate.now()).getYears())
            .average()
            .orElse(0.0);
        stats.put("average_age", Math.round(avgAge));
        
        return stats;
    }

    /**
     * Clears all medical records from the repository.
     */
    @Override
    public void clearAll() {
        records.clear();
    }
}
