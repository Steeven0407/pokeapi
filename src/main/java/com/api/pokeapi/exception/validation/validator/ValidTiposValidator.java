package com.api.pokeapi.exception.validation.validator;

import java.util.List;

import com.api.pokeapi.exception.validation.annotation.ValidTipos;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ValidTiposValidator implements ConstraintValidator<ValidTipos, List<String>>{

    @Override
    public boolean isValid(List<String> value, ConstraintValidatorContext context) {
        
        // Value cannot be null
        if (value == null){
            return false;
        }

        // Value cannot be empty
        if (value.isEmpty()){
            return false;
        }

        // Value cannot contain more than three elements
        if (value.size() > 3){
            return false;
        }

        return true;

    }
    
}
