package com.eh.stock.common.exception;

public enum ExceptionStatus {
    ERROR_DATABASE("시스템(DB) 연결의 문제가 있습니다."),
    ERROR_DUPLICATE_ORDER("이미 해당 건에 대한 주문이 진행되었습니다.");

    private final String value;

    ExceptionStatus(String value){
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
