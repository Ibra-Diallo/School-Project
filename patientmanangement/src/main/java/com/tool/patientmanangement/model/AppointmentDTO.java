package com.tool.patientmanangement.model;

public class AppointmentDTO {

    private long id;
    private String patientName;
    private String doctorName;
    private String medicinesPrescribed;
    private String comments;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
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
}