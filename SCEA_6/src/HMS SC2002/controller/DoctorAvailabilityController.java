//controller/DoctorAvailabilityController.java

package controller;

import java.time.*;
import java.util.*;

import controller.interfaces.DoctorAvailabilityService;
import entity.*;
import repository.DoctorAvailabilityRepository;
import repository.StaffRepository;

public class DoctorAvailabilityController implements DoctorAvailabilityService {
    private final DoctorAvailabilityRepository availabilityRepository;
    private final StaffRepository staffRepository;
    private final int SLOT_DURATION_MINUTES = 30;
    
    /**
     * Constructor to initialize the DoctorAvailabilityController with necessary repositories.
     */
    public DoctorAvailabilityController() {
        this.availabilityRepository = DoctorAvailabilityRepository.getInstance();
        this.staffRepository = StaffRepository.getInstance();
    }
    
    /**
     * Gets a list of available doctors on a given date.
     * A doctor is considered available if they have set availability on the given date 
     * and if the time is not in the past (for the current day).
     * 
     * @param date The date to check for available doctors.
     * @return A list of available doctors for the given date.
     */
    @Override
    public List<Doctor> getAvailableDoctors(LocalDate date) {
        // Get all doctors first
        List<Doctor> allDoctors = staffRepository.findAllDoctors();
        
        // Filter doctors who have availability on the given date
        return allDoctors.stream()
            .filter(doctor -> {
                Optional<DoctorAvailability> availability = 
                    availabilityRepository.findByDoctorAndDate(doctor, date);
                    
                return availability.map(avail -> 
                    // For current date, check if end time hasn't passed
                    date.equals(LocalDate.now()) ? 
                        avail.getEndTime().isAfter(LocalTime.now()) : true
                ).orElse(false);
            })
            .sorted(Comparator.comparing(Doctor::getName))
            .toList();
    }

    /**
     * Sets the availability for a doctor on a specific date and time range.
     * The method checks for validity of inputs such as non-null parameters, valid time range, 
     * and that the date is not in the past. If valid, it saves the availability.
     * 
     * @param doctor The doctor for whom availability is being set.
     * @param date The date the doctor is available.
     * @param startTime The start time of the doctor's availability.
     * @param endTime The end time of the doctor's availability.
     * @throws IllegalArgumentException if any of the parameters are invalid.
     */
    @Override
    public void setAvailability(Doctor doctor, LocalDate date, 
                              LocalTime startTime, LocalTime endTime) {
        // Validate inputs
        if (doctor == null || date == null || startTime == null || endTime == null) {
            throw new IllegalArgumentException("All parameters must be non-null");
        }
        
        if (endTime.isBefore(startTime) || endTime.equals(startTime)) {
            throw new IllegalArgumentException("End time must be after start time");
        }
        
        if (date.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Cannot set availability for past dates");
        }
        
        // If setting availability for today, validate times
        if (date.equals(LocalDate.now()) && startTime.isBefore(LocalTime.now())) {
            throw new IllegalArgumentException("Cannot set availability starting in the past");
        }
        
        // Create and save availability
        DoctorAvailability availability = new DoctorAvailability(doctor, date, startTime, endTime);
        availabilityRepository.save(availability);
        
        // Update doctor's own record
        doctor.setAvailability(availability);
        staffRepository.save(doctor);
    }
    
    /**
     * Generates appointment slots for a doctor based on their availability for a given date.
     * The method divides the availability time into slots of fixed duration and returns them.
     * 
     * @param availability The doctor's availability for a given date.
     * @return A list of appointment slots.
     */
    @Override
    public List<AppointmentSlot> generateSlots(DoctorAvailability availability) {
        // Return empty list if availability is null
        if (availability == null) {
            return new ArrayList<>();
        }
        
        List<AppointmentSlot> slots = new ArrayList<>();
        LocalTime currentTime = availability.getStartTime();
        LocalTime endTime = availability.getEndTime();
        
        // For current date, don't generate slots for times that have already passed
        if (availability.getDate().equals(LocalDate.now())) {
            LocalTime now = LocalTime.now();
            if (currentTime.isBefore(now)) {
                // Round up to next slot
                currentTime = now.plusMinutes(SLOT_DURATION_MINUTES - 
                    (now.getMinute() % SLOT_DURATION_MINUTES));
            }
        }
        
        // Generate slots
        while (currentTime.plusMinutes(SLOT_DURATION_MINUTES).isBefore(endTime) || 
               currentTime.plusMinutes(SLOT_DURATION_MINUTES).equals(endTime)) {
            
            AppointmentSlot slot = new AppointmentSlot(
                UUID.randomUUID().toString(),
                currentTime,
                currentTime.plusMinutes(SLOT_DURATION_MINUTES),
                availability.getDoctor(),
                availability.getDate()
            );
            
            slots.add(slot);
            currentTime = currentTime.plusMinutes(SLOT_DURATION_MINUTES);
        }
        
        return slots;
    }
    
    /**
     * Retrieves an appointment slot for a doctor on a specific date and time.
     * The method checks if the doctor is available at the given time and returns the corresponding slot.
     * 
     * @param doctor The doctor for whom the slot is being retrieved.
     * @param date The date to check for availability.
     * @param time The time to check for availability.
     * @return The appointment slot if available, otherwise null.
     */
    @Override
    public AppointmentSlot getSlotByDateTime(Doctor doctor, LocalDate date, LocalTime time) {
        // Get doctor's availability for the date
        Optional<DoctorAvailability> availability = 
            availabilityRepository.findByDoctorAndDate(doctor, date);
            
        if (availability.isEmpty()) {
            return null;
        }
        
        DoctorAvailability avail = availability.get();
        
        // Check if time falls within availability
        if (time.isBefore(avail.getStartTime()) || 
            time.isAfter(avail.getEndTime().minusMinutes(SLOT_DURATION_MINUTES))) {
            return null;
        }
        
        // Generate and find matching slot
        return generateSlots(avail).stream()
            .filter(slot -> slot.getStartTime().equals(time))
            .findFirst()
            .orElse(null);
    }
    
    /**
     * Retrieves all availabilities for a specific doctor.
     * 
     * @param doctor The doctor whose availabilities are to be retrieved.
     * @return A list of DoctorAvailability for the specified doctor.
     */
    public List<DoctorAvailability> getDoctorAvailabilities(Doctor doctor) {
        return availabilityRepository.findByDoctor(doctor);
    }
    
    /**
     * Retrieves all availabilities for a specific date.
     * 
     * @param date The date to check for doctor availabilities.
     * @return A list of DoctorAvailability for the specified date.
     */
    public List<DoctorAvailability> getAvailabilitiesByDate(LocalDate date) {
        return availabilityRepository.findByDate(date);
    }
    
    /**
     * Removes a doctor's availability for a specific date.
     * 
     * @param doctor The doctor whose availability is to be removed.
     * @param date The date of the availability to remove.
     * @return true if the availability was successfully removed, false otherwise.
     */
    public boolean removeAvailability(Doctor doctor, LocalDate date) {
        Optional<DoctorAvailability> availability = 
            availabilityRepository.findByDoctorAndDate(doctor, date);
            
        if (availability.isPresent()) {
            availabilityRepository.delete(availability.get().getId());
            return true;
        }
        return false;
    }
    
    /**
     * Updates the availability of a doctor for a specific date.
     * The method checks if the availability exists and then attempts to set the new availability.
     * 
     * @param doctor The doctor whose availability is to be updated.
     * @param date The date to update the availability for.
     * @param newStartTime The new start time of the doctor's availability.
     * @param newEndTime The new end time of the doctor's availability.
     * @return true if the availability was updated successfully, false otherwise.
     */
    public boolean updateAvailability(Doctor doctor, LocalDate date, 
                                    LocalTime newStartTime, LocalTime newEndTime) {
        Optional<DoctorAvailability> existing = 
            availabilityRepository.findByDoctorAndDate(doctor, date);
            
        if (existing.isEmpty()) {
            return false;
        }
        
        try {
            setAvailability(doctor, date, newStartTime, newEndTime);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
    
    /**
     * Checks if a doctor is available at a specific date and time.
     * 
     * @param doctor The doctor to check availability for.
     * @param date The date to check.
     * @param time The time to check.
     * @return true if the doctor is available, false otherwise.
     */
    public boolean isDoctorAvailable(Doctor doctor, LocalDate date, LocalTime time) {
        Optional<DoctorAvailability> availability = 
            availabilityRepository.findByDoctorAndDate(doctor, date);
            
        if (availability.isEmpty()) {
            return false;
        }
        
        DoctorAvailability avail = availability.get();
        return !time.isBefore(avail.getStartTime()) && 
               !time.isAfter(avail.getEndTime().minusMinutes(SLOT_DURATION_MINUTES));
    }
    
    /**
     * Retrieves the next available appointment slot for a doctor within the next 7 days.
     * The method checks each day for availability and returns the first available slot.
     * 
     * @param doctor The doctor for whom the next available slot is being checked.
     * @return An Optional containing the next available appointment slot, if found.
     */
    public Optional<AppointmentSlot> getNextAvailableSlot(Doctor doctor) {
        LocalDate currentDate = LocalDate.now();
        
        // Check next 7 days
        for (int i = 0; i < 7; i++) {
            LocalDate date = currentDate.plusDays(i);
            Optional<DoctorAvailability> availability = 
                availabilityRepository.findByDoctorAndDate(doctor, date);
                
            if (availability.isPresent()) {
                List<AppointmentSlot> slots = generateSlots(availability.get());
                Optional<AppointmentSlot> availableSlot = slots.stream()
                    .filter(AppointmentSlot::isAvailable)
                    .findFirst();
                    
                if (availableSlot.isPresent()) {
                    return availableSlot;
                }
            }
        }
        
        return Optional.empty();
    }
}
