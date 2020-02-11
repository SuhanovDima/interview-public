package com.devexperts.rest;

import com.devexperts.exception.DevExpertsArgumentException;
import com.devexperts.exception.DevExpertsNotEnoughMoneyException;
import com.devexperts.exception.DevExpertsNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ResponseHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(value = DevExpertsArgumentException.class)
    protected ResponseEntity<Void> handleArgumentException() {
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = DevExpertsNotFoundException.class)
    protected ResponseEntity<Void> handleNotFoundException() {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = DevExpertsNotEnoughMoneyException.class)
    protected ResponseEntity<Void> handleOperationException() {
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
