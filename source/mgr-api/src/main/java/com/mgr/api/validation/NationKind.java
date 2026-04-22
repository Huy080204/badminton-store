package com.mgr.api.validation;

import com.mgr.api.validation.impl.NationKindValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = NationKindValidator.class)
@Documented
public @interface NationKind {
    String message() default "Kind is invalid";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
