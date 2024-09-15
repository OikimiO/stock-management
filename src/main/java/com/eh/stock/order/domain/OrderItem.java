package com.eh.stock.order.domain;

import com.eh.stock.product.domain.Product;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long orderQuantityId;

    private long orderQuantity;
    private long productPrice;

    @ManyToOne
    @JoinColumn(name = "productId")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "orderId")
    private Order order;

    public static OrderItem of(long orderQuantity, long productPrice, Product product, Order order){
        return new OrderItem(orderQuantity, productPrice, product, order);
    }

    private OrderItem(long orderQuantity, long productPrice, Product product, Order order) {
        this.orderQuantity = orderQuantity;
        this.productPrice = productPrice;
        this.product = product;
        this.order = order;
    }
}
