package com.mgr.api.repository;

import com.mgr.api.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
    @Modifying
    @Transactional
    @Query("UPDATE Product p SET p.status = :status WHERE p.seller.id = :sellerId AND p.status <> :status")
    void updateStatusBySellerId(@Param("sellerId") Long sellerId, @Param("status") int status);

    // Tìm sản phẩm đang hoạt động (status=1) có stock thấp hơn ngưỡng — dùng cho scheduler cảnh báo
    @Query("SELECT p FROM Product p WHERE p.status = 1 AND p.stock <= :threshold")
    List<Product> findLowStockProducts(@Param("threshold") Integer threshold);
}
