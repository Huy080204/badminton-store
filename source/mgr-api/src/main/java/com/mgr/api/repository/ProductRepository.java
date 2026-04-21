package com.mgr.api.repository;

import com.mgr.api.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
    @Modifying
    @Transactional
    @Query("UPDATE Product p SET p.status = :status WHERE p.seller.id = :sellerId AND p.status <> :status")
    void updateStatusBySellerId(@Param("sellerId") Long sellerId, @Param("status") int status);
}