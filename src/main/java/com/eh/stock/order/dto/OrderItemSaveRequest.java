package com.eh.stock.order.dto;

public record OrderItemSaveRequest(String sku,
                                   long orderQuantity) {
}
