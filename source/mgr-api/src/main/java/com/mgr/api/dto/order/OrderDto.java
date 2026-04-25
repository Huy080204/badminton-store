package com.mgr.api.dto.order;

import com.mgr.api.dto.ABasicAdminDto;
import com.mgr.api.dto.address.AddressDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class OrderDto extends ABasicAdminDto {
    @ApiModelProperty(name = "totalPrice")
    private Double totalPrice;

    @ApiModelProperty(name = "paymentMethod")
    private Integer paymentMethod;

    @ApiModelProperty(name = "address")
    private AddressDto address;

    @ApiModelProperty(name = "items")
    private List<OrderItemDto> items;
}