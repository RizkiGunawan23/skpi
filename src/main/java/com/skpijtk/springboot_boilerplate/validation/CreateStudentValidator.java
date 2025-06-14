package com.skpijtk.springboot_boilerplate.validation;

import com.skpijtk.springboot_boilerplate.dto.request.admin.studentmanagement.CreateStudentRequest;
import com.skpijtk.springboot_boilerplate.dto.response.FieldErrorResponse;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CreateStudentValidator {
    public static List<FieldErrorResponse> validate(CreateStudentRequest req, LocalDate joinDate) {
        List<FieldErrorResponse> errors = new ArrayList<>();

        String[] parts = req.getStudentName().trim().split("\\s+");
        for (String part : parts) {
            if (part.length() > 50) {
                errors.add(new FieldErrorResponse("studentName", "Each name part must be at most 50 characters"));
            }
        }

        String expectedPrefix = String.format("%02d", joinDate.getYear() % 100);
        if (!req.getNim().startsWith(expectedPrefix)) {
            errors.add(new FieldErrorResponse("nim",
                    "NIM must start with last two digits of join year (" + expectedPrefix + ")"));
        }

        return errors;
    }
}