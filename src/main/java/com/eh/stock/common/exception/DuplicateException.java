package com.eh.stock.common.exception;

public class DuplicateException extends RuntimeException {

    public DuplicateException(ExceptionStatus exceptionStatus){
        super(exceptionStatus.getValue());
    }
}
