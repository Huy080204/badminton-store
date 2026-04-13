package com.mgr.api.mapper;

import com.mgr.api.dto.user.UserDto;
import com.mgr.api.form.user.CreateUserForm;
import com.mgr.api.form.user.UpdateUserForm;
import com.mgr.api.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import javax.validation.Valid;
import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {

    @Mapping(source = "account.id", target = "id")
    @Mapping(source = "account.phone", target = "phone")
    @Mapping(source = "account.email", target = "email")
    @Mapping(source = "account.fullName", target = "fullName")
    @Mapping(source = "account.lastLogin", target = "lastLogin")
    @Mapping(source = "gender", target = "gender")
    //@Mapping(source = "address", target = "address",qualifiedByName = "fromEntityToAddressMapper")
    UserDto fromUserEntityToDto(User User);

    @Mapping(source = "id", target = "account.id")
    @Mapping(source = "phone", target = "account.phone")
    @Mapping(source = "email", target = "account.email")
    @Mapping(source = "fullName", target = "account.fullName")
    @Mapping(source = "lastLogin", target = "account.lastLogin")
    @Mapping(source = "gender", target = "gender")
    //@Mapping(source = "address", target = "address",qualifiedByName = "fromEntityToAddressMapper")
    User fromUserDtoToEntity(UserDto userDto);

    @Mapping(source = "username", target = "account.username")
    @Mapping(source = "password", target = "account.password")
    @Mapping(source = "fullName", target = "account.fullName")
    @Mapping(source = "email", target = "account.email")
    @Mapping(source = "phone", target = "account.phone")
    @Mapping(source = "gender", target = "gender")
    @Mapping(source = "avatarPath", target = "account.avatarPath")
    @Mapping(source = "groupId", target = "account.group.id")
    //@Mapping(source = "addressDtoList", target = "address",qualifiedByName = "fromEntityToAddressDtoList")
    User fromCreateUserFormToEntity(@Valid CreateUserForm createUserForm);


    @Mapping(source = "fullName", target = "account.fullName")
    @Mapping(source = "phone", target = "account.phone")
    @Mapping(source = "gender", target = "gender")
    @Mapping(source = "avatarPath", target = "account.avatarPath")
    //@Mapping(source = "addressDtoList", target = "address",qualifiedByName = "fromEntityToAddressDtoList")
    User fromUpdateUserFormToEntity(@Valid UpdateUserForm form);
}

