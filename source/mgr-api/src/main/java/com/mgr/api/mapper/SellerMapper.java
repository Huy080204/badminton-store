package com.mgr.api.mapper;

import com.mgr.api.dto.seller.SellerDto;
import com.mgr.api.form.seller.CreateSellerForm;
import com.mgr.api.form.seller.UpdateSellerForm;
import com.mgr.api.form.seller.UpdateSellerProfileForm;
import com.mgr.api.model.Seller;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {AccountMapper.class})
public interface SellerMapper {

    @Mapping(source = "shopName", target = "shopName")
    @Mapping(source = "shopDescription", target = "shopDescription")
    Seller fromCreateFormToEntity(CreateSellerForm form);

    @Mapping(source = "shopName", target = "shopName")
    @Mapping(source = "shopDescription", target = "shopDescription")
    void mappingUpdateFormToEntity(UpdateSellerForm form, @MappingTarget Seller seller);

    @Mapping(source = "shopName", target = "shopName")
    @Mapping(source = "shopDescription", target = "shopDescription")
    void mappingUpdateProfileFormToEntity(UpdateSellerProfileForm form, @MappingTarget Seller seller);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "shopName", target = "shopName")
    @Mapping(source = "shopDescription", target = "shopDescription")
    @Mapping(source = "account", target = "account")
    SellerDto fromEntityToDto(Seller seller);

    @IterableMapping(elementTargetType = SellerDto.class)
    List<SellerDto> fromEntityListToDtoList(List<Seller> list);
}
