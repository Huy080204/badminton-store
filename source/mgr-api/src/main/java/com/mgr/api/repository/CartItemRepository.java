package com.mgr.api.repository;

import com.mgr.api.model.Cart;
import com.mgr.api.model.CartItem;
import com.mgr.api.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    Optional<CartItem> findByCartAndProduct(Cart cart, Product product);
}