package com.employment.service.dto;

import jakarta.validation.constraints.*;
import java.util.List;

public class ProfileRequest {
    @NotEmpty(message = "Skills list must not be empty")
    public List<String> skills;

    public String preferences;

    @NotBlank(message = "Location is required")
    public String location;
}
