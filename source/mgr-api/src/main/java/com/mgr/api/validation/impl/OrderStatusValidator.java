package com.mgr.api.validation.impl;

import com.mgr.api.constant.MgrConstant;
import com.mgr.api.validation.OrderStatus;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

public class OrderStatusValidator implements ConstraintValidator<OrderStatus, Integer> {
    private String message;

    @Override
    public void initialize(OrderStatus constraintAnnotation) {
        this.message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        return Objects.equals(value, MgrConstant.ORDER_STATUS_PENDING)
                || Objects.equals(value, MgrConstant.ORDER_STATUS_CONFIRMED)
                || Objects.equals(value, MgrConstant.ORDER_STATUS_SHIPPING)
                || Objects.equals(value, MgrConstant.ORDER_STATUS_COMPLETED)
                || Objects.equals(value, MgrConstant.ORDER_STATUS_CANCELLED);
    }
}
