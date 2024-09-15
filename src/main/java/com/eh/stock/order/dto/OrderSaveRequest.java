package com.eh.stock.order.dto;

import com.eh.stock.user.domain.UserStatus;

import java.util.List;
import java.util.UUID;

public record OrderSaveRequest(String name,
                               String address,
                               UserStatus userStatus,
                               UUID orderNumber,
                               List<OrderItemSaveRequest> orderItems) {
}
