package com.tool.patientmanangement.controller;

import com.tool.patientmanangement.model.Appointment;
import com.tool.patientmanangement.model.DoctorOperation;
import com.tool.patientmanangement.service.AppointmentService;
import com.tool.patientmanangement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

@RestController
public class OperationController {


    private UserService userService;
    private AppointmentService appointmentService;

    @Autowired
    public OperationController(UserService userService, AppointmentService appointmentService) {
        this.userService = userService;
        this.appointmentService = appointmentService;
    }

    @RequestMapping(value = {"/home"}, method = RequestMethod.GET)
    public ModelAndView getHomePage() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = ((UserDetails) auth.getPrincipal());

        // this method is used to get the home page of doctor or patient based on the role of the user
        if (auth != null && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("DOCTOR"))) {
            ModelAndView modelAndView = new ModelAndView();
            long userId = userService.getUserByUsername(userDetails.getUsername()).getId();
            modelAndView.addObject("temporary", new Appointment());
            modelAndView.addObject("showMessage", false);
            modelAndView.addObject("appointmentList", appointmentService.findDoctorAppointments(userId));
            modelAndView.setViewName("doctor_home_page");
            return modelAndView;
        } else {
            ModelAndView modelAndView = new ModelAndView();
            Appointment appointment = new Appointment();
            long userId = userService.getUserByUsername(userDetails.getUsername()).getId();
            appointment.setPatientId(userId);
            modelAndView.addObject("appointment", appointment);
            modelAndView.addObject("doctorList", userService.getAllDoctors());
            modelAndView.addObject("appointmentList", appointmentService.findPatientAppointments(userId));
            modelAndView.setViewName("patient_home_page");
            return modelAndView;
        }
    }

    @GetMapping("check/{id}")
    public ModelAndView searchAppointment(@PathVariable("id") long id,
                                          ModelAndView modelAndView) {
        // this is used to fetch appointment for the doctor
        modelAndView.addObject("appointment", appointmentService.findById(id));
        modelAndView.setViewName("appointment");
        return modelAndView;
    }

    @GetMapping("delete/{id}")
    public ModelAndView deleteAppointment(@PathVariable("id") long id,
                                          ModelAndView modelAndView) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = ((UserDetails) auth.getPrincipal());

        appointmentService.deleteAppointment(id);

        long userId = userService.getUserByUsername(userDetails.getUsername()).getId();
        modelAndView.addObject("temporary", new Appointment());
        modelAndView.addObject("showMessage", true);
        modelAndView.addObject("message", "Appointment Deleted!");
        modelAndView.addObject("appointmentList", appointmentService.findDoctorAppointments(userId));
        modelAndView.setViewName("doctor_home_page");
        return modelAndView;
    }

    @RequestMapping(value = "/add-appointment", method = RequestMethod.POST)
    public ModelAndView addAppointment(Appointment appointment, ModelAndView modelAndView) {

        // this is used to register a patient appointment request in the database
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = ((UserDetails) auth.getPrincipal());

        appointmentService.addAppointment(appointment);

        Appointment appointmentDetails = new Appointment();
        long userId = userService.getUserByUsername(userDetails.getUsername()).getId();
        appointment.setPatientId(userId);

        modelAndView.addObject("appointment", appointmentDetails);
        modelAndView.addObject("doctorList", userService.getAllDoctors());
        modelAndView.addObject("appointmentList", appointmentService.findPatientAppointments(userId));
        modelAndView.addObject("showMessage", true);
        modelAndView.addObject("message", "Appointment Added Successfully!");
        modelAndView.setViewName("patient_home_page");
        return modelAndView;

    }

    @RequestMapping(value = "/appointment", method = RequestMethod.POST)
    public ModelAndView appointment(Appointment appointment, ModelAndView modelAndView) {
        // this method is used to add comments and feedback to a patient appointment from the doctor page.
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = ((UserDetails) auth.getPrincipal());

        appointmentService.addAppointmentForDoctor(appointment);

        long userId = userService.getUserByUsername(userDetails.getUsername()).getId();
        appointment.setPatientId(userId);

        modelAndView.addObject("appointmentList", appointmentService.findDoctorAppointments(userId));
        modelAndView.addObject("showMessage", true);
        modelAndView.addObject("temporary", new Appointment());
        modelAndView.addObject("message", "Medicine Prescribed successfully!");
        modelAndView.setViewName("doctor_home_page");
        return modelAndView;

    }

    @RequestMapping(value = "/delete-doctor", method = RequestMethod.POST)
    public ModelAndView deleteDoctor(DoctorOperation doctorOperation, ModelAndView modelAndView) {
        // this method is used to add comments and feedback to a patient appointment from the doctor page.
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = ((UserDetails) auth.getPrincipal());

        userService.deleteDoctor(doctorOperation);


        List<String> doctorsDepartment = new ArrayList<>();
        doctorsDepartment.add("Anesthesiologist");
        doctorsDepartment.add("Cardiologist");
        doctorsDepartment.add("Dermatologist");
        doctorsDepartment.add("Physicians");
        doctorsDepartment.add("Gastroenterologist");
        doctorsDepartment.add("Oncologist");
        doctorsDepartment.add("Psychiatrist");

        DoctorOperation dOperation = new DoctorOperation();
        long userId = userService.getUserByUsername(userDetails.getUsername()).getId();
        modelAndView.addObject("department", doctorsDepartment);
        modelAndView.addObject("doctorOperation", dOperation);
        modelAndView.addObject("doctorList", userService.getAllDoctors());
        modelAndView.addObject("appointmentList", appointmentService.findPatientAppointments(userId));

        modelAndView.addObject("showMessage", true);
        modelAndView.addObject("message", "Doctor Deleted Successfully");

        modelAndView.setViewName("admin_home_page");
        return modelAndView;

    }


    @RequestMapping(value = "/update-doctor", method = RequestMethod.POST)
    public ModelAndView updateDoctor(DoctorOperation doctorOperation, ModelAndView modelAndView) {
        // this method is used to add comments and feedback to a patient appointment from the doctor page.
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = ((UserDetails) auth.getPrincipal());

        userService.updateDoctor(doctorOperation);


        List<String> doctorsDepartment = new ArrayList<>();
        doctorsDepartment.add("Anesthesiologist");
        doctorsDepartment.add("Cardiologist");
        doctorsDepartment.add("Dermatologist");
        doctorsDepartment.add("Physician");
        doctorsDepartment.add("Gastroenterologist");
        doctorsDepartment.add("Oncologists");
        doctorsDepartment.add("Psychiatrist");


        DoctorOperation dOperation = new DoctorOperation();
        long userId = userService.getUserByUsername(userDetails.getUsername()).getId();

        modelAndView.addObject("department", doctorsDepartment);
        modelAndView.addObject("doctorOperation", dOperation);
        modelAndView.addObject("doctorList", userService.getAllDoctors());
        modelAndView.addObject("appointmentList", appointmentService.findPatientAppointments(userId));

        modelAndView.addObject("showMessage", true);
        modelAndView.addObject("message", "Department Updated Successfully");
        modelAndView.setViewName("admin_home_page");
        return modelAndView;

    }
}
