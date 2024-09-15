package com.eh.stock.product.repository;

import com.eh.stock.product.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product,Long> {
    Optional<Product> findBySku(String sku);
}
