package com.mgr.api.mapper;

import com.mgr.api.dto.address.AddressDto;
import com.mgr.api.form.address.CreateAddressForm;
import com.mgr.api.form.address.UpdateAddressForm;
import com.mgr.api.model.Address;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {NationMapper.class})
public interface AddressMapper {
    @Mapping(source = "userId", target = "user.account.id")
    @Mapping(source = "street", target = "street")
    @Mapping(source = "zipCode", target = "zipCode")
    @Mapping(source = "isDefault", target = "isDefault")
    @Mapping(source = "provinceId", target = "province.id")
    @Mapping(source = "districtId", target = "district.id")
    @Mapping(source = "communeId", target = "commune.id")
    Address fromCreateFormToEntity(CreateAddressForm form);

    @Mapping(source = "street", target = "street")
    @Mapping(source = "zipCode", target = "zipCode")
    @Mapping(source = "isDefault", target = "isDefault")
    @Mapping(source = "provinceId", target = "province.id")
    @Mapping(source = "districtId", target = "district.id")
    @Mapping(source = "communeId", target = "commune.id")
    @Mapping(source = "userId", target = "user.account.id")
    @Mapping(source = "id", target = "id")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateAddressFromForm(UpdateAddressForm form, @MappingTarget Address entity);

    @Mapping(source = "street", target = "street")
    @Mapping(source = "zipCode", target = "zipCode")
    @Mapping(source = "isDefault", target = "isDefault")
    @Mapping(source = "province.id", target = "provinceId")
    @Mapping(source = "district.id", target = "districtId")
    @Mapping(source = "commune.id", target = "communeId")
    @Mapping(source = "user.account.id", target = "userId")
    @Mapping(source = "id", target = "id")
    @Named("fromEntityToAddressDto")
    AddressDto fromEntityToDto(Address address);


    @IterableMapping(elementTargetType = AddressDto.class, qualifiedByName = "fromEntityToDto")
    @Named("fromEntityToAddressDtoList")
    List<AddressDto> fromEntityListToDtoList(List<Address> address);

}
