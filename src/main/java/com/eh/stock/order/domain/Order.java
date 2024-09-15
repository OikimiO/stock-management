package com.eh.stock.order.domain;

import com.eh.stock.user.domain.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Entity
@NoArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long orderId;

    private UUID orderNumber;

    private long totalAmount;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @Enumerated(EnumType.STRING)
    private RemarkStatus remarkStatus;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    public static Order of(UUID orderNumber, OrderStatus orderStatus, User user){
        return new Order(orderNumber, orderStatus, user);
    }

    private Order(UUID orderNumber, OrderStatus orderStatus, User user) {
        this.orderNumber = orderNumber;
        this.orderStatus = orderStatus;
        this.user = user;
    }

    public void changeTotalAmount(long totalAmount){
        this.totalAmount = totalAmount;
    }

    public void changeCancelStatus(RemarkStatus remarkStatus) {
        changeOrderStatus(OrderStatus.CANCEL);
        this.remarkStatus = remarkStatus;
    }

    private void changeOrderStatus(OrderStatus orderStatus){
        this.orderStatus = orderStatus;
    }

}
