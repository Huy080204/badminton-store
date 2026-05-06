package com.mgr.api.service.schedule;

import com.mgr.api.model.Product;
import com.mgr.api.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.mgr.api.constant.MgrConstant.LOW_STOCK_THRESHOLD;

@Component
@Slf4j
public class ProductSchedulerService {

    @Autowired
    private ProductRepository productRepository;
    @Scheduled(cron = "0 0 8 * * *")
    public void checkLowStockProducts() {
        log.info(">>> [ProductScheduler] Bắt đầu kiểm tra tồn kho sản phẩm (ngưỡng <= {})...", LOW_STOCK_THRESHOLD);

        List<Product> lowStockProducts = productRepository.findLowStockProducts(LOW_STOCK_THRESHOLD);

        if (lowStockProducts.isEmpty()) {
            log.info(">>> [ProductScheduler] Tất cả sản phẩm đều có tồn kho đủ.");
            return;
        }

        log.warn(">>> [ProductScheduler] Phát hiện {} sản phẩm sắp hết hàng:", lowStockProducts.size());
        lowStockProducts.forEach(product ->
            log.warn("    - [id={}] {} | Stock còn lại: {} | Giá: {}",
                    product.getId(),
                    product.getName(),
                    product.getStock(),
                    product.getPrice())
        );

        log.info(">>> [ProductScheduler] Hoàn tất kiểm tra tồn kho.");
    }
}
