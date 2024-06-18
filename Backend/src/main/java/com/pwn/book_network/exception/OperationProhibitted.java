package com.pwn.book_network.exception;

public class OperationProhibitted extends RuntimeException { // only throws at runtime

    public OperationProhibitted(String cannotUpdate) {
        super(cannotUpdate);
    } // when having a new Exception, need to be handled by the program -->handler.GlobalExceptionHandler
}
