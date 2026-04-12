package com.mgr.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mgr.api.utils.DateUtils;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ABasicAdminDto {
    @ApiModelProperty(name = "id")
    private Long id;
    @ApiModelProperty(name = "status")
    private Integer status;
    @ApiModelProperty(name = "modifiedDate")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateUtils.FORMAT_DATE)
    private LocalDateTime modifiedDate;
    @ApiModelProperty(name = "createdDate")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateUtils.FORMAT_DATE)
    private LocalDateTime createdDate;
}
