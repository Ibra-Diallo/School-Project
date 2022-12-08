package com.tool.patientmanangement.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private long patientId;
    private long doctorId;
    private String medicinesPrescribed;
    private String comments;
    private boolean appointmentDone;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getPatientId() {
        return patientId;
    }

    public void setPatientId(long patientId) {
        this.patientId = patientId;
    }

    public long getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(long doctorId) {
        this.doctorId = doctorId;
    }

    public String getMedicinesPrescribed() {
        return medicinesPrescribed;
    }

    public void setMedicinesPrescribed(String medicinesPrescribed) {
        this.medicinesPrescribed = medicinesPrescribed;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public boolean isAppointmentDone() {
        return appointmentDone;
    }

    public void setAppointmentDone(boolean appointmentDone) {
        this.appointmentDone = appointmentDone;
    }
}