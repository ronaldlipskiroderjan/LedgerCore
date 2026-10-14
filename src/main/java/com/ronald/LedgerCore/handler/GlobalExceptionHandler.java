package com.ronald.LedgerCore.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ExceptionDTO> usernameNotFoundException(UsernameNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ExceptionDTO(
                ex.getMessage(),
                HttpStatus.NOT_FOUND.value()
        ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionDTO> exception(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ExceptionDTO(
                ex.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR.value()
        ));
    }

}
