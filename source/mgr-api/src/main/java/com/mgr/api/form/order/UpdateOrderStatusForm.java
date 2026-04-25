package com.mgr.api.form.order;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class UpdateOrderStatusForm {

    @NotNull(message = "id is required")
    @ApiModelProperty(name = "id", required = true)
    private Long id;

    /**
     * 1: Chờ xác nhận
     * 2: Đã xác nhận
     * 3: Đang giao
     * 4: Hoàn thành
     * 0: Đã hủy
     */
    @NotNull(message = "status is required")
    @ApiModelProperty(name = "status", required = true)
    private Integer status;
}
