//controller/interfaces/DoctorAvailabilityService.java

package controller.interfaces;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import entity.AppointmentSlot;
import entity.Doctor;
import entity.DoctorAvailability;

/**
 * Interface for managing doctor availability and appointment slot generation.
 */
public interface DoctorAvailabilityService {

    /**
     * Sets the availability of a doctor for a specific date and time range.
     *
     * @param doctor    The Doctor whose availability is being set.
     * @param date      The date for which the availability is set.
     * @param startTime The start time of the availability period.
     * @param endTime   The end time of the availability period.
     */
    void setAvailability(Doctor doctor, LocalDate date, LocalTime startTime, LocalTime endTime);

    /**
     * Generates appointment slots based on a doctor's availability.
     *
     * @param availability The DoctorAvailability object containing availability details.
     * @return A list of AppointmentSlot objects representing available slots within the specified availability.
     */
    List<AppointmentSlot> generateSlots(DoctorAvailability availability);

    /**
     * Retrieves a list of doctors who are available on a specific date.
     *
     * @param date The date for which available doctors are to be retrieved.
     * @return A list of Doctor objects representing doctors who have availability on the specified date.
     */
    List<Doctor> getAvailableDoctors(LocalDate date);

    /**
     * Retrieves a specific appointment slot for a doctor based on date and time.
     *
     * @param doctor The Doctor for whom the slot is being retrieved.
     * @param date   The date of the desired appointment slot.
     * @param time   The time of the desired appointment slot.
     * @return The AppointmentSlot object matching the specified doctor, date, and time, or null if no slot is found.
     */
    AppointmentSlot getSlotByDateTime(Doctor doctor, LocalDate date, LocalTime time);
}
