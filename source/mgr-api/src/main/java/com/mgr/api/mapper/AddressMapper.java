package com.mgr.api.mapper;

import com.mgr.api.dto.address.AddressDto;
import com.mgr.api.form.address.CreateAddressForm;
import com.mgr.api.form.address.UpdateAddressForm;
import com.mgr.api.model.Address;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface AddressMapper {
    @Mapping(source = "street", target = "street")
    @Mapping(source = "zipCode", target = "zipCode")
    @Mapping(source = "isDefault", target = "isDefault")
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "province", ignore = true)
    @Mapping(target = "district", ignore = true)
    @Mapping(target = "commune", ignore = true)
    @Mapping(target = "status", ignore = true)
    Address fromCreateFormToEntity(CreateAddressForm form);

    @Mapping(source = "street", target = "street")
    @Mapping(source = "zipCode", target = "zipCode")
    @Mapping(source = "isDefault", target = "isDefault")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "province", ignore = true)
    @Mapping(target = "district", ignore = true)
    @Mapping(target = "commune", ignore = true)
    @Mapping(target = "status", ignore = true)
    void updateEntityFromUpdateForm(UpdateAddressForm form, @MappingTarget Address address);

    @Mapping(source = "user.account.id", target = "userId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "id", target = "id")
    @Mapping(source = "street", target = "street")
    @Mapping(source = "zipCode", target = "zipCode")
    @Mapping(source = "isDefault", target = "isDefault")
    @Mapping(source = "province", target = "province")
    @Mapping(source = "district", target = "district")
    @Mapping(source = "commune", target = "commune")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "modifiedDate", target = "modifiedDate")
    @Mapping(source = "createdDate", target = "createdDate")
    AddressDto fromEntityToAddressDto(Address address);

    @IterableMapping(elementTargetType = AddressDto.class)
    List<AddressDto> fromEntityToAddressDtoList(List<Address> addresses);
}
