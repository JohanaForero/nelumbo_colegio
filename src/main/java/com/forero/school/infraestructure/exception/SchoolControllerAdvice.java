package com.forero.school.infraestructure.exception;

import com.forero.school.application.exception.CoreException;
import com.forero.school.domain.exception.CodeException;
import com.forero.school.infraestructure.controller.RegisterController;
import com.forero.school.openapi.model.ErrorObjectDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import java.util.AbstractMap;
import java.util.Map;

@Slf4j
@ControllerAdvice(assignableTypes = RegisterController.class)
public class SchoolControllerAdvice {
    private static final String LOGGER_PREFIX = String.format("[%s] ", RegisterController.class.getSimpleName());

    private static final Map<CodeException, HttpStatus> HTTP_STATUS_BY_CODE_EXCEPTION = Map.ofEntries(
            new AbstractMap.SimpleEntry<>(CodeException.EMPTY_LIST, HttpStatus.NOT_FOUND),
            new AbstractMap.SimpleEntry<>(CodeException.INVALID_PARAMETERS, HttpStatus.BAD_REQUEST),
            new AbstractMap.SimpleEntry<>(CodeException.STUDENT_NOT_FOUND, HttpStatus.BAD_REQUEST),
            new AbstractMap.SimpleEntry<>(CodeException.SUBJECT_NOT_FOUND, HttpStatus.BAD_REQUEST),
            new AbstractMap.SimpleEntry<>(CodeException.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR)
    );

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorObjectDto> handleMissingServletRequestParameterException(
            final MissingServletRequestParameterException missingServletRequestParameterException) {
        final CodeException codeException = CodeException.INVALID_PARAMETERS;
        final String paramName = missingServletRequestParameterException.getParameterName();

        final ErrorObjectDto errorObjectDto = new ErrorObjectDto();
        errorObjectDto.message(String.format(codeException.getMessageFormat(), paramName));

        final HttpStatus httpStatus = HTTP_STATUS_BY_CODE_EXCEPTION.getOrDefault(codeException, HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(errorObjectDto, httpStatus);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorObjectDto> handleHttpMessageNotReadableException(
            final HttpMessageNotReadableException httpMessageNotReadableException) {
        final CodeException codeException = CodeException.INVALID_PARAMETERS;
        final String paramName = "filters";
        final ErrorObjectDto errorObjectDto = new ErrorObjectDto();
//        errorObjectDto.setCode(codeException.name());
        errorObjectDto.setMessage(String.format(codeException.getMessageFormat(), paramName));

        final HttpStatus httpStatus = HTTP_STATUS_BY_CODE_EXCEPTION.getOrDefault(codeException, HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(errorObjectDto, httpStatus);
    }

    @ExceptionHandler(MissingServletRequestPartException.class)
    public ResponseEntity<ErrorObjectDto> handleMissingServletRequestPartException(
            final MissingServletRequestPartException missingServletRequestPartException) {
        final CodeException codeException = CodeException.INVALID_PARAMETERS;
        final String paramName = missingServletRequestPartException.getRequestPartName();

        final ErrorObjectDto errorObjectDto = new ErrorObjectDto();
//        errorObjectDto.code(codeException.name());
        errorObjectDto.message(String.format(codeException.getMessageFormat(), paramName));

        final HttpStatus httpStatus = HTTP_STATUS_BY_CODE_EXCEPTION.getOrDefault(codeException, HttpStatus.NOT_EXTENDED);

        return new ResponseEntity<>(errorObjectDto, httpStatus);
    }

    @ExceptionHandler(CoreException.class)
    public ResponseEntity<ErrorObjectDto> handlerException(final CoreException coreException) {
        final CodeException codeException = coreException.getCodeException();

        final ErrorObjectDto errorObjectDto = new ErrorObjectDto();
//        errorObjectDto.code(codeException.name());
        errorObjectDto.message(coreException.getMessage());

        final HttpStatus httpStatus = HTTP_STATUS_BY_CODE_EXCEPTION.getOrDefault(codeException, HttpStatus.NOT_EXTENDED);

        return new ResponseEntity<>(errorObjectDto, httpStatus);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorObjectDto> handlerException(final Exception exception) {
        log.error(LOGGER_PREFIX + "[handlerException] Unhandled exception", exception);
        final CodeException codeException = CodeException.INTERNAL_SERVER_ERROR;

        final ErrorObjectDto errorObjectDto = new ErrorObjectDto();
//        errorObjectDto.code(codeException.name());
        errorObjectDto.message(exception.getMessage());

        final HttpStatus httpStatus = HTTP_STATUS_BY_CODE_EXCEPTION.getOrDefault(codeException, HttpStatus.NOT_EXTENDED);

        return new ResponseEntity<>(errorObjectDto, httpStatus);
    }
}
