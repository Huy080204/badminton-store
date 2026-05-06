package com.mgr.api.service.schedule;

import com.mgr.api.model.Order;
import com.mgr.api.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.mgr.api.constant.MgrConstant.CANCELLED_STATUS;
import static com.mgr.api.constant.MgrConstant.EXPIRY_HOURS;

@Component
@Slf4j
public class OrderSchedulerService {
    @Autowired
    private OrderRepository orderRepository;
    @Scheduled(cron = "0 0 * * * *") // Chạy mỗi 1 giờ
    public void autoCancelExpiredOrders() {
        log.info(">>> [OrderScheduler] Bắt đầu kiểm tra đơn hàng PENDING quá hạn...");

        // Tính mốc thời gian: hiện tại - 24 giờ
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR_OF_DAY, -EXPIRY_HOURS);
        Date cutoffTime = calendar.getTime();

        // Truy vấn đơn hàng PENDING quá hạn
        List<Order> expiredOrders = orderRepository.findExpiredPendingOrders(cutoffTime);

        if (expiredOrders.isEmpty()) {
            log.info(">>> [OrderScheduler] Không có đơn hàng nào quá hạn.");
            return;
        }

        // Cập nhật status sang CANCELLED
        expiredOrders.forEach(order -> {
            log.warn(">>> [OrderScheduler] Hủy đơn hàng id={}, tạo lúc={}", order.getId(), order.getCreatedDate());
            order.setStatus(CANCELLED_STATUS);
        });

        orderRepository.saveAll(expiredOrders);

        log.info(">>> [OrderScheduler] Đã hủy {} đơn hàng quá hạn.", expiredOrders.size());
    }
}
