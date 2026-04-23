package com.mgr.api.mapper;


import com.mgr.api.dto.nation.NationDto;
import com.mgr.api.form.nation.CreateNationForm;
import com.mgr.api.model.Nation;
import org.mapstruct.*;

import java.util.List;


@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface NationMapper {
    @Mapping(source = "kind", target = "kind")
    @Mapping(source = "name", target = "name")
    @Mapping(target = "parent", ignore = true)
    Nation fromCreateFormToEntity(CreateNationForm form);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "kind", target = "kind")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "parent.id", target = "parentId")
    @BeanMapping(ignoreByDefault = true)
    NationDto fromEntityToNationDto(Nation nation);

    @IterableMapping(elementTargetType = NationDto.class)
    List<NationDto> fromEntityToNationDtoList(List<Nation> nations);
}
