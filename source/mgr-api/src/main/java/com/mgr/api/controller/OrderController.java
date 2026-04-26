package com.mgr.api.controller;

import com.mgr.api.constant.MgrConstant;
import com.mgr.api.dto.ApiMessageDto;
import com.mgr.api.dto.ErrorCode;
import com.mgr.api.dto.ResponseListDto;
import com.mgr.api.dto.order.OrderDto;
import com.mgr.api.dto.order.OrderStatisticsDto;
import com.mgr.api.dto.order.TopProductDto;
import com.mgr.api.exception.BadRequestException;
import com.mgr.api.exception.NotFoundException;
import com.mgr.api.form.order.UpdateOrderStatusForm;
import com.mgr.api.mapper.OrderMapper;
import com.mgr.api.model.Order;
import com.mgr.api.model.OrderItem;
import com.mgr.api.model.criteria.OrderCriteria;
import com.mgr.api.repository.OrderItemRepository;
import com.mgr.api.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/v1/order")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
public class OrderController extends ABasicController {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private OrderMapper orderMapper;

    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ORDER_L')")
    public ApiMessageDto<ResponseListDto<List<OrderDto>>> list(OrderCriteria orderCriteria, Pageable pageable) {
        Page<Order> page = orderRepository.findAll(orderCriteria.getSpecification(), pageable);

        ResponseListDto<List<OrderDto>> listDto = new ResponseListDto<>();
        listDto.setContent(orderMapper.fromEntityListToDtoList(page.getContent()));
        listDto.setTotalElements(page.getTotalElements());
        listDto.setTotalPages(page.getTotalPages());

        return makeSuccessResponse(listDto, "Get list order success");
    }
    @GetMapping(value = "/get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ORDER_V')")
    public ApiMessageDto<OrderDto> get(@PathVariable("id") Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Order not found", ErrorCode.ORDER_ERROR_NOT_FOUND));
        return makeSuccessResponse(orderMapper.fromEntityToOrderDto(order), "Get order success");
    }
    @PutMapping(value = "/update-status", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ORDER_U')")
    @Transactional
    public ApiMessageDto<Void> updateStatus(@Valid @RequestBody UpdateOrderStatusForm form,
                                            BindingResult bindingResult) {
        Order order = orderRepository.findById(form.getId())
                .orElseThrow(() -> new NotFoundException("Order not found", ErrorCode.ORDER_ERROR_NOT_FOUND));

        Integer newStatus = form.getStatus();
        int currentStatus = order.getStatus();

        // Validate luồng trạng thái hợp lệ
        boolean isValidTransition = isValidStatusTransition(currentStatus, newStatus);
        if (!isValidTransition) {
            throw new BadRequestException(
                    "Invalid status transition from " + currentStatus + " to " + newStatus,
                    ErrorCode.ORDER_ERROR_INVALID_STATUS
            );
        }

        order.setStatus(newStatus);
        orderRepository.save(order);
        return makeSuccessResponse(null, "Update order status success");
    }

    // =====================================================================
    @GetMapping(value = "/statistics", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ORDER_S')")
    public ApiMessageDto<OrderStatisticsDto> statistics(
            @RequestParam(value = "month", required = false) Integer month,
            @RequestParam(value = "year", required = false) Integer year) {

        // Mặc định là tháng/năm hiện tại nếu không truyền vào
        Calendar now = Calendar.getInstance();
        int targetMonth = (month != null) ? month : now.get(Calendar.MONTH) + 1;
        int targetYear = (year != null) ? year : now.get(Calendar.YEAR);

        // Tính fromDate (ngày 1 của tháng) và toDate (ngày cuối của tháng)
        Calendar cal = Calendar.getInstance();
        cal.set(targetYear, targetMonth - 1, 1, 0, 0, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date fromDate = cal.getTime();

        cal.add(Calendar.MONTH, 1);
        cal.add(Calendar.MILLISECOND, -1);
        Date toDate = cal.getTime();

        // Lấy các số liệu từ repository
        Double totalRevenue = orderRepository.sumRevenueByDateRange(fromDate, toDate);
        Long totalOrders = orderRepository.countOrdersByDateRange(fromDate, toDate);
        Long newOrders = orderRepository.countByStatusAndDateRange(MgrConstant.ORDER_STATUS_PENDING, fromDate, toDate);
        Long completedOrders = orderRepository.countByStatusAndDateRange(MgrConstant.ORDER_STATUS_COMPLETED, fromDate, toDate);
        Long cancelledOrders = orderRepository.countByStatusAndDateRange(MgrConstant.ORDER_STATUS_CANCELLED, fromDate, toDate);

        // Lấy top 5 sản phẩm bán chạy
        List<OrderItem[]> rawTopProducts = orderItemRepository.findTopSellingProducts(
                fromDate, toDate, PageRequest.of(0, 5));

        List<TopProductDto> topProducts = new ArrayList<>();
        for (Object[] row : rawTopProducts) {
           TopProductDto dto = new TopProductDto(
                    (Long) row[0],          // productId
                    (String) row[1],        // productName
                    (Long) row[2],          // totalQuantitySold
                    (Double) row[3]         // totalRevenue
            );
            topProducts.add(dto);
        }

        OrderStatisticsDto statisticsDto = new OrderStatisticsDto(
                totalRevenue,
                totalOrders,
                newOrders,
                completedOrders,
                cancelledOrders,
                topProducts
        );

        return makeSuccessResponse(statisticsDto, "Get order statistics success");
    }

    private boolean isValidStatusTransition(int currentStatus, Integer newStatus) {
        if (newStatus == null) return false;

        // Hủy đơn chỉ được khi đang Chờ xác nhận
        if (newStatus.equals(MgrConstant.ORDER_STATUS_CANCELLED)) {
            return currentStatus == MgrConstant.ORDER_STATUS_PENDING;
        }
        // Luồng tiến lên chỉ được +1 bước
        return newStatus == currentStatus + 1;
    }
}
