package com.forero.school.domain.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CodeException {
    EMPTY_LIST("No %s found in your school."),
    INTERNAL_SERVER_ERROR("Internal server error"),
    DUPLICATE_STUDENT_IN_EXCEL("Student %s is duplicated in Excel"),
    STUDENT_NOT_FOUND("Student %s not found"),
    PDF_GENERATION_ERROR("Error generating PDF"),
    RESOURCE_NOT_FOUND("Resource not found"),
    SUBJECT_NOT_FOUND("Subject not found"),
    INVALID_PARAMETERS("Invalid request parameters. Please check the %s and try again.");

    private final String messageFormat;
}
