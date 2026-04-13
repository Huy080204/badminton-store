package com.mgr.api.form.address;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UpdateAddressForm {

    @NotNull(message = "Address id is required")
    private Long id;

    @NotNull(message = "UserId id is required")
    private Long userId;

    private String street;
    private String zipCode;
    private Boolean isDefault;

    private Long provinceId;
    private Long districtId;
    private Long communeId;
}
