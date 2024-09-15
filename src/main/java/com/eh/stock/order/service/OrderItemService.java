package com.eh.stock.order.service;

import com.eh.stock.order.domain.Order;
import com.eh.stock.order.domain.OrderItem;
import com.eh.stock.order.dto.OrderItemSaveRequest;
import com.eh.stock.order.dto.OrderSaveRequest;
import com.eh.stock.order.repository.OrderItemRepository;
import com.eh.stock.product.domain.Product;
import com.eh.stock.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class OrderItemService {
    private static final long ZERO_PRICE = 0L;
    private static final long ZERO_STOCK = 0L;
    private static final long MIN_PURCHASE_STANDARD = 0L;

    private final OrderItemRepository orderItemRepository;
    private final ProductService productService;

    public long calculateTotalAmount(Order order, List<OrderItemSaveRequest> orderItemSaveRequests) {
        long totalAmount = ZERO_PRICE;

        for(int i = 0; i < orderItemSaveRequests.size(); i++) {
            OrderItemSaveRequest orderItemSaveRequest = orderItemSaveRequests.get(i);
            String sku = orderItemSaveRequest.sku();
            long orderQuantity = orderItemSaveRequest.orderQuantity();
            Product product = productService.searchBySku(sku);

            totalAmount += orderItemPrice(order, product, orderQuantity);
        }

        return totalAmount;
    }

    private long orderItemPrice(Order order, Product product, long orderQuantity) {
        if(product.getQuantity() - orderQuantity > MIN_PURCHASE_STANDARD){
            if(product.getQuantity() - orderQuantity == MIN_PURCHASE_STANDARD){
                productService.lackProduct(product, orderQuantity);
                return calculateOrderItemPrice(order, product, orderQuantity);
            }

            productService.decreaseProductQuantity(product, orderQuantity);
            return calculateOrderItemPrice(order, product, orderQuantity);
        }

        createOrderItem(order, product, ZERO_STOCK);

        return ZERO_PRICE;
    }

    private long calculateOrderItemPrice(Order order, Product product, long orderQuantity) {
        createOrderItem(order, product, orderQuantity);

        return orderQuantity * product.getPrice();
    }

    private void createOrderItem(Order order, Product product, long orderQuantity) {
        OrderItem orderItem = OrderItem.of(orderQuantity, product.getPrice(), product, order);
        orderItemRepository.save(orderItem);
    }
}
