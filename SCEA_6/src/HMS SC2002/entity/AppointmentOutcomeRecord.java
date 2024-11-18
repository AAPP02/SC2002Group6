//entity/AppointmentOutcomeRecord.java

package entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents the outcome record of an appointment, containing details such as the appointment date,
 * service type, prescriptions, and consultation notes.
 */
public class AppointmentOutcomeRecord {

    /** The date of the appointment. */
    private final LocalDate appointmentDate;

    /** The type of service provided during the appointment. */
    private final String serviceType;

    /** The list of prescriptions provided during the appointment. */
    private final List<Prescription> prescriptions;

    /** Notes from the consultation, if any. */
    private final String consultationNotes;

    /**
     * Constructs a new appointment outcome record with the given appointment date, service type,
     * list of prescriptions, and consultation notes.
     * 
     * @param appointmentDate The date of the appointment.
     * @param serviceType The type of service provided during the appointment.
     * @param prescriptions A list of prescriptions given during the appointment.
     * @param consultationNotes Notes from the consultation.
     */
    public AppointmentOutcomeRecord(LocalDate appointmentDate, String serviceType, 
                                  List<Prescription> prescriptions, String consultationNotes) {
        this.appointmentDate = appointmentDate;
        this.serviceType = serviceType;
        this.prescriptions = prescriptions;
        this.consultationNotes = consultationNotes;
    }

    /**
     * Returns a string representation of the appointment outcome record, including the appointment date,
     * service type, prescriptions, and consultation notes.
     * 
     * @return A string representing the appointment outcome record.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Date: %s\n", appointmentDate));
        sb.append(String.format("Service: %s\n", serviceType));
        sb.append("Prescriptions:\n");
        prescriptions.forEach(p -> sb.append(p.toString()).append("\n"));
        sb.append(String.format("Notes: %s", consultationNotes));
        return sb.toString();
    }

    /**
     * Returns the date of the appointment.
     * 
     * @return The appointment date.
     */
    public LocalDate getAppointmentDate() {
        return appointmentDate;
    }

    /**
     * Returns the type of service provided during the appointment.
     * 
     * @return The service type.
     */
    public String getServiceType() {
        return serviceType;
    }

    /**
     * Returns the list of prescriptions given during the appointment.
     * 
     * @return The list of prescriptions.
     */
    public List<Prescription> getPrescriptions() {
        return prescriptions;
    }

    /**
     * Returns the consultation notes for the appointment.
     * 
     * @return The consultation notes.
     */
    public String getConsultationNotes() {
        return consultationNotes;
    }
}
