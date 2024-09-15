package com.eh.stock.order.controller;

import com.eh.stock.order.dto.OrderSaveRequest;
import com.eh.stock.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/order")
    public ResponseEntity<Void> orderProduct(@RequestBody OrderSaveRequest orderSaveRequest) {
        orderService.orderProduct(orderSaveRequest);

        return new ResponseEntity<>(HttpStatus.CREATED); // Return 201 Created
    }
}
