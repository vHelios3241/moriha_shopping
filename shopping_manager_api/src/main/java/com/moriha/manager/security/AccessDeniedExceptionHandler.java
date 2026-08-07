package com.moriha.manager.security;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import org.springframework.security.access.AccessDeniedException;

@RestControllerAdvice
public class AccessDeniedExceptionHandler {
    @ExceptionHandler(AccessDeniedException.class)
    public void defaultExceptionHandler(AccessDeniedException e) throws AccessDeniedException {
        throw e;
    }
}
