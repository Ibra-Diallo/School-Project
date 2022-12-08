package com.tool.patientmanangement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class PatientManagementApplication {
    public static void main(String[] args) {
        SpringApplication.run(PatientManagementApplication.class, args);
    }

    @Bean
    public BCryptPasswordEncoder getEncoder() {
        return new BCryptPasswordEncoder(); //This injects the bean into the spring container.
    }
}
