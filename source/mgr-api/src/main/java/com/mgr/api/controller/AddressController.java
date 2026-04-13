package com.mgr.api.controller;

import com.mgr.api.dto.ApiMessageDto;
import com.mgr.api.dto.ErrorCode;
import com.mgr.api.dto.ResponseListDto;
import com.mgr.api.dto.address.AddressDto;
import com.mgr.api.exception.BadRequestException;
import com.mgr.api.exception.NotFoundException;
import com.mgr.api.form.address.CreateAddressForm;
import com.mgr.api.form.address.UpdateAddressForm;
import com.mgr.api.mapper.AddressMapper;
import com.mgr.api.model.Address;
import com.mgr.api.model.Nation;
import com.mgr.api.model.User;
import com.mgr.api.model.criteria.AddressCriteria;
import com.mgr.api.repository.AddressRepository;
import com.mgr.api.repository.NationRepository;
import com.mgr.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping("/v1/address")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AddressController extends ABasicController{
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private AddressMapper addressMapper;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private NationRepository nationRepository;

    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('A_C')")
    public ApiMessageDto<String> create (@Valid @RequestBody CreateAddressForm form, BindingResult bindingResult){
        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
        if (!isSuperAdmin()&& !form.getUserId().equals(getCurrentUser())){
            throw new BadRequestException("You don't have permission to create this address");
        }
        User user = userRepository.findById(form.getUserId())
                .orElseThrow(() -> new NotFoundException("User not found", ErrorCode.USER_ERROR_NOT_FOUND));
        Nation province = nationRepository.findById(form.getProvinceId())
                .orElseThrow(() -> new NotFoundException("Province not found", ErrorCode.NATION_ERROR_NOT_FOUND));
        Nation district = nationRepository.findById(form.getDistrictId())
                .orElseThrow(() -> new NotFoundException("District not found", ErrorCode.NATION_ERROR_NOT_FOUND));
        Nation commune = nationRepository.findById(form.getCommuneId())
                .orElseThrow(() -> new NotFoundException("Commune not found", ErrorCode.NATION_ERROR_NOT_FOUND));

        //Set only one address at a time
        if (form.getIsDefault()) {
            addressRepository.unsetDefaultByUserId(user.getAccount().getId());
        }
        Address address = addressMapper.fromCreateFormToEntity(form);
        address.setUser(user);
        address.setProvince(province);
        address.setDistrict(district);
        address.setCommune(commune);
        addressRepository.save(address);
        apiMessageDto.setMessage("Create Nation success");
        return apiMessageDto;
    }

    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('A-U')")
    public ApiMessageDto<String> update(@Valid @RequestBody UpdateAddressForm form, BindingResult bindingResult) {
        if (!isSuperAdmin()&& !form.getUserId().equals(getCurrentUser())){
            throw new BadRequestException("You don't have permission to create this product");
        }
        Address address = addressRepository.findById(form.getId())
                .orElseThrow(() -> new NotFoundException("Address not found", ErrorCode.ADDRESS_ERROR_NOT_FOUND));
        //Unset default with old address
        if (form.getIsDefault() != null && form.getIsDefault()) {
            addressRepository.unsetDefaultByUserId(address.getUser().getAccount().getId());
        }

        // 3. Map các field cơ bản (street, name, isDefault...)
        addressMapper.updateAddressFromForm(form, address);

        // 4. Update các liên kết Nation nếu có truyền ID mới
        if (form.getProvinceId() != null) {
            address.setProvince(nationRepository.findById(form.getProvinceId()).orElse(null));
        }
        if (form.getDistrictId() != null) {
            address.setDistrict(nationRepository.findById(form.getDistrictId()).orElse(null));
        }
        if (form.getCommuneId() != null) {
            address.setCommune(nationRepository.findById(form.getCommuneId()).orElse(null));
        }

        addressRepository.save(address);
        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
        apiMessageDto.setMessage("Update Address success");
        return apiMessageDto;
    }

    @GetMapping(value = "/get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('A-G')")
    public ApiMessageDto<AddressDto> get(@PathVariable("id") Long id) {
        if(!isSuperAdmin()){
            throw new BadRequestException("You don't have permission to get", ErrorCode.USER_ERROR_PERMISSION);
        }
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Address not found", ErrorCode.ADDRESS_ERROR_NOT_FOUND));
        AddressDto addressDto = addressMapper.fromEntityToDto(address);
        ApiMessageDto<AddressDto> apiMessageDto = new ApiMessageDto<>();
        apiMessageDto.setData(addressDto);
        apiMessageDto.setMessage("Get address success");
        return apiMessageDto;
    }
    @GetMapping(value = "/getMyAddress", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('A-G')")
    public ApiMessageDto<AddressDto> getMyAddress() {
        Address address = addressRepository.findById(getCurrentUser())
                .orElseThrow(() -> new NotFoundException("Address not found", ErrorCode.ADDRESS_ERROR_NOT_FOUND));
        AddressDto addressDto = addressMapper.fromEntityToDto(address);
        ApiMessageDto<AddressDto> apiMessageDto = new ApiMessageDto<>();
        apiMessageDto.setData(addressDto);
        apiMessageDto.setMessage("Get address success");
        return apiMessageDto;
    }

    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('A-D')")
    public ApiMessageDto<String> delete(@PathVariable("id") Long id) {
        Long currentUserID = getCurrentUser();
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Address not found", ErrorCode.ADDRESS_ERROR_NOT_FOUND));
        User userCurrent  = address.getUser();
//        if(getKind() !=1 && !currentUserID.equals(userCurrent.getAccount().getId())){
//            throw new BadRequestException("You don't have permission to delete address", ErrorCode.ADDRESS_ERROR_PERMISSION);
//        }
        addressRepository.delete(address);
        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
        apiMessageDto.setMessage("Delete Address success");
        return apiMessageDto;
    }
    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('A-L')")
    public ApiMessageDto<ResponseListDto<List<AddressDto>>> list(AddressCriteria criteria, Pageable pageable) {
//        if(getKind() !=1){
//            throw new BadRequestException("You don't have permission to get list", ErrorCode.ADDRESS_ERROR_PERMISSION);
//        }
        // 1. Query từ DB
        Page<Address> page = addressRepository.findAll(criteria.getSpecification(), pageable);

        // 2. Convert sang DTO List
        List<AddressDto> listDto = addressMapper.fromEntityListToDtoList(page.getContent());

        // 3. Đóng gói kết quả
        ResponseListDto<List<AddressDto>> response = new ResponseListDto<>();
        response.setContent(listDto);
        response.setTotalElements(page.getTotalElements());
        response.setTotalPages(page.getTotalPages());

        ApiMessageDto<ResponseListDto<List<AddressDto>>> apiMessageDto = new ApiMessageDto<>();
        apiMessageDto.setData(response);
        apiMessageDto.setMessage("Get address list success");
        return apiMessageDto;
    }

}
