package com.skpijtk.springboot_boilerplate.dto.request.admin.studentmanagement;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateStudentRequest {
    @NotBlank(message = "Student name cannot be blank")
    @Size(min = 3, max = 101, message = "Student name must be between 3 and 101 characters")
    @Pattern(regexp = "^[A-Za-z]+( [A-Za-z]+)+$", message = "Student name must only contain letters and spaces, at least two words, no trailing spaces")
    private String studentName;

    @NotBlank(message = "NIM cannot be blank")
    @Size(min = 9, max = 9, message = "NIM must be exactly 9 characters")
    @Pattern(regexp = "^[0-9]{9}$", message = "NIM must contain only digits and no spaces")
    private String nim;

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Invalid email format")
    @Size(min = 1, max = 50, message = "Email must be between {min} and {max} characters")
    @Pattern(regexp = "^[^\\s@]+@[^\\s@]+\\.[A-Za-z]{2,}$", message = "Email must contain a valid domain (e.g. .com, .id, .org) and no spaces")
    private String email;
}