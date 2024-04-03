package com.api.pokeapi.exception;

import java.io.IOException;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import com.api.pokeapi.exception.payload.ApiResponse;
import com.fasterxml.jackson.core.JsonProcessingException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Data fields errors
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse handleInvalidArguments(MethodArgumentNotValidException exception){

        StringBuilder builder;
        builder = new StringBuilder();

        exception.getBindingResult().getFieldErrors().forEach(error -> {
            builder.append(error.getDefaultMessage()).append(", ");
        });

        return ApiResponse
            .builder()
            .flag(false)
            .code(400)
            .message(builder.toString())
            .data("Input error")
            .build();

    }

    // Incorrect data type method arguments
    @ExceptionHandler(HandlerMethodValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse handleMethodValidationError(HandlerMethodValidationException exception){

        StringBuilder builder;
        builder = new StringBuilder();

        exception.getAllErrors().forEach(error -> {
            builder.append(error.getDefaultMessage()).append(", ");
        });

        return ApiResponse
            .builder()
            .flag(false)
            .code(400)
            .message(builder.toString())
            .data(exception.getCause())
            .build();

    }

    // Incorrect data type method arguments
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse handleTypeMismatchException(MethodArgumentTypeMismatchException exception){

        return ApiResponse
            .builder()
            .flag(false)
            .code(400)
            .message("Favor proveer el tipo de argumento correcto")
            .data(exception.getMessage())
            .build();

    }

    // Deserialization error
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse handleHttpMessageNotReadable(HttpMessageNotReadableException exception) {

        return ApiResponse
            .builder()
            .flag(false)
            .code(400)
            .message("Error en la deserialización del JSON")
            .data(exception.getMessage())
            .build();
        
    }

    // processing pokemon error
    @ExceptionHandler(JsonProcessingException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse handleJsonProcessingException(JsonProcessingException exception){

        return ApiResponse
            .builder()
            .flag(false)
            .code(400)
            .message(exception.getMessage())
            .data("Pokémon no identificado")
            .build();

    }

    // File exception
    @ExceptionHandler(IOException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse handleIoException(IOException exception){

        return ApiResponse
            .builder()
            .flag(false)
            .code(400)
            .message("Error al subir el archivo")
            .data(exception.getMessage())
            .build();

    }

    // Pokemon resource not found
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse handlerResourceNotFoundException(ResourceNotFoundException exception){

        return ApiResponse
            .builder()
            .flag(false)
            .code(400)
            .message(exception.getMessage())
            .data("Pokémon no identificado")
            .build();

    }

    // Pokemon already exists
    @ExceptionHandler(ResourceAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse handlerResourceAlreadyExistsException(ResourceAlreadyExistsException exception){
        
        return ApiResponse
            .builder()
            .flag(false)
            .code(400)
            .message(exception.getMessage())
            .data("Pokémon ya existente")
            .build();

    }

    // 404 errors
    @ExceptionHandler(NoResourceFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    ApiResponse handleNoResourceFoundException(NoResourceFoundException exception){
        
        return ApiResponse
            .builder()
            .flag(false)
            .code(404)
            .message("Recurso o URL no identificado")
            .data(exception.getMessage())
            .build();

    }

    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiResponse handlerNoHandlerFoundException(NoHandlerFoundException exception) {
        
        return ApiResponse
            .builder()
            .flag(false)
            .code(404)
            .message("No fue posible encontrar un manejador adecuado")
            .data(exception.getMessage())
            .build();

    }

    // Server errors
    @ExceptionHandler(DataAccessException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    ApiResponse handleDataAccessException(DataAccessException exception){

        return ApiResponse
            .builder()
            .flag(false)
            .code(500)
            .message("Error de base de datos")
            .data(exception.getMessage())
            .build();

    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    ApiResponse handleOtherException(Exception exception){

        return ApiResponse
            .builder()
            .flag(false)
            .code(500)
            .message("Error interno de servidor")
            .data(exception)
            .build();

    }
    
}
