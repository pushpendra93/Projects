package com.project.file.search.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class SearchException extends Exception {

    private final HttpStatus status;

    public SearchException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
}
