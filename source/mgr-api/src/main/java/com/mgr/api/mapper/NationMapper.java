package com.mgr.api.mapper;



import java.util.List;

import com.mgr.api.dto.nation.NationDto;
import com.mgr.api.form.nation.CreateNationForm;
import com.mgr.api.form.nation.UpdateNationForm;
import com.mgr.api.model.Nation;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface NationMapper {
    @Mapping(source = "kind", target = "kind")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "parentId", target = "parent.id")
    Nation fromCreateNationFormToEntity(CreateNationForm form);

    @Mapping(source = "kind", target = "kind")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "parentId", target = "parent.id")
    @Mapping(target = "id", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateNationFromForm(UpdateNationForm form, @MappingTarget Nation entity);

    @Mapping(source = "kind", target = "kind")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "parent.id", target = "parentId")
    NationDto fromEntityToDto(Nation nation);

    // MapStruct sẽ tự động gọi hàm fromEntityToDto cho từng phần tử trong list
    List<NationDto> fromEntityListToDtoList(List<Nation> nations);
}