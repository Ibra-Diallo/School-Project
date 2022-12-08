package com.tool.patientmanangement.controller;

import com.tool.patientmanangement.model.User;
import com.tool.patientmanangement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class RegisterController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/registerPage", method = RequestMethod.GET)
    public String getRegistrationPage(Model model) {
        model.addAttribute("user", new User());//Here we're adding attribute for our model which will be linked to our user object, so that the information passed during registration are saved in that new user.
        return "register";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String register(@ModelAttribute("user") User user, Model model) {//The @ModelAttribute maps the references of the user created at line 20 after submission to this new user getting created here for verification
        String output = userService.register(user);
        if (output == null) { //This null will be coming from the register function in the UserService Class. It basically means the user is already present.
            model.addAttribute("user", user);
            model.addAttribute("showErrorMessage", true);
            model.addAttribute("errorMessage", "Username is already present! Please try with different name");
            return "register";
        }
        model.addAttribute("user", new User());
        model.addAttribute("showErrorMessage", false);
        return "login";
    }
}
