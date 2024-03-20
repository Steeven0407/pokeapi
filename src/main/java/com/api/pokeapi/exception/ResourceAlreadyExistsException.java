package com.api.pokeapi.exception;

public class ResourceAlreadyExistsException extends RuntimeException {

    String resourceName;

    String fieldName;

    Object fieldValue;

    public ResourceAlreadyExistsException(String resourceName, String fieldName, Object fieldValue){
        super(String.format("El %s del %s %s ya se encuentra registrado", fieldName, resourceName, fieldValue));
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }
     
}
