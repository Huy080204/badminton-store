package com.mgr.api.dto.nation;

import com.mgr.api.dto.ABasicAdminDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class NationDto extends ABasicAdminDto {
    @ApiModelProperty(name = "kind")
    private Integer kind;
    @ApiModelProperty(name = "name")
    private String name;
    @ApiModelProperty(name = "parentId")
    private Long parentId;
}
