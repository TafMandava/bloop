package com.microservices.flash.bloop.client.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Using "Controller Advice" which is more like a top loader exception handler that will be applied to our overall project
 * Advice Spring that this is an exception handler 
 * This exception handler will be added to all controllers
 */
@ControllerAdvice
public class MvcExceptionHandler {

    /**
     * "ConstraintViolationException" is a standard exception from the Bean Valiation class 
     *     We will go ahead and handle it
     * 
     * Setup an exception handler and if we violate the constraints, the Controller will throw a ConstraintViolationException
     * When the controller does throw the error, this validation handler is going to capture the exception and the exception is extended to have information about what was wrong
     * We are going to take it and provide it back as a collection of errors and that will get returned to the client in the body of the request
     * This will give the client some insight as to what went wrong
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<List<String>> validationErrorHandler(ConstraintViolationException e) {
        List<String> errors = new ArrayList<>(e.getConstraintViolations().size());

        e.getConstraintViolations().forEach(error -> {
            errors.add(error.toString());
        });

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handle a bind exception 
     * Return a list of errors
     *     This is going to be a complex object for all the errors that happened and they will get converted to json and display to the client
     * 
     * Recommendation: Look at the spring boot object errors
     */
    @ExceptionHandler(BindException.class)
    public ResponseEntity<List<ObjectError>> handleBindException(BindException e) {
        return new ResponseEntity<>(e.getAllErrors(), HttpStatus.BAD_REQUEST);
    }

}