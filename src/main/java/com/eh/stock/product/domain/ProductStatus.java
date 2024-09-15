package com.eh.stock.product.domain;

public enum ProductStatus {
    NORMAL("정상"),
    LACK("재고부족");
    private final String value;

    ProductStatus(String value){
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
