package com.snm.security.controller;

import com.snm.security.dto.JsonResult;
import com.snm.security.exception.IncorrectCertificateException;
import com.snm.security.exception.NotUniqueException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.persistence.EntityNotFoundException;

import static com.snm.security.dto.JsonResult.ErrorCode.*;

@ControllerAdvice
public class ExceptionHandlingController {

    private static final Logger log = LoggerFactory.getLogger(ExceptionHandlingController.class);

    @ExceptionHandler(Exception.class)
    public ResponseEntity<JsonResult> handleException(Exception ex) {

        log.error("Unhandled exception", ex);
        return getResponse(SYSTEM_ERROR, ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<JsonResult> handleException(EntityNotFoundException ex) {

        log.error("Entity not found", ex);
        return getResponse(ENTITY_NOT_FOUND, ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NotUniqueException.class)
    public ResponseEntity<JsonResult> handleException(NotUniqueException ex) {

        log.error("Entity not unique", ex);
        return getResponse(ALREADY_EXIST, ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<JsonResult> handleException(IllegalArgumentException ex) {

        log.error("Invalid parameters", ex);
        return getResponse(VALIDATION_ERROR, ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IncorrectCertificateException.class)
    public ResponseEntity<JsonResult> handleException(IncorrectCertificateException ex) {

        log.error("Incorrect certificate", ex);
        return getResponse(AUTH_ERROR, ex.getMessage(), HttpStatus.BAD_REQUEST) ;
    }

    private ResponseEntity<JsonResult> getResponse(JsonResult.ErrorCode code, String message, HttpStatus status) {

        JsonResult result = new JsonResult(code);
        result.setMessage(message);
        return new ResponseEntity<>(result, status);
    }
}