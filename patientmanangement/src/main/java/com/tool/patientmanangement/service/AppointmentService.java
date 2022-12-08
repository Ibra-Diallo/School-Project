package com.tool.patientmanangement.service;

import com.tool.patientmanangement.model.Appointment;
import com.tool.patientmanangement.model.AppointmentDTO;
import com.tool.patientmanangement.repository.AppointmentRepository;
import com.tool.patientmanangement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AppointmentService {

    private AppointmentRepository appointmentRepository;
    private UserRepository userRepository;

    @Autowired
    public AppointmentService(AppointmentRepository appointmentRepository, UserRepository userRepository) {
        this.appointmentRepository = appointmentRepository;
        this.userRepository = userRepository;
    }

    public void addAppointment(Appointment appointment) {
        appointment.setAppointmentDone(false);
        appointmentRepository.save(appointment);
    }

    public void addAppointmentForDoctor(Appointment appointment) {
        appointment.setAppointmentDone(true);
        appointmentRepository.save(appointment);
    }

    public Appointment findById(Long id) {
        return appointmentRepository.findById(id).get();
    }

    public String deleteAppointment(Long id) {
        appointmentRepository.deleteById(id);
        return "Appointment Deleted";
    }

    // it is used to fetch the appointment based on the patient id. The appointments are then converted in to appointmentsDTO
    // so that the additional information such as patientName and doctorName can be displayed on the html
    public List<AppointmentDTO> findPatientAppointments(long patientID) {
        List<Appointment> appointments = appointmentRepository.findByPatientIdAndAppointmentDone(patientID, true);
        List<AppointmentDTO> result = new ArrayList<>();
        appointments.forEach(appointment -> {
            AppointmentDTO appointmentDTO = new AppointmentDTO();
            appointmentDTO.setPatientName(userRepository.findById(appointment.getPatientId()).get().getFirstName());
            appointmentDTO.setDoctorName(userRepository.findById(appointment.getDoctorId()).get().getFirstName());
            appointmentDTO.setComments(appointment.getComments());
            appointmentDTO.setMedicinesPrescribed(appointment.getMedicinesPrescribed());
            result.add(appointmentDTO);
        });

        return result;
    }

    // this is used to fetch all the appointments related to doctor. The data for comments and medicinePrescribed are set
    // to NOT PROVIDED.
    public List<AppointmentDTO> findDoctorAppointments(long doctorId) {
        List<Appointment> appointments = appointmentRepository.findByDoctorIdAndAppointmentDone(doctorId, false);
        List<AppointmentDTO> result = new ArrayList<>();
        appointments.forEach(appointment -> {
            AppointmentDTO appointmentDTO = new AppointmentDTO();
            appointmentDTO.setPatientName(userRepository.findById(appointment.getPatientId()).get().getFirstName());
            appointmentDTO.setDoctorName(userRepository.findById(appointment.getDoctorId()).get().getFirstName());
            appointmentDTO.setComments("NOT PROVIDED");
            appointmentDTO.setMedicinesPrescribed("NOT PROVIDED");
            appointmentDTO.setId(appointment.getId());
            result.add(appointmentDTO);
        });

        return result;
    }
}