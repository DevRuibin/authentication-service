package org.andy.chatfybackend.auth.config;

import io.jsonwebtoken.JwtException;
import org.andy.chatfybackend.auth.exceptions.DuplicateUserException;
import org.andy.chatfybackend.auth.exceptions.IncorrectPasswordException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ErrorDetails> handleNoHandlerFoundException(NoHandlerFoundException e) {
        ErrorDetails errorDetails = ErrorDetails.builder()
                .message("Endpoint not found")
                .details(e.getMessage())
                .build();
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(DuplicateUserException.class)
    public ResponseEntity<ErrorDetails> handleDuplicateUserException(DuplicateUserException e) {
        ErrorDetails errorDetails = new ErrorDetails(e.getMessage(), "Duplicate user registration attempt");
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IncorrectPasswordException.class)
    public ResponseEntity<ErrorDetails> handleIncorrectPasswordException(IncorrectPasswordException e) {
        ErrorDetails errorDetails = new ErrorDetails(e.getMessage(), "Incorrect password attempt");
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ErrorDetails> handleJwtException(JwtException e) {
        ErrorDetails errorDetails = new ErrorDetails(e.getMessage(), "Invalid token attempt");
        return new ResponseEntity<>(errorDetails, HttpStatus.UNAUTHORIZED);
    }
}

