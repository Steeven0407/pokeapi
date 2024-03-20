package com.api.pokeapi.validation.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.api.pokeapi.validation.validator.ValidTiposValidator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Documented
@Constraint(validatedBy = ValidTiposValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface ValidTipos {

    String message() default "{custom.validation.message}";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
    
}
