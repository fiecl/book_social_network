package com.fiecl.book.exception;

// We want to extend to 'RuntimeException' instead of 'Throwable' since we want the exception to be 'unchecked'
public class OperationNotPermittedException extends RuntimeException {
    public OperationNotPermittedException(String msg) {
        super(msg);
    }
}
