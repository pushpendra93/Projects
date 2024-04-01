package com.project.file.search.exception;

import org.springframework.http.HttpStatus;

public class SearchException extends Exception {

    HttpStatus status;

    public SearchException(String message,HttpStatus status) {
        super(message);
        this.status = status;
    }
}
