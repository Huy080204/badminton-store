package com.mgr.api.dto.order;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TopProductDto {
    @ApiModelProperty(name = "productId")
    private Long productId;

    @ApiModelProperty(name = "productName")
    private String productName;

    @ApiModelProperty(name = "totalQuantitySold")
    private Long totalQuantitySold;

    @ApiModelProperty(name = "totalRevenue")
    private Double totalRevenue;
}
