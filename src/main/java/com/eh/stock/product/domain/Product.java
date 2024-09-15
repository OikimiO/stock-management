package com.eh.stock.product.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long productId;

    @Column(unique = true, nullable = false)
    private String sku;
    private String name;
    private long price;
    private long quantity;

    @Enumerated(EnumType.STRING)
    private ProductStatus productStatus;

    public static Product of(String sku, String name, long price, long quantity){
        return new Product(sku, name, price, quantity, ProductStatus.NORMAL);
    }

    private Product(String sku, String name, long price, long quantity, ProductStatus productStatus){
        this.sku = sku;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.productStatus = productStatus;
    }

    public void lackProduct(ProductStatus productStatus, long quantity){
        changeProductStatus(productStatus);
        decreaseQuantity(quantity);
    }

    private void changeProductStatus(ProductStatus productStatus) {
        this.productStatus = productStatus;
    }
    public void decreaseQuantity(long quantity){
        long remain = this.quantity - quantity;

        changeQuantity(remain);
    }

    private void changeQuantity(long remain) {
        if(remain <= 0L){
            this.quantity = 0L;
        }

        this.quantity = remain;
    }

}
