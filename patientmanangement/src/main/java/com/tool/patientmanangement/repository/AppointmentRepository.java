package com.tool.patientmanangement.repository;

import com.tool.patientmanangement.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByPatientIdAndAppointmentDone(long patientId, boolean appointmentDone);

    List<Appointment> findByDoctorIdAndAppointmentDone(long doctorId, boolean appointmentDone);
}