package com.eh.stock.order.domain;

public enum RemarkStatus {
    ORDER_CANCEL_ALL_PRODUCT_LACK("모든 주문 상품 재고 부족");

    private final String value;

    RemarkStatus(String value){
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
