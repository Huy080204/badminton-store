package com.mgr.api.form.address;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class CreateAddressForm {
    @NotEmpty(message = "Street is required")
    private String street;

    private String zipCode;

    @NotNull(message = "isDefault cannot be null")
    private Boolean isDefault;

    @NotNull(message = "userId is required")
    private Long userId;

    @NotNull(message = "provinceId is required")
    private Long provinceId;

    @NotNull(message = "districtId is required")
    private Long districtId;

    @NotNull(message = "communeId is required")
    private Long communeId;
}
