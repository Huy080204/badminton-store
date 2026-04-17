package com.mgr.api.validation.impl;

import com.mgr.api.constant.MgrConstant;
import com.mgr.api.validation.NationKind;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

public class NationKindValidator implements ConstraintValidator<NationKind, Integer> {
    private String message;

    @Override
    public void initialize(NationKind constraintAnnotation) {
        this.message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        if (Objects.equals(value, MgrConstant.NATION_KIND_PROVINCE)
                || Objects.equals(value, MgrConstant.NATION_KIND_DISTRICT)
                || Objects.equals(value, MgrConstant.NATION_KIND_COMMUNE)) {
            return true;
        }
        return false;
    }
}
