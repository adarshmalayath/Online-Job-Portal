package com.employment.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.Contact;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Local Employment & Skills Matching Service API", version = "1.0", description = "SOA Groupwork 2 – REST implementation of the Employment and Skills Matching Service. "
        +
        "Implements ReqInt (user-facing), ODPInt (job data adapter), and CAPInt (course API adapter).", contact = @Contact(name = "Group 7 – CO7214", email = "am1691@student.le.ac.uk")))
public class EmploymentServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(EmploymentServiceApplication.class, args);
    }
}
