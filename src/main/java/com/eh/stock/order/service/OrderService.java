package com.eh.stock.order.service;

import com.eh.stock.common.exception.DuplicateException;
import com.eh.stock.common.exception.ExceptionStatus;
import com.eh.stock.config.redis.RedissonLock;
import com.eh.stock.order.domain.Order;
import com.eh.stock.order.domain.OrderStatus;
import com.eh.stock.order.domain.RemarkStatus;
import com.eh.stock.order.dto.OrderSaveRequest;
import com.eh.stock.order.repository.OrderRepository;
import com.eh.stock.user.domain.User;
import com.eh.stock.user.domain.UserStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

    private final OrderItemService orderItemService;
    private final OrderRepository orderRepository;

    private static final long ZERO_PRICE = 0L;

    @RedissonLock(value = "#orderProduct")
    public void orderProduct(OrderSaveRequest orderSaveRequest){
        validateDuplicateOrderNumber(orderSaveRequest.orderNumber());

        User user = User.of(orderSaveRequest.name(), orderSaveRequest.address(), UserStatus.GUEST);
        Order order = Order.of(orderSaveRequest.orderNumber(), OrderStatus.PROGRESS, user);
        Order savedOrder = orderRepository.save(order);
        long totalAmount = orderItemService.calculateTotalAmount(savedOrder, orderSaveRequest.orderItems());

        if(totalAmount == ZERO_PRICE){
            order.changeCancelStatus(RemarkStatus.ORDER_CANCEL_ALL_PRODUCT_LACK);
        }

        order.changeTotalAmount(totalAmount);
    }

    private void validateDuplicateOrderNumber(UUID uuid) {
        Optional<Order> isDuplicated = orderRepository.findByOrderNumber(uuid);
        isDuplicated.orElseThrow(() -> new DuplicateException(ExceptionStatus.ERROR_DUPLICATE_ORDER));
    }
}
