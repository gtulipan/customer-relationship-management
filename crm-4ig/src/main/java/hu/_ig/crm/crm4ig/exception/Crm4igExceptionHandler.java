package hu._ig.crm.crm4ig.exception;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;
import java.util.List;

import static hu._ig.crm.crm4ig.constants.Constants.COLON;
import static hu._ig.crm.crm4ig.constants.Constants.EMPTY_STRING;

@ControllerAdvice
public class Crm4igExceptionHandler {

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<List> validationErrorHandler(ConstraintViolationException e) {
        List<String> errors = new ArrayList<>(e.getConstraintViolations().size());
        e.getConstraintViolations().forEach(constraintViolation -> {
            errors.add(String.join(EMPTY_STRING,
                    constraintViolation.getPropertyPath().toString(), COLON, constraintViolation.getMessage()));
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
}
