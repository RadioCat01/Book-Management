package com.pwn.book_network.handler;

import com.pwn.book_network.exception.OperationProhibitted;
import jakarta.mail.MessagingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashSet;
import java.util.Set;

/*
      whole point of this to return understandable exception to user
      here all the methods will build and return an object in type of ExceptionResponse --> custom class
      BusinessErrorCode --> custom enum is implemented which returns code, description and httpStatus
 */

@RestControllerAdvice // used for exception handling
public class GlobalExceptionHandler {

    @ExceptionHandler(LockedException.class)
    public ResponseEntity<ExceptionResponse> handleException(LockedException exp){
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(
                        ExceptionResponse.builder()
                                .businessErrorCode(BusinessErrorCode.ACCOUNT_LOCKED.getCode())
                                .businessErrorDescription(BusinessErrorCode.ACCOUNT_LOCKED.getDescription())
                                .error(exp.getMessage())
                                .build());}

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<ExceptionResponse> handleException(DisabledException exp){
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(
                        ExceptionResponse.builder()
                                .businessErrorCode(BusinessErrorCode.ACCOUNT_DISABLED.getCode())
                                .businessErrorDescription(BusinessErrorCode.ACCOUNT_DISABLED.getDescription())
                                .error(exp.getMessage())
                                .build());}

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ExceptionResponse> handleException(BadCredentialsException exp){
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(
                        ExceptionResponse.builder()
                                .businessErrorCode(BusinessErrorCode.BAD_CREDENTIALS.getCode())
                                .businessErrorDescription(BusinessErrorCode.BAD_CREDENTIALS.getDescription())
                                .error(BusinessErrorCode.BAD_CREDENTIALS.getDescription())
                                .build());}

    @ExceptionHandler(MessagingException.class)
    public ResponseEntity<ExceptionResponse> handleException(MessagingException exp){
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(
                        ExceptionResponse.builder()
                                .error(exp.getMessage())
                                .build());}

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handleException(MethodArgumentNotValidException exp){
        Set<String> errors = new HashSet<>();
        exp.getBindingResult().getAllErrors()
                .forEach(error -> {
                    var errorMessage = error.getDefaultMessage();
                    errors.add(errorMessage);
                });
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        ExceptionResponse.builder()
                                .validationErrors(errors)
                                .build());
    }

    //Non-specific exception
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleException(Exception exp){
        exp.printStackTrace();
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(
                        ExceptionResponse.builder()
                                .businessErrorDescription("Internal error, Contact admin")
                                .error(exp.getMessage())
                                .build());}

    @ExceptionHandler(OperationProhibitted.class)
    public ResponseEntity<ExceptionResponse> handleException(OperationProhibitted exp){
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(
                        ExceptionResponse.builder()
                                .error(exp.getMessage())
                                .build());}

}
