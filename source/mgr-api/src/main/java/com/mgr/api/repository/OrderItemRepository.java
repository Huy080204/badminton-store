package com.mgr.api.repository;

import com.mgr.api.model.OrderItem;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    @Query("SELECT oi.product.id, oi.product.name, SUM(oi.quantity), SUM(oi.price * oi.quantity) " +
           "FROM OrderItem oi " +
           "WHERE oi.order.status = 4 " +
           "AND oi.order.createdDate >= :fromDate AND oi.order.createdDate <= :toDate " +
           "GROUP BY oi.product.id, oi.product.name " +
           "ORDER BY SUM(oi.quantity) DESC")
    List<OrderItem[]> findTopSellingProducts(@Param("fromDate") Date fromDate,
                                         @Param("toDate") Date toDate,
                                         Pageable pageable);
}