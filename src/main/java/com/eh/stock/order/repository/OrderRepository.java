package com.eh.stock.order.repository;

import com.eh.stock.order.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface OrderRepository  extends JpaRepository<Order,Long> {
    Optional<Order> findByOrderNumber(UUID uuid);
}
