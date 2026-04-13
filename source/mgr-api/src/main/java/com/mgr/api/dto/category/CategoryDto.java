package com.mgr.api.dto.category;

import com.mgr.api.dto.ABasicAdminDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CategoryDto extends ABasicAdminDto {
    @ApiModelProperty(name = "name")
    private String name;

    @ApiModelProperty(name = "description")
    private String description;

    @ApiModelProperty(name = "parentId")
    private Long parentId;
<<<<<<< HEAD
}
=======
}
>>>>>>> 6582894c7d0f3da30d2e54336b17bac8803fcdf7
