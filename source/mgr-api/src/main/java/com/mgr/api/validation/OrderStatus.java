package com.mgr.api.validation;

import com.mgr.api.validation.impl.OrderStatusValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = OrderStatusValidator.class)
@Documented
public @interface OrderStatus {
    String message() default "Order status is invalid";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
