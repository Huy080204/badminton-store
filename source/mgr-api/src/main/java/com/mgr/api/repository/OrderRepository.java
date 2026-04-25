package com.mgr.api.repository;

import com.mgr.api.model.Account;
import com.mgr.api.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long>, JpaSpecificationExecutor<Order> {

    List<Order> findAllByAccount(Account account);

    // Đếm số đơn theo trạng thái và khoảng ngày
    @Query("SELECT COUNT(o) FROM Order o WHERE o.status = :status " +
           "AND o.createdDate >= :fromDate AND o.createdDate <= :toDate")
    Long countByStatusAndDateRange(@Param("status") Integer status,
                                   @Param("fromDate") Date fromDate,
                                   @Param("toDate") Date toDate);

    // Tính tổng doanh thu (chỉ tính đơn hoàn thành, status = 4)
    @Query("SELECT COALESCE(SUM(o.totalPrice), 0) FROM Order o WHERE o.status = 4 " +
           "AND o.createdDate >= :fromDate AND o.createdDate <= :toDate")
    Double sumRevenueByDateRange(@Param("fromDate") Date fromDate,
                                 @Param("toDate") Date toDate);

    // Đếm tổng số đơn trong khoảng ngày
    @Query("SELECT COUNT(o) FROM Order o WHERE o.createdDate >= :fromDate AND o.createdDate <= :toDate")
    Long countOrdersByDateRange(@Param("fromDate") Date fromDate,
                                @Param("toDate") Date toDate);
}