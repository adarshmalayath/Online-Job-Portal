package com.employment.service.dto;

import jakarta.validation.constraints.*;
import java.util.List;

// DTO: Register request
public class RegisterRequest {
    @NotBlank public String name;
    @NotBlank @Email public String email;
    @NotBlank @Size(min = 8) public String password;
    @NotBlank public String location;
}
