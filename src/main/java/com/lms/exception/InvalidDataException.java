package com.lms.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidDataException extends RuntimeException {
    private List<String> errors;

    public InvalidDataException(String message) {
        super(message);
    }

    public InvalidDataException(List<String> errors) {
        super("Invalid data provided");
        this.errors = errors;
    }

    public List<String> getErrors() {
        return errors;
    }
}