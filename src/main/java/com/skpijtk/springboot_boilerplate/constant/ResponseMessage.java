package com.skpijtk.springboot_boilerplate.constant;

public enum ResponseMessage {
    SIGNUP_SUCCESS("Signup successful"),
    LOGIN_SUCCESS("Login successful"),
    STUDENT_FOUND("Student data successfully found"),
    DATA_DISPLAY_SUCCESS("Data successfully displayed"),
    UPDATE_STUDENT_SUCCESS("Student {studentName} successfully updated"),
    DELETE_STUDENT_SUCCESS("Student {studentName} successfully deleted"),
    CHECKIN_SUCCESS("Checkin successful"),
    CHECKOUT_SUCCESS("Checkout successful"),
    DATA_SAVE_SUCCESS("Data successfully saved"),
    APP_SETTINGS_UPDATE_SUCCESS("App settings successfully updated"),

    CHECKIN_LATE("Your checkin is late"),
    CHECKOUT_LATE("Your checkout is late"),

    INVALID_CREDENTIALS("Invalid email or password"),
    INTERNAL_SERVER_ERROR("Internal server error. Please try again later"),
    STUDENT_NOT_FOUND("Student data not found"),
    USER_NOT_FOUND("User not found with email {email}"),
    APP_SETTINGS_NOT_FOUND("App settings not found"),
    DATA_DISPLAY_ERROR("Data failed to display"),
    DELETE_STUDENT_FAILED("Student {studentName} deletion failed"),
    EMAIL_ALREADY_EXISTS("Email has been used"),
    CHECKIN_FAILED("Checkin failed because the note is empty"),
    CHECKOUT_FAILED("Checkout failed because the note is empty"),
    DATA_SAVE_FAILED("Data failed to saved"),
    STUDENT_ALREADY_CHECKED_IN("Student {studentName} has already checked in today"),
    STUDENT_NOT_YET_CHECKED_OUT_YESTERDAY(
            "Student {studentName} has not checked out yesterday, please check out first"),
    STUDENT_NOT_YET_CHECK_IN_TODAY("Student {studentName} has not checked in today, please check in first"),
    STUDENT_ALREADY_CHECKED_OUT("Student {studentName} has already checked out today");

    private final String message;

    ResponseMessage(String message) {
        this.message = message;
    }

    public String format(Object... args) {
        String formatted = message;
        for (Object arg : args) {
            formatted = formatted.replaceFirst("\\{[^}]+\\}", arg != null ? arg.toString() : "");
        }
        return formatted;
    }

    @Override
    public String toString() {
        return message;
    }

}
