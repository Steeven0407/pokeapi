package com.api.pokeapi.exception;

public class PokemonConversionException extends RuntimeException{

    public PokemonConversionException(String message, Throwable cause){
        super(message, cause);
    }
    
}
