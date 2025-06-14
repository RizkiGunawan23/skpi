package com.skpijtk.springboot_boilerplate.validation;

import com.skpijtk.springboot_boilerplate.dto.request.admin.dashboard.StudentCheckinListQuery;
import com.skpijtk.springboot_boilerplate.dto.response.FieldErrorResponse;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class StudentCheckinListValidator {
    public static List<FieldErrorResponse> validate(StudentCheckinListQuery query) {
        List<String> allowedSortBy = List.of("nim", "status");

        List<FieldErrorResponse> errors = new ArrayList<>();

        // Validasi startdate dan enddate
        LocalDate start = null, end = null;
        if (query.getStartdate() != null && !query.getStartdate().isBlank()) {
            try {
                start = LocalDate.parse(query.getStartdate());
            } catch (DateTimeParseException e) {
                errors.add(new FieldErrorResponse("startdate", "startdate format is not valid (yyyy-MM-dd)"));
            }
        }
        if (query.getEnddate() != null && !query.getEnddate().isBlank()) {
            try {
                end = LocalDate.parse(query.getEnddate());
            } catch (DateTimeParseException e) {
                errors.add(new FieldErrorResponse("enddate", "enddate format is not valid (yyyy-MM-dd)"));
            }
        }
        if (start != null && end != null && start.isAfter(end)) {
            errors.add(new FieldErrorResponse("startdate", "startdate must be before or equal to enddate"));
        }

        // Validasi sortBy dan sortDir
        if (!allowedSortBy.contains(query.getSortBy()))
            errors.add(new FieldErrorResponse("sortBy", "sortBy must be one of: " + String.join(", ", allowedSortBy)));

        if (!query.getSortDir().equalsIgnoreCase("asc") && !query.getSortDir().equalsIgnoreCase("desc"))
            errors.add(new FieldErrorResponse("sortDir", "sortDir must be either 'asc' or 'desc'"));

        // Validasi page dan size
        if (query.getPage() != null && !query.getPage().isBlank()) {
            try {
                int page = Integer.parseInt(query.getPage());
                if (page < 0)
                    errors.add(new FieldErrorResponse("page", "page must be >= 0"));
            } catch (NumberFormatException e) {
                errors.add(new FieldErrorResponse("page", "page must be a valid integer"));
            }
        }
        if (query.getSize() != null && !query.getSize().isBlank()) {
            try {
                int size = Integer.parseInt(query.getSize());
                if (size <= 0)
                    errors.add(new FieldErrorResponse("size", "size must be > 0"));
            } catch (NumberFormatException e) {
                errors.add(new FieldErrorResponse("size", "size must be a valid integer"));
            }
        }

        return errors;
    }
}