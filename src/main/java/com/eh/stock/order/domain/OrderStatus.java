package com.eh.stock.order.domain;

public enum OrderStatus {
    PROGRESS("진행중"),
    CANCEL("취소");

    private final String value;

    OrderStatus(String value){
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
