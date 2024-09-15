package com.eh.stock.product.service;

import com.eh.stock.common.exception.ExceptionStatus;
import com.eh.stock.common.exception.SystemException;
import com.eh.stock.product.domain.Product;
import com.eh.stock.product.domain.ProductStatus;
import com.eh.stock.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public void decreaseProductQuantity(Product product, long orderQuantity) {
        product.decreaseQuantity(orderQuantity);
    }

    public Product searchBySku(String sku) {
        Optional<Product> checkSku = productRepository.findBySku(sku);
        validateProduct(checkSku);

        return checkSku.get();
    }

    private void validateProduct(Optional<Product> checkSku) {
        checkSku.orElseThrow(() -> new SystemException(ExceptionStatus.ERROR_DATABASE));
    }

    public void lackProduct(Product product, long orderQuantity) {
        product.lackProduct(ProductStatus.LACK, orderQuantity);
    }
}
