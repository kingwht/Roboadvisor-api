package com.hsbc.roboadvisor.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class MissingHeaderException extends RuntimeException
{
    private static final long serialVersionUID = 1L;

    public MissingHeaderException(String message) {
        super(message);
    }
}
