package com.api.pokeapi.exception;

public class ResourceNotFoundException extends RuntimeException {

    String resourceName;

    String fieldName;

    Object fieldValue;

    public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("No se encontró el %s con %s = '%s'", resourceName, fieldName, fieldValue));
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }
    
}
