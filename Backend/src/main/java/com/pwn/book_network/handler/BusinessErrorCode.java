package com.pwn.book_network.handler;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

public enum BusinessErrorCode {

    NO_CODE(0, NOT_IMPLEMENTED, "No code"),
    ACCOUNT_LOCKED(2, FORBIDDEN, "Account Locked"),
    INCORRECT_PASSWORD(3, BAD_REQUEST, "Password is incorrect"),
    NEW_PASSWORD_DOES_NOT_MATCH(4, BAD_REQUEST, "Password does not match"),
    ACCOUNT_DISABLED(5, FORBIDDEN, " Account is disabled "),
    BAD_CREDENTIALS(6, FORBIDDEN, "User name or password incorrect")
    ;

    @Getter
    private int code;
    @Getter
    private String description;
    @Getter
    private HttpStatus httpStatus;

    BusinessErrorCode(int code, HttpStatus httpStatus, String description) {
        this.code = code;
        this.description = description;
        this.httpStatus = httpStatus;
    }
}
