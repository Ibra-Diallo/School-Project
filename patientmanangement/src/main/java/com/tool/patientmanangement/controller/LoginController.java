package com.tool.patientmanangement.controller;

import com.tool.patientmanangement.model.Appointment;
import com.tool.patientmanangement.model.DoctorOperation;
import com.tool.patientmanangement.service.AppointmentService;
import com.tool.patientmanangement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

@Controller
public class LoginController {

    private UserService userService;
    private AppointmentService appointmentService;

    @Autowired
    public LoginController(UserService userService, AppointmentService appointmentService) {
        this.userService = userService;
        this.appointmentService = appointmentService;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String getIndexPage(Model model) { // This function will return our index.html page and will display its content on the UI.
        return "index";
    }

    @RequestMapping(value = "/loginPage", method = RequestMethod.GET)
    public String getLoginPage(Model model) {// This function will return our index.html page and will display its content on the UI.
        return "login";
    }

    @RequestMapping(value = "/loginSuccess", method = RequestMethod.GET)
    public ModelAndView login(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = ((UserDetails) auth.getPrincipal());

        // this method is used to get the home page of doctor or patient based on the role of the user
        if (auth != null && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("Doctor"))) {
            ModelAndView modelAndView = new ModelAndView();
            long userId = userService.getUserByUsername(userDetails.getUsername()).getId();
            modelAndView.addObject("temporary", new Appointment());
            modelAndView.addObject("showMessage", false);
            modelAndView.addObject("appointmentList", appointmentService.findDoctorAppointments(userId));
            modelAndView.setViewName("doctor_home_page");
            return modelAndView;
        }
        if (auth != null && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("Admin"))) {
            ModelAndView modelAndView = new ModelAndView();


            List<String> doctorsDepartment = new ArrayList<>();
            doctorsDepartment.add("Anesthesiologists");
            doctorsDepartment.add("Cardiologists");
            doctorsDepartment.add("Dermatologists");
            doctorsDepartment.add("Family Physicians");
            doctorsDepartment.add("Gastroenterologists");
            doctorsDepartment.add("Oncologists");
            doctorsDepartment.add("Psychiatrists");


            long userId = userService.getUserByUsername(userDetails.getUsername()).getId();

            DoctorOperation dOperation = new DoctorOperation();
            modelAndView.addObject("department", doctorsDepartment);
            modelAndView.addObject("doctorOperation", dOperation);
            modelAndView.addObject("doctorList", userService.getAllDoctors());
            modelAndView.addObject("appointmentList", appointmentService.findPatientAppointments(userId));
            modelAndView.setViewName("admin_home_page");
            return modelAndView;
        } else {
            ModelAndView modelAndView = new ModelAndView();
            Appointment appointment = new Appointment();
            long userId = userService.getUserByUsername(userDetails.getUsername()).getId();
            appointment.setPatientId(userId);
            modelAndView.addObject("patientName", userDetails.getUsername());
            modelAndView.addObject("appointment", appointment);
            modelAndView.addObject("doctorList", userService.getAllDoctors());
            modelAndView.addObject("appointmentList", appointmentService.findPatientAppointments(userId));
            modelAndView.setViewName("patient_home_page");
            return modelAndView;
        }
    }
}
