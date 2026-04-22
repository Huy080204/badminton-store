package com.mgr.api.controller;

import com.mgr.api.constant.MgrConstant;
import com.mgr.api.dto.ApiMessageDto;
import com.mgr.api.dto.ErrorCode;
import com.mgr.api.exception.BadRequestException;
import com.mgr.api.exception.NotFoundException;
import com.mgr.api.form.address.CreateAddressForm;
import com.mgr.api.form.address.UpdateAddressForm;
import com.mgr.api.mapper.AddressMapper;
import com.mgr.api.model.Address;
import com.mgr.api.model.Nation;
import com.mgr.api.model.User;
import com.mgr.api.repository.AddressRepository;
import com.mgr.api.repository.NationRepository;
import com.mgr.api.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/v1/address")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
public class AddressController extends ABasicController {

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NationRepository nationRepository;

    @Autowired
    private AddressMapper addressMapper;

    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ADDR_C')")
    public ApiMessageDto<Void> create(@Valid @RequestBody CreateAddressForm form, BindingResult bindingResult) {
        User user = userRepository.findById(getCurrentUser())
                .orElseThrow(() -> new NotFoundException("User not found", ErrorCode.ADDRESS_ERROR_USER_NOT_FOUND));

        Address address = addressMapper.fromCreateFormToEntity(form);
        address.setUser(user);
        address.setStatus(MgrConstant.STATUS_ACTIVE);
        addressRepository.save(address);

        if (Boolean.TRUE.equals(address.getIsDefault())) {
            addressRepository.resetDefaultAddress(user, address.getId());
        }

        return makeSuccessResponse("Create Success");
    }

    @PostMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ADDR_U')")
    public ApiMessageDto<Void> update(@Valid @RequestBody UpdateAddressForm form, BindingResult bindingResult) {
        Address address = addressRepository.findById(form.getId())
                .orElseThrow(() -> new NotFoundException("Address not found", ErrorCode.ADDRESS_ERROR_NOT_FOUND));

        if (!address.getUser().getId().equals(getCurrentUser())) {
            throw new BadRequestException("You do not have permission to update this address", ErrorCode.ADDRESS_ERROR_UNAUTHORIZED);
        }

        addressMapper.updateEntityFromUpdateForm(form, address);

        addressRepository.save(address);

        if (Boolean.TRUE.equals(address.getIsDefault())) {
            addressRepository.resetDefaultAddress(address.getUser(), address.getId());
        }

        return makeSuccessResponse("Update Success");
    }
}
