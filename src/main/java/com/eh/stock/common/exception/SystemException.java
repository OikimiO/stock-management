package com.eh.stock.common.exception;

public class SystemException extends RuntimeException{
    public SystemException(ExceptionStatus exceptionStatus){
        super(exceptionStatus.getValue());
    }
}
