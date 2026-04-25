package com.mgr.api.dto.order;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderStatisticsDto {

    @ApiModelProperty(name = "totalRevenue", notes = "Tổng doanh thu trong tháng")
    private Double totalRevenue;

    @ApiModelProperty(name = "totalOrders", notes = "Tổng số đơn hàng trong tháng")
    private Long totalOrders;

    @ApiModelProperty(name = "newOrders", notes = "Số đơn hàng mới (Chờ xác nhận)")
    private Long newOrders;

    @ApiModelProperty(name = "completedOrders", notes = "Số đơn hàng hoàn thành")
    private Long completedOrders;

    @ApiModelProperty(name = "cancelledOrders", notes = "Số đơn hàng đã hủy")
    private Long cancelledOrders;

    @ApiModelProperty(name = "topProducts", notes = "Top sản phẩm bán chạy nhất trong tháng")
    private List<TopProductDto> topProducts;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TopProductDto {
        @ApiModelProperty(name = "productId")
        private Long productId;

        @ApiModelProperty(name = "productName")
        private String productName;

        @ApiModelProperty(name = "totalQuantitySold")
        private Long totalQuantitySold;

        @ApiModelProperty(name = "totalRevenue")
        private Double totalRevenue;
    }
}
